/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.ui

import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.generator.IGenerator2
import org.eclipse.xtext.ui.editor.XtextEditor

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class CQLUiModule extends AbstractCQLUiModule 
{
	
	def Class<? extends XtextEditor> bindXtextEditor() 
	{
		return CQLEditor;
	}
	
	def Class<? extends IGenerator2> bindGenerator() 
	{
		return CQLUiGenerator;
	}
	
}
