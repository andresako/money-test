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
import com.example.minimoneybox.viewmodel.individualaccount.IndividualAccountFactory
import com.example.minimoneybox.viewmodel.individualaccount.IndividualAccountViewModel
import kotlinx.android.synthetic.main.activity_individual_account.*

class IndividualAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: IndividualAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_account)

        val accountId = intent.extras?.getInt("ID")
        val accountName = intent.extras?.getString("NAME")
        val accountPlanValue = intent.extras?.getString("PLAN_VALUE")
        val accountMoneyboxValue = intent.extras?.getString("MONEYBOX_VALUE")

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
            if (accountId is Int)
                viewModel.payment(10.0, accountId)
        }
        viewModel.paymentResult.observe(this, Observer {
            tv_moneybox_value_value.text = it.toString()
        })
        viewModel.paymentError.observe(this, Observer {
            when(it){
                "KO" -> Toast.makeText(this, "Payment Error", Toast.LENGTH_LONG).show()
                else -> Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }
}
