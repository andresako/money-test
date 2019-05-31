package com.example.minimoneybox.viewmodel.login

import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.datasource.model.LoginResponse
import com.example.minimoneybox.repository.Repository
import com.example.minimoneybox.repository.ResponseResult
import com.example.minimoneybox.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : BaseViewModel() {

    val loginResult = MutableLiveData<ResponseResult<LoginResponse>>()

    fun loginUser(email: String, password: String) {
        uiScope.launch {
            loginResult.postValue(repository.getToken(email, password))
        }
    }
}