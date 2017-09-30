/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.iql.qdl.ui.labeling;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.iql.basic.ui.labeling.AbstractIQLLabelProvider;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

/**
 * Provides labels for EObjects.
 * 
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#label-provider
 */
@SuppressWarnings("all")
public class QDLLabelProvider extends AbstractIQLLabelProvider {
  @Inject
  public QDLLabelProvider(final AdapterFactoryLabelProvider delegate) {
    super(delegate);
  }
  
  String image(final QDLQuery ele) {
    return "graph.png";
  }
}
