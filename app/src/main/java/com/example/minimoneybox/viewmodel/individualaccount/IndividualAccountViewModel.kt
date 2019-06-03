package com.example.minimoneybox.viewmodel.individualaccount

import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.datasource.model.OnePaymentDto
import com.example.minimoneybox.repository.Repository
import com.example.minimoneybox.repository.ResponseResult
import com.example.minimoneybox.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class IndividualAccountViewModel(
    private val repository: Repository
) : BaseViewModel() {

    var paymentResult = MutableLiveData<Double>()
    var paymentError = MutableLiveData<String>()

    fun payment(amount: Double, id: Int) {
        uiScope.launch {
            val result = repository.addPayment(amount, id)
            when (result) {
                is ResponseResult.Success -> {
                    if (result.data is OnePaymentDto)
                        paymentResult.postValue(result.data.moneybox)
                    else paymentError.postValue("KO")
                }
                is ResponseResult.Error -> {
                    paymentError.postValue(result.exception.message)
                }
            }
        }
    }
}