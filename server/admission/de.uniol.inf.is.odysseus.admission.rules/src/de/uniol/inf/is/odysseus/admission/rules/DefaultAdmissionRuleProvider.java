package de.uniol.inf.is.odysseus.admission.rules;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionRuleProvider;

public class DefaultAdmissionRuleProvider implements IAdmissionRuleProvider {

	@Override
	public Collection<IAdmissionRule<?>> getRules() {
		Collection<IAdmissionRule<?>> rules = Lists.newArrayList();
		
//		rules.add(new CPU60TimingPartialQueryAdmissionRule());
//		rules.add(new CPU60PartialQueryAdmissionRule());
		
//		rules.add(new CPUMax70QueryAdmissionRule());
//		rules.add(new CPUMax70TimingAdmissionRule());
		
		return rules;
	}

}
