package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import domain.DataList;

public class DB {
	private static Logger log = Logger.getLogger(DB.class);
	private final static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	// -DDB.path="jdbc:sqlserver://202.202.5.211;databaseName=ww;user=wwnb;password=wwnb;"//测试数据库地址
	private static String connectionUrl;
	static {
		if (System.getProperty("DB.path") != null) {
			connectionUrl = System.getProperty("DB.path");
			log.debug(connectionUrl);
			log.debug("使用DB.path对应的数据库");
		} else {
			connectionUrl = "jdbc:sqlserver://116.62.24.156:1433;databaseName=wwnb;user=wwnb;password=CQUa1502;";// 运行数据库地址
			log.debug("使用默认数据库");
		}
		log.debug("数据库URL" + connectionUrl);
	}

	public static Connection getConnection() throws Exception {
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connectionUrl);
		return conn;
	}
	/**
	 * 更新数据库，不建议使用
	 * @param sql
	 * @param objs
	 * @return
	 */
	@Deprecated
	public static boolean doUpdate(String sql, Object... objs) {
		Connection conn = null;
		try {
			conn = DB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			for (int i = 0; i < objs.length; i++)
				st.setObject(i + 1, objs[i]);
			st.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return false;
	}

	/**
	 * 查询数据库不建议使用
	 * @param sql
	 * @param objs
	 * @return
	 */
	@Deprecated
	public static DataList doQuery(String sql, Object... objs) {
		Connection conn = null;
		try {
			conn = DB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			for (int i = 0; i < objs.length; i++)
				st.setObject(i + 1, objs[i]);
			return new DataList(st);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return new DataList();
	}
	/**
	 * 获取表的列名，不建议使用
	 * @param tableName
	 * @return
	 */
	@Deprecated
	public static List<String> getColumnsFromTable(String tableName) {
		List<String> columns = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = DB.getConnection();
			PreparedStatement st = conn.prepareStatement("select * from [" + tableName + "]");
			ResultSet rs = st.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
				columns.add(rsmd.getColumnName(i));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return columns;
	}
	public static String getConnectionUrl() {
		return connectionUrl;
	}
	public static String getDriver() {
		return driver;
	}

}
