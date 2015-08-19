/*
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.odl.ui;


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
import de.uniol.inf.is.odysseus.iql.basic.ui.executor.IIQLUiExecutor;
import de.uniol.inf.is.odysseus.iql.basic.ui.hover.IQLDispatchingEObjectTextHover;
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.odl.scoping.ODLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.odl.ui.coloring.ODLHighlightingConfiguration;
import de.uniol.inf.is.odysseus.iql.odl.ui.coloring.ODLSemanticHighlightingCalculator;
import de.uniol.inf.is.odysseus.iql.odl.ui.contentassist.ODLProposalProvider;
import de.uniol.inf.is.odysseus.iql.odl.ui.contentassist.ODLTemplateProposalProvider;
import de.uniol.inf.is.odysseus.iql.odl.ui.executor.ODLUiExecutor;
import de.uniol.inf.is.odysseus.iql.odl.ui.generator.ODLUiGenerator;
import de.uniol.inf.is.odysseus.iql.odl.ui.hover.ODLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.odl.ui.hover.ODLEObjectHoverProvider;
import de.uniol.inf.is.odysseus.iql.odl.ui.scoping.ODLUiScopeProvider;


/**
 * Use this class to register components to be used within the IDE.
 */
@SuppressWarnings("restriction")
public class ODLUiModule extends de.uniol.inf.is.odysseus.iql.odl.ui.AbstractODLUiModule {
	public ODLUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}
	
	public Class<? extends IGenerator> bindGenerator() {
		return ODLUiGenerator.class;
	}
	
	public Class<? extends IIQLScopeProvider> bindIQLScopeProvider() {
		return ODLUiScopeProvider.class;
	}
	
	public Class<? extends org.eclipse.xtext.scoping.IScopeProvider> bindIScopeProvider() {
		return ODLUiScopeProvider.class;
	}
	
	public Class<? extends IIQLUiExecutor> bindIQLUiExecutor() {
		return ODLUiExecutor.class;
	}
	
	public Class<? extends IIQLJdtTypeProviderFactory> bindIQLJdtTypeProviderFactory() {
		return IQLJdtTypeProviderFactory.class;
	}	
	
	@Override
	public Class<? extends org.eclipse.xtext.common.types.access.IJvmTypeProvider.Factory> bindIJvmTypeProvider$Factory() {
		return ODLClasspathTypeProviderFactory.class;
	}
	
	@Override
	public Class<? extends org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
		return ClasspathBasedTypeScopeProvider.class;
	}
	
	@Override
	public Class<? extends org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider> bindIContentProposalProvider() {
		return ODLProposalProvider.class;
	}
	
	public Class<? extends IEObjectHover> bindIEObjectHover() {
		return IQLDispatchingEObjectTextHover.class;
	}
	
	public Class<? extends IEObjectHoverProvider> bindIEObjectHoverProvider() {
	    return ODLEObjectHoverProvider.class;
	}
	 
	public Class<? extends IEObjectDocumentationProvider> bindIEObjectDocumentationProviderr() {
	    return ODLEObjectDocumentationProvider.class;
	}
	
	public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration () {
	     return ODLHighlightingConfiguration.class;
	}
	
	public Class<? extends ISemanticHighlightingCalculator> bindIHighlightingCalculator () {
	     return ODLSemanticHighlightingCalculator.class;
	}
	
	@Override
	public Class<? extends ITemplateProposalProvider> bindITemplateProposalProvider() {
		return ODLTemplateProposalProvider.class;
	}

	
	@Override
	public Class<? extends IXtextEditorCallback> bindIXtextEditorCallback() {
		return IQLXtextEditorCallback.class;
	}
}
