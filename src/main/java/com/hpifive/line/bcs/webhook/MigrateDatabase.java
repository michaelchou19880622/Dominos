package com.hpifive.line.bcs.webhook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MigrateDatabase {
	
	public static void main(String[] args) {
		
		//<!-- XtremeApp Test -->
		//<prop key="user">sa</prop>
		//<prop key="password">XtremeApp53811062</prop>
		//<prop key="serverName">172.104.113.123</prop>
		//<prop key="databaseName">Domino</prop>
		//<prop key="portNumber">1433</prop>
		
		//<!-- Dominos Internal Test -->
		//<prop key="user">sa</prop>
		//<prop key="password">Hpifive16890535</prop>
		//<prop key="serverName">dominos-aio</prop>
		//<prop key="databaseName">Dominos</prop>
		//<prop key="portNumber">1433</prop>
		
		//<!-- Dominos Official Test -->
		//<prop key="user">sa</prop>
		//<prop key="password">Hpifive16890535</prop>
		//<prop key="serverName">13.94.31.7</prop>
		//<prop key="databaseName">Dominos</prop>
		//<prop key="portNumber">1433</prop>
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			String connectionUrl = "jdbc:sqlserver://172.104.113.123:1433;"
					+ "database=Domino;user=sa;password=XtremeApp53811062;"
					+ "encrypt=true;trustServerCertificate=false;loginTimeout=30;";
			
			Connection connection = DriverManager.getConnection(connectionUrl);
			
			Statement statement = connection.createStatement();

			String selectSql = "SELECT * FROM BCS_LINE_USER";
			
			ResultSet resultSet = statement.executeQuery(selectSql);
			
			System.err.println("resultSet.getFetchSize() = " + resultSet.getFetchSize());

			// Print results from select statement
			while (resultSet.next()) {
				System.err.println(resultSet.getString(2) + " " + resultSet.getString(3));
			}
		} catch (SQLException e) {
			System.err.println("SQLException = " + e.getCause());
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException = " + e.getCause());
		}
		

//		ResultSet resultSet = null;
//
//		try (Connection connection = DriverManager.getConnection(connectionUrl); Statement statement = connection.createStatement();) {
//
//			// Create and execute a SELECT SQL statement.
//			resultSet = statement.executeQuery(selectSql);
//			
//			System.err.println("resultSet.getFetchSize() = " + resultSet.getFetchSize());
//
//			// Print results from select statement
//			while (resultSet.next()) {
//				System.err.println(resultSet.getString(2) + " " + resultSet.getString(3));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
}
