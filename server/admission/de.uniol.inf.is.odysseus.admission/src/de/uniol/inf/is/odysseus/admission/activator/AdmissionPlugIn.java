package de.uniol.inf.is.odysseus.admission.activator;

import de.uniol.inf.is.odysseus.admission.action.AdmissionActionComponentRegistry;
import de.uniol.inf.is.odysseus.admission.rule.AdmissionRuleRegistry;
import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusComponentRegistry;


public class AdmissionPlugIn{

	private static final AdmissionRuleRegistry RULE_REGISTRY = new AdmissionRuleRegistry();
	private static final AdmissionStatusComponentRegistry STATUS_COMPONENT_REGISTRY = new AdmissionStatusComponentRegistry();
	private static final AdmissionActionComponentRegistry ACTION_COMPONENT_REGISTRY = new AdmissionActionComponentRegistry();
	
	public static AdmissionRuleRegistry getAdmissionRuleRegistry() {
		return RULE_REGISTRY;
	}
	
	public static AdmissionStatusComponentRegistry getAdmissionStatusComponentRegistry() {
		return STATUS_COMPONENT_REGISTRY;
	}
	
	public static AdmissionActionComponentRegistry getAdmissionActionComponentRegistry() {
		return ACTION_COMPONENT_REGISTRY;
	}
}
