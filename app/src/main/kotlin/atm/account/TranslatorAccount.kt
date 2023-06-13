package atm.account

fun translateAccountMessage(allDigitsMessage: ValidatorErrors): String =
        when (allDigitsMessage) {
            ValidatorErrors.ALL_DIGITS -> "Account Number should only contains numbers for invalid Account Number"
            ValidatorErrors.SIX_DIGITS -> "Account Number should have 6 digits length for invalid Account Number"
        }


fun translatePINMessage(allDigitsMessage: ValidatorErrors): String =
        when (allDigitsMessage) {
            ValidatorErrors.ALL_DIGITS -> "PIN should only contains numbers for invalid PIN"
            ValidatorErrors.SIX_DIGITS -> "PIN should have 6 digits length for invalid PIN"
        }
