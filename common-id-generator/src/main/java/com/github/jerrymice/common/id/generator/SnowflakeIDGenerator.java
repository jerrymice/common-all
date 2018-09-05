package com.github.jerrymice.common.id.generator;


import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author tumingjian
 * twitter snowflake算法
 * 可以支持最大节点数为1024个节点,每个节点每毫秒可产生4096个趋势递增的Long值
 * 算法优点:整体上按照时间自增排序，
 * 并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，如果满节点部署每秒能够产生26万个ID左右
 */
@Slf4j
public class SnowflakeIDGenerator implements IDGenerator {

    /**
     * 机器id所占的位数
     */
    private static final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private static final long datacenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    public static final long MAX_WORKER_ID = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    public static final long MAX_DATACENTER_ID = -1L ^ (-1L << datacenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;
    /**
     * 开始时间的毫秒数,这个时间一旦在生产环境固定,便不能更改.否则会造成ID重复
     */
    private long startTimestamp;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;
    /**
     *
     */
    private long maxBackTimestamp;
    /**
     * 批量生成ID的最大数量
     */
    private final int MAX_BATCH_GENERATE_COUNT = 100;

    /**
     * @param workerId         工作ID (0~31)
     * @param datacenterId     数据中心ID (0~31)
     * @param startTimestamp   要生成ID的开始时间
     * @param lastTimestamp    生成ID的最后更新时间
     * @param maxBackTimestamp 最大回退时间,可以理解为lastTimestamp
     *                         与本地系统时间System.currentTime()的最大误差时,如果高于这个误差值,那么会抛出异常
     */
    public SnowflakeIDGenerator(long workerId, long datacenterId, long startTimestamp, long lastTimestamp, long maxBackTimestamp) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.startTimestamp = startTimestamp;
        this.lastTimestamp = lastTimestamp;
        this.maxBackTimestamp = maxBackTimestamp;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    @Override
    public synchronized Long id() {
        long timestamp = currentTimeMillis();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退.
        if (timestamp < lastTimestamp) {
            timestamp = clockBackProcess(timestamp);
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出,毫秒内的序列号已经用完
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - startTimestamp) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 时间回退处理
     *
     * @param timestamp 时间
     * @return 返回一个时间
     */
    private long clockBackProcess(long timestamp) {
        long backTimestamp = lastTimestamp - timestamp;
        //如果超过最大等待时间,那么停止服务并抛出异常
        if (backTimestamp > maxBackTimestamp) {
            String errorMessage = MessageFormat.format("Clock moved backwards. lasttime:{0},currenttime:{0} ,Refusing to generate id for {2} milliseconds", lastTimestamp, timestamp, backTimestamp);
            log.error(errorMessage);
            throw new RuntimeException(new TimeoutException(errorMessage));
        } else {
            //如果回拔时间小于2秒.阻塞时间直到当前时间大于lastTimestamp
            timestamp = tilNextMillis(lastTimestamp);
            log.info(MessageFormat.format("Clock moved backwards,but fix bug,currentTime:{0},backTimestamp:{1}", timestamp, lastTimestamp));
        }
        return timestamp;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 批量生成ID.
     * @param count 要生成的数量
     * @return 返回一个size为count的数组
     * @see IDGenerator
     */
    @Override
    public List<Long> batchId(int count) {
        if (count > MAX_BATCH_GENERATE_COUNT || count <= 0) {
            throw new IndexOutOfBoundsException("batch id max count value 100");
        }
        ArrayList<Long> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(id());
        }
        return result;
    }
}
