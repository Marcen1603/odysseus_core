/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.eca.formatting2;

import de.uniol.inf.is.odysseus.eca.eCA.Constant;
import de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent;
import de.uniol.inf.is.odysseus.eca.eCA.EcaValue;
import de.uniol.inf.is.odysseus.eca.eCA.Model;
import de.uniol.inf.is.odysseus.eca.eCA.Rule;
import de.uniol.inf.is.odysseus.eca.eCA.Source;
import de.uniol.inf.is.odysseus.eca.eCA.Timer;
import de.uniol.inf.is.odysseus.eca.eCA.Window;
import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.formatting2.AbstractFormatter2;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class ECAFormatter extends AbstractFormatter2 {
  protected void _format(final Model model, @Extension final IFormattableDocument document) {
    EList<Constant> _constants = model.getConstants();
    for (final Constant constant : _constants) {
      document.<Constant>format(constant);
    }
    EList<DefinedEvent> _defEvents = model.getDefEvents();
    for (final DefinedEvent definedEvent : _defEvents) {
      document.<DefinedEvent>format(definedEvent);
    }
    document.<Window>format(model.getWindowSize());
    document.<Timer>format(model.getTimeIntervall());
    EList<Rule> _rules = model.getRules();
    for (final Rule rule : _rules) {
      document.<Rule>format(rule);
    }
  }
  
  protected void _format(final DefinedEvent definedEvent, @Extension final IFormattableDocument document) {
    document.<Source>format(definedEvent.getDefinedSource());
    document.<EcaValue>format(definedEvent.getDefinedValue());
  }
  
  public void format(final Object definedEvent, final IFormattableDocument document) {
    if (definedEvent instanceof XtextResource) {
      _format((XtextResource)definedEvent, document);
      return;
    } else if (definedEvent instanceof DefinedEvent) {
      _format((DefinedEvent)definedEvent, document);
      return;
    } else if (definedEvent instanceof Model) {
      _format((Model)definedEvent, document);
      return;
    } else if (definedEvent instanceof EObject) {
      _format((EObject)definedEvent, document);
      return;
    } else if (definedEvent == null) {
      _format((Void)null, document);
      return;
    } else if (definedEvent != null) {
      _format(definedEvent, document);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(definedEvent, document).toString());
    }
  }
}
