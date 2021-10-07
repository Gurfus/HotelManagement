package ferranechaves.ioc.HolidayManagement;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegistreIncomplert {

    @Rule
    public ActivityTestRule<Benvinguda> mActivityTestRule = new ActivityTestRule<>(Benvinguda.class);

    @Test
    public void registreIncomplert() throws InterruptedException {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnRegistre), withText("Registre"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.txtRegis), withText("Registre"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        4),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.statusRegistre), withText("Email es necesari"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Email es necesari")));
        Thread.sleep(1000);
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.userR),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        3),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("ferran1@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.txtRegis), withText("Registre"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        4),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.statusRegistre), withText("El nom es necesari"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText("El nom es necesari")));
        Thread.sleep(1000);
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.txtnames),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        3),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("marc"), closeSoftKeyboard());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.txtRegis), withText("Registre"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        4),
                                0),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.statusRegistre), withText("La contrasenya es necesaria"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("La contrasenya es necesaria")));
        Thread.sleep(1000);
        ViewInteraction textView4 = onView(
                allOf(withId(R.id.statusRegistre), withText("La contrasenya es necesaria"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView4.check(matches(withText("La contrasenya es necesaria")));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.pwdR),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.txtRegis), withText("Registre"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        4),
                                0),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.statusRegistre), withText("El cognom es necesari"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView5.check(matches(withText("El cognom es necesari")));
        Thread.sleep(1000);
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.userCognom),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        3),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("ech"), closeSoftKeyboard());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.txtRegis), withText("Registre"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        4),
                                0),
                        isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.statusRegistre), withText("El telèfon es necesari"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView6.check(matches(withText("El telèfon es necesari")));
        Thread.sleep(1000);
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
