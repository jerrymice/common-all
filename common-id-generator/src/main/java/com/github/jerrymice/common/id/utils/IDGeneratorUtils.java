package com.github.jerrymice.common.id.utils;

import com.github.jerrymice.common.id.generator.IDGenerator;
import com.github.jerrymice.common.id.generator.SnowflakeIDGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author tumingjian
 *  一个基于snowflake算法的ID生成器工具类,支持简单的多点部署配置.没有集群环境的检查功能.
 *  在多点部署时,workId与datacenterId联合起来的值需要你自己在配置的时候在集群中保证唯一性
 */
@Slf4j
public class IDGeneratorUtils {
    /**
     * 默认的生成ID的开始时间:2018-08-08
     */
    private final static Long START_TIMESTAMP = 1514736000000L;
    /**
     * startTimestamp properties key name
     */
    private final static String START_TIMESTAMP_CONFIG_KEY = "jerrymice.common.id.generator.start-timestamp";
    /**
     * workId properties key name
     */
    private final static String WORK_ID_CONFIG_KEY = "jerrymice.common.id.generator.work-id";
    /**
     * datacenterId properties key name
     */
    private final static String DATACENTER_ID_CONFIG_KEY = "jerrymice.common.id.generator.datacenter-id";
    /**
     * IDGenerator,直接使用了SnowflakeIDGenerator
     *
     * @see IDGenerator
     * @see SnowflakeIDGenerator
     */
    private final static IDGenerator ID_GENERATOR;

    static {
        /**
         * 启用spring boot 配置文件支持.
         */
        InputStream resourceAsStream = IDGeneratorUtils.class.getClassLoader().getResourceAsStream("/application.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
        } catch (Exception e) {

        }
        if (properties != null) {
            Long startTimestamp;
            if (properties.get(START_TIMESTAMP_CONFIG_KEY) != null) {
                startTimestamp = Long.valueOf(properties.get(START_TIMESTAMP_CONFIG_KEY).toString());
            } else {
                startTimestamp = START_TIMESTAMP;
            }
            Long workId = Long.valueOf(properties.getOrDefault(WORK_ID_CONFIG_KEY, 0).toString());
            Long datacenterId = Long.valueOf(properties.getOrDefault(DATACENTER_ID_CONFIG_KEY, 0).toString());
            ID_GENERATOR = new SnowflakeIDGenerator(startTimestamp, workId, datacenterId, System.currentTimeMillis(), 2000);
        } else {
            log.warn("IDGeneratorUtils use default config,workId:0,datacenterId:0.");
            ID_GENERATOR = new SnowflakeIDGenerator(START_TIMESTAMP, 0L, 0L, System.currentTimeMillis(), 2000);
        }
    }

    /**
     * 生成Long id
     *
     * @return 返回Long类型的ID
     */
    public static Long id() {
        return ID_GENERATOR.id();
    }

    /**
     * 生成Long id
     * @param count 要指量获取ID的个数
     * @return 返回Long类型的List
     */
    public static List<Long> batchId(int count) {
        return ID_GENERATOR.batchId(count);
    }
}
