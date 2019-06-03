package com.example.minimoneybox.viewmodel.login

import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.datasource.model.LoginDto
import com.example.minimoneybox.repository.Repository
import com.example.minimoneybox.repository.ResponseResult
import com.example.minimoneybox.utils.ResultUtils
import com.example.minimoneybox.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginViewModel(
    private val repository: Repository
) : BaseViewModel() {

    companion object {
        const val EMAIL_REGEX = "[^@]+@[^.]+\\..+"
        const val NAME_REGEX = "[a-zA-Z]{6,30}"
        const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z]).{10,50}$"
        val firstAnimation = 0 to 109
        val secondAnimation = 131 to 158
    }

    val loginResult = MutableLiveData<String>()

    fun loginUser(email: String, password: String) {
        uiScope.launch {
            val result = repository.getTokenRemote(email, password)
            when (result) {
                is ResponseResult.Success -> {
                    if (result.data is LoginDto) {
                        saveUserToken(result.data.session.bearerToken)
                        loginResult.postValue(ResultUtils.KEY_OK)
                    } else {
                        loginResult.postValue(ResultUtils.KEY_ERROR)
                    }
                }
                is ResponseResult.Error -> loginResult.postValue(result.exception.message)
            }
        }
    }

    fun saveUserName(name: String) {
        repository.saveUserNameLocal(name)
    }

    fun saveUserToken(token: String) {
        repository.saveTokenLocal(token)
    }

    fun isValidEmail(email: String): Boolean = Pattern.matches(EMAIL_REGEX, email)
    fun isValidPassword(password: String): Boolean = Pattern.matches(PASSWORD_REGEX, password)
    fun isValidName(name: String): Boolean =
        (Pattern.matches(NAME_REGEX, name) or name.isEmpty())
}