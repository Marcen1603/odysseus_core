/**
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.eca.ui.labeling;

import com.google.inject.Inject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

/**
 * Provides labels for EObjects.
 * 
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#label-provider
 */
@SuppressWarnings("all")
public class ECALabelProvider extends DefaultEObjectLabelProvider {
  @Inject
  public ECALabelProvider(final AdapterFactoryLabelProvider delegate) {
    super(delegate);
  }
}
