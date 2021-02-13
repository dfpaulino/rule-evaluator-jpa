package org.farmtec.res.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by dp on 13/02/2021
 */
@ConfigurationProperties(prefix = "res")
public class RuleServiceProperties {
    /**
     * Name of the service
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
