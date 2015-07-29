/*
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.qdl.ui;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.builder.nature.NatureAddingEditorCallback;
import org.eclipse.xtext.builder.nature.ToggleXtextNatureAction;
import org.eclipse.xtext.common.types.xtext.ClasspathBasedTypeScopeProvider;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;
import org.eclipse.xtext.ui.editor.IXtextEditorCallback;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateProposalProvider;
import org.eclipse.xtext.ui.editor.hover.IEObjectHover;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.IQLDispatchingEObjectTextHover;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.qdl.ui.coloring.QDLHighlightingConfiguration;
import de.uniol.inf.is.odysseus.iql.qdl.ui.coloring.QDLSemanticHighlightingCalculator;
import de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist.QDLTemplateProposalProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.hover.QDLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.qdl.ui.hover.QDLEObjectHoverProvider;

/**
 * Use this class to register components to be used within the IDE.
 */
@SuppressWarnings("restriction")
public class QDLUiModule extends de.uniol.inf.is.odysseus.iql.qdl.ui.AbstractQDLUiModule {
	public QDLUiModule(AbstractUIPlugin plugin) {
		super(plugin);
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
	public Class<? extends org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
		return ClasspathBasedTypeScopeProvider.class;
	}

	@Override
	public Class<? extends IXtextEditorCallback> bindIXtextEditorCallback() {
		return IQLXtextEditorCallback.class;
	}
	
	@Override
	public Class<? extends org.eclipse.xtext.common.types.access.IJvmTypeProvider.Factory> bindIJvmTypeProvider$Factory() {
		return QDLClasspathTypeProviderFactory.class;
	}
	
	
	private static class IQLXtextEditorCallback extends NatureAddingEditorCallback{
		@Inject
		private ToggleXtextNatureAction toggleNature;
		
		@Override
		public void afterCreatePartControl(XtextEditor editor) {
			IResource resource = editor.getResource();
			if (resource != null) {
				IProject project = resource.getProject();
				if (project != null && project.isAccessible() && !project.isHidden() && !toggleNature.hasNature(project)) {
					toggleNature.toggleNature(project);
				}
			}			
		}		
	}
}
