package com.oserion.framework.api.business.impl.jsoup;

import com.oserion.framework.api.business.IPage;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.PageReference;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsaww on 1/17/2016.
 */
public class JsoupPage implements IPage {

    private Document jsoupDocument;
    private PageReference pageReference;
    private String lang;
    private ITemplate template;
    private List<ContentElement> listPageElement;


    public JsoupPage(JsoupTemplate t, PageReference pr){
        template = t;
        jsoupDocument = Jsoup.parse(t.getHtml());
        pageReference = pr;
    }

    public Document getJsoupDocument(){
        return jsoupDocument;
    }

    @Override
    public String getHtml() {
        return jsoupDocument.html();
    }

    @Override
    public void setHtml(String html) {
        jsoupDocument.html(html);
    }

    @Override
    public PageReference getPageReference() {
        return pageReference;
    }

    @Override
    public void setPageReference(PageReference pr) {
        pageReference = pr;
    }

    @Override
    public String getLang() {
        return lang;
    }

    @Override
    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public ITemplate getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(ITemplate t) {
        template = t;
    }

    @Override
    public List<ContentElement> getListPageElement() {
        return listPageElement;
    }

    @Override
    public void setListPageElement(List<ContentElement> list) {
        listPageElement = list;
    }
}
