package de.uniol.inf.is.odysseus.query.codegenerator.jre.utils;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import de.uniol.inf.is.odysseus.query.codegenerator.jre.model.EscapeStringRenderer;

public class StringTemplate {
	
	private STGroup group;
	private ST st;
	
	public StringTemplate(String folder, String templateFile){
		 group = new STGroupDir("templates/"+folder,'$','$');
		 setSt(group.getInstanceOf(templateFile));
		 group.registerRenderer(String.class, new EscapeStringRenderer());
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