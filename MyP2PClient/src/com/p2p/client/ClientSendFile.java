package com.p2p.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.p2p.datapackage.DataPackage;


class SendFileThread extends Thread
{
	//客户端请求方的socket
	private Socket clientSocket;
	
	//定义IO句柄
	private ObjectInputStream sin;
	private ObjectOutputStream sout;
	
	//保存客户端数据包
	private DataPackage dp;
	
	public SendFileThread(Socket socket) throws IOException
	{
		clientSocket = socket;
		
		//初始化sin和sout的句柄
		sin=new ObjectInputStream(clientSocket.getInputStream());
		sout=new ObjectOutputStream(clientSocket.getOutputStream());
		
		//开启线程
		start();
	}
	
	//线程主体函数
	public void run() {
		// TODO Auto-generated method stub
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
				
				if(dp.getDataHeader().equals("downloadFile"))
				{
					ProcessDownloadFile();
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
	
	public void ProcessDownloadFile()
	{
		System.out.println("In Client-Server received the info:"+dp.getDataHeader()+" "+dp.getFid().getFpath()
				+" "+dp.getFid().getFname()+" "+dp.getFid().getFsize()+"K");
		
		//从本地读文件
		byte[] fileData = ReadLocalFileData(dp.getFid().getFpath(),dp.getFid().getFname(),dp.getFid().getFsize());
		
		dp.setFileData(fileData);
		
		if(fileData != null)
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
	
	public byte[] ReadLocalFileData(String fpath,String fname,String size)
	{
		//直接和指定客户端连接，进行文件传输
		//向ObjectInputStream中写文件数据
		byte[] buf =new byte[Integer.parseInt(size)];
		File fileRead;
		FileInputStream fileIn = null;
		try {
			fileRead=new File(fpath,fname);
			fileIn=new FileInputStream(fileRead);

			int num=fileIn.read(buf);
			
			while(num!=-1)
			{
			   num=fileIn.read(buf);
			}
			fileIn.close();
			return buf;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				fileIn.close();
				return null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
		} 
	}
}

public class ClientSendFile
{
	public ClientSendFile(int clientPort)
	{
		//ClientSendFile的socket
		ServerSocket s = null;
		try {
			s = new ServerSocket(clientPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("The ClientServer is start:"+s);
		
		try
		{
			for(;;)
			{
				try {
					//阻塞，直到有客户端连接
					Socket socket=s.accept();
					//通过构造函数，启动线程
					new SendFileThread(socket);
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
}
