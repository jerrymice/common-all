package com.github.jerrymice.common.entity.pojo;

import java.sql.Timestamp;

/**
 * @author tumingjian
 * 数据据实体的基类抽象接口,这是一个旧版本的数据库表记录兼容抽象接口,如果是新表,请使用 @see BaseRecord
 */
@Deprecated
public interface BasePojo extends DataModel {

    /**
     * 获取记录创建时间
     * @return
     */
    public Timestamp getCreatetime();

    /**
     * 设置记录创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(Timestamp createtime);

    /**
     * 获取记录创建者
     * @return
     */
    default String getCreater(){
        return null;
    }

    /**
     * 设置记录创建者
     * @param creater 创建者名称
     */
    default void setCreater(String creater){
    }

    /**
     * 获取记录最后一次更新时间
     * @return
     */
    default Timestamp getEditetime(){
        return null;
    }

    /**
     * 设置记录最后一次更新时间
     * @param editetime 更新时间
     */
    default void setEditetime(Timestamp editetime){

    }

    /**
     * 获取记录最后一次修改者
     * @return
     */
    public String getOperator();

    /**
     * 设置记录修改者
     * @param operator 修改者名称
     */
    public void setOperator(String operator);
}
