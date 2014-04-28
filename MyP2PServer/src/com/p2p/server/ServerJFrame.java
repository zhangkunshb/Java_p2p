package com.p2p.server;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;

class ServerJFrame extends JFrame implements ActionListener
{
	private JButton start,stop;

	private Vector socketVector;
	
	private StartThread st;

	public ServerJFrame()
	{
		socketVector = new Vector();
		
		this.setLayout(new BorderLayout());

		start = new JButton("Start server");
		stop = new JButton("Stop server");
		stop.setEnabled(false);
		start.addActionListener(this);
		stop.addActionListener(this);
		
		this.add(start,BorderLayout.WEST);
		this.add(stop,BorderLayout.CENTER);

		this.setTitle("P2P Server");
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(width / 2 - 110, height / 2 - 55);
		this.setSize(220,110);
		this.setVisible( true );
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	public void actionPerformed( ActionEvent actionEvent)
	{
		if(actionEvent.getSource() == start)
		{
			start.setEnabled(false);
			stop.setEnabled(true);
			st = new StartThread();
		}
		else
		{
			start.setEnabled(true);
			stop.setEnabled(false);
			st.stop();
			st.StopServer();
		}
	}
}
