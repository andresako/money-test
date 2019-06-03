package com.example.minimoneybox.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.datasource.api.RetrofitUtils
import com.example.minimoneybox.repository.Repository
import com.example.minimoneybox.utils.IntentUtils
import com.example.minimoneybox.utils.ResultUtils
import com.example.minimoneybox.viewmodel.individualaccount.IndividualAccountFactory
import com.example.minimoneybox.viewmodel.individualaccount.IndividualAccountViewModel
import kotlinx.android.synthetic.main.activity_individual_account.*

class IndividualAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: IndividualAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_account)

        val accountId = intent.extras?.getInt(IntentUtils.KEY_ID)
        val accountName = intent.extras?.getString(IntentUtils.KEY_NAME)
        val accountPlanValue = intent.extras?.getString(IntentUtils.KEY_PLAN_VALUE)
        val accountMoneyboxValue = intent.extras?.getString(IntentUtils.KEY_MONEYBOX_VALUE)

        val factory =
            IndividualAccountFactory(
                Repository(
                    RetrofitUtils.createService(),
                    PreferenceManager.getDefaultSharedPreferences(this)
                )
            )
        viewModel = ViewModelProviders.of(this, factory).get(IndividualAccountViewModel::class.java)

        tv_individual_account_title.text = accountName
        tv_plan_value_value.text = accountPlanValue
        tv_moneybox_value_value.text = accountMoneyboxValue
        btn_add_value.setOnClickListener {
            if (accountId is Int) {
                shouldEnableButton(false)
                viewModel.payment(10.0, accountId)
            }
        }
        viewModel.paymentResult.observe(this, Observer {
            tv_moneybox_value_value.text = it.toString()
        })
        viewModel.paymentError.observe(this, Observer {
            shouldEnableButton(true)
            when (it) {
                ResultUtils.KEY_ERROR -> Toast.makeText(
                    this,
                    getString(R.string.payment_error),
                    Toast.LENGTH_LONG
                ).show()
                else -> Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun shouldEnableButton(enable: Boolean) {
        btn_add_value.isEnabled = enable
    }

}
