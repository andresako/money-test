package com.example.minimoneybox.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimoneybox.R
import com.example.minimoneybox.datasource.api.RetrofitUtils
import com.example.minimoneybox.datasource.model.InvestorProductsDto
import com.example.minimoneybox.datasource.model.ProductResponsesEntity
import com.example.minimoneybox.repository.Repository
import com.example.minimoneybox.viewmodel.useraccount.UserAccountFactory
import com.example.minimoneybox.viewmodel.useraccount.UserAccountViewModel
import kotlinx.android.synthetic.main.activity_user_account.*
import kotlinx.android.synthetic.main.card_individual_account.view.*

class UserAccountActivity : AppCompatActivity(), OnItemInteractionListener {

    private lateinit var viewModel: UserAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account)

        val factory =
            UserAccountFactory(
                Repository(
                    RetrofitUtils.createService(),
                    PreferenceManager.getDefaultSharedPreferences(this)
                )
            )
        viewModel = ViewModelProviders.of(this, factory).get(UserAccountViewModel::class.java)

        setupViews()
        viewModel.getInvestorsProducts()
    }

    private fun setupViews() {
        viewModel.investorProductsResult.observe(this, Observer {
            updateUI(it)
            shouldShowLoading(false)
        })
        viewModel.investorProductsError.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            shouldShowLoading(false)
        })
        rv_individual_product.layoutManager = LinearLayoutManager(this)
        rv_individual_product.adapter = ProductAdapter(emptyList(), this)
    }

    private fun updateUI(products: InvestorProductsDto) {
        val userName = viewModel.getUserName()
        if (!userName.isNullOrEmpty()) {
            tv_hello.visibility = View.VISIBLE
            tv_hello.text = getString(R.string.hello_user, userName)
        }
        tv_total_plan_value.text = products.totalPlanValue.toString()
        rv_individual_product.adapter = ProductAdapter(products.productResponsesEntity, this)
    }

    private fun shouldShowLoading(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    override fun onClick(position: Int) {
        val productName = (rv_individual_product.adapter as ProductAdapter).dataset[position].product.name
        Toast.makeText(this, productName, Toast.LENGTH_LONG).show()
    }
}

class ProductAdapter(var dataset: List<ProductResponsesEntity>, private val listener: OnItemInteractionListener) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(
        val view: View,
        private val listener: OnItemInteractionListener
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClick(layoutPosition)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_individual_account, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.card_title.text = dataset[position].product.name
        holder.view.card_plan_value_value.text = dataset[position].planValue.toString()
        holder.view.card_moneybox_value_value.text = dataset[position].moneybox.toString()
    }
}

interface OnItemInteractionListener {
    fun onClick(position: Int)
}
