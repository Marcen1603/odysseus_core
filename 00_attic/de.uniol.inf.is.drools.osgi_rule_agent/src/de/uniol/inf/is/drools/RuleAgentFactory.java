package de.uniol.inf.is.drools;

import java.util.Properties;

import org.drools.RuleBaseConfiguration;
import org.drools.agent.RuleAgent;
import org.osgi.framework.BundleContext;

public class RuleAgentFactory {
	@SuppressWarnings("unchecked")
	public static RuleAgent createRuleAgent(BundleContext bundleContext,
			String rulePath, String log) {
		Class<?> cla;
		try {
			cla = Class.forName("org.drools.agent.OSGiPathScanner");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		RuleAgent.PACKAGE_PROVIDERS.put("OSGI", cla);
		OSGiDroolsClassLoader classLoader = new OSGiDroolsClassLoader(
				bundleContext, Thread.currentThread().getContextClassLoader(),
				bundleContext.getBundle());

		Properties config = new Properties();
		config.setProperty("OSGI", rulePath);
		config.put("osgi.class_loader", classLoader);
		config.put("osgi.bundle_context", bundleContext);
		config.setProperty("osgi.log", log);

		RuleBaseConfiguration ruleBaseConfig = new RuleBaseConfiguration(
				classLoader);
		return RuleAgent.newRuleAgent(config, ruleBaseConfig);
	}
}
