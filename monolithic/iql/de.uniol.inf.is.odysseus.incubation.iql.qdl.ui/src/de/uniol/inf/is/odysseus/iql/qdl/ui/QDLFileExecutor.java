package de.uniol.inf.is.odysseus.iql.qdl.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.ui.executor.IIQLUiExecutor;
import de.uniol.inf.is.odysseus.incubation.iql.qdl.ui.internal.QdlActivator;
import de.uniol.inf.is.odysseus.rcp.editor.text.IFileExecutor;

public class QDLFileExecutor implements IFileExecutor{

	@Override
	public String getFileExtension() {
		return "qdl";
	}

	@Override
	public void run(String text, Context context) {
		String projectName = (String) context.get("PROJECT");
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		String filePath = (String) context.get("FILEPATH");
        URI uri = URI.createPlatformResourceURI(filePath, true);
        
		IResourceSetProvider resourceSetProvider = QdlActivator.getInstance().getInjector(QdlActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_QDL_QDL).getInstance(IResourceSetProvider.class);
		ResourceSet resourceSetToUse = resourceSetProvider.get(project);
        Resource res = resourceSetToUse.getResource(uri, true);
        if (res.getContents().size() > 0) {
        	IQLModel model = (IQLModel) res.getContents().get(0);
			IIQLUiExecutor executor = QdlActivator.getInstance().getInjector(QdlActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_QDL_QDL).getInstance(IIQLUiExecutor.class);
			executor.parse(model);
		}
	}
}
