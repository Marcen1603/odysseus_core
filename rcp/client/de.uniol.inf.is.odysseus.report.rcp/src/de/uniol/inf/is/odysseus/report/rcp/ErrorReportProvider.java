package de.uniol.inf.is.odysseus.report.rcp;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.eclipse.core.resources.ResourcesPlugin;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportProvider;

public class ErrorReportProvider implements IReportProvider {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getTitle() {
		return "Error Log";
	}

	@Override
	public String getReport(ISession session) {
		StringBuilder sb = new StringBuilder();

		String localRootLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		Path path = FileSystems.getDefault().getPath(localRootLocation, ".metadata", ".log");
		
		Util.loadFromFile(sb, path, "Info Service Log");
		
		return sb.toString();
	}
}
