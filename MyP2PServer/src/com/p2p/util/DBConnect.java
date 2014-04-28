package com.p2p.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {
	public static Statement getStatement(){
		Statement stat=null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//1.���ӵ�ַ��2.�û���3������
			//DriverManager-----�������
			Connection connect = null;
			try {
				connect = DriverManager.
				getConnection("jdbc:mysql://localhost:3306/zhangkun","root","19910630");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//connection.open();
			//ִ�ж˿�
			try {
				stat=connect.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return stat;
	}
}
