package de.uniol.inf.is.odysseus.rcp.application;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.uniol.inf.is.odysseus.rcp.perspective.observer.IObserverPerspectiveConstants;
import de.uniol.inf.is.odysseus.rcp.user.Login;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return IObserverPerspectiveConstants.PERSPECTIVE_ID;
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
	}
	
	@Override
	public void postStartup() {
		super.postStartup();
		Login.login(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), false, false);
		
		// Log4j-Ausgaben in Console umleiten
		// Init der Message Console
		// Tempor�r wieder auf die Eclipse Console
		
		//		MessageConsole log4jConsole = new MessageConsole("Log4J", null);
//		MessageConsoleStream log4jStream = log4jConsole.newMessageStream();
//		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { log4jConsole });
//		
//		Logger logger = Logger.getRootLogger();
//		logger.addAppender(new MessageConsoleAppender(log4jStream));
//		
//		// Quelle: http://jprog.blogspot.com/2005/09/using-eclipse-console-view-in-rcp.html
//		//
//		MessageConsole myConsole = new MessageConsole("System.out", null); // declare console
//		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { myConsole });
//		try{
//			myConsole.setWaterMarks(10000, 10001);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		MessageConsoleStream stream = myConsole.newMessageStream();
//		PrintStream myS = new PrintStream(stream);
//		System.setOut(myS); // link standard output stream to the console
//		System.setErr(myS); // link error output stream to the console	
	}
}
