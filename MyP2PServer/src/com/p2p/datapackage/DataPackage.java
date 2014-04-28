package com.p2p.datapackage;

import java.io.Serializable;
import java.util.Vector;

public class DataPackage implements Serializable
{
	//数据包头，用于区分请求的类型，例如登录则为”loginRequest“，注册则为”registerRequest“
	private String dataHeader;
	
	//服务器对请求的结果反馈，操作成功则为“success",失败则为”failed“
	private String result;
	
	//主界面的搜索条件文本框中的内容，即搜索关键字
	private String seachKeyword;
	
	//查询类型，可取1,2,3（1代表关键字搜索  2代表查询服务器全部文件列表  3代表查询自己上传文件列表）
	private int searchType;
	
	//以下是两个内置对象，分别封装用户信息以及文件信息，详细定义见UserInfoDTO.java和FileInfo.java文件
	private UserInfoDTO uid;
	private FileInfoDTO fid;
	
	//查询请求返回的数据集，对应主界面的JTable控件，用户更新JTable数据
	private Vector tableData;
	
	//客户端之间传输文件时，将文件的数据封住成byte字节数组
	private byte[] fileData;
	
	
	public String getDataHeader() {
		return dataHeader;
	}

	public void setDataHeader(String dataHeader) {
		this.dataHeader = dataHeader;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public UserInfoDTO getUid() {
		return uid;
	}

	public void setUid(UserInfoDTO uid) {
		this.uid = uid;
	}

	public FileInfoDTO getFid() {
		return fid;
	}

	public void setFid(FileInfoDTO fid) {
		this.fid = fid;
	}

	public Vector getTableData() {
		return tableData;
	}

	public void setTableData(Vector tableData) {
		this.tableData = tableData;
	}
	
	public String getSeachKeyword() {
		return seachKeyword;
	}

	public void setSeachKeyword(String seachKeyword) {
		this.seachKeyword = seachKeyword;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	
	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
}
