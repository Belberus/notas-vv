// Alberto Martinez, Dario Sanchez, Adrian Martinez

import android.test.ActivityInstrumentationTestCase2;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.NotesDbAdapter;


public class Notepadv3NotesTest extends ActivityInstrumentationTestCase2<Notepadv3> {

    private Notepadv3 mNotepad;
    private NotesDbAdapter mDbHelper;
    private long originalRows;

    public Notepadv3NotesTest(){
        super(Notepadv3.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mNotepad = getActivity();
        mDbHelper = mNotepad.getNotesDbAdapter();
        // Cramos una nota para saber desde que ID tenemos que borrar al final
        originalRows = mDbHelper.createNote("Titulo","Contenido",1);

    }

    public void testNumberOfNotes() {
        int nNotasPre = mDbHelper.getNumberOfNotes();

        mDbHelper.createNote("Titulo","Contenido",1);

        int nNotasPost = mDbHelper.getNumberOfNotes();

        assertEquals(nNotasPre + 1, nNotasPost);
    }

    public void testCrearNotaCorrecta(){
        long resultado = mDbHelper.createNote("titulo", "cuerpo", 0);
        assertTrue(resultado >= 0);
    }

    public void testCrearNotaTituloNull() {
        long resultado = mDbHelper.createNote(null, "cuerpo", 0);
        assertEquals(resultado, -1);
    }

    public void testCrearNotaSinTitulo(){
        long resultado = mDbHelper.createNote("", "cuerpo", 0);
        assertTrue(resultado >= 0);
    }

    public void testCrearNotaCuerpoNull() {
        long resultado = mDbHelper.createNote("titulo", null, 0);
        assertEquals(resultado,-1);
    }

    public void testCrearNotaSinCuerpo(){
        long resultado = mDbHelper.createNote("titulo", "", 0);
        assertTrue(resultado >= 0);
    }

    public void testBorrarNotaCorrecta() {
        long newNoteId = mDbHelper.createNote("titulo", "cuerpo", 0);
        boolean resultado = mDbHelper.deleteNote(newNoteId);
        assertTrue(resultado);
    }

    public void testBorrarNotaIdNegativo() {
        boolean resultado = mDbHelper.deleteNote(-1);
        assertFalse(resultado);
    }

    public void testBorrarNotaIdCero() {
        boolean resultado = mDbHelper.deleteNote(0);
        assertFalse(resultado);
    }

    public void testModificarNotaCorrecta() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,"tituloNew","cuerpoNew",0);
        assertTrue(resultado);
    }

    public void testModificarNotaConTituloNull() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,null,"cuerpoNew",0);
        assertFalse(resultado);
    }

    public void testModificarNotaTituloVacio() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,"","cuerpoNew",0);
        assertTrue(resultado);
    }

    public void testModificarNotaConCuerpoNull() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,"tituloNew",null,0);
        assertFalse(resultado);
    }

    public void testModificarNotaCuerpoVacio() {
        long newNoteId = mDbHelper.createNote("titulo","cuerpo",0);
        boolean resultado = mDbHelper.updateNote(newNoteId,"tituloNew","",0);
        assertTrue(resultado);
    }

    public void testModificarNotaIdNegativo() {
        boolean resultado = mDbHelper.updateNote(-1,"tituloNew","cuerpoNew",0);
        assertFalse(resultado);
    }

    public void testModificarNotaIdCero() {
        boolean resultado = mDbHelper.updateNote(0,"tituloNew","cuerpoNew",0);
        assertFalse(resultado);
    }

    // EXTRA TESTS

    public void testCrearNotaCategoriaExistente() {
        long idCat = mDbHelper.createCategory("Categoria");
        long idNota = mDbHelper.createNote("Titulo","Cuerpo",(int)idCat);
        assertTrue(idNota != -1);
    }

    public void testCrearNotaCategoriaInexistente() {
        long idNota = mDbHelper.createNote("Titulo","Cuerpo",-1);
        assertTrue(idNota == -1);
    }

    public void testModificarCategoriaDeNotaAExistente() {
        long idCat = mDbHelper.createCategory("Categoria");
        long idNota = mDbHelper.createNote("Titulo","Ciuerpo",0);
        boolean updated = mDbHelper.updateNote(idNota,"Titulo","Cuerpo", (int)idCat);
        assertTrue(updated);
    }

    public void testModificarCategoriaDeNotaACero() {
        long idCat = mDbHelper.createCategory("Categoria");
        long idNota = mDbHelper.createNote("Titulo","Ciuerpo",(int)idCat);
        boolean updated = mDbHelper.updateNote(idNota,"Titulo","Cuerpo", 0);
        assertTrue(updated);
    }
    public void testModificarCategoriaDeNotaAInexistente() {
        long idCat = mDbHelper.createCategory("Categoria");
        long idNota = mDbHelper.createNote("Titulo","Ciuerpo",(int)idCat);
        boolean updated = mDbHelper.updateNote(idNota,"Titulo","Cuerpo", -1);
        assertFalse(updated);
    }


    @Override
    protected void tearDown() throws Exception{
        for (long i = originalRows; i < mDbHelper.getNumberOfNotes(); i++) {
            mNotepad.getNotesDbAdapter().deleteNote(i);
        }
        super.tearDown();
    }
}