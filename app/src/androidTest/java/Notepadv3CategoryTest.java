// Alberto Martinez, Dario Sanchez, Adrian Martinez

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.NotesDbAdapter;


public class Notepadv3CategoryTest extends ActivityInstrumentationTestCase2<Notepadv3> {

    private Notepadv3 mNotepad;
    private NotesDbAdapter mDbHelper;
    private long originalRows;

    public Notepadv3CategoryTest(){
        super(Notepadv3.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        mNotepad = getActivity();
        mDbHelper = mNotepad.getNotesDbAdapter();
        // Cramos una nota para saber desde que ID tenemos que borrar al final
        originalRows = mDbHelper.createCategory("Nombre");
    }

    @Test
    public void testCreateCategory() {
        int nCatPre = mDbHelper.getNumberOfCategories();

        mDbHelper.createCategory("TituloCategoria");

        int nCatPost = mDbHelper.getNumberOfCategories();

        assertEquals(nCatPre + 1, nCatPost);
    }

    @Test
    public void testCreateCategoryCorrecta() {
        long result = mDbHelper.createCategory("TituloCategoriaCorrecta");
        assertTrue(result != -1);
    }

    @Test
    public void testCreateCategoryNull() {
        long result = mDbHelper.createCategory(null);
        assertTrue(result == -1);
    }

    @Test
    public void testCreateCategoryVacia() {
        long result = mDbHelper.createCategory("");
        assertTrue(result != -1);
    }

    @Test
    public void testDeleteCategoryIdCorrecto() {
        long result = mDbHelper.createCategory("TituloCategoriaCorrecta");
        boolean deleted = mDbHelper.deleteCategory(result);
        assertTrue(deleted);
    }

    @Test
    public void testDeleteCategoryIdCero() {
        boolean deleted = mDbHelper.deleteCategory(0);
        assertFalse(deleted);
    }

    @Test
    public void testDeleteCategoryIdNegativo() {
        boolean deleted = mDbHelper.deleteCategory(-2);
        assertFalse(deleted);
    }

    @Test
    public void testUpdateCategoryCorrecta() {
        long id = mDbHelper.createCategory("TituloCategoriaCorrecta");
        boolean updated = mDbHelper.updateCategory(id, "NuevoTitutloCategoria");
        assertTrue(updated);
    }

    @Test
    public void testUpdateCategoryNull() {
        long id = mDbHelper.createCategory("TituloCategoriaCorrecta");
        boolean updated = mDbHelper.updateCategory(id, null);
        assertFalse(updated);

    }

    @Test
    public void testUpdateCategoryVacia() {
        long id = mDbHelper.createCategory("TituloCategoriaCorrecta");
        boolean updated = mDbHelper.updateCategory(id, "");
        assertTrue(updated);
    }


    @After
    protected void tearDown() throws Exception{
        for (long i = originalRows; i < mDbHelper.getNumberOfCategories(); i++) {
            mNotepad.getNotesDbAdapter().deleteCategory(i);
        }
        super.tearDown();
    }

}