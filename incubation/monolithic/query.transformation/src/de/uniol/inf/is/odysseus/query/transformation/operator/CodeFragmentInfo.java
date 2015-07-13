package de.uniol.inf.is.odysseus.query.transformation.operator;

import java.util.HashSet;
import java.util.Set;

public class CodeFragmentInfo {
	
	private String code ="";
	private Set<String> imports =  new HashSet<String>();
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Set<String> getImports() {
		return imports;
	}
	public void setImports(Set<String> imports) {
		this.imports = imports;
	}
	
	public void addCode(String newCode){
		this.code += newCode;
	}
	
	public void addImports(Set<String> newImports){
			this.imports.addAll(newImports);
	}
	
	public void addImport(String newImports){
			this.imports.add(newImports);
	}
	
	
	public void addCodeFragmentInfo(CodeFragmentInfo newFragment){
		addCode(newFragment.getCode());
		addImports(newFragment.getImports());	
	}
	
	

}
