package de.uniol.inf.is.odysseus.codegenerator.modell;

import java.util.HashSet;
import java.util.Set;

public class CodeFragmentInfo {
	
	private String code ="";
	private Set<String> frameworkImports =  new HashSet<String>();
	private Set<String> imports =  new HashSet<String>();
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Set<String> getFrameworkImports() {
		return frameworkImports;
	}
	
	public Set<String> getImports() {
		return imports;
	}
	
	public void setImports(Set<String> imports) {
		this.frameworkImports = imports;
	}
	
	public void addCode(String newCode){
		this.code += newCode;
	}
	
	public void addFrameworkImports(Set<String> newImports){
			this.frameworkImports.addAll(newImports);
	}
	
	public void addFrameworkImport(String newImports){
			this.frameworkImports.add(newImports);
	}
	
	
	public void addImports(Set<String> imports){
		this.imports.addAll(imports);
	}

	public void addImport(String imports){
		this.imports.add(imports);
	}
	
	
	
	public void addCodeFragmentInfo(CodeFragmentInfo newFragment){
		addCode(newFragment.getCode());
		addFrameworkImports(newFragment.getFrameworkImports());	
		addImports(newFragment.getImports());
	}
	

}
