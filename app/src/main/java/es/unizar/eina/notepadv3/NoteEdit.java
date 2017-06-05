package es.unizar.eina.notepadv3;

/*
 * Alberto Martínez Menéndez (681061)
 * Pablo Piedrafita Castañeda (691812)
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NoteEdit extends AppCompatActivity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Spinner mCategoryText;
    private Long mRowId;
    private NotesDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.note_edit);
        setTitle(R.string.edit_note);

        mTitleText = (EditText) findViewById(R.id.title);

        if (mTitleText == null || (mTitleText.getText().toString()).equals("")) {
            mTitleText.setText("Nueva_nota_" + (mDbHelper.getMaxId(true) + 1));
        }

        mBodyText = (EditText) findViewById(R.id.body);

        mCategoryText = (Spinner) findViewById(R.id.spinner);

        Cursor cursorCat = mDbHelper.fetchAllCategories(true);
        String[] arraySpinner = new String[cursorCat.getCount() + 1];
        if (arraySpinner.length != 0) {
            cursorCat.moveToFirst();
        }

        arraySpinner[0] = "Sin categoria";
        for (int i = 1; i < arraySpinner.length; i++) {
            String catName = cursorCat.getString(cursorCat.getColumnIndex(NotesDbAdapter.KEY_CAT));
            Log.d("Category",catName);
            cursorCat.moveToNext();
            arraySpinner[i] = catName;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        mCategoryText.setAdapter(adapter);

        EditText mIDText = (EditText) findViewById(R.id.title_template);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                    : null;
            //El id recuperado del putExtra (mRowId) se lo ponemos al cuadro
            // de texto id (EditText) de note_edit (Layout)
            if (mRowId != null) {
                mIDText.setText(String.valueOf(mRowId));
            } else setTitle(R.string.menu_insert);
        }
        populateFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //setResult(RESULT_OK);
                finish();
            }

        });
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            note.moveToFirst();
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));

            try{
            mCategoryText.setSelection(((ArrayAdapter) mCategoryText.getAdapter()).getPosition(
                        mDbHelper.getCat(note.getInt(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_CAT)))));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        int idCat = 0;
        if (mCategoryText.getSelectedItem() != null) {
            if (mCategoryText.getSelectedItem().toString().equals("Sin categoria")) {
                idCat = 0;
            } else {
                String category = mCategoryText.getSelectedItem().toString();
                idCat = mDbHelper.getIdCat(category);
            }
        }

        if (title.equals("")) {
            if (mRowId != null)
                title = "Nueva_nota_" + mRowId;
            else
                title = "Nueva_nota_" + (mDbHelper.getMaxId(true) + 1);
        }

        if (mRowId == null) {
            try {
                long id = mDbHelper.createNote(title, body, idCat);
                if (id > 0) {
                    mRowId = id;
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

        } else {
            try {
                mDbHelper.updateNote(mRowId, title, body, idCat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}