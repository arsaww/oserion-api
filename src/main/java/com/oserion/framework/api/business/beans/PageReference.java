package com.oserion.framework.api.business.beans;

public class PageReference{

	private String url;
	
	private int key;

	public PageReference() {}
	public PageReference(String url, int key) {
		this.url = url;
		this.key = key;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
}

