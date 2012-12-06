/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.console;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * @author Dennis Geesen
 * 
 */
public class OdysseusConsole extends AppenderSkeleton {

	private static final String CONSOLE_NAME = "Odysseus Console";

	private MessageConsoleStream outStream;
	protected Color color;

	public void printMessage(String message) {
		outStream.println(message);
	}

	public void createConsole() {
		MessageConsole myConsole = findConsole(CONSOLE_NAME);
		outStream = myConsole.newMessageStream();
		this.layout = new PatternLayout("%-4r %-5p %c{1} %x - %m - %l %n");
	}

	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	@Override
	public void close() {
		this.closed = true;
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	protected void append(LoggingEvent event) {
		if (PlatformUI.isWorkbenchRunning()) {
			String message = this.layout.format(event);
			String trace[];
			outStream.print(message);
			if ((trace = event.getThrowableStrRep()) != null) {
				for (int i = 0; i < trace.length; i++) {
					outStream.println(trace[i]);
				}
			}
		}
	}

}
