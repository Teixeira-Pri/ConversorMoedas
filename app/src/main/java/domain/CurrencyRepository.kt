package domain


interface CurrencyRepository {
    suspend fun convertCurrency(from: String, to: String, amount: Double): Double
}
