package com.p2p.client;

import java.io.*;
import java.net.*;
import com.p2p.datapackage.*;

public class ClientSendBean extends Thread
{
	//客户端的socket
	private Socket socket;

	//定义IO对象
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	//客户端数据包
	private DataPackage dp;

	//保存构造函数传入的请求类型
	private String requestType;

	//保存构造函数传入的变长参数列表
	private String para[];
	
	//定义服务器端的IP地址和端口号
	private static String serverIP = "219.245.97.94";
	private static int serverPort = 3333;
	
	
	//构造函数
	public ClientSendBean(String peerIP,int peerPort,String requestType,String...para)
	{
		try {
			//构建与服务端的连接
			if(peerIP == null && peerPort == 0)
			{
				socket=new Socket(serverIP,serverPort);
			}
			//构建与客户端的点对点连接
			else
			{
				socket=new Socket(peerIP,peerPort);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			//赋初值
			this.requestType = requestType;
			this.para = para;
			
			//实例化IO对象
			out=new ObjectOutputStream(socket.getOutputStream());
			in=new ObjectInputStream(socket.getInputStream());
			
			start();
			
		} catch (IOException e) {
			try
			{
				socket.close();
			}
			catch(IOException e2)
			{
				e.printStackTrace();
			}
		}
	}
	
	//线程主体方法
	public void run()
	{
		//注册
		if(requestType.equals("registerRequest"))
		{
			this.SubmitRegisterInfo(para[0],para[1],para[2],Integer.parseInt(para[3])
					,Integer.parseInt(para[4]));
		}
		//登录
		else if(requestType.equals("loginRequest"))
		{
			this.SubmitNamePw(para[0], para[1]);
		}
		//上传
		else if(requestType.equals("uploadFile"))
		{
			this.SubmitFileInfo(para[0], para[1], para[2], para[3]);
		}
		//查询
		else if(requestType.equals("queryFile"))
		{
			this.QueryFileInfo(para[0], para[1],Integer.parseInt(para[2]));
		}
		//删除
		else if(requestType.equals("deleteFile"))
		{
			this.DeleteFileInfo(para[0]);
		}
		//更新
		else if(requestType.equals("updateUserInfo"))
		{
			this.UpdateUserInfo(para[0],para[1],para[2]);
		}
		//下载
		else if(requestType.equals("downloadFile"))
		{
			this.DownloadFile(para[0],para[1],para[2]);
		}
	}
	
	//向服务器提交登录信息:用户名和密码
	public void SubmitNamePw(String username,String password)
	{
		try {
			dp = new DataPackage();
			dp.setDataHeader("loginRequest");
			
			UserInfoDTO uid = new UserInfoDTO();
			uid.setUname(username);
			uid.setUpw(password);
			
			dp.setUid(uid);
			
			out.writeObject(dp);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//向服务器提交注册信息:用户名、密码和IP地址等
	public void SubmitRegisterInfo(String username,String password,String ip,int port,int online)
	{
		try {
			dp = new DataPackage();
			dp.setDataHeader("registerRequest");
			
			UserInfoDTO uid = new UserInfoDTO();
			uid.setUname(username);
			uid.setUpw(password);
			uid.setUip(ip);
			uid.setUport(port);
			uid.setUonline(online);
			
			dp.setUid(uid);
			
			out.writeObject(dp);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//向服务器提交上传元数据信息
	public void SubmitFileInfo(String filename,String filepath,String filesize,String fileowner)
	{
		try {
			dp = new DataPackage();
			dp.setDataHeader("uploadFile");
			
			FileInfoDTO fid = new FileInfoDTO();
			fid.setFname(filename);
			fid.setFpath(filepath);
			fid.setFsize(filesize);
			fid.setFowner(fileowner);
			
			dp.setFid(fid);
			
			out.writeObject(dp);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//向服务器提交查询元数据信息请求
	public void QueryFileInfo(String fname,String fowner,int type)
	{
		try {
			
			dp = new DataPackage();
			dp.setDataHeader("queryFile");
			
			FileInfoDTO fid = new FileInfoDTO();
			fid.setFowner(fowner);
			
			dp.setFid(fid);
			dp.setSearchType(type);
			dp.setSeachKeyword(fname);
			
			out.writeObject(dp);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//向服务器提交上传删除元数据信息请求
	public void DeleteFileInfo(String fname)
	{
		try {
			FileInfoDTO fid =new FileInfoDTO();
			fid.setFname(fname);
			
			dp = new DataPackage();
			
			dp.setDataHeader("deleteFile");
			dp.setFid(fid);
			
			out.writeObject(dp);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//向服务器提交更新用户信息(IP，在线状态等)请求
	public void UpdateUserInfo(String fname,String ip,String online)
	{
		try {
			dp = new DataPackage();
			dp.setDataHeader("updateUserInfo");
			
			UserInfoDTO uid = new UserInfoDTO();
			uid.setUname(fname);
			uid.setUip(ip);
			uid.setUonline(Integer.parseInt(online));
			
			dp.setUid(uid);
			
			out.writeObject(dp);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//向服务器下载文件信息(路径，文件名，大小)请求
	public void DownloadFile(String filename,String filepath,String filesize)
	{
		try {
			dp = new DataPackage();
			dp.setDataHeader("downloadFile");
			
			FileInfoDTO fid = new FileInfoDTO();
			fid.setFname(filename);
			fid.setFpath(filepath);
			fid.setFsize(filesize);
			
			dp.setFid(fid);
			
			out.writeObject(dp);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataPackage getDp() {
		return dp;
	}

	public void setDp(DataPackage dp) {
		this.dp = dp;
	}
	
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String[] getPara() {
		return para;
	}

	public void setPara(String[] para) {
		this.para = para;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}
}
