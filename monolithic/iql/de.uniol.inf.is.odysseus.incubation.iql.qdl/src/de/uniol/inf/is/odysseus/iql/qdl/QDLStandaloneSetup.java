/*
* generated by Xtext
*/
package de.uniol.inf.is.odysseus.iql.qdl;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class QDLStandaloneSetup extends QDLStandaloneSetupGenerated{

	public static void doSetup() {
		new QDLStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

