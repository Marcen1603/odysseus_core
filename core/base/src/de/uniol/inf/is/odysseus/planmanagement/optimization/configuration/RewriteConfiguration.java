package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import java.util.HashSet;
import java.util.Set;

public class RewriteConfiguration implements IOptimizationSetting<Set<String>>{

	private Set<String> rulesToApply = new HashSet<String>();

	public RewriteConfiguration(){
		
	}
	
	public RewriteConfiguration(Set<String> rulesToApply) {
		this.rulesToApply = rulesToApply;
	}

	public Set<String> getRulesToApply() {
		return this.rulesToApply;
	}

	@Override
	public Set<String> getValue() {		
		return getRulesToApply();
	}
}
