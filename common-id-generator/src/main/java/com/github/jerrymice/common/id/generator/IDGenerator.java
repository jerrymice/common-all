package com.github.jerrymice.common.id.generator;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author tumingjian

 * 说明:id生成器
 */
@Slf4j
public class IDGenerator {
    private final static SnowflakeIdWorker ID_GENGERATOR;
    static {
        InputStream resourceAsStream = IDGenerator.class.getClassLoader().getResourceAsStream("/application.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
        } catch (Exception e) {

        }
        if (properties != null) {
            Long workId = Long.valueOf(properties.getOrDefault("zhongjin.base.id.generator.work-id", 0).toString());
            Long datacenterId = Long.valueOf(properties.getOrDefault("zhongjin.base.id.generator.datacenter-id", 0).toString());
            ID_GENGERATOR = new SnowflakeIdWorker(workId, datacenterId, System.currentTimeMillis(), 2000);
        } else {
            log.warn("IDGenerator use default config,workId:0,datacenterId:0.");
            ID_GENGERATOR = new SnowflakeIdWorker(0L, 0L, System.currentTimeMillis(), 2000);
        }
    }

    /**
     * 生成UUID
     *
     * @return 返回Long类型的ID
     */
    public static Long id() {
        return ID_GENGERATOR.generateId();
    }
}
