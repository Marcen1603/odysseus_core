package de.uniol.inf.is.odysseus.server.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.IFileExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.server.generator.IStreamingSparqlParser;
import de.uniol.inf.is.odysseus.server.streamingsparql.ui.internal.StreamingsparqlActivator;

public class StreamingSparqlFileExecutor implements IFileExecutor {

	
	@Override
	public String getFileExtension() {
		// TODO Auto-generated method stub
		return "sparql";
	}

	@Override
	public void run(String text, Context context) {
		
		String projectName = (String) context.get("PROJECT");
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		String filePath = (String) context.get("FILEPATH");
        URI uri = URI.createPlatformResourceURI(filePath, true);
		IResourceSetProvider resourceSetProvider = StreamingsparqlActivator.getInstance().getInjector(StreamingsparqlActivator.DE_UNIOL_INF_IS_ODYSSEUS_SERVER_STREAMINGSPARQL).getInstance(IResourceSetProvider.class);
		ResourceSet resourceSetToUse = resourceSetProvider.get(project);
        Resource res = resourceSetToUse.getResource(uri, true);
        IStreamingSparqlParser parser = StreamingsparqlActivator.getInstance().getInjector(StreamingsparqlActivator.DE_UNIOL_INF_IS_ODYSSEUS_SERVER_STREAMINGSPARQL).getInstance(IStreamingSparqlParser.class);
		String qry = parser.parse(res);
		
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		executor.addQuery(qry, "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), context);
	}

}
