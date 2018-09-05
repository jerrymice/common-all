package com.github.jerrymice.common.id.generator;


import lombok.extern.slf4j.Slf4j;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author tumingjian
 * 说明:snowflake算法
 */
@Slf4j
public class SnowflakeIdWorker{
    // ==============================Fields===========================================
    /**
     * 开始时间截 (2018-01-01)
     */
    private final long start = 1514736000000L;

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
    private final int MAX_BATCH_GENERATE_COUNT=100;

    /**
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     * @param lastTimestamp 最后更新时间
     * @param maxBackTimestamp 最大回退时间
     */
    public SnowflakeIdWorker(long workerId, long datacenterId, long lastTimestamp, long maxBackTimestamp) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.lastTimestamp=lastTimestamp;
        this.maxBackTimestamp=maxBackTimestamp;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long generateId() {
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
        return ((timestamp - start) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 时间回退处理
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
        } else{
            //如果回拔时间小于2秒.阻塞时间直到当前时间大于lastTimestamp
            timestamp=tilNextMillis(lastTimestamp);
            log.info(MessageFormat.format("Clock moved backwards,but fix bug,currentTime:{0},backTimestamp:{1}",timestamp,lastTimestamp));
        }
        return timestamp;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
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
     */
    public List<Long> batchGenerateId(int count){
        if(count>MAX_BATCH_GENERATE_COUNT || count<=0){
            throw new IndexOutOfBoundsException("批量生成ID的最大数量为100");
        }
        ArrayList<Long> result = new ArrayList<>(count);
        for(int i=0;i<count;i++){
            result.add(generateId());
        }
        return result;
    }
}
