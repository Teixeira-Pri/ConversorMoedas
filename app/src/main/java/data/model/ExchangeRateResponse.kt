package data.model

data class ExchangeRateResponse(
    val success: Boolean,
    val result: Double,
    val query: Query?,
    val info: Info?
)

data class Query(
    val from: String,
    val to: String,
    val amount: Double
)

data class Info(
    val rate: Double
)

