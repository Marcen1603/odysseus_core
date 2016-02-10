package de.uniol.inf.is.odysseus.rcp.editor.script.rules;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.script.IOdysseusScriptTransformRule;
import de.uniol.inf.is.odysseus.rcp.editor.script.IOdysseusScriptTransformRuleProvider;

public class DefaultTransformRuleProvider implements IOdysseusScriptTransformRuleProvider {

	@Override
	public Collection<IOdysseusScriptTransformRule> getRules() {
		List<IOdysseusScriptTransformRule> rules = Lists.newArrayList();

		rules.add(new DefineKeywordTransformRule());

		return rules;
	}

}
