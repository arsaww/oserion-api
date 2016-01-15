package com.oserion.framework.api.util;

public class CodeReturn {

	
	public static String error21(String templateName ) {
		return "template " + templateName + " already exists. ";
	}
	
	public static String
			error22 = new String ("no template with this name");
	public static String error23 = new String ("pageReference already in Collection, "
			+ "you must destroy it if you want to link it to new template ");
	public static String error24 = new String ("this ref is not a variable ref.");
	
	
	public static String error25 (String templateName , String ref) {
		return "for the template : " + templateName + ", there is no listVariableElement, nor "
				+ "listTemplateElement with ref : " + ref;
	}


	public static String error26(String ref, String type) {
		return "pas de ContentElement qui ont ref : " + ref + " et type : " + type;
	}

	
}

