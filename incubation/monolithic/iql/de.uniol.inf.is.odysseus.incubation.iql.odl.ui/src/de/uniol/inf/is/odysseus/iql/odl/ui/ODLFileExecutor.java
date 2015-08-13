package de.uniol.inf.is.odysseus.iql.odl.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.xtext.ui.resource.IResourceSetProvider;

import de.uniol.inf.is.odysseus.core.collection.Context;
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
				IIQLUiParser parser = ODLActivator.getInstance().getInjector(ODLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL).getInstance(IIQLUiParser.class);
				parser.parse(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
//	@Override
//	public void run(String text, Context context) {
//		String projectName = (String) context.get("PROJECT");
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
//		String filePath = (String) context.get("FILE");
//        URI uri = URI.createPlatformResourceURI(filePath, true);
//		IResourceSetProvider resourceSetProvider = ODLActivator.getInstance().getInjector(ODLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL).getInstance(IResourceSetProvider.class);
//		ResourceSet resourceSetToUse = resourceSetProvider.get(project);
//        Resource res = resourceSetToUse.getResource(uri, true);
//        if (res.getContents().size() > 0) {
//        	ODLModel model = (ODLModel) res.getContents().get(0);
//			parse(model);
//        }
//        res.getContents().get(0);
//	}

//	@Override
//	public void run(String text, Context context, IEditorPart editor) {
//		if (editor instanceof XtextEditor) {
//			XtextEditor xtextEditor = (XtextEditor) editor;
//			xtextEditor.getDocument().readOnly(new IUnitOfWork<ODLModel, XtextResource>() {
//
//				@Override
//				public ODLModel exec(XtextResource state) throws Exception {
//					EObject obj = state.getParseResult().getRootASTElement();
//					parse((IQLModel) obj);
//					return null;
//					
//				}
//			});
//		}		
//	}


}
