package com.oserion.framework.api.util;

import java.util.ArrayList;
import java.util.List;

import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.PageReference;

public class Supervision {
	
	public MongoOperations mongoOperation;

	
	public String listPageFromTemplateName(String templateName) {
		List<PageReference> mylistPageReference = new ArrayList<PageReference>();
		Query q1 = new Query(Criteria.where("name").is(templateName));
		MongoTemplate t1 = (MongoTemplate) mongoOperation.findOne(q1, MongoTemplate.class);
		if(t1 == null) 
			return CodeReturn.error22;

		Query q2 = new Query(Criteria.where("template").is(t1));
		mylistPageReference = (List<PageReference>) mongoOperation.find(q2, PageReference.class);
		
		// affichage : 
		System.out.println("List des PageReferences : ");
		System.out.println("----------------------------------");
		for(PageReference p : mylistPageReference) {
			System.out.println( "url : " + p.getUrl());
			System.out.println( "key : " + p.getKey());
			System.out.println("----------------------------------");
		}
		return null;
	}
	
	
	public String listContentElementFromTemplateName(String templateName) {
		Query q1 = new Query(Criteria.where("name").is(templateName));
		MongoTemplate t1 = (MongoTemplate) mongoOperation.findOne(q1, MongoTemplate.class);
		if(t1 == null) {
			return CodeReturn.error22;
		}

		System.out.println("Pour le template " + templateName + " : ");
		System.out.println("ListTemplateElement : ");
		System.out.println(" ******************* ");
		for(ContentElement ct1 : t1.getListTemplateElement()) {
			System.out.println(" ref : " + ct1.getRef() );
			System.out.println(" type : " + ct1.getType() );
			System.out.println(" value : " + ct1.getValue() );
			System.out.println(" -------------- ");
		}
		System.out.println("ListVariableElement : ");
		System.out.println(" ******************* ");
		for(ContentElement ct1 : t1.getListVariableElement()) {
			System.out.println(" ref : " + ct1.getRef() );
			System.out.println(" type : " + ct1.getType() );
			System.out.println(" value : " + ct1.getValue() );
			System.out.println(" -------------- ");
		}
		
		return null;
	}
	

}
