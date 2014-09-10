package com.duoduo.gencode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * 代码生成器
 * @author chengesheng@gmail.com
 * @date 2014-8-3 上午12:30:45
 * @version 1.0.0
 */
public class Generator {

	// private static final String DATE_FORMAT_STRING = "yyyy-MM-dd";
	// private static final String DATETIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

	private String templateDir = System.getProperty("user.dir") + "/src/main/java/com/duoduo/gencode/template";
	private Configuration cfg;
	private Connection connection;

	private void initial() {
		try {
			if (null == cfg) {
				cfg = new Configuration();
			}
			cfg.setDirectoryForTemplateLoading(new File(templateDir));
			connection = DBConnectionUtils.getJDBCConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cfg.setObjectWrapper(new DefaultObjectWrapper());
	}

	public void generator(String packageName, String outputPath) throws Exception {
		initial();

		File file = new File(outputPath);
		if (!file.exists()) {
			file.mkdirs();
		}

		cfg.setDefaultEncoding("utf-8");
		Template entityTemplate = cfg.getTemplate("entity.ftl");
		Template sqlTemplate = cfg.getTemplate("sql.ftl");
		List<Map<String, Object>> templates = generatorTemplateData();

		String currentDate = getCurrentDate();
		String currentDateTime = getCurrentDatetime();
		for (Map<String, Object> o : templates) {
			// 包名
			o.put("packageName", packageName);
			// 增加日期和时间相关数据
			o.put("currentDate", currentDate);
			o.put("currentDateTime", currentDateTime);

			// 输出到文件
			File beanFile = new File(outputPath + "/" + o.get("beanName") + ".java");
			Writer beanWriter = new FileWriter(beanFile);
			entityTemplate.process(o, beanWriter);
			beanWriter.close();

			File sqlFile = new File(outputPath + "/Tbl_" + o.get("beanName") + "Mapper.xml");
			Writer sqlWriter = new FileWriter(sqlFile);
			sqlTemplate.process(o, sqlWriter);
			sqlWriter.close();

			System.out.println("生成:" + o.get("beanName"));
		}
	}

	private List<Map<String, Object>> generatorTemplateData() throws Exception {
		DatabaseMetaData dbmd = connection.getMetaData();
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		String[] tables = {
			"Table"
		};
		ResultSet tableSet = dbmd.getTables(null, null, "%", tables);//
		while (tableSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			String tableName = tableSet.getString("TABLE_NAME");
			ResultSet columnSet = dbmd.getColumns(null, "%", tableName, "%");
			List<Column> columns = new ArrayList<Column>();
			boolean hasDateColumn = false;
			while (columnSet.next()) {
				String columnName = columnSet.getString("COLUMN_NAME");
				String attributeName = handlerColumnName(columnSet.getString("COLUMN_NAME"));
				String columnType = columnSet.getString("TYPE_NAME");
				String attributeType = handlerColumnType(columnSet.getString("TYPE_NAME"));
				Column column = new Column();
				column.setColumnName(columnName);
				column.setColumnType(columnType);
				column.setAttributeName(attributeName);
				column.setAttributeType(attributeType);
				columns.add(column);

				if (attributeType.startsWith("Date")) {
					hasDateColumn = true;
				}
			}
			map.put("tableName", tableName);
			map.put("beanName", handlerTableName(tableName));
			map.put("columns", columns);
			map.put("hasDateColumn", hasDateColumn);
			lists.add(map);
		}
		connection.close();
		return lists;
	}

	public static String handlerColumnName(String oldName) {
		String[] arrays = oldName.split("_");
		String newName = "";
		if (arrays.length > 0) {
			newName = arrays[0];
		}
		for (int i = 1; i < arrays.length; i++) {
			newName += (arrays[i].substring(0, 1).toUpperCase() + arrays[i].substring(1, arrays[i].length()));
		}
		return newName;
	}

	public static String handlerTableName(String oldName) {
		if (oldName.indexOf("_") == -1) {
			return oldName;
		}

		String[] arrays = oldName.split("_");
		String newName = "";
		for (int i = 1; i < arrays.length; i++) {
			newName += (arrays[i].substring(0, 1).toUpperCase() + arrays[i].substring(1, arrays[i].length()));
		}
		return newName;
	}

	public static String handlerColumnType(String oldType) {
		if (oldType.toUpperCase().startsWith("VARCHAR")) {
			return "String";
		}
		if (oldType.toUpperCase().startsWith("INT")) {
			return "Integer";
		}
		if (oldType.toUpperCase().startsWith("DATETIME")) {
			return "Date";
		}
		if (oldType.toUpperCase().startsWith("TIMESTAMP")) {
			return "Date";
		}
		if (oldType.toUpperCase().startsWith("CHAR")) {
			return "String";
		}
		if (oldType.toUpperCase().startsWith("TINYINT")) {
			return "Integer";
		}
		if (oldType.toUpperCase().startsWith("BIT")) {
			return "Integer";
		}
		if (oldType.toUpperCase().startsWith("BIGINT")) {
			return "Long";
		}
		return oldType;
	}

	// private boolean mkdirAll(String path) {
	// File file = new File(path);
	// if(!file.exists()) {
	// String p[] = path.split("\\");
	// if(p.length<=1) {
	// p = path.split("/");
	// }
	// String fullPath="";
	// for(int i=0,len=p.length;i<len;i++) {
	// if(!"".equals(fullPath)) {
	// fullPath=fullPath + File.separator;
	// }
	// fullPath=fullPath + p[i];
	// file=new File(fullPath);
	// if(!file.exists()) {
	// file.mkdir();
	// }
	// }
	// }
	// return true;
	// }

	/** 获取当前时间戳并转换成yyyy-MM-dd hh:mm:ss格式的日期时间字符串 */
	private String getCurrentDatetime() {
		return DateFormat.getDateTimeInstance().format(new Date());
	}

	/** 获取当前时间戳并转换成yyyy-MM-dd格式的日期字符串 */
	private String getCurrentDate() {
		return DateFormat.getDateInstance().format(new Date());
	}

	public static void main(String[] args) throws Exception {
		Generator gen = new Generator();
		gen.generator("com.duoduo.frame", System.getProperty("user.dir") + "/generated/com/duoduo/frame");
	}
}
