/*
* generated by Xtext
*/
package de.uniol.inf.is.odysseus.iql.basic;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class BasicIQLStandaloneSetup extends BasicIQLStandaloneSetupGenerated{

	public static void doSetup() {
		new BasicIQLStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}
