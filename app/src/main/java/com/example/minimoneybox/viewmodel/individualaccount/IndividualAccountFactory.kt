package com.example.minimoneybox.viewmodel.individualaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.repository.Repository

class IndividualAccountFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        IndividualAccountViewModel(repository) as T
}