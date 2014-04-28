package com.p2p.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.p2p.datapackage.UserInfoDTO;
import com.p2p.util.DBConnect;

public class UserInfoDAO 
{
	//新建用户
	public boolean newUser(UserInfoDTO ft) {
		String sql="insert into UserInfo values ('"+ft.getUname()+"','"+ft.getUpw()+"','"+
					ft.getUip()+"',"+ft.getUport()+","+ft.getUonline()+")";
		
		System.out.println(sql);
		try {
			Statement stat=DBConnect.getStatement();
			boolean result=stat.execute(sql);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("用户名"+ft.getUname()+"已存在,请重新输入");
			return false;
		}
	}
	
	//登录时更新该用户的IP地址
	public boolean updataUserInfo(UserInfoDTO ft) {
		String sql;
		if(ft.getUip().equals(""))
		{
			sql="update UserInfo set uonline = "+ft.getUonline()+" where uname = '"+ft.getUname()+"'";
		}
		else
		{
			sql="update UserInfo set uip = '"+ft.getUip()+"',uonline = "+ft.getUonline()+" where uname = '"+ft.getUname()+"'";
		}
		System.out.println(sql);
		try {
			Statement stat=DBConnect.getStatement();
			boolean result=stat.execute(sql);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("该用户名"+ft.getUname()+"不存在,无法更新");
			return false;
		}
	}
	
	//查询该用户是否存在
	public Object getUser(String fno,String upw) {
		String sql="select * from userinfo where uname='"+fno+"' and upw='"+upw+"'";
		System.out.println(sql);
		try {
			Statement stat=DBConnect.getStatement();
			ResultSet rs=stat.executeQuery(sql);

			UserInfoDTO uid=null;
			while(rs.next())
			{
				uid=new UserInfoDTO();
				uid.setUname(rs.getString(1));
				uid.setUpw(rs.getString(2));
				uid.setUip(rs.getString(3));
				uid.setUport(rs.getInt(4));
				uid.setUonline(rs.getInt(5));
			}
			return uid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
