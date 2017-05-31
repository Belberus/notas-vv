import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.NotesDbAdapter;

public class Notepadv3SobrecargaTest extends ActivityInstrumentationTestCase2<Notepadv3> {
    private Notepadv3 mNotepad;
    private NotesDbAdapter mDbHelper;
    private long originalRows;

    public Notepadv3SobrecargaTest(){
        super(Notepadv3.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        mNotepad = getActivity();
        mDbHelper = mNotepad.getNotesDbAdapter();
        // Cramos una nota para saber desde que ID tenemos que borrar al final
        originalRows = mDbHelper.createNote("Titulo","Contenido",1);

    }

    @Test
    public void testSobrecarga() {
        int notasCreadas = 0;
        while(true) {
            mDbHelper.createNote("Nota_" + notasCreadas, "cuerpo", 0);
            Log.d("TEST SOBRECARGA", " CREADA Nota_" + notasCreadas + " creada.");
            notasCreadas++;
        }
    }

    @After
    protected void tearDown() throws Exception{
        for (long i = originalRows; i < mDbHelper.getNumberOfNotes(); i++) {
            mNotepad.getNotesDbAdapter().deleteNote(i);
        }
        super.tearDown();
    }
}