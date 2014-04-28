package com.p2p.client;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.p2p.datapackage.DataPackage;
import com.p2p.gui.*;
import com.p2p.util.FileInfoModel;

public class ClientReceiveBean extends Thread
{
	private UserLoginJFrame ulf;
	private RegisterJFrame rf;
	private MainJFrame mjf;
	
	private ClientSendBean csb;
	
    private String requestType = "";

	private String savePath;
    private String filename;
    
	//客户端数据包
	private DataPackage dp;
	
	//保存ClientSendBean的IO对象，用于接受反馈数据
	private ObjectInputStream sin;
	private ObjectOutputStream sout;
    
	//构造函数
    public ClientReceiveBean(String requestType,UserLoginJFrame ulf,RegisterJFrame rf,
    		MainJFrame mjf,ClientSendBean csb) 
    {
		// TODO Auto-generated constructor stub
    	this.csb = csb;
    	this.sin = csb.getIn();
    	this.sout = csb.getOut();
		this.requestType = requestType;
		this.ulf = ulf;
		this.rf = rf;
    	this.mjf = mjf;
    	
    	this.start();
	}
    
    //线程主体函数
	public void run()
	{
		while(true)
		{
			if(!"".equals(requestType))
			{
				dp = new DataPackage();
				
				try {
					dp =(DataPackage)sin.readObject();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if("loginRequest".equals(requestType))
			{
				LoginResult();
			}
			else if("registerRequest".equals(requestType))
			{
				RegisterResult();
			}
			else if("queryFile".equals(requestType))
			{
				QueryFileResult();
			}
			else if("updateUserInfo".equals(requestType))
			{
				UpdateResult();
			}
			else if("deleteFile".equals(requestType))
			{
				DeleteFileResult();
			}
			else if("uploadFile".equals(requestType))
			{
				UploadFileResult();
			}
			else if("downloadFile".equals(requestType))
			{
				DownloadFileResult();
			}
			else if("".equals(requestType))
			{
				break;
			}
		}
	}
	
	public void LoginResult()
	{
		if(dp != null)
		{
			if(dp.getResult() != null)
			{
				//获取验证结果
				if("success".equals(dp.getResult()))
				{
					//进入新的MainJFrame
					ulf.dispose();
					
					//获取本机实时IP地址
					String ip="";
					InetAddress addr = null;
					InetAddress addrName = null;
					try {
						addr = InetAddress.getLocalHost();
						ip=addr.getHostAddress().toString();//获得本机IP
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					requestType = "";
					
					System.out.println("登录"+dp.getResult()+"!");
					new MainJFrame(dp.getUid().getUname(),ip,dp.getUid().getUport());	
				}
				else if("failed".equals(dp.getResult()))
				{
					JOptionPane.showMessageDialog(null, "用户名或密码错误,请重新输入!", "消息", JOptionPane.ERROR_MESSAGE);
					ulf.jPasswordField1.setText("");
					ulf.jTextFieldUserName.grabFocus();
					requestType = "";
					
					System.out.println("登录"+dp.getResult()+"!");
				}
				
			}
		}
	}
   
	public void RegisterResult()
	{
		if(dp != null)
		{
			if(dp.getResult() != null)
			{
				//获取验证结果
				if("success".equals(dp.getResult()))
				{
					//进入新的Frame
					JOptionPane.showMessageDialog(null, "用户注册成功,现在进入主界面!", "消息", JOptionPane.INFORMATION_MESSAGE);
					rf.dispose();
					ulf.dispose();
					
					requestType ="";
					
					System.out.println("注册"+dp.getResult()+"!");
					new MainJFrame(csb.getPara()[0],csb.getPara()[2],Integer.parseInt(csb.getPara()[3]));	
				}
				else if("failed".equals(dp.getResult()))
				{
					JOptionPane.showMessageDialog(null, "用户名已存在,请重新输入!", "消息", JOptionPane.ERROR_MESSAGE);
					rf.jPasswordField.setText("");
					rf.jTextFieldUserName.grabFocus();
					requestType ="";
					
					System.out.println("注册"+dp.getResult()+"!");
				}
			}
		}
	}
	
	public void QueryFileResult()
	{
		if(dp != null)
		{
			if(dp.getResult() != null)
			{
				//获取验证结果
				if ("success".equals(dp.getResult())) {
					if (dp.getTableData().size() == 0) 
					{
						if(dp.getSearchType() == 3)
						{
							mjf.jLabelDownLoadOver.setText("该用户未上传文件!");
						}
						else if(dp.getSearchType() == 2)
						{
							mjf.jLabelDownLoadOver.setText("服务端文件列表为空!");
						}
						else
						{
							mjf.jLabelDownLoadOver.setText("无满足条件的文件!");
						}
					} 
					else 
					{
						mjf.jLabelDownLoadOver.setText("查询成功!");
					}
		
					//更新表格数据
					mjf.fim = new FileInfoModel(dp.getTableData());
					mjf.jTableFileInfo.setModel(mjf.fim);
					//更新数据完毕
					
					//mjf.jTextFieldKeyWord.setText("");
					requestType = "";
					
					System.out.println("查询元数据"+dp.getResult()+"!");
				} 
				else if("failed".equals(dp.getResult()))
				{
					JOptionPane.showMessageDialog(null, "文件查询失败,请重新查询!", "消息",
							JOptionPane.ERROR_MESSAGE);
					mjf.jLabelDownLoadOver.setText("查询失败!");
					mjf.jTextFieldKeyWord.setText("");
					requestType = "";
					
					System.out.println("查询元数据"+dp.getResult()+"!");
				}
				
				
			}
		}
	}
	
	public void DeleteFileResult()
	{
		if(dp != null)
		{
			if(dp.getResult() != null)
			{
				//获取验证结果
				if ("success".equals(dp.getResult())) 
				{
					//刷新JTable中的数据
					//重新调用查询过程
					mjf.ProcessQuery(3);
					
					mjf.jLabelDownLoadOver.setText("删除成功!");
					mjf.jTextFieldKeyWord.setText("");
					requestType = "";
					
					System.out.println("删除元数据"+dp.getResult()+"!");
				} 
				else if("failed".equals(dp.getResult())) 
				{
					//刷新JTable中的数据
					//重新调用查询过程
					mjf.ProcessQuery(3);
					
					mjf.jLabelDownLoadOver.setText("删除失败!");
					mjf.jTextFieldKeyWord.setText("");
					requestType = "";
					
					System.out.println("删除元数据"+dp.getResult()+"!");
				}
			}
		}
	}
	
	public void UploadFileResult()
	{
		if(dp != null)
		{
			if(dp.getResult() != null)
			{
				//获取验证结果
				if ("success".equals(dp.getResult())) 
				{
					mjf.jLabelDownLoadOver.setText("上传成功!");
					requestType = "";
					
					//更新JTable数据
					mjf.ProcessQuery(2);
					
					mjf.deleteButton.setEnabled(false);
					
					System.out.println("上传元数据"+dp.getResult()+"!");
				}
				else if("failed".equals(dp.getResult()))
				{
					JOptionPane.showMessageDialog(null, "文件已存在,请重新上传!", "消息",
							JOptionPane.ERROR_MESSAGE);
					mjf.jLabelDownLoadOver.setText("上传失败!");
					requestType = "";
					
					//更新JTable数据
					mjf.ProcessQuery(2);
					
					mjf.deleteButton.setEnabled(false);
					
					System.out.println("上传元数据"+dp.getResult()+"!");
				}
			}
		}
	}
	
	public void DownloadFileResult()
	{
		if(dp != null)
		{
			if(dp.getResult() != null)
			{
				//获取验证结果
				if ("success".equals(dp.getResult())) 
				{
					WriteFile(savePath,filename,dp.getFileData());
					
					mjf.jLabelDownLoadOver.setText("下载成功!");
					
					JOptionPane.showMessageDialog(null, "文件下载成功!", "消息",
							JOptionPane.INFORMATION_MESSAGE);
					
					requestType = "";
					
					System.out.println("文件下载"+dp.getResult()+"!");
				}
				else if("failed".equals(dp.getResult()))
				{
					mjf.jLabelDownLoadOver.setText("下载失败!");
					
					JOptionPane.showMessageDialog(null, "文件下载失败,请重新下载!", "消息",
							JOptionPane.ERROR_MESSAGE);
					
					requestType = "";
					
					System.out.println("文件下载"+dp.getResult()+"!");
				}
			}
		}
	}
	
	public void UpdateResult()
	{
		if(dp != null)
		{
			if(dp.getResult() != null)
			{
				//获取验证结果
				if ("success".equals(dp.getResult())) 
				{
					requestType = "";
					System.out.println("更新"+dp.getResult()+"!");
					if(dp.getUid().getUonline() == 0)
					{
						System.exit(0);
					}
				}
				else if("failed".equals(dp.getResult()))
				{
					requestType = "";
					System.out.println("更新"+dp.getResult()+"!");
				}
			}
		}
	}
	
	public void WriteFile(String savePath,String filename,byte[] fileData)
	{
		//从ObjectOutputStream中获取文件数据
		File fileSave=new File(savePath,filename);
        if(fileSave.exists())
        {
        	fileSave.delete();
        }
        
        RandomAccessFile wFile = null;
		try {
			fileSave.createNewFile();
			wFile = new RandomAccessFile(fileSave,"rw");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        

		try {
			wFile.write(fileData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				wFile.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally{
			try {
				wFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	////////////////////////////////////
	
	public UserLoginJFrame getUlf() {
		return ulf;
	}

	public void setUlf(UserLoginJFrame ulf) {
		this.ulf = ulf;
	}

	public MainJFrame getMjf() {
		return mjf;
	}

	public void setMjf(MainJFrame mjf) {
		this.mjf = mjf;
	}

	public RegisterJFrame getRf() {
		return rf;
	}

	public void setRf(RegisterJFrame rf) {
		this.rf = rf;
	}
	
	public ClientSendBean getCsb() {
		return csb;
	}

	public void setCsb(ClientSendBean csb) {
		this.csb = csb;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public DataPackage getDp() {
		return dp;
	}

	public void setDp(DataPackage dp) {
		this.dp = dp;
	}
}
