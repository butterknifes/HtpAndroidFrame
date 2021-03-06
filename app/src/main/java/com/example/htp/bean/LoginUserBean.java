package com.example.htp.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 */

@SuppressWarnings("serial")
@XStreamAlias("htp")
public class LoginUserBean extends Entity {
	
	@XStreamAlias("result")
	private Result result;
	
	@XStreamAlias("user")
	private User user;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}