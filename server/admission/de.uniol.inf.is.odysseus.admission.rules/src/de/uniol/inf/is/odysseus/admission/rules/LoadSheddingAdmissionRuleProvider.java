package de.uniol.inf.is.odysseus.admission.rules;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionRuleProvider;
import loadShedding.AddQueryToStatusComponentsRule;
import loadShedding.MeasureStatusRule;
import loadShedding.RemoveQueryFromStatusComponentsRule;
import loadShedding.RunLoadSheddingRule;
import loadShedding.RollBackLoadSheddingRule;

public class LoadSheddingAdmissionRuleProvider implements IAdmissionRuleProvider {

	@Override
	public Collection<IAdmissionRule<?>> getRules() {
		Collection<IAdmissionRule<?>> rules = Lists.newArrayList();
		
		rules.add(new AddQueryToStatusComponentsRule());
		
		rules.add(new RemoveQueryFromStatusComponentsRule());
		
		rules.add(new RunLoadSheddingRule());
		
		rules.add(new RollBackLoadSheddingRule());
		
		rules.add(new MeasureStatusRule());
		
		return rules;
	}

}
