import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.junit.After;
import org.junit.Before;

import java.util.Random;

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

    public void testSobrecarga() {
        int notasCreadas = 0;

        while(true) {
            int length = notasCreadas*100;
            mDbHelper.createNote("Nota_" + notasCreadas, random(length), 0);
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

    /**
     * Create a random string of length indicated by the parameter
     * @param length int that represent the max length of the string return
     * @return string of length indicated
     */
    private static String random(int length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(length);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}