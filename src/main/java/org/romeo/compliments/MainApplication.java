package org.romeo.compliments;

import static springfox.documentation.builders.PathSelectors.regex;

import org.romeo.compliments.persistence.UserRepository;
import org.romeo.compliments.persistence.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class MainApplication {

    public static void main(String[] args) {
        ApplicationContext cxt = SpringApplication.run(MainApplication.class, args);

        //Seed database with 2 users
        //FIXME: turn off when not using in memory db
        UserRepository userRepository = cxt.getBean(UserRepository.class);
        addTestUsers(userRepository);
    }

    private static void addTestUsers(UserRepository userRepository) {
        List<User> users = new ArrayList<User>(2);
        users.add(new User("Tyler Romeo", "tyler.romeo@example.com", "http://placekitten.com/300/200"));
        users.add(new User("Kevin Welcher", "kevin.welcher@example.com", "http://placekitten.com/200/200"));
        userRepository.save(users);
    }

    @Bean
    public Docket usersApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/users.*|/compliments.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Compliments webservice")
                .description("Webservice to support and persist data for the compliments web app")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
                .version("0.0.1")
                .build();
    }
}
