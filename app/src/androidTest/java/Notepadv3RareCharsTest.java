import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.NotesDbAdapter;
import org.junit.Test;

// En estos test, observaremos el comportamiento de la aplicacion ante caracteres no imprimibles [0..32 ASCII]
public class Notepadv3RareCharsTest extends ActivityInstrumentationTestCase2<Notepadv3> {
    private Notepadv3 mNotepad;
    private NotesDbAdapter mDbHelper;
    private long originalRows;

    public Notepadv3RareCharsTest(){
        super(Notepadv3.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mNotepad = getActivity();
        mDbHelper = mNotepad.getNotesDbAdapter();
        // Creamos una nota para saber el ID a partir del cual tenemos que limpiar luego notas
        originalRows = mDbHelper.createNote("Titulo","Contenido",1);
    }


    @Test
    public void TestCaracteresRaros() {
        int asciiInicial = 0;
        for (int i= asciiInicial; i<=32; i++) {
            System.out.println("Prueba caracter ASCII: " + asciiInicial);
            long result = mDbHelper.createNote(Character.toString((char) i), "Contenido",1);
            if (result == -1) {
                System.out.println("Nota no creada.");
            } else System.out.println("Nota creada.");
        }

        assertEquals(asciiInicial,0);
    }

    @Override
    protected void tearDown() throws Exception{
        for (long i = originalRows; i < mDbHelper.getNumberOfNotes(); i++) {
            mNotepad.getNotesDbAdapter().deleteNote(i);
        }
        super.tearDown();
    }

}

