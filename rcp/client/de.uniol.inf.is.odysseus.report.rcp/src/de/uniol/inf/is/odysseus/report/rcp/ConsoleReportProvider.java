package de.uniol.inf.is.odysseus.report.rcp;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportProvider;

public class ConsoleReportProvider implements IReportProvider {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getTitle() {
		return "Console";
	}

	@Override
	public String getReport(ISession session) {
		StringBuilder sb = new StringBuilder();
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		
		if (plugin != null) {
			IConsoleManager conMan = plugin.getConsoleManager();
			IConsole[] existing = conMan.getConsoles();
			for (int i = 0; i < existing.length; i++) {
				MessageConsole console = (MessageConsole) existing[i];
				IDocument document = console.getDocument();
				sb.append("\t* ").append(console.getName()).append(":\n");
				sb.append(document.get()).append("\n\n");
			}
		}

		return sb.toString();
	}
}
