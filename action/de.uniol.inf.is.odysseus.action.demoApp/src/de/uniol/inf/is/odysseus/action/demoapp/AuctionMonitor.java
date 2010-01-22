package de.uniol.inf.is.odysseus.action.demoapp;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AuctionMonitor {

	public static Shell runApplication(Display display) {
		AuctionMonitor monitor = new AuctionMonitor();
		return monitor.init(display);
	}

	private Shell init(Display display) {
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);
		shell.open();
		return shell;
	}

}
