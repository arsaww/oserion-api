package com.oserion.framework.api.business;


import com.oserion.framework.api.exceptions.OserionDatabaseException;
import org.springframework.stereotype.Component;

@Component
public interface ITemplificator {

	ITemplate createTemplateFromHTML(String name, String html);
	String generateHtmlPage(String url, boolean showToolbar) throws OserionDatabaseException;

	IPage generateFilledPageFromTemplate(String url, ITemplate t);

	ITemplate generateFilledTemplateFromUrl(String url, boolean showToolbar) throws OserionDatabaseException;

	String generateHtmlTemplate(String url, boolean showToolbar, boolean enableJS) throws OserionDatabaseException;


	/*ContentElement majContenu(ContentElement e);
	String construireFlux(MongoTemplate t1, int key);
	
	List<ITemplate> selectTemplates();*/
	
}
