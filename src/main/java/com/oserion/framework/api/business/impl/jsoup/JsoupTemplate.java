package com.oserion.framework.api.business.impl.jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.PageReference;
import org.jsoup.nodes.Document;


public class JsoupTemplate implements ITemplate {

	private Document jsoupDocument;

	private String name = null;
	private String html = null;
	
	private List<ContentElement> listTemplateElement  = new ArrayList<>();
	private List<ContentElement> listVariableElement  = new ArrayList<>();
	private List <PageReference> listPage   = new ArrayList<>();
	
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
	public List <PageReference> getListPage() {
		return listPage;
	}

	@Override
	public void setListPage(List<PageReference> listPage) {
		this.listPage = listPage;
	}

	@JsonIgnore
	public Document getJsoupDocument() {
		return jsoupDocument;
	}

	public void setJsoupDocument(Document jsoupDocument) {
		this.jsoupDocument = jsoupDocument;
	}
	
	/**
	 * Les constructeurs
	 * @param name
	 */
	public JsoupTemplate(String name) {
		this.name = name;
	}
	public JsoupTemplate() {
	}



}



