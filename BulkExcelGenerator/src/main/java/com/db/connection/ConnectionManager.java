package com.db.connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public class ConnectionManager {

	private static Connection conn;
	private ConnectionManager() {
		
	}
	
	public static Connection getConnection(String dbUser, String dbPwd, String dbURL) {
		synchronized (ConnectionManager.class) {
			try {
				if(conn==null || conn.isClosed()) {
					
						conn = DriverManager.getConnection(dbURL, dbUser, dbPwd);
					
				}
			} catch (SQLException e) {
				conn=null;
				e.printStackTrace();
			}
		}
		return conn;
	}
	public static void main(String[] args) {
		System.out.println(String.valueOf(null));
	}
}
