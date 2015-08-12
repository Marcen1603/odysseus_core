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
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.ui.IQLXtextEditorCallback;
import de.uniol.inf.is.odysseus.iql.basic.ui.hover.IQLDispatchingEObjectTextHover;
import de.uniol.inf.is.odysseus.iql.basic.ui.parser.IIQLUiParser;
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.qdl.ui.coloring.QDLHighlightingConfiguration;
import de.uniol.inf.is.odysseus.iql.qdl.ui.coloring.QDLSemanticHighlightingCalculator;
import de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist.QDLProposalProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist.QDLTemplateProposalProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.generator.QDLUiGenerator;
import de.uniol.inf.is.odysseus.iql.qdl.ui.hover.QDLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.hover.QDLEObjectHoverProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.parser.QDLUiParser;
import de.uniol.inf.is.odysseus.iql.qdl.ui.scoping.QDLUiScopeProvider;

/**
 * Use this class to register components to be used within the IDE.
 */
@SuppressWarnings("restriction")
public class QDLUiModule extends de.uniol.inf.is.odysseus.iql.qdl.ui.AbstractQDLUiModule {
	public QDLUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}
	
	public Class<? extends IGenerator> bindGenerator() {
		return QDLUiGenerator.class;
	}

	
	public Class<? extends IIQLJdtTypeProviderFactory> bindJdtTypeProviderFactory() {
		return IQLJdtTypeProviderFactory.class;
	}
	
	public Class<? extends IIQLScopeProvider> bindIQLScopeProvider() {
		return QDLUiScopeProvider.class;
	}
	
	
	public Class<? extends org.eclipse.xtext.scoping.IScopeProvider> bindIScopeProvider() {
		return QDLUiScopeProvider.class;
	}
	
	public Class<? extends IIQLUiParser> bindIQLUiParser() {
		return QDLUiParser.class;
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
