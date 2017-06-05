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
import android.widget.Button;
import android.widget.EditText;

public class CategoryEdit extends AppCompatActivity {

    private EditText mTitleText;
    private Long mRowId;
    private NotesDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.category_edit);
        setTitle(R.string.menu_editC);

        mTitleText = (EditText) findViewById(R.id.title);

        if(mTitleText==null || (mTitleText.getText().toString()).equals("")){
            mTitleText.setText("Nueva_categoria_"+(mDbHelper.getMaxId(false)+1));
        }

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();


            mRowId = (extras != null) ? extras.getLong(NotesDbAdapter.KEY_ROWID)
             : null;
        }

        populateFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void populateFields() {

        Log.i(getPackageName(), String.valueOf(mRowId)+"___");
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchCategory(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_CAT)));
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

        if(title.equals("")){
            title="Nueva_categoria_"+(mDbHelper.getMaxId(false)+1);
        }

        if (mRowId == null) {
            long id = mDbHelper.createCategory(title);
            if (id > 0) {
                mRowId = id;
            }
        }else{
            mDbHelper.updateCategory(mRowId,title);
        }
    }
}
