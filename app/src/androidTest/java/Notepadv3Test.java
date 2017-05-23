// Alberto Martinez, Dario Sanchez, Adrian Martinez

import android.test.ActivityInstrumentationTestCase2;
import org.junit.Test;
import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.NotesDbAdapter;

public class Notepadv3Test extends ActivityInstrumentationTestCase2<Notepadv3> {

    private Notepadv3 mNotepad;
    private NotesDbAdapter mDbHelper;
    private long originalRows;

    public Notepadv3Test(){
        super(Notepadv3.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mNotepad = getActivity();
        mDbHelper = mNotepad.getNotesDbAdapter();
    }

    @Test
    public void testNumberOfNotes() {
        int nNotasPre = mDbHelper.getNumberOfNotes();

        originalRows = mDbHelper.createNote("Titulo","Contenido",1);

        int nNotasPost = mDbHelper.getNumberOfNotes();

        assertEquals(nNotasPre + 1, nNotasPost);
    }

    @Test
    public void testCrearNotaCorrecta(){
        long resultado = mDbHelper.createNote("titulo", "cuerpo", 0);
        assertTrue(resultado >= 0);
    }

    @Test
    public void testCrearNotaTituloNull() {
        long resultado = mDbHelper.createNote(null, "cuerpo", 0);
        assertEquals(resultado, -1);
    }

    @Test
    public void testCrearNotaSinTitulo(){
        long resultado = mDbHelper.createNote("", "cuerpo", 0);
        assertTrue(resultado >= 0);
    }

    @Test
    public void testCrearNotaCuerpoNull() {
        long resultado = mDbHelper.createNote("titulo", null, 0);
        assertEquals(resultado,-1);
    }

    @Test
    public void testCrearNotaSinCuerpo(){
        long resultado = mDbHelper.createNote("titulo", "", 0);
        assertTrue(resultado >= 0);
    }

    @Test
    public void testBorrarNotaCorrecta() {
        long newNoteId = mDbHelper.createNote("titulo", "cuerpo", 0);
        boolean resultado = mDbHelper.deleteNote(newNoteId);
        assertTrue(resultado);
    }

    @Test
    public void testBorrarNotaIdNegativo() {
        boolean resultado = mDbHelper.deleteNote(-1);
        assertFalse(resultado);
    }

    @Test
    public void testBorrarNotaIdCero() {
        boolean resultado = mDbHelper.deleteNote(0);
        assertFalse(resultado);
    }

    @Test
    public void testModificarNotaCorrecta() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,"tituloNew","cuerpoNew",0);
        assertTrue(resultado);
    }

    @Test 
    public void testModificarNotaConTituloNull() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,null,"cuerpoNew",0);
        assertFalse(resultado);
    }

    @Test
    public void testModificarNotaTituloVacio() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,"","cuerpoNew",0);
        assertTrue(resultado);
    }

    @Test
    public void testModificarNotaConCuerpoNull() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,"tituloNew",null,0);
        assertFalse(resultado);
    }

    @Test
    public void testModificarNotaCuerpoVacio() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,"tituloNew","",0);
        assertTrue(resultado);
    }

    @Test
    public void testModificarNotaIdNegativo() {
        boolean resultado = mDbHelper.updateNote(-1,"tituloNew","cuerpoNew",0);
        assertFalse(resultado);
    }

    @Test
    public void testModificarNotaIdCero() {
        boolean resultado = mDbHelper.updateNote(0,"tituloNew","cuerpoNew",0);
        assertFalse(resultado);
    }

    @Override
    protected void tearDown() throws Exception{
        for (long i = originalRows; i < mDbHelper.getNumberOfNotes(); i++) {
            mNotepad.getNotesDbAdapter().deleteNote(i);
        }
        super.tearDown();
    }

}
