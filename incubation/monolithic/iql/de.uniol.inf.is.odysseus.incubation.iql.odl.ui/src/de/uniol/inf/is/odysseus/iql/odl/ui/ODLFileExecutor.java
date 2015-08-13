package de.uniol.inf.is.odysseus.iql.odl.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.ui.parser.IIQLUiParser;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLModel;
import de.uniol.inf.is.odysseus.iql.odl.ui.internal.ODLActivator;
import de.uniol.inf.is.odysseus.rcp.editor.text.IFileExecutor;

import org.eclipse.xtext.junit4.util.ParseHelper;

public class ODLFileExecutor implements IFileExecutor{

	
	@Override
	public String getFileExtension() {
		return "odl";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(String text, Context context) {
		ParseHelper<ODLModel> parseHelper = ODLActivator.getInstance().getInjector(ODLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL).getInstance(ParseHelper.class);
		String projectName = (String) context.get("PROJECT");
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (project != null) {
			IResourceSetProvider resourceSetProvider = ODLActivator.getInstance().getInjector(ODLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL).getInstance(IResourceSetProvider.class);
			ResourceSet resourceSetToUse = resourceSetProvider.get(project);
			try {
				ODLModel model = parseHelper.parse(text, resourceSetToUse);
				parse(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run(String text, Context context, IEditorPart editor) {
		if (editor instanceof XtextEditor) {
			XtextEditor xtextEditor = (XtextEditor) editor;
			xtextEditor.getDocument().readOnly(new IUnitOfWork<ODLModel, XtextResource>() {

				@Override
				public ODLModel exec(XtextResource state) throws Exception {
					EObject obj = state.getParseResult().getRootASTElement();
					parse((IQLModel) obj);
					return null;
					
				}
			});
		}		
	}
	
	private void parse(IQLModel model) {
		IIQLUiParser parser = ODLActivator.getInstance().getInjector(ODLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL).getInstance(IIQLUiParser.class);
		parser.parse(model);
	}

}
