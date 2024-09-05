package com.example.flowapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flowapp.R
import com.example.flowapp.model.Transaction


class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(
    TransactionDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)

        println("Exibindo transação: $transaction")
    }


    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewType: TextView = itemView.findViewById(R.id.textViewType)
        private val textViewDetail: TextView = itemView.findViewById(R.id.textViewDetail)
        private val textViewValue: TextView = itemView.findViewById(R.id.textViewValue)
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)

        fun bind(transaction: Transaction) {
            textViewType.text = transaction.type
            textViewDetail.text = transaction.detail
            textViewValue.text = "R$ ${"%.2f".format(transaction.value)}"
            textViewValue.setTextColor(if(transaction.type.equals("Crédito")) { Color.GREEN} else {Color.RED})
            textViewDate.text = transaction.date
        }
    }

    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}
