package de.uniol.inf.is.odysseus.rcp.editor.script.rules;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.script.IOdysseusScriptTransformRule;
import de.uniol.inf.is.odysseus.rcp.editor.script.IOdysseusScriptTransformRuleProvider;

public class TransformRuleRegistry {

	private static TransformRuleRegistry instance;
	
	private final Collection<IOdysseusScriptTransformRule> rules = Lists.newArrayList();
	
	// called by OSGi-DS
	public void activate() {
		instance = this;
	}
	
	// called by OSGi-DS
	public void deactivate() {
		instance = null;
	}
	
	public static TransformRuleRegistry getInstance() {
		return instance;
	}
	
	// called by OSGi-DS
	public void bindOdysseusScriptTransformRule(IOdysseusScriptTransformRule serv) {
		synchronized( rules ) {
			rules.add(serv);
		}
	}

	// called by OSGi-DS
	public void unbindOdysseusScriptTransformRule(IOdysseusScriptTransformRule serv) {
		synchronized(rules) {
			rules.remove(serv);
		}
	}
	
	// called by OSGi-DS
	public void bindOdysseusScriptTransformRuleProvider(IOdysseusScriptTransformRuleProvider serv) {
		for( IOdysseusScriptTransformRule rule : serv.getRules() ) {
			bindOdysseusScriptTransformRule(rule);
		}
	}

	// called by OSGi-DS
	public void unbindOdysseusScriptTransformRuleProvider(IOdysseusScriptTransformRuleProvider serv) {
		for( IOdysseusScriptTransformRule rule : serv.getRules() ) {
			unbindOdysseusScriptTransformRule(rule);
		}
	}
	
	public Collection<IOdysseusScriptTransformRule> getRules() {
		synchronized( rules ) {
			return rules;
		}
	}
}
