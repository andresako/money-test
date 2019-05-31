package com.example.minimoneybox.viewmodel.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.repository.Repository

class LoginFactory(
    private val repository: Repository,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        LoginViewModel(repository, sharedPreferences) as T
}