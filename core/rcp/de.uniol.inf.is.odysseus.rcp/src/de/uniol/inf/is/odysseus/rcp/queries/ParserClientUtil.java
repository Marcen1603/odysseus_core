package de.uniol.inf.is.odysseus.rcp.queries;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class ParserClientUtil {

	private static final String REPLACEMENT_START_KEY = "${";
	private static final String REPLACEMENT_END_KEY = "}";

	public static String replaceClientReplacements(String text, IFile file) {
		IProject project = file.getProject();
		Map<String, String> replacements = new HashMap<>();
		String localRootLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		replacements.put("WORKSPACE", localRootLocation);
		replacements.put("WORKSPACE/", localRootLocation + File.separator);
		replacements.put("WORKSPACE\\", localRootLocation + File.separator);
		replacements.put("PROJECT", project.getName());
		replacements.put("PROJECTPATH", project.getLocation().toOSString());
		replacements.put("PROJECTPATH\\", project.getLocation().toOSString() + File.separator);
		replacements.put("PROJECTPATH/", project.getLocation().toOSString() + File.separator);
		replacements.put("WORKSPACEPROJECT", localRootLocation + File.separator + project.getName());
		replacements.put("WORKSPACEPROJECT\\", localRootLocation + File.separator + project + File.separator);
		replacements.put("WORKSPACEPROJECT/", localRootLocation + File.separator + project + File.separator);
		replacements.put("\\", File.separator);
		replacements.put("/", File.separator);
		for (Entry<String, String> replacement : replacements.entrySet()) {
			String pattern = REPLACEMENT_START_KEY + replacement.getKey() + REPLACEMENT_END_KEY;
			text = text.replace(pattern, replacement.getValue());

		}
		return text;

	}

	public static String[] replaceClientReplacements(String[] lines, IFile scriptFile) {
		for (int i = 0; i < lines.length; i++) {
			lines[i] = replaceClientReplacements(lines[i], scriptFile);
		}
		return lines;

	}

}
