/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.server;

import de.uniol.inf.is.odysseus.server.AbstractStreamingsparqlRuntimeModule;
import de.uniol.inf.is.odysseus.server.generator.StreamingsparqlGenerator;
import org.eclipse.xtext.generator.IGenerator2;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
@SuppressWarnings("all")
public class StreamingsparqlRuntimeModule extends AbstractStreamingsparqlRuntimeModule {
  public Class<? extends IGenerator2> bindGenerator() {
    return StreamingsparqlGenerator.class;
  }
}
