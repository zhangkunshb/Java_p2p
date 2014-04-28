package com.p2p.datapackage;

import java.io.Serializable;

public class UserInfoDTO implements Serializable
{
	private String uname;
	private String upw;
	private String uip;
	private int uport;
	private int uonline;
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpw() {
		return upw;
	}
	public void setUpw(String upw) {
		this.upw = upw;
	}
	public String getUip() {
		return uip;
	}
	public void setUip(String uip) {
		this.uip = uip;
	}
	public int getUport() {
		return uport;
	}
	public void setUport(int uport) {
		this.uport = uport;
	}
	public int getUonline() {
		return uonline;
	}
	public void setUonline(int uonline) {
		this.uonline = uonline;
	}
}
