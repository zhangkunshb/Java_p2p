package com.p2p.gui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.p2p.client.ClientReceiveBean;
import com.p2p.client.ClientSendBean;


public class RegisterJFrame extends JFrame implements ActionListener
{
	public RegisterJFrame(UserLoginJFrame ulf)
	{
		initComponents();
		
		//保存父窗口句柄
		this.ulf = ulf;
		
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		
		//设置窗体属性
		this.setTitle("用户注册");
		this.setSize(250,175);
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int height=Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(width/2-100,height/2-150);
		this.setResizable(false);//禁止改变窗体大小
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initComponents()
	{
		register = new JButton("确定");
		cancel = new JButton("取消");
		
		register.addActionListener(this);
		cancel.addActionListener(this);
		
		username = new JLabel("用户名:");
		password = new JLabel("密     码:");
		port = new JLabel("端口号:");
		
		jTextFieldUserName = new JTextField(10);
		jTextFieldPort = new JTextField(10);
		jPasswordField = new JPasswordField(10);
		
		j1 =new JPanel();
		j2 =new JPanel();
		j3 =new JPanel();
		j4 =new JPanel();
		
		j1.add(username);
		j1.add(jTextFieldUserName);
		
		j2.add(password);
		j2.add(jPasswordField);
		
		j3.add(port);
		j3.add(jTextFieldPort);
		
		j4.add(register);
		j4.add(cancel);
		
		this.setLayout(new GridLayout(4,1));
		
		this.add(j1);
		this.add(j2);
		this.add(j3);
		this.add(j4);
		
		this.setVisible(true);
		this.setResizable(false);
	}
	
	//定义需要的组件
	private JButton register;
    private JButton cancel;
    
    public static JLabel username;
    public static JLabel password;
    public static JLabel port;
    
    public static JTextField jTextFieldUserName;
    public static JTextField jTextFieldPort;
    public static JPasswordField jPasswordField;
    
    public JPanel j1,j2,j3,j4;
    
    private UserLoginJFrame ulf;
    
    protected void processWindowEvent(WindowEvent e) 
	{   
        if (e.getID() == WindowEvent.WINDOW_CLOSING) 
        {  
        	this.dispose();
        }
    }  
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == register)
		{
			//封装用户注册信息（用户名，密码，ip地址，端口号，在线状态）
			String username = jTextFieldUserName.getText();
			String password = new String(jPasswordField.getPassword());
			String port = jTextFieldPort.getText();
			
			String ip="";
			try {
				InetAddress addr = InetAddress.getLocalHost();
				ip=addr.getHostAddress().toString();//获得本机IP
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//用户信息传递给服务器,进行注册
			ClientSendBean csb=new ClientSendBean(null,0,"registerRequest",username,password,ip,port+"","1");
			//cb.SubmitRegisterInfo(username,password,ip,port,1);
				
			//接收验证结果
			ClientReceiveBean crb = new ClientReceiveBean("registerRequest",this.ulf,this,null,csb);
		}
		else
		{
			this.dispose();
		}
	}
}
