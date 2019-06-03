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
import com.example.minimoneybox.utils.ResultUtils
import com.example.minimoneybox.viewmodel.login.LoginFactory
import com.example.minimoneybox.viewmodel.login.LoginViewModel
import com.example.minimoneybox.viewmodel.login.LoginViewModel.Companion.firstAnimation
import com.example.minimoneybox.viewmodel.login.LoginViewModel.Companion.secondAnimation
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val factory =
            LoginFactory(
                Repository(
                    RetrofitUtils.createService(),
                    PreferenceManager.getDefaultSharedPreferences(this)
                )
            )
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)

        setupViews()
    }

    override fun onStart() {
        super.onStart()
        setupAnimation()
        shouldShowLoading(false)
    }

    private fun setupViews() {
        viewModel.loginResult.observe(this, Observer {
            shouldShowLoading(false)
            when (it) {
                ResultUtils.KEY_OK -> {
                    viewModel.saveUserName(et_name.text.toString())
                    startActivity(Intent(this@LoginActivity, UserAccountActivity::class.java))
                    finish()
                }
                ResultUtils.KEY_ERROR -> Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_LONG).show()
                else -> Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        btn_sign_in.setOnClickListener {
            if (allFieldsValid()) {
                shouldShowLoading(true)
                viewModel.loginUser(et_email.text.toString().trim(), et_password.text.toString().trim())
            }
        }

    }

    private fun allFieldsValid(): Boolean {
        val isValidEmail = viewModel.isValidEmail(et_email.text.toString())
        val isValidName = viewModel.isValidName(et_name.text.toString())
        val isValidPassword = viewModel.isValidPassword(et_password.text.toString())

        til_email.error = if (isValidEmail) null else getString(R.string.email_address_error)
        til_password.error = if (isValidPassword) null else getString(R.string.password_error)
        til_name.error = if (isValidName) null else getString(R.string.full_name_error)

        return isValidEmail && isValidPassword && isValidName
    }


    private fun setupAnimation() {
        pigAnimation.playAnimation()

        pigAnimation.addAnimatorUpdateListener {
            if (pigAnimation.frame == firstAnimation.second) {
                pigAnimation.setMinAndMaxFrame(secondAnimation.first, secondAnimation.second)
            }
        }
    }

    private fun shouldShowLoading(show: Boolean) {
        btn_sign_in.isEnabled = !show
        loading.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }
}
