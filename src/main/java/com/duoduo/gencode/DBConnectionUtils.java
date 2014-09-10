package com.duoduo.gencode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接设置
 * @author chengesheng@gmail.com
 * @date 2014-8-3 上午12:29:07
 * @version 1.0.0
 */
public class DBConnectionUtils {

	private static Connection conn = null;

	public static Connection getJDBCConnection() {
		if (null == conn) {
			try {
				Class.forName("com.mysql.jdbc.Driver"); //
				String url = "jdbc:mysql://127.0.0.1:3306/permission_4level";//
				String user = "root";
				String password = "";
				conn = DriverManager.getConnection(url, user, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	public static void close() {
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
