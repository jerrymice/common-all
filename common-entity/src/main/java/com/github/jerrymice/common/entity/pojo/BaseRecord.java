package com.github.jerrymice.common.entity.pojo;

import java.sql.Timestamp;

/**
 * @author tumingjian
 * 创建时间: 2019-09-08 12:44
 * 功能说明:
 */
public interface BaseRecord {
    default String getCreator(){
        return null;
    }

    default void setCreator(String creator){

    }

    default Timestamp getUpdatetime(){
        return null;
    }

    default void setUpdatetime(Timestamp updatetime){

    }
    Timestamp getCreatetime();

    void setCreatetime(Timestamp createtime);


    String getOperator();

    void setOperator(String operator);
}
