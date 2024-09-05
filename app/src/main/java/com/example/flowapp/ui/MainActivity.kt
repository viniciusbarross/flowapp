package com.example.flowapp.ui

import TransactionRepository
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.example.flowapp.R
import com.example.flowapp.model.Transaction

import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerTipo: Spinner
    private lateinit var spinnerDetalhe: Spinner
    private lateinit var editTextValor: EditText
    private lateinit var editTextDataLanc: EditText
    private lateinit var buttonLanc: Button
    private lateinit var buttonVerLancamentos: Button
    private lateinit var buttonSaldo: Button

    private val repository by lazy { TransactionRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerTipo = findViewById(R.id.spinnerTipo)
        spinnerDetalhe = findViewById(R.id.spinnerDetalhe)
        editTextValor = findViewById(R.id.editTextValor)
        editTextDataLanc = findViewById(R.id.editTextDataLanc)
        buttonLanc = findViewById(R.id.buttonLanc)
        buttonVerLancamentos = findViewById(R.id.buttonVerLancamentos)
        buttonSaldo = findViewById(R.id.buttonSaldo)

        setupSpinners()
        setupDateField()

        buttonLanc.setOnClickListener { handleLaunch() }
        buttonVerLancamentos.setOnClickListener { handleViewLaunches() }
        buttonSaldo.setOnClickListener { handleCheckBalance() }
    }

    private fun setupSpinners() {
        val tipoAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.tipo_array,
            android.R.layout.simple_spinner_item
        )
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = tipoAdapter

        val detalheAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.detalhe_credito_array,
            android.R.layout.simple_spinner_item
        )
        detalheAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDetalhe.adapter = detalheAdapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val tipo = spinnerTipo.selectedItem.toString()
                val detalheArray = if (tipo == "Crédito") {
                    R.array.detalhe_credito_array
                } else {
                    R.array.detalhe_debito_array
                }

                val detalheAdapter = ArrayAdapter.createFromResource(
                    this@MainActivity,
                    detalheArray,
                    android.R.layout.simple_spinner_item
                )
                detalheAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDetalhe.adapter = detalheAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun setupDateField() {
        editTextDataLanc.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                editTextDataLanc.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun handleLaunch() {
        val tipo = spinnerTipo.selectedItem.toString()
        val detalhe = spinnerDetalhe.selectedItem.toString()
        val valor = editTextValor.text.toString().toDoubleOrNull() ?: 0.0
        val dataLanc = editTextDataLanc.text.toString()

        val transaction = Transaction(type = tipo, detail = detalhe, value = valor, date = dataLanc)
        repository.addTransaction(transaction)
        clearFields()
    }

    private fun handleViewLaunches() {
        val transactions = repository.getAllTransactions()
        println("Transações no banco de dados: $transactions")
        if (transactions.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Lançamentos")
                .setMessage("Nenhum lançamento encontrado")
                .setPositiveButton("OK", null)
                .show()
        } else {
            transactions.forEach { transaction ->
                println("Transação: $transaction")
            }
            val intent = Intent(this, TransactionsActivity::class.java).apply {
                putExtra("transactions", ArrayList(transactions))
            }
            startActivity(intent)
        }
    }




    private fun handleCheckBalance() {
        val balance = repository.getBalance()
        val message = "Saldo atual: R$ ${"%.2f".format(balance)}"

        val color = if (balance >= 0) {
            android.graphics.Color.GREEN
        } else {
            android.graphics.Color.RED
        }

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Saldo")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
        alertDialog.findViewById<TextView>(android.R.id.message)?.setTextColor(color)
    }


    private fun clearFields() {
        editTextValor.text.clear()
        editTextDataLanc.text.clear()
        spinnerTipo.setSelection(0)
        spinnerDetalhe.setSelection(0)
    }
}
