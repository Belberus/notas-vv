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
import android.widget.TextView;

public class CategoryList extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
    private static final int LIST_CAT = Menu.FIRST + 3;

    private NotesDbAdapter mDbHelper;
    private ListView mList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        fillData();

        registerForContextMenu(mList);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        TextView tvCat = (TextView) view.findViewById(R.id.text1);
        String category = tvCat.getText().toString();
        int idCat = mDbHelper.getIdCat(category);
        Intent i = new Intent(this, Notepadv3.class);
        i.putExtra("idcat", idCat);
        startActivity(i);
        finish();
    }

    /**
     * Show categories stored in data base
     */
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor mCategoriesCursor = mDbHelper.fetchAllCategories(false);

        if(mCategoriesCursor.getCount() > 0){
            mList.setVisibility(View.VISIBLE);
        }else{
            mList.setVisibility(View.GONE);
        }

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { NotesDbAdapter.KEY_CAT };

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, mCategoriesCursor,
                        from, to);
        mList.setAdapter(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.menu_insertC);
        menu.add(Menu.NONE, LIST_CAT, Menu.NONE, R.string.menu_orderByTitle);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createCategory();
                return true;
            case LIST_CAT:
                fillData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_editC);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_deleteC);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteCategory(info.id);
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editCategory(info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createCategory() {
        Intent i = new Intent(this, CategoryEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void editCategory(long id) {
        Intent i = new Intent(this, CategoryEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Notepadv3.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
