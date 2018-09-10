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
        File parent = file.getLocation().makeAbsolute().toFile().getParentFile();
        String projectName = project.getName();
        Context context = Context.empty();
        context.put("FILE", file.getName());
        context.put("FILEPATH", file.getFullPath().toString());
        context.put("FILEPATH\\", file.getFullPath().toString() + File.separator);
        context.put("FILEPATH/", file.getFullPath().toString() + File.separator);
        context.put("FILEOSPATH", file.getFullPath().toOSString());
        context.put("FILEOSPATH\\", file.getFullPath().toOSString() + File.separator);
        context.put("FILEOSPATH/", file.getFullPath().toOSString() + File.separator);
        context.put("ROOT", parent.getAbsolutePath());
        context.put("ROOT\\", parent.getAbsolutePath() + File.separator);
        context.put("ROOT/", parent.getAbsolutePath() + File.separator);
		context.put("WORKSPACE", localRootLocation);
		context.put("WORKSPACE/", localRootLocation + File.separator);
		context.put("WORKSPACE\\", localRootLocation + File.separator);
		context.put("PROJECT", project.getName());
		context.put("PROJECTPATH", project.getLocation().toOSString());
		context.put("PROJECTPATH\\", project.getLocation().toOSString() + File.separator);
		context.put("PROJECTPATH/", project.getLocation().toOSString() + File.separator);
		context.put("WORKSPACEPROJECT", project.getLocation().toOSString());
		context.put("WORKSPACEPROJECT\\", project.getLocation().toOSString() + File.separator);
		context.put("WORKSPACEPROJECT/", project.getLocation().toOSString() + File.separator);
		// this is used to run tests inside of studio
		context.put("BUNDLE-ROOT", localRootLocation + File.separator + projectName);
		context.put("\\", File.separator);
		context.put("/", File.separator);
		
		return context;
	}
}
