package com.oserion.framework.api.business;

import java.util.List;

import com.oserion.framework.api.exceptions.OserionDatabaseException;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.beans.ContentElement;

@Component
public interface IDataHandler {

	boolean insertOrUpdateTemplate(String templateName, String fluxHtml);
	void insertTemplate(String templateName, String fluxHtml) throws OserionDatabaseException;
	void updateTemplate(ITemplate t, String html) throws OserionDatabaseException;

	ITemplate selectTemplateByUrl(String url) throws OserionDatabaseException;
	ITemplate selectTemplateByName(String name) throws OserionDatabaseException;

	boolean insertOrUpdateManyContent(List<ContentElement> listElement);
	boolean insertOrUpdateContent(ContentElement ele);

	boolean deletePageURL(String URL);
	boolean deleteContent(String contentId, String contentType);
	boolean deleteTemplate(String templateName);

	String selectHTMLTemplate(String templateName);

	IPage selectFullPage(String Url);
	List<ITemplate> selectTemplates() throws OserionDatabaseException;
	ContentElement selectContent(String contentId, String contentType);
	void insertPageUrl(String templateName, String newUrl ) throws OserionDatabaseException;


	/** DEBUG */
	void displayContentBase();

	void upsertContentElementValue(ContentElement c) throws OserionDatabaseException;

	void upsertContentElementValue(String ref, String type, String value) throws OserionDatabaseException;

	List<ContentElement> fillContentElements(List<ContentElement> elements) throws OserionDatabaseException;
}
