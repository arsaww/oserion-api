package com.oserion.framework.api;

import java.util.List;
import java.util.logging.Logger;

import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.exceptions.OserionDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplate;

@Component
public class OserionApiFacade {

	private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


	@Autowired
	private IDataHandler dataHandler;

	@Autowired
	private ITemplificator templificator;

	public void createTemplate(String name, String html) throws OserionDatabaseException {
		dataHandler.insertTemplate(name, html);
	}

	public List<ITemplate> getTemplates() throws OserionDatabaseException {
		return dataHandler.selectTemplates();
	}

	public void addPageUrl(String templateName, String url) throws OserionDatabaseException {
		dataHandler.insertPageUrl(templateName, url);
	}

	public String getHtmlPage(String url, boolean showToolbar) throws OserionDatabaseException {
		return templificator.generateHtmlPage(url, showToolbar);
	}

	public void setContentElementValue(ContentElement c) throws OserionDatabaseException {
		dataHandler.upsertContentElementValue(c);
	}


	public String getHtmlTemplate(String url, boolean showToolbar, boolean enableJS) throws OserionDatabaseException {
		return templificator.generateHtmlTemplate(url, showToolbar, enableJS);
	}
}

