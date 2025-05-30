package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import domain.CurrencyRepository
import kotlinx.coroutines.launch

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {

    private val _conversionResult = MutableLiveData<Double>()
    val conversionResult: LiveData<Double> = _conversionResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun convertCurrency(from: String, to: String, amount: Double) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.convertCurrency(from, to, amount)
                _conversionResult.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Erro ao converter: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

