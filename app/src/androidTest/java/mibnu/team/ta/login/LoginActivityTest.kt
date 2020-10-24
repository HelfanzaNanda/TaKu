package mibnu.team.ta.login

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import mibnu.team.ta.R
import mibnu.team.ta.activity.LoginActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
@LargeTest
class LoginActivityTest{

    @get:Rule
    var loginActivity : ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun testWithInvalidEmail(){
        val wrongNik = ""
        val etEmail = loginActivity.activity?.findViewById<TextInputEditText>(R.id.et_nik)!!
        setTextToView(wrongNik, etEmail)
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withId(R.id.in_nik)).check(matches(
            hasTextInputLayoutErrorText("nik tidak valid")))
    }



    private fun setTextToView(text: String, textInputEditText: TextInputEditText){
        loginActivity.runOnUiThread {
            textInputEditText.setText(text)
        }
    }
    private fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View?>? {
        return object : TypeSafeMatcher<View?>() {
            override fun matchesSafely(item: View?): Boolean {
                if (item !is TextInputLayout) {
                    return false
                }
                val error = item.error ?: return false
                val hint = error.toString()
                return expectedErrorText == hint
            }

            override fun describeTo(description: Description?) {}
        }
    }

    private fun hasNoErrorText(): Matcher<View?>? {
        return object : BoundedMatcher<View?, TextInputLayout>(TextInputLayout::class.java) {
            override fun describeTo(description: Description) {}
            override fun matchesSafely(view: TextInputLayout): Boolean {
                return view.error == null
            }
        }
    }

}
