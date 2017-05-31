import android.support.test.espresso.Espresso;

import org.junit.Test;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.R;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;


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

public class Notepadv3PathTest {

    @Rule
    public ActivityTestRule<Notepadv3> mActivityRule =
            new ActivityTestRule<>(Notepadv3.class);

    @Test
    public void testFromNotepadToNoteCreateAndCreateNote() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Añadir nota")).perform(click());
        onView(withId(R.id.body)).perform(replaceText("Cuerpo nota"));
        onView(withId(R.id.confirm)).perform(click());
    }

    @Test
    public void testFromNotepadToNoteCreateAndReturn() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Añadir nota")).perform(click());
        Espresso.pressBack();
        Espresso.pressBack();
    }

    @Test
    public void testFromNotepadToNoteEditbyEditing() {
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
        onView(withText("Editar nota")).perform(click());
        onView(withId(R.id.confirm)).perform(click());
    }

    @Test
    public void testFromNotepadToNoteEditbyEditingAndReturn() {
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
        onView(withText("Editar nota")).perform(click());
        Espresso.pressBack();
        Espresso.pressBack();
    }

    @Test
    public void testFromNotepadToCategoryListAndReturn() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Ver categorías")).perform(click());
        Espresso.pressBack();
    }

    @Test
    public void testFromNotepadToCategoryEditAndAddCategory() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Ver categorías")).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Añadir categoria")).perform(click());
        onView(withId(R.id.title)).perform(replaceText("CategoríaPrueba"));
        onView(withId(R.id.confirm)).perform(click());
    }

    @Test
    public void testFromNotepadToCategoryEditAndReturn() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Ver categorías")).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Añadir categoria")).perform(click());
        Espresso.pressBack();
    }

    @Test
    public void testFromNotepadToCategoryListAndEditCategory() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Ver categorías")).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
        onView(withText("Editar categoría")).perform(click());
        onView(withId(R.id.title)).perform(replaceText("CategoriaPrueba"));
        onView(withId(R.id.confirm)).perform(click());
    }

    @Test
    public void testFromNotepadToCategoryListAndEditNoteWithReturn() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Ver categorías")).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
        onView(withText("Editar categoría")).perform(click());
        Espresso.pressBack();
    }

    @Test
    public void testFromNotepadToCategoryListAndOpenCategory() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Ver categorías")).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(longClick());
    }
}