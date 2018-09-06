package com.github.jerrymice.common.entity.pojo;

import java.sql.Timestamp;
/**
 * @author tumingjian
 * 数据据实体的基类抽象接口
 */
public interface BasePojo extends DataModel {

	public Timestamp getCreatetime();

	public void setCreatetime(Timestamp createtime);

	public String getCreater();

	public void setCreater(String creater);

	public Timestamp getEditetime();

	public void setEditetime(Timestamp editetime);

	public String getOperator();

	public void setOperator(String operator);
}
