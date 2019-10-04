package com.github.jerrymice.common.entity.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author tumingjian
 * 数据据实体的基类抽象接口,这是一个旧版兼容接口.
 */
@Deprecated
public interface DataModel extends Serializable {
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
}
