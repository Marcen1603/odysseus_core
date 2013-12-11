package de.uniol.inf.is.odysseus.rcp.queries;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import de.uniol.inf.is.odysseus.core.collection.Context;

public class ParserClientUtil {

	public static Context createRCPContext(IFile file) {
		IProject project = file.getProject();
		String localRootLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();

		Context context = Context.empty();
		context.put("WORKSPACE", localRootLocation);
		context.put("WORKSPACE/", localRootLocation + File.separator);
		context.put("WORKSPACE\\", localRootLocation + File.separator);
		context.put("PROJECT", project.getName());
		context.put("PROJECTPATH", project.getLocation().toOSString());
		context.put("PROJECTPATH\\", project.getLocation().toOSString() + File.separator);
		context.put("PROJECTPATH/", project.getLocation().toOSString() + File.separator);
		context.put("WORKSPACEPROJECT", localRootLocation + File.separator + project.getName());
		context.put("WORKSPACEPROJECT\\", localRootLocation + File.separator + project + File.separator);
		context.put("WORKSPACEPROJECT/", localRootLocation + File.separator + project + File.separator);
		context.put("\\", File.separator);
		context.put("/", File.separator);
		
		return context;
	}
}
