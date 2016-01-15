package com.oserion.framework.api.business.impl.mongo.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.beans.PageReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.oserion.framework.api.business.beans.ContentElement;


@Document(collection = "templates")
public class MongoTemplate implements ITemplate{

	@Id
    private String id;
	
	@Indexed(unique = true)
	private String name;
    
	private String html;

	private List<ContentElement> listTemplateElement  = new ArrayList<>();
	private List<ContentElement> listVariableElement  = new ArrayList<>();
	private List<PageReference> listPage = new ArrayList<>();

	public MongoTemplate() {}
	public MongoTemplate(ITemplate t) {
		this.name = t.getName();
		this.html = t.getHtml();
		this.listPage = t.getListPage();
		this.listTemplateElement = t.getListTemplateElement();
		this.listVariableElement = t.getListVariableElement();
	}

	@JsonIgnore
	public String getId() {
		return id;
	}
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}


	@Override
	public List<ContentElement> getListTemplateElement() {
		return listTemplateElement;
	}

	@Override
	public void setListTemplateElement(List<ContentElement> listTemplateElement) {
		this.listTemplateElement = listTemplateElement;
	}

	@Override
	public List<ContentElement> getListVariableElement() {
		return listVariableElement;
	}

	@Override
	public void setListVariableElement(List<ContentElement> listVariableElement) {
		this.listVariableElement = listVariableElement;
	}

	@Override
	public List<PageReference> getListPage() {
		return listPage;
	}

	@Override
	public void setListPage(List<PageReference> listPage) {
		this.listPage = listPage;
	}

	@Override
    public String toString() {
        return String.format(
                "MongoTemplate[name=%s, html='%s']",
                name, html);
    }
    
}
