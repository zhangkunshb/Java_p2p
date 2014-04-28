package com.p2p.server;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Vector;

import com.p2p.util.*;
import com.p2p.dao.*;
import com.p2p.datapackage.*;

class ThreadServer extends Thread
{
	//客户端的socket
	private Socket clientSocket;
	
	//定义IO句柄
	private ObjectInputStream sin;
	private ObjectOutputStream sout;
	
	//保存客户端数据包
	private DataPackage dp;
	

	//默认的构造函数
	public ThreadServer()
	{
		
	}
	
	//带参构造函数
	public ThreadServer(Socket s) throws IOException
	{
		clientSocket=s;
	
		//初始化sin和sout的句柄
		sin=new ObjectInputStream(clientSocket.getInputStream());
		sout=new ObjectOutputStream(clientSocket.getOutputStream());
		
		//开启线程
		start();
	}
	
	//关闭服务
	public void ThreadServerStop()
	{
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//处理用户的登录请求
	public void ProcessLogin()
	{
		System.out.println("In Server received the info:"+dp.getDataHeader()+" "+dp.getUid().getUname()+" "+dp.getUid().getUpw());
		
		UserInfoDAO uia= new UserInfoDAO();
		UserInfoDTO uid= (UserInfoDTO)uia.getUser(dp.getUid().getUname(), dp.getUid().getUpw());
		if(uid != null)
		{
			dp.setResult("success");
			dp.setUid(uid);
		}
		else
		{
			dp.setResult("failed");
			dp.setUid(null);
		}
		try {
			sout.writeObject(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//处理用户的注册请求
	public void ProcessRegister()
	{
		System.out.println("In Server received the info:"+dp.getDataHeader()+" "+dp.getUid().getUname()+" "+dp.getUid().getUpw()+" "+dp.getUid().getUip()+" "+dp.getUid().getUport()+" "+dp.getUid().getUonline());
		
		
		UserInfoDAO uia=new UserInfoDAO();
		
		if(uia.newUser(dp.getUid()))
		{
			dp.setResult("success");	
		}
		else
		{
			dp.setResult("failed");
		}
		
		try {
			sout.writeObject(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//处理用户的上传文件请求
	public void ProcessUploadFile()
	{
		System.out.println("In Server received the info:"+dp.getDataHeader()+" "+dp.getFid().getFpath()+" "+dp.getFid().getFname()+" "+dp.getFid().getFsize()+" "+dp.getFid().getFowner());
		
		FileInfoDAO fia=new FileInfoDAO();
		if(fia.newFile(dp.getFid()))
		{
			dp.setResult("success");	
		}
		else
		{
			dp.setResult("failed");
		}
		
		try {
			sout.writeObject(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//处理用户的查询文件请求
	public void ProcessQueryFile()
	{
		System.out.println("In Server received the info:"+dp.getDataHeader()+" "+dp.getSeachKeyword()+" "+dp.getFid().getFowner());
		
		FileInfoDAO fia=new FileInfoDAO();
		Vector resultSet = fia.getFileInfo(dp.getSeachKeyword(), dp.getFid().getFowner(),dp.getSearchType());
		if(resultSet != null)
		{
			dp.setResult("success");
			dp.setTableData(resultSet);
		}
		else
		{
			dp.setResult("failed");
			dp.setTableData(null);
		}
		
		try {
			sout.writeObject(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//处理用户的删除文件请求
	public void ProcessDeleteFile()
	{
		System.out.println("In Server received the info:"+dp.getDataHeader()+" "+dp.getFid().getFname());
		
		FileInfoDAO fia=new FileInfoDAO();
		if(fia.deleteFileInfo(dp.getFid().getFname()))
		{
			dp.setResult("success");
		}
		else
		{
			dp.setResult("failed");
		}
		try {
			sout.writeObject(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//处理用户的更新用户信息请求
	public void ProcessUpdateUserInfo()
	{
		System.out.println("In Server received the info:"+dp.getDataHeader()+" "+dp.getUid().getUname()+" "+dp.getUid().getUip()+" "+dp.getUid().getUonline());
		
		UserInfoDAO uia=new UserInfoDAO();
		if(uia.updataUserInfo(dp.getUid()))
		{
			dp.setResult("success");
		}
		else
		{
			dp.setResult("failed");
		}
		try {
			sout.writeObject(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//线程执行的主体函数
	public void run()
	{
		try {
			//用循环来监听通讯内容
			//for(;;)
			//{
				dp = new DataPackage();
				
				try {
					dp =(DataPackage)sin.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//注册请求
				if(dp.getDataHeader().equals("registerRequest"))
				{
					ProcessRegister();
				}
				//登录请求
				else if(dp.getDataHeader().equals("loginRequest"))
				{
					ProcessLogin();
				}
				//上传文件元数据请求
				else if(dp.getDataHeader().equals("uploadFile"))
				{
					ProcessUploadFile();
				}
				//查询文件请求
				else if(dp.getDataHeader().equals("queryFile"))
				{
					ProcessQueryFile();
				}
				//删除文件请求
				else if(dp.getDataHeader().equals("deleteFile"))
				{
					ProcessDeleteFile();
				}
				//更新用户信息请求
				else if(dp.getDataHeader().equals("updateUserInfo"))
				{
					ProcessUpdateUserInfo();
				}
			//}
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally
		{
			System.out.println("close the Server socket and the io");
			try
			{
				clientSocket.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}

class StartThread extends Thread
{
	//端口号
	static final int portNo=3333;

	//服务器端的socket
	ServerSocket s = null;
	
	Vector socketVector;
	
	public StartThread()
	{
		socketVector = new Vector();
		start();
	}
	
	public void run()
	{
		this.StartServer();
	}
	
	public void StartServer()
	{
		
		try {
			s = new ServerSocket(portNo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("The Server is start:"+s);
		
		try
		{
			for(;;)
			{
				try {
					//阻塞，直到有客户端连接
					Socket socket=s.accept();
					socketVector.add(socket);
					//通过构造函数，启动线程
					new ThreadServer(socket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		finally
		{
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void StopServer()
	{
		for(Iterator iter = socketVector.iterator();iter.hasNext();)
		{
			Socket socket = (Socket)iter.next();
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("The Server is stopped!");
	}
}






