package repository

import android.util.Log
import domain.CurrencyRepository
import data.remote.RetrofitInstance

class CurrencyRepositoryImpl : CurrencyRepository {

    override suspend fun convertCurrency(from: String, to: String, amount: Double): Double {
        Log.d("CurrencyRepository", "Enviando: de $from para $to, valor: $amount")

        val response = RetrofitInstance.api.convertCurrency(from, to, amount)

        if (response.isSuccessful) {
            val body = response.body()
            Log.d("CurrencyRepository", "Resposta da API: $body")

            if (body != null && body.success) {
                return body.result
            } else {
                throw Exception("Resposta inválida ou erro na conversão.")
            }
        } else {
            val errorBody = response.errorBody()?.string()
            Log.e("CurrencyRepository", "Erro da API: $errorBody")
            throw Exception("Erro na chamada da API: ${response.code()}")
        }
    }
}

