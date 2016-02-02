package com.oserion.framework.api.business.impl.mongo;


import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.exceptions.OserionDatabaseNotFoundException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.oserion.framework.api.business.*;
import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;
import com.oserion.framework.api.exceptions.OserionDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.oserion.framework.api.business.beans.ContentElement;

@EnableMongoRepositories
public class MongoDBDataHandler implements IDataHandler {

    private static final String SEQUENCE_NAME_PAGE_KEY = "pageKey";
    private static final String COLLECTION_CONTENT_ELEMENT = "contentElements";

    @Autowired
    private ITemplificator templificator;

    @Autowired
    private IDBConnection connection;
    private MongoOperations operations;


    public MongoDBDataHandler(IDBConnection connection) throws OserionDatabaseException {
        setConnection(connection);
    }

    @Override
    public boolean insertOrUpdateTemplate(String templateName, String fluxHtml) {
        return false;
    }

    @Override
    public void insertTemplate(String name, String html) throws OserionDatabaseException {
        Query q = new Query(Criteria.where("name").is(name));
        MongoTemplate t = operations.findOne(q, MongoTemplate.class);

        if (t != null)
            throw new OserionDatabaseException(
                    String.format("The template %s already exists.", name));

        t = new MongoTemplate(templificator.createTemplateFromHTML(name, html));

        operations.insert(t);
    }

    @Override
    public List<ITemplate> selectTemplates() throws OserionDatabaseException {
        try {
            return (List<ITemplate>) (List<?>) operations.findAll(MongoTemplate.class);
        } catch (Exception e) {
            throw new OserionDatabaseException("An error happen while retrieving templates");
        }
    }

    @Override
    public void insertPageUrl(String templateName, String newUrl) throws OserionDatabaseException {
        Query q = new Query(Criteria.where("listPage").elemMatch(Criteria.where("url").is(newUrl)));
        MongoTemplate t = operations.findOne(q, MongoTemplate.class);
        if (t != null)
            throw new OserionDatabaseException(
                    String.format("The url %s already exists.", newUrl));

        q = new Query(Criteria.where("name").is(templateName));
        t = operations.findOne(q, MongoTemplate.class);
        if (t == null)
            throw new OserionDatabaseException(
                    String.format("The template %s does not exists.", templateName));

        List<PageReference> l = t.getListPage() == null ? new ArrayList<>() : t.getListPage();
        l.add(new PageReference(newUrl, getNextSequence(SEQUENCE_NAME_PAGE_KEY)));
        t.setListPage(l);
        operations.save(t);
    }

    @Override
    public void updateTemplate(ITemplate t, String html) throws OserionDatabaseException {

        if (t == null)
            throw new OserionDatabaseException(
                    String.format("The template is null"));
        if (!(t instanceof MongoTemplate))
            throw new OserionDatabaseException(
                    String.format("The template %s is not valid.", t.getName()));

        ITemplate template = templificator.createTemplateFromHTML(t.getName(), html);
        t.setHtml(html);
        t.setListTemplateElement(template.getListTemplateElement());
        t.setListVariableElement(template.getListVariableElement());

        operations.save(t);
    }

    @Override
    public ITemplate selectTemplateByUrl(String url) throws OserionDatabaseException {
        Query q = new Query(Criteria.where("listPage").elemMatch(Criteria.where("url").is(url)));
        MongoTemplate t = operations.findOne(q, MongoTemplate.class);
        if (t == null)
            throw new OserionDatabaseNotFoundException(
                    String.format("There is no template matching the following url : %s", url));

        return t;
    }

    @Override
    public ITemplate selectTemplateByName(String name) throws OserionDatabaseException {
        Query q = new Query(Criteria.where("name").is(name));
        MongoTemplate t = operations.findOne(q, MongoTemplate.class);
        if (t == null)
            throw new OserionDatabaseException(
                    String.format("The template %s does not exists.", name));

        return t;
    }

    @Override
    public boolean insertOrUpdateManyContent(List<ContentElement> listElement) {
        return false;
    }

    @Override
    public boolean insertOrUpdateContent(ContentElement ele) {
        return false;
    }

    @Override
    public boolean deletePageURL(String URL) {
        return false;
    }

    @Override
    public boolean deleteContent(String contentId, String contentType) {
        return false;
    }

    @Override
    public boolean deleteTemplate(String templateName) {
        return false;
    }

    @Override
    public String selectHTMLTemplate(String templateName) {
        return null;
    }

    @Override
    public IPage selectFullPage(String Url) {
        return null;
    }

    @Override
    public ContentElement selectContent(String ref, String type) {
        Query q = new Query(new Criteria().andOperator(
                Criteria.where("ref").is(ref),
                Criteria.where("type").is(type)
            )
        );
        /*q.fields().include("ref");
        q.fields().include("type");
        q.fields().include("value");*/
        ContentElement c = operations.findOne(q, ContentElement.class, "contentElements");
        return c;
    }

    @Override
    public void displayContentBase() {

    }

    public ITemplificator getTemplificator() {
        return templificator;
    }

    public void setTemplificator(ITemplificator templificator) {
        this.templificator = templificator;
    }

    public IDBConnection getConnection() {
        return connection;
    }

    public void setConnection(IDBConnection connection) throws OserionDatabaseException {
        this.connection = connection;
        if (this.connection instanceof MongoDBConnection)
            this.operations = ((MongoDBConnection) this.connection).getOperations();
        else
            throw new OserionDatabaseException("Invalid Connection to MongoDB");
    }

    private int getNextSequence(String sequenceName) throws OserionDatabaseException {
        try {
            BasicDBObject obj = new BasicDBObject();
            obj.append("$eval", String.format("getNextSequence('%s')", sequenceName));

            CommandResult t = operations.executeCommand(obj);
            Object obj1 = t.get("retval");
            return ((Double) obj1).intValue();
        } catch (Exception e) {
            throw new OserionDatabaseException(
                    String.format("Impossible to get the next '%s' sequence", sequenceName));
        }
    }

    @Override
    public void upsertContentElementValue(ContentElement c) throws OserionDatabaseException {
        upsertContentElementValue(c.getRef(), c.getType(), c.getValue());
    }

    @Override
    public void upsertContentElementValue(String ref, String type, String value) throws OserionDatabaseException {
        try {
            Query q = new Query(new Criteria().andOperator(
                    Criteria.where("ref").is(ref),
                    Criteria.where("type").is(type)
                )
            );
            Update u = new Update();
            u.set("value",value);

            operations.upsert(q, u, COLLECTION_CONTENT_ELEMENT);
        } catch (Exception e) {
            throw new OserionDatabaseException(
                    String.format("Impossible to fill element '%s : %s'", ref, type));
        }
    }


    @Override
    public List<ContentElement> fillContentElements(List<ContentElement> elements) throws OserionDatabaseException {
        if(elements != null && elements.size() > 0) {
            try {
                Criteria orConditions[] = new Criteria[elements.size()];
                for (int i = 0; i < elements.size(); i++) {
                    ContentElement e = elements.get(i);
                    orConditions[i] =
                            new Criteria().andOperator(
                                    Criteria.where("ref").is(e.getRef()),
                                    Criteria.where("type").is(e.getType())
                            );
                }
                Query q = new Query(new Criteria().orOperator(orConditions));
                List<ContentElement> l = operations.find(q, ContentElement.class, COLLECTION_CONTENT_ELEMENT);
                return l;
            } catch (Exception e) {
                e.printStackTrace();
                throw new OserionDatabaseException("Impossible to fill content elements");
            }
        }
        return elements;
    }

}

