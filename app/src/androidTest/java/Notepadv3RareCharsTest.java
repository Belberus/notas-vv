import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.NotesDbAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// En estos test, observaremos el comportamiento de la aplicacion ante caracteres no imprimibles [0..32 ASCII]
public class Notepadv3RareCharsTest extends ActivityInstrumentationTestCase2<Notepadv3> {
    private Notepadv3 mNotepad;
    private NotesDbAdapter mDbHelper;
    private long originalRows;

    private static final String TAG = "TestRareChars";
    public Notepadv3RareCharsTest(){
        super(Notepadv3.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        mNotepad = getActivity();
        mDbHelper = mNotepad.getNotesDbAdapter();
        // Creamos una nota para saber el ID a partir del cual tenemos que limpiar luego notas
        originalRows = mDbHelper.createNote("Titulo","Contenido",1);
    }

    @Test
    public void testCaracteresRaros() {
        int asciiInicial = 0;
        for (int i= asciiInicial; i<=32; i++) {
           Log.d(TAG,"Prueba caracter ASCII: " + i);
            long result = mDbHelper.createNote(Character.toString((char) i), "Contenido",1);
            if (result == -1) {
                Log.d(TAG,"Nota no creada.");
            } else Log.d(TAG,"Nota creada.");
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

