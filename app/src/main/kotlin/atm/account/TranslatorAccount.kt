package atm.account

private const val ALL_DIGITS_MESSAGE = "Account Number should only contains numbers for invalid Account Number"

private const val SIX_DIGITS_MESSAGE = "Account Number should have 6 digits length for invalid Account Number"

fun translateAccountMessage(allDigitsMessage: ValidatorErrors): String =
    when(allDigitsMessage) {
        ValidatorErrors.ALL_DIGITS -> ALL_DIGITS_MESSAGE
        ValidatorErrors.SIX_DIGITS -> SIX_DIGITS_MESSAGE
    }
