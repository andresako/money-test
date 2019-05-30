package com.example.minimoneybox

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import java.util.regex.Pattern

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    lateinit var btn_sign_in: Button
    lateinit var til_email: TextInputLayout
    lateinit var et_email: EditText
    lateinit var til_password: TextInputLayout
    lateinit var et_password: EditText
    lateinit var til_name: TextInputLayout
    lateinit var et_name: EditText
    lateinit var pigAnimation: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        setupAnimation()
    }

    private fun setupViews() {
        btn_sign_in = findViewById(R.id.btn_sign_in)
        til_email = findViewById(R.id.til_email)
        et_email = findViewById(R.id.et_email)
        til_password = findViewById(R.id.til_password)
        et_password = findViewById(R.id.et_password)
        til_name = findViewById(R.id.til_name)
        et_name = findViewById(R.id.et_name)
        pigAnimation = findViewById(R.id.animation)

        btn_sign_in.setOnClickListener {
            if (allFieldsValid()) {
                Toast.makeText(this, R.string.input_valid, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun allFieldsValid(): Boolean {
        til_email.error = if (isValidEmail()) null else getString(R.string.email_address_error)
        til_password.error = if (isValidPassword()) null else getString(R.string.password_error)
        til_name.error = if (isValidName()) null else getString(R.string.full_name_error)

        return isValidEmail() && isValidPassword() && isValidName()
    }

    private fun isValidEmail(): Boolean = Pattern.matches(EMAIL_REGEX, et_email.text.toString())
    private fun isValidPassword(): Boolean = Pattern.matches(PASSWORD_REGEX, et_password.text.toString())
    private fun isValidName(): Boolean = (Pattern.matches(NAME_REGEX, et_name.text.toString()) or et_name.text.isEmpty())

    private fun setupAnimation() {
        pigAnimation.playAnimation()

        pigAnimation.addAnimatorUpdateListener {
            if (pigAnimation.frame == firstAnim.second) {
                pigAnimation.setMinAndMaxFrame(secondAnim.first, secondAnim.second)
            }
        }
    }

    companion object {
        const val EMAIL_REGEX = "[^@]+@[^.]+\\..+"
        const val NAME_REGEX = "[a-zA-Z]{6,30}"
        const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z]).{10,50}$"
        val firstAnim = 0 to 109
        val secondAnim = 131 to 158
    }
}
