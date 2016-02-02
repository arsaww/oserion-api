package com.oserion.framework.api.business.impl.jsoup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.IPage;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.ContentElement.Type;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;
import com.oserion.framework.api.exceptions.OserionDatabaseException;
import com.oserion.framework.api.util.CodeReturn;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class JsoupTemplificator implements ITemplificator {

    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final String REF_PAGE = "--page";
    
	@Autowired
	private IDataHandler dataHandler;

	/*@Autowired
	public MongoOperations mongoOperation;*/

	//	@Autowired
	//	private ITemplate template = null;

	
	/*public List<ITemplate> selectTemplates() {
		List<MongoTemplate> listTemplate = mongoOperation.findAll(MongoTemplate.class);
		List<ITemplate> listITemplate = new ArrayList<ITemplate>();
		
		for(MongoTemplate t1 : listTemplate ) {
			ITemplate itemp = new JsoupTemplate(t1.getName());
			
			List<PageReference> listPageReference = getPageReferenceFromTemplate(t1);
			if(listPageReference != null)
				itemp.getListPage().addAll(listPageReference);
				
			listITemplate.add(itemp);
		}
		return listITemplate;
	}*/

	
	/*private List<PageReference> getPageReferenceFromTemplate(MongoTemplate t1) {
		Query q1 = new Query(Criteria.where("template").is(t1));
		List<PageReference> listPageRef = (List<PageReference>) mongoOperation.find(q1, PageReference.class);
		return listPageRef;
	}*/


	/*public ContentElement majContenu(ContentElement e) {
		ContentElement cte = new ContentElement(e.getRef(), e.getType() , e.getValue());
		//		ContentElement cte = new ContentElement(e.getRef(), ContentElement.Type.valueOf(e.getType()), e.getValue());
		return cte; 
	}*/

	public JsoupTemplate createTemplateFromHTML(String name, String html) {
		JsoupTemplate template = new JsoupTemplate(name);
		template.setHtml(html);
		splitContenu(html, template);
		return template;
	}

	@Override
	public String generateHtmlPage(String url, boolean showToolbar) throws OserionDatabaseException {
        //TODO CACHE HERE
		//getPageFromFinalCachePage
		//getPageFromCacheTemplate
		//getTemplate
		ITemplate t = generateFilledTemplateFromUrl(url, showToolbar);
		//getPageFromCacheTemplate
		IPage p = generateFilledPageFromTemplate(url, t);
		return p.getHtml();
	}

	@Override
	public IPage generateFilledPageFromTemplate(String url, ITemplate t) {

		/** Create page **/
		PageReference pr = findPageReferenceFromUrl(url, t);
		JsoupPage p = new JsoupPage((JsoupTemplate) t, pr);

		/** Prepare Variables **/
		definePageContentElementReferences(p);

		/** Fill the dom document **/
		fillPageHtml(p);

		return p;
	}

	private void fillPageHtml(JsoupPage p){
		for(ContentElement c : p.getListPageElement()){
			if(c.getValue() != null) {
				Element e = p.getJsoupDocument().getElementById(c.getRef());
				if(e != null && e.hasClass(c.getType())){
					fillDomElement(e,c);
				}
			}
		}
	}

	private void definePageContentElementReferences(JsoupPage p){
		List<ContentElement> undefinedVarELements = (p.getTemplate()).getListVariableElement();
		List<ContentElement> definedVarElements = new ArrayList<>();
		for(ContentElement undefined : undefinedVarELements){
            Elements e = p.getJsoupDocument().select("#" + undefined.getRef() + "." + undefined.getType());
            String ref = getTrueIdentifier(undefined.getRef(), p);
            e.attr("id",ref);
			String type = undefined.getType();/*getTrueIdentifier(undefined.getType(), p);
            e.removeClass(undefined.getType());
            e.addClass(type);*/
			ContentElement defined = dataHandler.selectContent(ref, type);
			if(defined != null)
				definedVarElements.add(defined);
		}
		p.setListPageElement(definedVarElements);
	}

	private String getTrueIdentifier(String identifier, JsoupPage p){
		return identifier.replaceAll(REF_PAGE, "" + p.getPageReference().getKey());
	}
	private PageReference findPageReferenceFromUrl(String url, ITemplate t){
		for(PageReference pr : t.getListPage()){
			if(pr.getUrl().equals(url)){
				return pr;
			}
		}
		return null;
	}

	@Override
	public ITemplate generateFilledTemplateFromUrl(String url,  boolean showToolbar) throws OserionDatabaseException {
		ITemplate dbTemplate = dataHandler.selectTemplateByUrl(url);
		JsoupTemplate jsoupTemplate = buildJsoupTemplate(dbTemplate);
		List<ContentElement> filledElements = dataHandler.fillContentElements(jsoupTemplate.getListTemplateElement());
		jsoupTemplate.setListTemplateElement(filledElements);
		fillTemplateHtml(jsoupTemplate);
		if(showToolbar)
			addToolbar(jsoupTemplate);
		return jsoupTemplate;
	}

    @Override
    public String generateHtmlTemplate(String url, boolean showToolbar, boolean enableJS) throws OserionDatabaseException {
        ITemplate dbTemplate = dataHandler.selectTemplateByUrl(url);
        JsoupTemplate jsoupTemplate = buildJsoupTemplate(dbTemplate);
		makeJsoupReadableScripts(enableJS,jsoupTemplate.getJsoupDocument());
        if(showToolbar)
            addToolbar(jsoupTemplate);
        return jsoupTemplate.getHtml();
    }

	@Override
	public String clearOserionMarkup(String html) {
		Document d = Jsoup.parse(html);
		d.select(".oserion-script").remove();
		d.select("#oserion_toolbar").remove();
		d.select(".oserion-style").remove();
		d.select(".oserion_tootlbar_container").removeClass(".oserion_tootlbar_container");
		return d.html();
	}

	private void makeJsoupReadableScripts(boolean readable, Document doc){
		if(readable){
			doc.select("script-oserion-disable").tagName("script").removeAttr("style");
		}else{
			doc.select("script").tagName("script-oserion-disable").attr("style","display:none;");
		}

	}

    private void addToolbar(JsoupTemplate t){
		Document doc = t.getJsoupDocument();
		Element jQuery = doc.body().appendElement("script");
		jQuery.attr("src","/assets/scripts/jquery.min.js");
		jQuery.attr("class","oserion-script");
		Element toolbarElement = doc.body().appendElement("script");
		toolbarElement.attr("src","/assets/oserion-toolbar/oserion-toolbar.js");
		toolbarElement.attr("class","oserion-script");
		t.setHtml(t.getJsoupDocument().html());
	}

	private void fillTemplateHtml(JsoupTemplate t){
		for(ContentElement c : t.getListTemplateElement()){
			if(c.getValue() != null) {
				Element e = t.getJsoupDocument().getElementById(c.getRef());
				if(e != null && e.hasClass(c.getType())){
					fillDomElement(e,c);
				}
			}
		}
		t.setHtml(t.getJsoupDocument().html());
	}

	private void fillDomElement(Element e, ContentElement c){
		switch(c.getType()){
			case "editable": e.html(c.getValue());
				break;
		}
	}

	private JsoupTemplate buildJsoupTemplate(ITemplate template){
		if(template instanceof JsoupTemplate)
			return (JsoupTemplate) template;
		else {
			JsoupTemplate t = new JsoupTemplate(template.getName());
			t.setHtml(template.getHtml());
			t.setListVariableElement(template.getListVariableElement());
			t.setListTemplateElement(template.getListTemplateElement());
			t.setListPage(template.getListPage());
			t.setJsoupDocument(Jsoup.parse(template.getHtml()));
			return t;
		}
	}


	/**
	 * Rempli les champs listTemplateElement et listVariableElement de l'objet template qui est passé en paramètre. 
	 * Cet objet est un JsoupTemplate. 
	 * 
	 * @param fluxTemplate
	 * @param template
	 */
	public void splitContenu(String fluxTemplate, JsoupTemplate template ) {
		Document docJsoup = Jsoup.parse(fluxTemplate);
		template.setJsoupDocument(docJsoup);
		Elements ele = getAllElement(docJsoup);
		//System.out.println("taille ele => " + ele.size());

		Iterator<Element> it = ele.iterator();
		while(it.hasNext()) {
			Element balise = it.next();
			String type = getClassBalise(balise);
			if(balise.id().contains("--page")) {
				//System.out.println(balise.toString());
				ContentElement cte = new ContentElement(balise.id(), type , null /*balise.html()*/);
				//				ContentElement cte = new ContentElement(balise.id(), ContentElement.Type.EDITABLE.toString());
				template.getListVariableElement().add(cte);
			} else if (!balise.id().isEmpty()) {
				ContentElement cte = new ContentElement(balise.id(), type , null /*balise.html()*/);
				//				ContentElement cte = new ContentElement(balise.id(), ContentElement.Type.EDITABLE.toString());
				template.getListTemplateElement().add(cte);
			}
		}
	}
	
	
	/**
	 * la fct retourne la liste de toutes les balises qui contiennent au moins une des classes css
	 * contenue dans l'enumération {@link Type} . 
	 * 
	 * @param docJsoup
	 * @return
	 */
	private Elements getAllElement(Document docJsoup) {
		Elements listEle = null;
		Type[] lesTypes = ContentElement.Type.values();
		for(int i=0;i<lesTypes.length;i++) {
			if(listEle == null)
				listEle = docJsoup.select("."+lesTypes[i].name());
			else 
				listEle.addAll(docJsoup.select("."+lesTypes[i].name()));
		}
		return listEle;
	}
	

	/**
	 * La fct parcourt l'ensemble des {@link Type} 
	 * afin de le comparer avec celui de la balise passée en paramètre.
	 * Si la balise contient un des types, celui-ci est retourné sous forme de String.
	 * @param ele
	 * @return
	 */
	private String getClassBalise(Element ele) {
		Type[] lesTypes = ContentElement.Type.values();
		for(int i=0;i<lesTypes.length;i++) {
			if(ele.hasClass(lesTypes[i].name())) 
				return lesTypes[i].name();
		}
		return null;
	}

	public String construireFlux(MongoTemplate t1, int key) {
		List<ContentElement> listTemplateElement = t1.getListTemplateElement();
		List<ContentElement> listVariableElement = t1.getListVariableElement();
		String htmlVariabiliser = t1.getHtml().replaceAll("ref:page", String.valueOf(key));
		Document docJsoup = Jsoup.parse(htmlVariabiliser);
		Elements ele = docJsoup.select(".editable");
		Iterator<Element> it = ele.iterator();
		while(it.hasNext()) {
			Element balise = it.next();
			String value = getContentFromRef(listVariableElement, balise.id(), Type.editable.name());
			if(value != null) {
				docJsoup.getElementById(balise.id()).html(value);
			} else {
				value = getContentFromRef(listTemplateElement, balise.id(), Type.editable.name());
				if(value!=null)
					docJsoup.getElementById(balise.id()).html(value);
				else 					
					return CodeReturn.error25(t1.getName(), balise.id() );
			}
		}
		
		return docJsoup.toString();
	}

	private String getContentFromRef(List<ContentElement> listElement, String ref, String type) {
		for(ContentElement cte : listElement) {
			if(cte.getType().equalsIgnoreCase(type) && cte.getRef().equalsIgnoreCase(ref)) {
				System.out.println("ref => " + ref + " , cte.getValue() => " + cte.getValue() );
				return cte.getValue();
			}
		}
		return null;
	}

	public IDataHandler getDataHandler() {
		return dataHandler;
	}

	public void setDataHandler(IDataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}

}



