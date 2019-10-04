package com.github.jerrymice.common.entity.pojo;

import java.sql.Timestamp;

/**
 * @author tumingjian
 * 创建时间: 2019-09-08 12:44
 * 功能说明:一个数据据表的通用字段接口
 */
public interface BaseRecord {
    /**
     * 获取记录创建者
     * @return
     */
    default String getCreator(){
        return null;
    }

    /**
     * 设置记录创建者
     * @param creator 创建者名称
     */
    default void setCreator(String creator){

    }

    /**
     * 获取记录最后一次更新时间
     * @return
     */
    default Timestamp getUpdatetime(){
        return null;
    }

    /**
     * 设置记录最后一次更新时间
     * @param updatetime 更新时间
     */
    default void setUpdatetime(Timestamp updatetime){

    }

    /**
     * 获取记录创建时间
     * @return
     */
    Timestamp getCreatetime();

    /**
     * 设置记录创建时间
     * @param createtime 创建时间
     */
    void setCreatetime(Timestamp createtime);

    /**
     * 获取最后一次记录修改者
     * @return
     */
    String getOperator();

    /**
     * 设置最后一次记录修改者
     * @param operator 记录修改者
     */
    void setOperator(String operator);
}
