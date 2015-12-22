package com.example.htp.model;

import java.util.Date;
import java.util.Map.Entry;

import com.alibaba.fastjson.annotation.JSONField;

public class SysAcct implements java.io.Serializable, Entry{

	@Override
	public String toString() {
		return "SysAcct [id=" + id + ", acctType=" + acctType + ", acct="
				+ acct + ", phone=" + phone + ", password=" + password
				+ ", lastLoginDate=" + lastLoginDate + ", lastLoginIp="
				+ lastLoginIp + ", loginTimes=" + loginTimes + ", createdDate="
				+ createdDate + ", modifiedDate=" + modifiedDate + ", enable="
				+ enable + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -9183114005095455517L;

	
	private String acctName;
	private String id;
	
	private int acctType;  //1用户   2 员工  
	
	//账号信息
	private String acct;
	private String phone;
	
	@JSONField(serialize = false)
	private String password;
	
	//登陆信息
	@JSONField(serialize = false)
	private Date lastLoginDate;
	
	@JSONField(serialize = false)
	private String lastLoginIp;
	
	@JSONField(serialize = false)
	private Integer loginTimes;
	
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private int enable;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAcctType() {
		return acctType;
	}

	public void setAcctType(int acctType) {
		this.acctType = acctType;
	}

	public String getAcct() {
		return acct;
	}

	public void setAcct(String acct) {
		this.acct = acct;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Object getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object setValue(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	
	
	
	
}
