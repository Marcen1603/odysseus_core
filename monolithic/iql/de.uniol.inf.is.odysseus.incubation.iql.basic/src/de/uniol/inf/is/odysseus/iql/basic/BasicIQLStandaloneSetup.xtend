/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.iql.basic


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class BasicIQLStandaloneSetup extends BasicIQLStandaloneSetupGenerated {

	def static void doSetup() {
		new BasicIQLStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
