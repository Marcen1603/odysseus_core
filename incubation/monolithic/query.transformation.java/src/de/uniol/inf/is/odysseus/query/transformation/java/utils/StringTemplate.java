package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

public class StringTemplate {
	
	
	private STGroup group;
	private ST st;
	
	public StringTemplate(String folder, String templateFile){
		 group = new STGroupDir(folder,'$','$');
		 setSt(group.getInstanceOf(templateFile));
	}
	
	
	public StringTemplate(String templateFile){
		 group = new STGroupDir("templates",'$','$');
		 setSt(group.getInstanceOf(templateFile));
	}


	public ST getSt() {
		return st;
	}


	public void setSt(ST st) {
		this.st = st;
	}
	
	
}
