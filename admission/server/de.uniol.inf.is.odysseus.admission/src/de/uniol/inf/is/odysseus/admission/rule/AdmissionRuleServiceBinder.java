package de.uniol.inf.is.odysseus.admission.rule;

import java.util.Collection;

import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionRuleProvider;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;

public class AdmissionRuleServiceBinder {

	// called by OSGi-DS
	public static void bindAdmissionRule(IAdmissionRule<?> serv) {
		AdmissionPlugIn.getAdmissionRuleRegistry().addAdmissionRuleType(serv);
	}

	// called by OSGi-DS
	public static void unbindAdmissionRule(IAdmissionRule<?> serv) {
		AdmissionPlugIn.getAdmissionRuleRegistry().removeAdmissionRuleType(serv);
	}
	
	// called by OSGi-DS
	public static void bindAdmissionRuleProvider(IAdmissionRuleProvider serv) {
		Collection<IAdmissionRule<?>> rules = serv.getRules();
		if( rules == null || rules.isEmpty() ) {
			return;
		}
		
		for( IAdmissionRule<?> rule : rules ) {
			bindAdmissionRule(rule);
		}
	}

	// called by OSGi-DS
	public static void unbindAdmissionRuleProvider(IAdmissionRuleProvider serv) {
		Collection<IAdmissionRule<?>> rules = serv.getRules();
		if( rules == null || rules.isEmpty() ) {
			return;
		}
		
		for( IAdmissionRule<?> rule : rules ) {
			unbindAdmissionRule(rule);
		}
	}
}
