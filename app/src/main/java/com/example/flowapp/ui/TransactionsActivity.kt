package com.example.flowapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flowapp.R
import com.example.flowapp.model.Transaction
import com.example.flowapp.ui.adapter.TransactionAdapter

class TransactionsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter()
        recyclerView.adapter = adapter

        val transactions = intent.getSerializableExtra("transactions") as? ArrayList<Transaction>

        transactions?.forEach { transaction ->
            println("Transação recebida: $transaction")
        }

        adapter.submitList(transactions ?: emptyList())

    }
}
