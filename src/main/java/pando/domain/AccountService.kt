package pando.domain

import java.util.*

class AccountService {

    //pasar a repositorio de cuentas
    private val accounts = HashMap<String, Account>()

    fun create(secret: String): Account {

        var accountId = UUID.randomUUID().toString().replace("-".toRegex(), "")
        val encodedSecret = encode(secret)

        accountId += encodedSecret

        val acc = Account(accountId, encodedSecret, 1, 0)

        accounts[accountId] = acc

        return acc
    }

    fun recover(accountId: String, oldSecret: String, newSecret: String): Account? {
        val acc = accounts[accountId]
        acc?.let {
            val encodedOldSecret = encode(oldSecret)

            if (acc.secret.equals(encodedOldSecret)) {
                accounts.remove(accountId)

                var accountId = accountId.substring(0, accountId.length - encodedOldSecret.length)

                val encodedSecret = encode(newSecret)

                accountId += encodedSecret

                val account = Account(accountId, encodedSecret, acc.level, acc.wins)

                accounts[accountId] = account

                return account
            }
        }

        return null
    }


    private fun encode(secret: String): String {
        val bytes = secret.toByteArray()
        var value = 0
        var encoded = ""

        for (i in bytes.indices) {
            encoded += bytes[i]

            if (encoded.length > 6) {
                value += Integer.parseInt(encoded) % 1000000000
                encoded = ""
            }
        }

        return value.toString().replace("-".toRegex(), "")
    }

}
