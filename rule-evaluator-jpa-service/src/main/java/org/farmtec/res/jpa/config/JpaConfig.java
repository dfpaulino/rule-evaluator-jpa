package org.farmtec.res.jpa.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by dp on 31/01/2021
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages ="org.farmtec.res.jpa.repositories" )
@EntityScan(basePackages = "org.farmtec.res.jpa.model")
@ComponentScan(basePackages = "org.farmtec.res.jpa")
public class JpaConfig {

}
