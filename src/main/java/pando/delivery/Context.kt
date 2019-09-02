package pando.delivery

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pando.domain.*
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
class Context {

    // Services
    @Bean
    fun matchsService(matchs: Matchs): MatchsService {
        return MatchsService(matchs)
    }

    @Bean
    fun accountService(): AccountService {
        return AccountService()
    }

    // Repositories
    @Bean
    fun matchs(): Matchs {
        return InMemoryMatchs()
    }

    @Bean
    fun creatureCards(): CreatureCards {
        return CreatureCards()
    }

}


@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
    }
}