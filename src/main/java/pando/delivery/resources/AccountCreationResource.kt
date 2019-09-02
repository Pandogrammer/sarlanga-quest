package pando.delivery.resources

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pando.domain.AccountService

@RestController
@RequestMapping("account")
class AccountCreationResource(private val accountService: AccountService) {

    @PostMapping
    fun generate(@RequestBody secret: String): String {
        val account = accountService.create(secret)
        println("Creada la cuenta: "+account.accountId)

        return account.accountId
    }

}
