package com.oserion.framework.api.business;


import com.oserion.framework.api.exceptions.OserionDatabaseException;
import org.springframework.stereotype.Component;

@Component
public interface ITemplificator {

	ITemplate createTemplateFromHTML(String name, String html);
	String generateHtmlPage(String url, boolean showToolbar) throws OserionDatabaseException;

	ITemplate generateFilledTemplateFromUrl(String url, boolean showToolbar) throws OserionDatabaseException;


	/*ContentElement majContenu(ContentElement e);
	String construireFlux(MongoTemplate t1, int key);
	
	List<ITemplate> selectTemplates();*/
	
}
