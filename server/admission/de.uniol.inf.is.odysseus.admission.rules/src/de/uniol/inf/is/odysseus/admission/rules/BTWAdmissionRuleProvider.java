package de.uniol.inf.is.odysseus.admission.rules;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionRuleProvider;

public class BTWAdmissionRuleProvider implements IAdmissionRuleProvider {

	@Override
	public Collection<IAdmissionRule<?>> getRules() {
		Collection<IAdmissionRule<?>> rules = Lists.newArrayList();
		
		// 1. Regel: Wenn die Last über 70% steigt, dann eine zufällig gewählte aktive Anfrage pausieren
		//rules.add( new PauseRandomQueryWhenCpuOver70AdmissionRule());
		
		// 2. Regel: Wenn eine Anfrage gestartet wird und die Last bereits über 70% ist, dann wird die Anfrage sofort pausiert
		//rules.add( new PauseStartingQueryWhenCpuOver70AdmissionRule());
		
		// 3. Regel: Alle 10 Sekunden wird eine pausierte Anfrage wieder gestartet
		//rules.add( new ActivateLongestPausedQueryAdmissionRule());
		
		return rules;
	}

}
