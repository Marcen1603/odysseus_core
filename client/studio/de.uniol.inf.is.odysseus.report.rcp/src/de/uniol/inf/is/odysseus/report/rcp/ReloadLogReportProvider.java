package de.uniol.inf.is.odysseus.report.rcp;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportProvider;

public class ReloadLogReportProvider implements IReportProvider {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getTitle() {
		return "ReloadLog information";
	}

	@Override
	public String getReport(ISession session) {
		StringBuilder sb = new StringBuilder();
		
		Path path = FileSystems.getDefault().getPath(OdysseusBaseConfiguration.getHomeDir(), "reloadlog.store");
		Util.loadFromFile(sb, path, "Reload Log");
		
		return sb.toString();
	}
}
