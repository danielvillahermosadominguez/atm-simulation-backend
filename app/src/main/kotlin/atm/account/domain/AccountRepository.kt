package atm.account.domain

interface AccountRepository {
    fun save(account: Account)
    fun findById(s: String): Account?

    companion object {
        fun apply() = object : AccountRepository {
            override fun save(account: Account) {
                TODO()
            }

            override fun findById(s: String): Account? {
                TODO("Not yet implemented")
            }
        }
    }
}



