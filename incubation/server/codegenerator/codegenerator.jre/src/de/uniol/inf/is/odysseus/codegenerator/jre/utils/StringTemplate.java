package de.uniol.inf.is.odysseus.codegenerator.jre.utils;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.CustomizedStringRenderer;

/**
 * This class wrapp the StringTemplate engine for better and 
 * easyer access. This class is used from the operator rules.
 * 
 * @author MarcPreuschaft
 *
 */
public class StringTemplate {
	//define the stringTempalte group
	private STGroup group;
	
	//string template instanz
	private ST st;
	
	public StringTemplate(String templateFile){
		 group = new STGroupDir("templates",'$','$');
		 setSt(group.getInstanceOf(templateFile));
	}
	
	public StringTemplate(String folder, String templateFile){
		 group = new STGroupDir("templates/"+folder,'$','$');
		 setSt(group.getInstanceOf(templateFile));
		 group.registerRenderer(String.class, new CustomizedStringRenderer());
	}
	

	/**
	 * return the StringTemplate instanz
	 * @return
	 */
	public ST getSt() {
		return st;
	}
	
	/**
	 * set the StringTemplate instanz
	 * @param st
	 */
	public void setSt(ST st) {
		this.st = st;
	}
}
