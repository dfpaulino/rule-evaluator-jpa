package org.farmtec.res.jpa.controller;

import org.farmtec.res.jpa.controller.dto.RuleReloadStatus;
import org.farmtec.res.service.rule.loader.RuleLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by dp on 08/03/2021
 */
@RestController
@RequestMapping("/reload")
public class RuleReloadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleReloadController.class);

    private final RuleLoaderService ruleLoaderService;

    public RuleReloadController(RuleLoaderService ruleLoaderService) {
        this.ruleLoaderService = ruleLoaderService;
    }

    @PostMapping("/rules")
    public RuleReloadStatus reloadAndWait () throws Exception{
        Future<Boolean> loadRequest = ruleLoaderService.refreshRules();
        RuleReloadStatus ruleReloadStatus = new RuleReloadStatus();


            if(loadRequest.get(10, TimeUnit.SECONDS)) {
                LOGGER.info("reload process concluded {}",ruleLoaderService.isLoadRuleSuccess());
            } else {
                LOGGER.error("reload failed");
            }
        ruleReloadStatus.setSuccess(ruleLoaderService.isLoadRuleSuccess());
        ruleReloadStatus.setLastUpdateTime(ruleLoaderService.getLastUpdateTime());
        return ruleReloadStatus;
    }

    @GetMapping("/rules")
    public RuleReloadStatus lastUpdateState(){
        RuleReloadStatus ruleReloadStatus = new RuleReloadStatus();
        ruleReloadStatus.setSuccess(ruleLoaderService.isLoadRuleSuccess());
        ruleReloadStatus.setLastUpdateTime(ruleLoaderService.getLastUpdateTime());
        return ruleReloadStatus;
    }
}
