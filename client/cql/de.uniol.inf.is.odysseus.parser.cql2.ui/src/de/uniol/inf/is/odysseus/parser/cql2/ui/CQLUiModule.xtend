/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.ui

import de.uniol.inf.is.odysseus.parser.cql2.ui.internal.Cql2Activator
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.ui.plugin.AbstractUIPlugin

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
class CQLUiModule extends AbstractCQLUiModule {

	new(AbstractUIPlugin plugin) {
		super(plugin)
	}
	
	def Class<? extends XtextEditor> bindXtextEditor() {
		return CQLEditor;
	}
	
	def Class<? extends Cql2Activator> bindCqlActivator() {
		return Activator
	}

}
