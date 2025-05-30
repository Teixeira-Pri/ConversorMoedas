package presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.priscilateixeira.exchange.R
import repository.CurrencyRepositoryImpl
import presentation.viewmodel.CurrencyViewModel
import presentation.viewmodel.CurrencyViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = CurrencyRepositoryImpl()
        val factory = CurrencyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CurrencyViewModel::class.java]

        val fromSpinner: Spinner = findViewById(R.id.fromSpinner)
        val toSpinner: Spinner = findViewById(R.id.toSpinner)
        val amountEditText: EditText = findViewById(R.id.editAmount)
        val convertButton: Button = findViewById(R.id.btnConvert)
        val resultTextView: TextView = findViewById(R.id.textResult)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        // Lista de moedas
        val currencies = listOf("USD", "EUR", "BRL", "JPY")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromSpinner.adapter = adapter
        toSpinner.adapter = adapter

        convertButton.setOnClickListener {
            val from = fromSpinner.selectedItem.toString()
            val to = toSpinner.selectedItem.toString()
            val amountText = amountEditText.text.toString()

            if (amountText.isEmpty()) {
                Toast.makeText(this, "Informe o valor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0.0) {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.convertCurrency(from, to, amount)
        }

        // Observers
        viewModel.conversionResult.observe(this, Observer { result ->
            resultTextView.text = "Resultado: %.2f".format(result)
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }
}
