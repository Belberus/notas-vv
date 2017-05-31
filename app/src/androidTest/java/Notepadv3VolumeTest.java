import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.NotesDbAdapter;

public class Notepadv3VolumeTest extends ActivityInstrumentationTestCase2<Notepadv3> {
    private Notepadv3 mNotepad;
    private NotesDbAdapter mDbHelper;
    private long originalRows;

    public Notepadv3VolumeTest(){
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
    public void testVolume() {
        int notasCreadas = 0;
        for (int i=1;i<=1050;i++) {
            mDbHelper.createNote("Nota_" + i, "cuerpo", 0);
            Log.d("TEST VOLUMEN", " CREADA Nota_" + i + " creada.");
            notasCreadas++;
        }
        Log.d("testVolumen","El sistema ha soportado la creación de "+ notasCreadas + " notas.");
    }

    @After
    protected void tearDown() throws Exception{
        for (long i = originalRows; i < mDbHelper.getNumberOfNotes(); i++) {
            mNotepad.getNotesDbAdapter().deleteNote(i);
        }
        super.tearDown();
    }
}