package com.github.jerrymice.common.entity.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author tumingjian
 * 数据据实体的基类抽象接口
 */
@Deprecated
public interface DataModel extends Serializable {
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
}
