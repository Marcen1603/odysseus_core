/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.eca;

import de.uniol.inf.is.odysseus.eca.ECAStandaloneSetupGenerated;

/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
@SuppressWarnings("all")
public class ECAStandaloneSetup extends ECAStandaloneSetupGenerated {
  public static void doSetup() {
    ECAStandaloneSetup _eCAStandaloneSetup = new ECAStandaloneSetup();
    _eCAStandaloneSetup.createInjectorAndDoEMFRegistration();
  }
}
