package com.oserion.framework.api.business;

import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.PageReference;

import java.io.Serializable;
import java.util.List;

public interface IPage extends Serializable {

    String getHtml();
    void setHtml(String html);
    PageReference getPageReference();
    void setPageReference(PageReference pr);
    String getLang();
    void setLang(String lang);

    ITemplate getTemplate();
    void setTemplate(ITemplate t);

    List<ContentElement> getListPageElement();
    void setListPageElement(List<ContentElement> list);

}

