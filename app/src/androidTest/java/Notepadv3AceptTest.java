import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class Notepadv3AceptTest {

    @Rule
    public ActivityTestRule<Notepadv3> mActivityRule =
            new ActivityTestRule<>(Notepadv3.class);


    @Test
    public void testCrearNota() {
        sleep();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleep();
        onView(withText("Añadir nota")).perform(click());
        sleep();
        onView(withId(R.id.title)).perform(replaceText("Mi titulo para la nota"));
        sleep();
        onView(withId(R.id.body)).perform(replaceText("Mi cuerpo para la nota"));
        sleep();
        onView(withId(R.id.confirm)).perform(click());
        sleep();
    }

    @Test
    public void testModificarNota() {
        sleep();
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
        sleep();
        onView(withText("Editar nota")).perform(click());
        sleep();
        onView(withId(R.id.body)).perform(replaceText("Mi cuerpo modificado"));
        sleep();
        onView(withId(R.id.confirm)).perform(click());
        sleep();
    }

    @Test
    public void testEliminarNota() {
        sleep();
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
        sleep();
        onView(withText("Eliminar nota")).perform(click());
        sleep();
    }

    @Test
    public void testCrearCategoria() {
        sleep();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleep();
        onView(withText("Ver categorías")).perform(click());
        sleep();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleep();
        onView(withText("Añadir categoria")).perform(click());
        sleep();
        onView(withId(R.id.title)).perform(replaceText("MiCategoria"));
        sleep();
        onView(withId(R.id.confirm)).perform(click());
        sleep();
    }

    @Test
    public void testEditarCategoria() {
        sleep();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleep();
        onView(withText("Ver categorías")).perform(click());
        sleep();
        onView(withText("MiCategoria")).perform(longClick());
        sleep();
        onView(withText("Editar categoría")).perform(click());
        sleep();
        onView(withId(R.id.title)).perform(replaceText("MiCategoriaEditada"));
        sleep();
        onView(withId(R.id.confirm)).perform(click());
        sleep();
    }

    @Test
    public void eliminarCategoria() {
        sleep();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleep();
        onView(withText("Ver categorías")).perform(click());
        sleep();
        onView(withText("MiCategoriaEditada")).perform(longClick());
        sleep();
        onView(withText("Eliminar categoria")).perform(click());
        sleep();
    }

    @Test
    public void testAsignarCategoria() {
        sleep();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleep();
        onView(withText("Añadir nota")).perform(click());
        sleep();
        onView(withId(R.id.title)).perform(replaceText("Mi titulo para la nota"));
        sleep();
        onView(withId(R.id.spinner)).perform(click());
        sleep();
        onView(withText("CategoriaBasica1")).perform(click());
        sleep();
        onView(withId(R.id.body)).perform(replaceText("Mi cuerpo para la nota"));
        sleep();
        onView(withId(R.id.confirm)).perform(click());
        sleep();
    }

    @Test
    public void testModificarCategoriaAsignada() {
        sleep();
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
        sleep();
        onView(withText("Editar nota")).perform(click());
        sleep();
        onView(withId(R.id.spinner)).perform(click());
        sleep();
        onView(withText("CategoriaBasica2")).perform(click());
        sleep();
        onView(withId(R.id.confirm)).perform(click());
        sleep();
    }



    @Test
    public void testEliminarCategoriaAsignada() {
        sleep();
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
        sleep();
        onView(withText("Editar nota")).perform(click());
        sleep();
        onView(withId(R.id.spinner)).perform(click());
        sleep();
        onView(withText("Sin categoria")).perform(click());
        sleep();
    }


    @Test
    public void testFiltrarNotasPorCategoria() {
        sleep();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleep();
        onView(withText("Ver categorías")).perform(click());
        sleep();
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());
        sleep();
    }

    @Test
    public void testOrdenarNotasPorTitulo() {
        sleep();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleep();
        onView(withText("Ordenar por título")).perform(click());
        sleep();
    }

    @Test
    public void testOrdenarNotasPorCategoria() {
        sleep();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleep();
        onView(withText("Ordenar por categoría")).perform(click());
        sleep();
    }

    @Test
    public void enviarNota() {
        sleep();
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
        sleep();
        onView(withText("Enviar nota")).perform(click());
        sleep();
    }

    private void sleep() {
        try{
          Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}