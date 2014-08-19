package de.uniol.inf.is.odysseus.admission.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.admission.rule.AdmissionRuleRegistry;


public class AdmissionPlugIn implements BundleActivator {

	private static final AdmissionRuleRegistry RULE_REGISTRY = new AdmissionRuleRegistry();
	
	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static AdmissionRuleRegistry getAdmissionRuleRegistry() {
		return RULE_REGISTRY;
	}
}
