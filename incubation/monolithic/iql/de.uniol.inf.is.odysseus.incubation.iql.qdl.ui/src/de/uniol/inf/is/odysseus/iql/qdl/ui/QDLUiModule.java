/*
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.qdl.ui;


import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.common.types.xtext.ClasspathBasedTypeScopeProvider;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.ui.editor.IXtextEditorCallback;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateProposalProvider;
import org.eclipse.xtext.ui.editor.hover.IEObjectHover;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.ui.wizard.IProjectCreator;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.basic.ui.IQLXtextEditorCallback;
import de.uniol.inf.is.odysseus.iql.basic.ui.executor.IIQLUiExecutor;
import de.uniol.inf.is.odysseus.iql.basic.ui.hover.IQLDispatchingEObjectTextHover;
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.ui.service.IIQLUiServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.ui.wizard.IQLProjectCreator;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.IQDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist.QDLProposalProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist.QDLTemplateProposalProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.executor.QDLUiExecutor;
import de.uniol.inf.is.odysseus.iql.qdl.ui.generator.QDLUiGenerator;
import de.uniol.inf.is.odysseus.iql.qdl.ui.highlighting.QDLAntlrTokenToAttributeIdMapper;
import de.uniol.inf.is.odysseus.iql.qdl.ui.highlighting.QDLHighlightingConfiguration;
import de.uniol.inf.is.odysseus.iql.qdl.ui.highlighting.QDLSemanticHighlightingCalculator;
import de.uniol.inf.is.odysseus.iql.qdl.ui.hover.QDLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.hover.QDLEObjectHoverProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.scoping.QDLUiScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.service.IQDLUiServiceObserver;
import de.uniol.inf.is.odysseus.iql.qdl.ui.service.QDLUiServiceObserver;
import de.uniol.inf.is.odysseus.iql.qdl.ui.typing.QDLUiTypeUtils;

/**
 * Use this class to register components to be used within the IDE.
 */
@SuppressWarnings("restriction")
public class QDLUiModule extends de.uniol.inf.is.odysseus.iql.qdl.ui.AbstractQDLUiModule {
	public QDLUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}
	
	public Class<? extends IQDLTypeUtils> bindQDLTypeUtils() {
		return QDLUiTypeUtils.class;
	}
	
	public Class<? extends IIQLTypeUtils> bindIQLTypeUtils() {
		return QDLUiTypeUtils.class;
	}

	
	public Class<? extends IIQLUiServiceObserver> bindIQLUiServiceObserver() {
		return QDLUiServiceObserver.class;
	}
	
	public Class<? extends IProjectCreator> bindIProjectCreator() {
		return IQLProjectCreator.class;
	}
	
	public Class<? extends IQDLUiServiceObserver> bindQDLUiServiceObserver() {
		return QDLUiServiceObserver.class;
	}
	
	public Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindAbstractAntlrTokenToAttributeIdMapper() {
		return QDLAntlrTokenToAttributeIdMapper.class;
	}
	
	
	public Class<? extends IIQLScopeProvider> bindIQLScopeProvider() {
		return QDLUiScopeProvider.class;
	}
	public Class<? extends IQDLScopeProvider> bindQDLScopeProvider() {
		return QDLUiScopeProvider.class;
	}
	
	
	
	public Class<? extends IGenerator> bindGenerator() {
		return QDLUiGenerator.class;
	}

	
	public Class<? extends IIQLJdtTypeProviderFactory> bindJdtTypeProviderFactory() {
		return IQLJdtTypeProviderFactory.class;
	}
	

	
	public Class<? extends org.eclipse.xtext.scoping.IScopeProvider> bindIScopeProvider() {
		return QDLUiScopeProvider.class;
	}
	
	public Class<? extends IIQLUiExecutor> bindIQLUiExecutor() {
		return QDLUiExecutor.class;
	}
	
	
	@Override
	public Class<? extends org.eclipse.xtext.common.types.access.IJvmTypeProvider.Factory> bindIJvmTypeProvider$Factory() {
		return QDLClasspathTypeProviderFactory.class;
	}
	
	@Override
	public Class<? extends org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
		return ClasspathBasedTypeScopeProvider.class;
	}
	
	@Override
	public Class<? extends org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider> bindIContentProposalProvider() {
		return QDLProposalProvider.class;
	}

	
	public Class<? extends IEObjectHover> bindIEObjectHover() {
		return IQLDispatchingEObjectTextHover.class;
	}
	
	public Class<? extends IEObjectHoverProvider> bindIEObjectHoverProvider() {
	    return QDLEObjectHoverProvider.class;
	}
	 
	public Class<? extends IEObjectDocumentationProvider> bindIEObjectDocumentationProviderr() {
	    return QDLEObjectDocumentationProvider.class;
	}
	
	public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration () {
	     return QDLHighlightingConfiguration.class;
	}
	
	public Class<? extends ISemanticHighlightingCalculator> bindIHighlightingCalculator () {
	     return QDLSemanticHighlightingCalculator.class;
	}
	
	@Override
	public Class<? extends ITemplateProposalProvider> bindITemplateProposalProvider() {
		return QDLTemplateProposalProvider.class;
	}

	@Override
	public Class<? extends IXtextEditorCallback> bindIXtextEditorCallback() {
		return IQLXtextEditorCallback.class;
	}
	

}
