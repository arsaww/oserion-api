package com.oserion.framework.api.business.beans;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contentElements")
public class ContentElement {

	public enum Type { editable, repeatable }
	
	private String ref;
	private String type ;
	private String value;

	public ContentElement() {}
	public ContentElement(String ref, String type, String value) {
		this.ref = ref;
		this.type = type;
		this.value = value;
	}
	public ContentElement(String ref, String type) {
		this.ref = ref;
		this.type = type;
	}

	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}

