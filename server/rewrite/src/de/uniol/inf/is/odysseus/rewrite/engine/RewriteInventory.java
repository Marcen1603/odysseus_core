/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rewrite.engine;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.AbstractInventory;

public class RewriteInventory extends AbstractInventory {
	
	private static RewriteInventory instance = null;	
	private final Collection<String> ruleNames = new HashSet<>();
	
	public RewriteInventory() {
		super();
	}
	
	public RewriteInventory(RewriteInventory currentinstance) {
		super(currentinstance);
	}

	public RewriteInventory(RewriteInventory currentinstance, Set<String> rulesToApply) {
		super(currentinstance, rulesToApply);
	}

	
	public static synchronized RewriteInventory getInstance() {
		if (instance == null) {
			instance = new RewriteInventory();
		}
		return instance;
	}
	
	
	@Override
	public void bindRuleProvider(IRuleProvider provider) {
		super.bindRuleProvider(provider);
		List<IRule<?, ?>> rules = provider.getRules();
		for (IRule<?,?> r: rules){
			ruleNames.add(r.getName());
		}
	}
	
	@Override
	public void unbindRuleProvider(IRuleProvider provider) {
		super.unbindRuleProvider(provider);
		List<IRule<?, ?>> rules = provider.getRules();
		for (IRule<?,?> r: rules){
			ruleNames.remove(r.getName());
		}
	}
	
	@Override
	public AbstractInventory getCurrentInstance(){
		return getInstance();
	}
	
	@Override
	public String getInventoryName(){
		return "Rewrite";
	}

	public Collection<String> getRules() {
		return ruleNames;
	}		
}
