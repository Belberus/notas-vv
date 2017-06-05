package es.unizar.eina.notepadv3;

/*
 * Alberto Martínez Menéndez (681061)
 * Pablo Piedrafita Castañeda (691812)
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class NotesDbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_CAT = "category";
    private static final String TAG = "NotesDbAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table if not exists notes (_id integer primary key autoincrement, "
                    + "title text not null, body text not null, category integer);";
    private static final String DATABASE_CREATECAT =
            "create table if not exists categories (_id integer primary key autoincrement, "
                    + "category text not null);";



    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final String DATABASE_TABLECAT = "categories";
    private static final int DATABASE_VERSION = 10;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATECAT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the note
     * @param body the body of the note
     * @param category the category of the note
     * @return rowId or -1 if failed
     * @throws Throwable if can not insert note
     */
    public long createNote(String title, String body, int category) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_CAT, category);

        if (category > -1) {
            return mDb.insert(DATABASE_TABLE, null, initialValues);
        } else return -1;


    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteNote(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Create a new category using the title provided. If the category is
     * successfully created return the new rowId for that category, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the category
     * @return rowId or -1 if failed
     */
    public long createCategory(String title) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CAT, title);

        return mDb.insert(DATABASE_TABLECAT, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteCategory(long rowId) {
        return mDb.delete(DATABASE_TABLECAT, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database order by insert
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_CAT}, null, null, null, null, null);
    }


    /**
     * Return a Cursor over the list of all notes in the database order by title
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotesByNote() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_CAT}, null, null, null, null, KEY_TITLE + " COLLATE NOCASE ASC");
    }

    /**
     * Return a Cursor over the list of all notes in the database order by category
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotesByCategory() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_CAT}, null, null, null, null, KEY_CAT + "  COLLATE NOCASE ASC");
    }

    /**
     * Return a Cursor over the list of all notes from the category
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotesFromCategory(int cat) {
        return mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE "
                + KEY_CAT + "=" + cat + " ORDER BY " + KEY_TITLE + "  COLLATE NOCASE ASC", null);
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllCategories(boolean fromNotes) {
        if(fromNotes){
            return mDb.query(DATABASE_TABLECAT, new String[]{KEY_ROWID, KEY_CAT},
                    null, null, null, null, KEY_CAT + " COLLATE NOCASE ASC");
        }else {
            return mDb.query(DATABASE_TABLECAT, new String[]{KEY_ROWID, KEY_CAT},
                    KEY_ROWID + "!=" + 0, null, null, null, KEY_CAT + "  COLLATE NOCASE ASC");
        }
    }


    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY, KEY_CAT}, KEY_ROWID + "=" + rowId,
                        null,null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @param category the category of the note
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long rowId, String title, String body, int category) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put(KEY_CAT, category);

        if (title == null || body == null) {
            return false;
        } else {
            return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null)
                    > 0;
        }
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchCategory(long rowId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLECAT, new String[] {KEY_ROWID,
                                KEY_CAT}, KEY_ROWID + "=" + rowId,
                        null,null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the category using the details provided. The category to be updated is
     * specified using the rowId, and it is altered to use the category
     * values passed in
     * @param rowId id of note to update
     * @param title value to set category title to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateCategory(long rowId, String title) {
        if (title == null) {
            return false;
        } else {
            ContentValues args = new ContentValues();
            args.put(KEY_CAT, title);

            return mDb.update(DATABASE_TABLECAT, args, KEY_ROWID + "=" + rowId, null)
                    > 0;
        }
    }

    /**
     * Search the max id of notes or categories stored in data base
     * @param notes boolean that indicates if it is need the max Id of notes or category
     * @return the id asked
     */
    public int getMaxId(boolean notes) {
        Cursor c = null;
        if (notes) {
            c = mDb.rawQuery("SELECT MAX(_id) FROM "+DATABASE_TABLE, null);
        }else{
            c = mDb.rawQuery("SELECT MAX(_id) FROM "+DATABASE_TABLECAT, null);
        }
        if (c == null) {
            return -1;
        } else {
            c.moveToFirst();
            return c.getInt(0);
        }
    }

    /**
     * Search the category id that match with the in parameter, category name
     * @param category string that represents the category name
     * @return the id of the category
     */
    public int getIdCat(String category) {
            Cursor c = mDb.rawQuery("SELECT _id FROM " + DATABASE_TABLECAT + " WHERE " + KEY_CAT + " = '" + category + "'", null);
            if (c == null || c.getColumnCount() <= 0) {
                return 0;
            } else {
                c.moveToFirst();
                return c.getInt(0);
            }
    }

    /**
     * Search the category name that match with the in parameter, category id
     * @param idCat int that represents the category id
     * @return string, the name of the category
     */
    public String getCat(int idCat) throws Exception {
        Cursor c = mDb.rawQuery("SELECT "+KEY_CAT+" FROM " + DATABASE_TABLECAT + " WHERE "
                + KEY_ROWID + " = '" + idCat +"'", null);
        if (c == null || c.getColumnCount() <= 0) {
            return "";
        } else {
            c.moveToFirst();
            return c.getString(0);
        }
    }

    /* Devuelve el numero de notas guardadas */
    public int getNumberOfNotes() {
        Cursor cursor = fetchAllNotes();
        return cursor.getCount();
    }

    /* Devuelve el numero de categorias guardadas */
    public int getNumberOfCategories() {
        Cursor cursor = fetchAllCategories(true);
        return cursor.getCount();
    }

    /**
     * Remove all from database.
     */
    public void removeAll(){
        mDb.delete(DATABASE_TABLE, null, null); //Borro notas
        mDb.delete(DATABASE_TABLECAT, null, null); //Borro categorias
        mDb.execSQL("delete from sqlite_sequence where name='"+DATABASE_TABLE+"'");
        mDb.execSQL("delete from sqlite_sequence where name='"+DATABASE_TABLECAT+"'");
    }
}