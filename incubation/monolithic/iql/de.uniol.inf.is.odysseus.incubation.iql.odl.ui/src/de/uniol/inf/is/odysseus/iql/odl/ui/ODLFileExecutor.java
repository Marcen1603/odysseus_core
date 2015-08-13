package de.uniol.inf.is.odysseus.iql.odl.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.iql.basic.ui.parser.IIQLUiParser;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLModel;
import de.uniol.inf.is.odysseus.iql.odl.ui.internal.ODLActivator;
import de.uniol.inf.is.odysseus.rcp.editor.text.IFileExecutor;


public class ODLFileExecutor implements IFileExecutor{

	
	@Override
	public String getFileExtension() {
		return "odl";
	}

	@Override
	public void run(String text, Context context) {
		String projectName = (String) context.get("PROJECT");
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		String filePath = (String) context.get("FILEPATH");
        URI uri = URI.createPlatformResourceURI(filePath, true);
		IResourceSetProvider resourceSetProvider = ODLActivator.getInstance().getInjector(ODLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL).getInstance(IResourceSetProvider.class);
		ResourceSet resourceSetToUse = resourceSetProvider.get(project);
        Resource res = resourceSetToUse.getResource(uri, true);
        if (res.getContents().size() > 0) {
        	ODLModel model = (ODLModel) res.getContents().get(0);
			IIQLUiParser parser = ODLActivator.getInstance().getInjector(ODLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL).getInstance(IIQLUiParser.class);
			parser.parse(model);
		}
	}


}
