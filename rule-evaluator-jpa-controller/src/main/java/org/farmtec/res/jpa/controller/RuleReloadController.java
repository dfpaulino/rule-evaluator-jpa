package org.farmtec.res.jpa.controller;

import org.farmtec.res.jpa.controller.dto.RuleReloadStatus;
import org.farmtec.res.service.RuleService;
import org.farmtec.res.service.rule.loader.RuleLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by dp on 08/03/2021
 */
@RestController
@RequestMapping("/reload")
public class RuleReloadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleReloadController.class);

    private final RuleService ruleService;

    public RuleReloadController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping("/rules")
    public RuleReloadStatus reloadAndWait()  throws Exception{
        boolean isLoaded = ruleService.updateRules();
        RuleReloadStatus ruleReloadStatus = new RuleReloadStatus();

        ruleReloadStatus.setSuccess(isLoaded);
        ruleReloadStatus.setLastUpdateTime(ruleService.getLastUpdate());
        return ruleReloadStatus;
    }

    @GetMapping("/rules")
    public RuleReloadStatus lastUpdateDate() {
        RuleReloadStatus ruleReloadStatus = new RuleReloadStatus();
        ruleReloadStatus.setSuccess(true);
        ruleReloadStatus.setLastUpdateTime(ruleService.getLastUpdate());
        return ruleReloadStatus;
    }
}
