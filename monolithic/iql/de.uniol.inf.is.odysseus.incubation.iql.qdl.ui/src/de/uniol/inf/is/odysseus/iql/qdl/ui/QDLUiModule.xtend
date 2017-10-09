/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.iql.qdl.ui

import org.eclipse.xtext.documentation.IEObjectDocumentationProvider
import de.uniol.inf.is.odysseus.iql.qdl.ui.generator.QDLUiGenerator
import de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist.QDLTemplateProposalProvider
import de.uniol.inf.is.odysseus.iql.qdl.ui.service.QDLUiServiceObserver
import org.eclipse.xtext.ui.editor.IXtextEditorCallback
import de.uniol.inf.is.odysseus.iql.qdl.scoping.IQDLScopeProvider
import de.uniol.inf.is.odysseus.iql.qdl.ui.highlighting.QDLHighlightingConfiguration
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.ui.IQLXtextEditorCallback
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper
import de.uniol.inf.is.odysseus.iql.qdl.ui.scoping.QDLUiScopeProvider
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLUiCrossReferenceValidator
import de.uniol.inf.is.odysseus.iql.qdl.ui.highlighting.QDLSemanticHighlightingCalculator
import de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist.QDLProposalProvider
import de.uniol.inf.is.odysseus.iql.qdl.ui.hover.QDLEObjectHoverProvider
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLCrossReferenceValidator
import de.uniol.inf.is.odysseus.iql.qdl.ui.highlighting.QDLAntlrTokenToAttributeIdMapper
import org.eclipse.xtext.ui.editor.contentassist.ITemplateProposalProvider
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration
import de.uniol.inf.is.odysseus.iql.qdl.ui.typing.QDLUiTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.ui.service.IIQLUiServiceObserver
import org.eclipse.xtext.ui.wizard.IProjectCreator
import org.eclipse.ui.plugin.AbstractUIPlugin
import de.uniol.inf.is.odysseus.iql.basic.ui.hover.IQLDispatchingEObjectTextHover
import de.uniol.inf.is.odysseus.iql.basic.generator.IIQLGenerator
import de.uniol.inf.is.odysseus.iql.basic.ui.wizard.IQLProjectCreator
import de.uniol.inf.is.odysseus.iql.qdl.ui.service.IQDLUiServiceObserver
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProviderFactory
import de.uniol.inf.is.odysseus.iql.qdl.ui.hover.QDLEObjectDocumentationProvider
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLJdtTypeProviderFactory
import de.uniol.inf.is.odysseus.iql.basic.ui.executor.IIQLUiExecutor
import org.eclipse.xtext.ui.editor.hover.IEObjectHover
import de.uniol.inf.is.odysseus.iql.qdl.ui.executor.QDLUiExecutor
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLClasspathTypeProviderFactory
import org.eclipse.xtext.generator.IGenerator2
import org.eclipse.xtext.ide.editor.syntaxcoloring.ISemanticHighlightingCalculator
import org.eclipse.xtext.common.types.xtext.ClasspathBasedTypeScopeProvider

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */ 
@SuppressWarnings("restriction")class QDLUiModule extends de.uniol.inf.is.odysseus.iql.qdl.ui.AbstractQDLUiModule {
	
	 new(AbstractUIPlugin plugin) {
		super(plugin)
	}
	def Class<? extends IIQLGenerator> bindIQLGenerator() {
		return QDLUiGenerator 
	}
	def Class<? extends IIQLCrossReferenceValidator> bindCrossReferenceValidator() {
		return IQLUiCrossReferenceValidator 
	}
	def Class<? extends IQDLTypeUtils> bindQDLTypeUtils() {
		return QDLUiTypeUtils 
	}
	def Class<? extends IIQLTypeUtils> bindIQLTypeUtils() {
		return QDLUiTypeUtils 
	}
	def Class<? extends IIQLUiServiceObserver> bindIQLUiServiceObserver() {
		return QDLUiServiceObserver 
	}
	def Class<? extends IProjectCreator> bindIProjectCreator() {
		return IQLProjectCreator 
	}
	def Class<? extends IQDLUiServiceObserver> bindQDLUiServiceObserver() {
		return QDLUiServiceObserver 
	}
	def Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindAbstractAntlrTokenToAttributeIdMapper() {
		return QDLAntlrTokenToAttributeIdMapper 
	}
	def Class<? extends IIQLScopeProvider> bindIQLScopeProvider() {
		return QDLUiScopeProvider 
	}
	def Class<? extends IQDLScopeProvider> bindQDLScopeProvider() {
		return QDLUiScopeProvider 
	}
	def Class<? extends IGenerator2> bindGenerator() {
		return QDLUiGenerator 
	}
	def Class<? extends IIQLJdtTypeProviderFactory> bindJdtTypeProviderFactory() {
		return IQLJdtTypeProviderFactory 
	}
	def Class<? extends org.eclipse.xtext.scoping.IScopeProvider> bindIScopeProvider() {
		return QDLUiScopeProvider 
	}
	def Class<? extends IIQLUiExecutor> bindIQLUiExecutor() {
		return QDLUiExecutor 
	}
	override Class<? extends org.eclipse.xtext.common.types.access.IJvmTypeProvider.Factory> bindIJvmTypeProvider$Factory() {
		return QDLClasspathTypeProviderFactory 
	}
	override Class<? extends org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
		return ClasspathBasedTypeScopeProvider 
	}
	override Class<? extends org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider> bindIContentProposalProvider() {
		return QDLProposalProvider 
	}
	override Class<? extends IEObjectHover> bindIEObjectHover() {
		return IQLDispatchingEObjectTextHover 
	}
	def Class<? extends IEObjectHoverProvider> bindIEObjectHoverProvider() {
		return QDLEObjectHoverProvider 
	}
	def Class<? extends IEObjectDocumentationProvider> bindIEObjectDocumentationProviderr() {
		return QDLEObjectDocumentationProvider 
	}
	def Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration() {
		return QDLHighlightingConfiguration 
	}
	def Class<? extends ISemanticHighlightingCalculator> bindIHighlightingCalculator() {
		return QDLSemanticHighlightingCalculator 
	}
	override Class<? extends ITemplateProposalProvider> bindITemplateProposalProvider() {
		return QDLTemplateProposalProvider 
	}
	override Class<? extends IXtextEditorCallback> bindIXtextEditorCallback() {
		return IQLXtextEditorCallback 
	}
}
