package org.farmtec.res.autoconfiguration;


import org.farmtec.res.service.RuleServiceImpl;
import org.farmtec.res.service.model.Rule;
import org.farmtec.res.service.rule.loader.RuleLoaderService;
import org.farmtec.res.service.rule.loader.RuleLoaderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by dp on 13/02/2021
 */
class ResJpaAutoConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestUserConfig.class)
            .withConfiguration(AutoConfigurations.of(ResJpaAutoConfiguration.class));

    @Test
    public void testConfiguration() {
        this.contextRunner
                .withPropertyValues("spring.datasource.url=jdbc:h2:mem:testdb")
                .withPropertyValues("spring.datasource.driverClassName=org.h2.Driver")
                .run((context) ->{
           assertThat(context.getBean("ruleLoaderService")).isNotNull();
           assertThat(context.getBean("ruleLoaderService")).isInstanceOf(RuleLoaderServiceImpl.class);
           assertThat(context.getBean("ruleService")).isNotNull();
        });

    }

    @Test
    public void testConfiguration_WhenNoClass() {
        this.contextRunner
                .withPropertyValues("spring.datasource.url=jdbc:h2:mem:testdb")
                .withPropertyValues("spring.datasource.driverClassName=org.h2.Driver")
                .withClassLoader(new FilteredClassLoader(RuleServiceImpl.class))
                .run((context) -> assertThat(context).doesNotHaveBean("ruleLoaderService"));

    }

    @Test
    public void testConfiguration_WhenDefineCustomeClass() {
        this.contextRunner
                .withPropertyValues("spring.datasource.url=jdbc:h2:mem:testdb")
                .withPropertyValues("spring.datasource.driverClassName=org.h2.Driver")
                .withUserConfiguration(MyConfig.class)
                .run((context) ->{
                    assertThat(context).hasBean("ruleLoaderService");
                    assertThat(context.getBean("ruleLoaderService")).isInstanceOf(MyRuleLoaderService.class);
                });

    }


    @Configuration(proxyBeanMethods = false)
    public static class MyConfig {

        @Bean
        public RuleLoaderService ruleLoaderService() {
            return new MyRuleLoaderService();
        }
    }
    public static class MyRuleLoaderService implements RuleLoaderService {

        @Override
        public List<Rule> getRules() {
            return null;
        }

        @Override
        public Future<Boolean> refreshRules() {
            return null;
        }

        @Override
        public Date getLastUpdateTime() {
            return null;
        }

        @Override
        public boolean isLoadRuleDone() {
            return false;
        }

        @Override
        public boolean isLoadRuleSuccess() {
            return false;
        }
    }


}