package org.kitastic;

import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DbManager {
	  private Connection connect;
	  public DbManager(){
		  
		  try {
			Class.forName("com.mysql.jdbc.Driver");
			this.connect = DriverManager.getConnection("jdbc:mysql://localhost/kitastic?"+ "user=root&password=");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
	  
	  public ResultSet doQuery(String queryString){
		  Statement statement;
		  ResultSet resultSet;
		      try {
		    	statement = connect.createStatement();
			    resultSet = statement.executeQuery(queryString);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultSet = null;
			}
		return resultSet;
	  }
}
