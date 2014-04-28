package com.p2p.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.p2p.datapackage.FileInfoDTO;
import com.p2p.datapackage.UserInfoDTO;
import com.p2p.util.DBConnect;

public class FileInfoDAO 
{
	//新建一条元数据
	public boolean newFile(FileInfoDTO ft) {
		String sql="insert into FileInfo values ('"+ft.getFname()+"','"+ft.getFpath()+"','"+
					ft.getFsize()+"','"+ft.getFowner()+"')";
		
		System.out.println(sql);
		try {
			Statement stat=DBConnect.getStatement();
			boolean result=stat.execute(sql);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("文件名  "+ft.getFname()+"已存在,请重新输入");
			return false;
		}
	}
	
	//删除一条元数据
	public boolean deleteFileInfo(String fname) {
		String sql="delete from fileinfo where fname='"+fname+"'";
		
		System.out.println(sql);
		try {
			Statement stat=DBConnect.getStatement();
			boolean result=stat.execute(sql);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	//根据指定条件查询元数据
	public Vector getFileInfo(String fname,String fowner,int type) {
		String sql="";
		switch(type)
		{
		case 1:
			sql="select fname,fpath,fsize,fowner,uip,uport,uonline from fileinfo,userinfo where fname like '%"+fname+"%' and fowner = uname";
			break;
		case 2:
			sql="select fname,fpath,fsize,fowner,uip,uport,uonline from fileinfo,userinfo where fowner = uname";
			break;
		case 3:
			sql="select fname,fpath,fsize,fowner,uip,uport,uonline from fileinfo,userinfo where fowner ='"+fowner+"' and fowner = uname";
			break;
		}
		
		System.out.println(sql);
		
		Vector resultSet = new Vector();
		try {
			Statement stat=DBConnect.getStatement();
			ResultSet rs=stat.executeQuery(sql);

			while(rs.next())
			{
				Vector hang = new Vector();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getString(3));
				hang.add(rs.getString(4));
				hang.add(rs.getString(5));
				hang.add(rs.getInt(6)+"");
				
				if(rs.getInt(7) == 1)
					hang.add("是");
				else
				{
					hang.add("否");
				}
				resultSet.add(hang);
			}
			
			return resultSet;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
