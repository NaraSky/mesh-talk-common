package com.lb.im.common.cache.id;

import com.lb.im.common.cache.time.SystemClock;

import java.util.Date;

/**
 * 雪花算法实现类，用于生成全局唯一ID。ID由时间戳、数据中心标识、机器标识和序列号组成。
 */
public class SnowFlake {

    /**
     * 起始的时间戳:Fri Apr 11 22:55:28 CST 2025，使用时此值不可修改
     */
    private final static long START_STMP = 1744383328694L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT   = 12; //序列号占用的位数
    private final static long MACHINE_BIT    = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM    = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE       = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT    = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT   = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;  //数据中心
    private long machineId;     //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳

    /**
     * 构造函数，初始化数据中心ID和机器ID。
     * @param datacenterId 数据中心标识，范围[0, MAX_DATACENTER_NUM]
     * @param machineId 机器标识，范围[0, MAX_MACHINE_NUM]
     */
    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 生成下一个全局唯一ID。
     * @return 生成的64位唯一ID
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();

        // 检测时间回拨，拒绝生成ID
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            // 同一毫秒内生成序列号
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            // 不同毫秒，重置序列号
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    /**
     * 获取下一个更大的毫秒时间戳，确保时间单调递增。
     * @return 下一个有效时间戳
     */
    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    /**
     * 获取当前系统时间戳（毫秒）。
     * @return 当前时间戳
     */
    private long getNewstmp() {
        return SystemClock.millisClock().now();
    }

    /**
     * 获取数据中心标识的最大允许值。
     * @return 数据中心最大值
     */
    public static Long getMaxDataCeneterNum() {
        return MAX_DATACENTER_NUM;
    }

    /**
     * 获取机器标识的最大允许值。
     * @return 机器最大值
     */
    public static Long getMaxMachineNum() {
        return MAX_MACHINE_NUM;
    }

    /**
     * 主方法，用于测试常量值和当前时间。
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        System.out.println(MAX_DATACENTER_NUM);
        System.out.println(MAX_MACHINE_NUM);
        System.out.println(MAX_SEQUENCE);
        Date date = new Date();
        System.out.println(date);
        System.out.println(date.getTime());
    }

}
