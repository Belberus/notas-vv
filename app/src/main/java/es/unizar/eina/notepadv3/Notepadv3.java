package es.unizar.eina.notepadv3;

/*
 * Alberto Martínez Menéndez (681061)
 * Pablo Piedrafita Castañeda (691812)
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.unizar.eina.send.MailImplementor;

public class Notepadv3 extends AppCompatActivity {

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
    private static final int SEND_ID = Menu.FIRST + 3;
    private static final int VIEW_CAT = Menu.FIRST + 4;
    private static final int LIST_CAT = Menu.FIRST + 5;
    private static final int LIST_NOTE = Menu.FIRST + 6;
    private static final int RESET = Menu.FIRST + 7;

    private NotesDbAdapter mDbHelper;
    private ListView mList;
    private int posNote;

    public NotesDbAdapter getNotesDbAdapter() {
        return mDbHelper;
    }


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepadv3);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            int idCat = bundle.getInt("idcat");
            try {
                setTitle("Notas de " + mDbHelper.getCat(idCat));
            }catch (Exception e){
                e.printStackTrace();
            }
            fillDataCat(idCat);
        }else {
            fillData();
        }
        registerForContextMenu(mList);
    }

    /**
     * Show notes stored in data base with no order
     */
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor);

        if(notesCursor.getCount() > 0){
            mList.setVisibility(View.VISIBLE);
        }else{
            mList.setVisibility(View.GONE);
        }

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { NotesDbAdapter.KEY_TITLE };

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor,
                        from, to);
        mList.setAdapter(notes);
        mList.setSelection(posNote);
    }

    /**
     * Show categories stored in data base order by note alphabetically
     * or by categories
     * @param byNote boolean that indicates the type of order required
     */
    private void fillDataOrder(boolean byNote) {
        // Get all of the notes from the database and create the item list
        Cursor notesCursor;

        if(byNote) {
            notesCursor = mDbHelper.fetchAllNotesByNote();
        } else{
            notesCursor = mDbHelper.fetchAllNotesByCategory();
        }
        startManagingCursor(notesCursor);

        if(notesCursor.getCount() > 0){
            mList.setVisibility(View.VISIBLE);
        }else{
            mList.setVisibility(View.GONE);
        }

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { NotesDbAdapter.KEY_TITLE };

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor,
                        from, to);
        mList.setAdapter(notes);
        mList.setSelection(posNote);
    }

    /**
     * Show notes belonging to the categorie introduced by parameter
     * @param idCat id of the category
     */
    private void fillDataCat(int idCat) {
        // Get all of the notes from the database and create the item list
        Cursor notesCursor = mDbHelper.fetchAllNotesFromCategory(idCat);

        startManagingCursor(notesCursor);

        if(notesCursor.getCount() > 0){
            mList.setVisibility(View.VISIBLE);
        }else{
            mList.setVisibility(View.GONE);
        }

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { NotesDbAdapter.KEY_TITLE };

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor,
                        from, to);
        mList.setAdapter(notes);
        mList.setSelection(posNote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.menu_insert);
        menu.add(Menu.NONE, VIEW_CAT, Menu.NONE, R.string.menu_listC);
        menu.add(Menu.NONE, LIST_CAT, Menu.NONE, R.string.menu_orderByC);
        menu.add(Menu.NONE, LIST_NOTE, Menu.NONE, R.string.menu_orderByTitle);
        menu.add(Menu.NONE, RESET, Menu.NONE, R.string.reset);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                posNote = mList.getCount();
                createNote();
                return true;
            case VIEW_CAT:
                showCategories();
                return true;
            case LIST_CAT:
                posNote=0;
                fillDataOrder(false);
                return true;
            case LIST_NOTE:
                posNote=0;
                fillDataOrder(true);
                return true;
            case RESET:
                reset();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit);
        menu.add(Menu.NONE, SEND_ID, Menu.NONE, R.string.menu_send);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteNote(info.id);
                posNote=info.position;
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editNote(info.id);
                posNote=info.position;
                return true;
            case SEND_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                sendNote(info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Create an activity of NoteEdit to allow user to create a new note
     */
    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivity(i);
    }


    /**
     * Create an activity of NoteEdit to allow user to edit the note selected
     * @param id id of editing note
     */
    private void editNote(long id) {
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivity(i);
    }


    /**
     * Send the note selected by mail or SMS depending of the body length of the note
     * @param id id of sending note
     */
    private void sendNote(long id){
        String title, body;

        Cursor note = mDbHelper.fetchNote(id);
        startManagingCursor(note);
        title=note.getString(
                note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE));
        body=note.getString(
                note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY));

        MailImplementor sendMail = new MailImplementor(this);

        sendMail.send(title, body);
    }

    /**
     * Create an activity of Category to visualize all the categories created
     */
    private void showCategories(){
        Intent intent = new Intent(this, CategoryList.class); // creamos el intent
        startActivity(intent);
        finish();
    }

    /**
     * Reset the data base, deleting all notes from there
     */
    private void reset(){
        mDbHelper.removeAll();
        fillData();
    }

    @Override
    protected void onResume() {
        fillData();
        super.onResume();
    }
}