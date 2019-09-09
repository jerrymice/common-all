package com.github.jerrymice.common.entity.pojo;

import java.sql.Timestamp;

/**
 * @author tumingjian
 * 数据据实体的基类抽象接口
 */
@Deprecated
public interface BasePojo extends DataModel {


    public Timestamp getCreatetime();

    public void setCreatetime(Timestamp createtime);

    default String getCreater(){
        return null;
    }

    default void setCreater(String creater){
    }

    default Timestamp getEditetime(){
        return null;
    }

    default void setEditetime(Timestamp editetime){

    }

    public String getOperator();

    public void setOperator(String operator);
}
