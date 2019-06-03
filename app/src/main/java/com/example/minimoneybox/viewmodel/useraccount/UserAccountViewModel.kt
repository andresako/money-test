package com.example.minimoneybox.viewmodel.useraccount

import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.datasource.model.InvestorProductsDto
import com.example.minimoneybox.repository.Repository
import com.example.minimoneybox.repository.ResponseResult
import com.example.minimoneybox.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class UserAccountViewModel(
    private val repository: Repository
) : BaseViewModel() {

    val investorProductsResult = MutableLiveData<InvestorProductsDto>()
    val investorProductsError = MutableLiveData<String>()

    fun getInvestorsProducts() {
        uiScope.launch {
            val result = repository.getInvestorProductsRemote()
            when (result) {
                is ResponseResult.Success -> {
                    investorProductsResult.postValue(result.data)
                }
                is ResponseResult.Error -> investorProductsError.postValue(result.exception.message)
            }
        }
    }

    fun getUserName(): String? {
        return repository.getUserNameLocal()
    }
}