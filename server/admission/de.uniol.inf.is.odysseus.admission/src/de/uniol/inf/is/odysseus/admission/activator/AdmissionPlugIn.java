package de.uniol.inf.is.odysseus.admission.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.admission.rule.AdmissionRuleRegistry;
import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusComponentRegistry;


public class AdmissionPlugIn implements BundleActivator {

	private static final AdmissionRuleRegistry RULE_REGISTRY = new AdmissionRuleRegistry();
	private static final AdmissionStatusComponentRegistry STATUS_COMPONENT_REGISTRY = new AdmissionStatusComponentRegistry();
	
	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static AdmissionRuleRegistry getAdmissionRuleRegistry() {
		return RULE_REGISTRY;
	}
	
	public static AdmissionStatusComponentRegistry getAdmissionStatusComponentRegistry() {
		return STATUS_COMPONENT_REGISTRY;
	}
}
