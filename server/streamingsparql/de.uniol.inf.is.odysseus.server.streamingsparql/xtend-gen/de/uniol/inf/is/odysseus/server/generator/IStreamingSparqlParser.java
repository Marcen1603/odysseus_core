package de.uniol.inf.is.odysseus.server.generator;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("all")
public interface IStreamingSparqlParser {
  public abstract String parse(final Resource resource);
  
  public abstract String parse(final String query, final ISession user, final IDataDictionary dd, final Context context, final IMetaAttribute metaAttribute, final IServerExecutor executor);
}
