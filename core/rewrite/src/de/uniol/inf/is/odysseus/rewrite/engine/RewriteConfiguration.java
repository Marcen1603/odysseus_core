package de.uniol.inf.is.odysseus.rewrite.engine;

import java.util.HashSet;
import java.util.Set;

public class RewriteConfiguration {
	
	private Set<String> rulesToApply = new HashSet<String>();
	
	public RewriteConfiguration(Set<String> rulesToApply){
		this.rulesToApply = rulesToApply;
	}
	
	public Set<String> getRulesToApply(){
		return this.rulesToApply;
	}
	
	
	
}
