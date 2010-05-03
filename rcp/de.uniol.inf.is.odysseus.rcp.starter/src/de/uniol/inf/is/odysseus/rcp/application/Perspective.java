package de.uniol.inf.is.odysseus.rcp.application;

import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {

		// Log4j-Ausgaben in Console umleiten
		// Init der Message Console
		MessageConsole log4jConsole = new MessageConsole("Log4J", null);
		MessageConsoleStream log4jStream = log4jConsole.newMessageStream();
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { log4jConsole });
		
		Logger logger = Activator.LOGGER;
		logger.addAppender(new MessageConsoleAppender(log4jStream));
		
		// Quelle: http://jprog.blogspot.com/2005/09/using-eclipse-console-view-in-rcp.html
		//
		MessageConsole myConsole = new MessageConsole("System", null); // declare console
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { myConsole });
		MessageConsoleStream stream = myConsole.newMessageStream();
		PrintStream myS = new PrintStream(stream);
		System.setOut(myS); // link standard output stream to the console
		System.setErr(myS); // link error output stream to the console

		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		layout.addStandaloneView("org.eclipse.ui.console.ConsoleView", true, IPageLayout.BOTTOM, 1.0f, editorArea);
	}

}
