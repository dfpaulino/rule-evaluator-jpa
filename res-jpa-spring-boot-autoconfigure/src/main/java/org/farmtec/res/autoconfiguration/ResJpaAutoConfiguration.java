package org.farmtec.res.autoconfiguration;

import org.farmtec.res.jpa.config.JpaConfig;
import org.farmtec.res.service.RuleService;
import org.farmtec.res.service.RuleServiceImpl;
import org.farmtec.res.service.rule.loader.RuleLoaderService;
import org.farmtec.res.service.rule.loader.RuleLoaderServiceImpl;
import org.farmtec.res.service.rule.loader.RulesParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by dp on 13/02/2021
 */
@Configuration
@EnableConfigurationProperties(RuleServiceProperties.class)
@ConditionalOnClass(RuleServiceImpl.class)
@Import(JpaConfig.class)
public class ResJpaAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public RuleLoaderService ruleLoaderService(RulesParser rulesParser) {
        return new RuleLoaderServiceImpl(rulesParser);
    }
    @Bean
    @ConditionalOnMissingBean
    public RuleService ruleService(RuleLoaderService ruleLoaderService) {
        return new RuleServiceImpl(ruleLoaderService);
    }





}
