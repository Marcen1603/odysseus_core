/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.eca.formatting2

import de.uniol.inf.is.odysseus.eca.eCA.Constant
import de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent
import de.uniol.inf.is.odysseus.eca.eCA.Model
import de.uniol.inf.is.odysseus.eca.eCA.Rule
import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument

class ECAFormatter extends AbstractFormatter2 {
	
//	@Inject extension ECAGrammarAccess

	def dispatch void format(Model model, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Constant constant : model.getConstants()) {
			constant.format;
		}
		for (DefinedEvent definedEvent : model.getDefEvents()) {
			definedEvent.format;
		}
		model.getWindowSize.format;
		model.getTimeIntervall.format;
		for (Rule rule : model.getRules()) {
			rule.format;
		}
	}

	def dispatch void format(DefinedEvent definedEvent, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		definedEvent.getDefinedSource.format;
		definedEvent.getDefinedValue.format;
	}
	
	// TODO: implement for Rule, CONDITIONS, Expression, SUBCONDITION, RuleSource, SOURCECONDITION, QUERYCONDITION, SYSTEMCONDITION, ACTIONS, COMMANDACTION
}
