package com.duoduo.gencode;

/**
 * 数据库列模型
 * @author chengesheng@gmail.com
 * @date 2014-8-3 上午12:29:37
 * @version 1.0.0
 */
public class Column {

	private String columnName;
	private String columnType;
	private String attributeName;
	private String attributeType;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

}
