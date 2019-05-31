package com.example.minimoneybox.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.datasource.api.RetrofitUtils
import com.example.minimoneybox.repository.Repository
import com.example.minimoneybox.repository.ResponseResult
import com.example.minimoneybox.viewmodel.login.LoginFactory
import com.example.minimoneybox.viewmodel.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val factory =
            LoginFactory(Repository(RetrofitUtils.createService()), PreferenceManager.getDefaultSharedPreferences(this))
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)

        setupViews()
    }

    override fun onStart() {
        super.onStart()
        setupAnimation()
        showLoading(false)
    }

    private fun setupViews() {

        viewModel.loginResult.observe(this, Observer {
            showLoading(false)
            when (it) {
                is ResponseResult.Success -> {
                    val token = it.data?.Session?.BearerToken!!
                    viewModel.saveToken(token)
                    viewModel.saveName(et_name.text.toString())

                    startActivity(Intent(this@LoginActivity, UserAccountActivity::class.java))
                }
                is ResponseResult.Error -> Toast.makeText(this, "Login fail", Toast.LENGTH_LONG).show()
            }
        })

        btn_sign_in.setOnClickListener {
            if (allFieldsValid()) {
                showLoading(true)
                Toast.makeText(this, R.string.input_valid, Toast.LENGTH_LONG).show()
                viewModel.loginUser(et_email.text.toString().trim(), et_password.text.toString().trim())
            }
        }

        et_email.setText("androidtest@moneyboxapp.com")
        et_password.setText("P455word12")
    }

    private fun allFieldsValid(): Boolean {
        til_email.error = if (isValidEmail()) null else getString(R.string.email_address_error)
        til_password.error = if (isValidPassword()) null else getString(R.string.password_error)
        til_name.error = if (isValidName()) null else getString(R.string.full_name_error)

        return isValidEmail() && isValidPassword() && isValidName()
    }

    private fun isValidEmail(): Boolean = Pattern.matches(EMAIL_REGEX, et_email.text.toString())
    private fun isValidPassword(): Boolean = Pattern.matches(PASSWORD_REGEX, et_password.text.toString())
    private fun isValidName(): Boolean =
        (Pattern.matches(NAME_REGEX, et_name.text.toString()) or et_name.text.isEmpty())

    private fun setupAnimation() {
        pigAnimation.playAnimation()

        pigAnimation.addAnimatorUpdateListener {
            if (pigAnimation.frame == firstAnim.second) {
                pigAnimation.setMinAndMaxFrame(secondAnim.first, secondAnim.second)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val EMAIL_REGEX = "[^@]+@[^.]+\\..+"
        const val NAME_REGEX = "[a-zA-Z]{6,30}"
        const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z]).{10,50}$"
        val firstAnim = 0 to 109
        val secondAnim = 131 to 158
    }
}
