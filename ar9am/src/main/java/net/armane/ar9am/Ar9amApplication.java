package net.armane.ar9am;

import net.armane.ar9am.Security.account.Role;
import net.armane.ar9am.Security.account.RoleValue;
import net.armane.ar9am.entity.Task;
import net.armane.ar9am.Security.account.User;
import net.armane.ar9am.repository.RoleRepository;
import net.armane.ar9am.repository.TaskRepository;
import net.armane.ar9am.repository.UserRepository;
import net.armane.ar9am.service.AccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

@SpringBootApplication
@EnableOpenApi

public class Ar9amApplication  {

    public static void main(String[] args) {
        SpringApplication.run(Ar9amApplication.class, args);
    }

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AccountManager accountManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;


    @PostConstruct
     void init() {
        Logger logger = LoggerFactory.getLogger(Ar9amApplication.class);
        logger.info(" build finish  ");
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Stream.of(
                new Task(1, "karima"),
                new Task(3, "karimva"),
                new Task(12, "karima2")

        ).forEach(taskRepository::save);

        Stream.of(
                new User(null, "admin", "1234", "Armane@armane.net", null),
                new User(null, "test", "1234", "Armane@armane.net", null),
                new User(null, "user", "1234", "Armane@armane.net", null)
        ).forEach(accountManager::saveUser);

        Stream.of(
                new Role(null, RoleValue.ADMIN),
                new Role(null, RoleValue.ASSISTANT),
                new Role(null, RoleValue.TEACHER),
                new Role(null, RoleValue.STUDENT)
        ).forEach(accountManager::saveRole);

        accountManager.addRoleToUser("admin", RoleValue.ADMIN);
        accountManager.addRoleToUser("user", RoleValue.STUDENT);
        accountManager.addRoleToUser("user", RoleValue.TEACHER);
        accountManager.addRoleToUser("test", RoleValue.TEACHER);


    }


    //<editor-fold desc="Beans : Roles, Swagger and BCrypt">
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // config swagger
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.armane.ar9am.controller"))
                .build();
    }

    @Bean(name = "roles")
    RoleValue.Names roleNames() {
        return new RoleValue.Names();
    }
    //</editor-fold>


}
