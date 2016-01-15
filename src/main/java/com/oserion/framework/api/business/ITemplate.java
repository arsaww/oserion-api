package com.oserion.framework.api.business;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.PageReference;

public interface ITemplate extends Serializable{

	String getName() ;
	void setName(String name) ;
	String getHtml() ;
	void setHtml(String html) ;

	List<ContentElement> getListTemplateElement() ;
	void setListTemplateElement(List<ContentElement> listTemplateElement) ;

	List<ContentElement> getListVariableElement() ;
	void setListVariableElement(List<ContentElement> listVariableElement) ;

	List<PageReference> getListPage();
	void setListPage(List<PageReference> listPage);
		
}

