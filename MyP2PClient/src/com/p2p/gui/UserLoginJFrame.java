package com.p2p.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class UserLoginJFrame extends JFrame implements ActionListener
{
	public UserLoginJFrame() 
    {
        initComponents();
        
        //设置窗体属性
		this.setTitle("P2P文件共享系统");
		this.setSize(280,175);
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int height=Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(width/2-100,height/2-130);
		this.setResizable(false);//禁止改变窗体大小
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //显示窗体
        this.setVisible(true);
        
    }
	
	private void initComponents()
	{
		//创建组件
		login = new JButton("登陆");
        reset = new JButton("重置");
        register = new JButton("<HTML><font color=blue>新用户注册</font></HTML>");
        register.setFont(f2);
        
        login.addActionListener(this);
        reset.addActionListener(this);
        register.addActionListener(this);
        
        username =new JLabel("用户名:");
        password =new JLabel("密     码:");
        welcome =new JLabel("Welocme to P2P File System");
        welcome.setFont(f1);
        welcome.setForeground(Color.RED);
        
        jTextFieldUserName = new JTextField(10);
        jPasswordField1 = new JPasswordField(10);
        
        up = new JPanel();
        center1 = new JPanel();
        center2 = new JPanel();
        down = new JPanel();
        
        //设置布局管理器
        this.setLayout(new GridLayout(4,1));
        
        //添加组件
        up.add(welcome);
        
        center1.add(username);
        center1.add(jTextFieldUserName);
        
        center2.add(password);
        center2.add(jPasswordField1);
        
        down.add(login);
        down.add(reset);
        down.add(register);
        
        this.add(up);
        this.add(center1);
        this.add(center2);
        this.add(down);
	}
	
	public void actionPerformed(ActionEvent e) 
    {
		//按下”登录“按钮
		if(e.getSource()== login)
		{
			String username = jTextFieldUserName.getText();
			String password = new String(jPasswordField1.getPassword());
			
			String ip="";
			try {
				InetAddress addr = InetAddress.getLocalHost();
				ip=addr.getHostAddress().toString();//获得本机IP
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//用户名和密码传给服务器,进行登录验证
			ClientSendBean csb=new ClientSendBean(null,0,"loginRequest",username,password);
				
			//接收验证结果
			ClientReceiveBean crb = new ClientReceiveBean("loginRequest",this,null,null,csb);
		}
		//按下”重置“按钮
		else if(e.getSource()== reset)
		{
			jTextFieldUserName.setText("");
			jPasswordField1.setText("");
		}
		//按下”新用户注册“按钮
		else if(e.getSource()== register)
		{
			RegisterJFrame rf = new RegisterJFrame(this);
		}
    }
	
	//定义需要的组件
	private JButton login;
    private JButton reset;
    private JButton register;
    
    public static JLabel username;
    public static JLabel password;
    public static JLabel welcome;
    
    public static JTextField jTextFieldUserName;
    public static JPasswordField jPasswordField1;
    
    public JPanel up,center1,center2,down;
    
    Font f1=new Font("宋体",Font.BOLD,15);
    Font f2=new Font("Serif",Font.BOLD,12);
    
}

