package delivery

import domain.InMemoryMatchs
import domain.MatchsService
import domain.Matchs
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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

    // Repositories
    @Bean
    fun matchs(): Matchs {
        return InMemoryMatchs()
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