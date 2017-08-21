/**********************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.exception;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.report.IReportGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class BugReport {

	private static IReportGenerator reportGenerator;
	
	private final Throwable exception;


	// called by OSGi-DS
	public static void bindReportGenerator(IReportGenerator serv) {
		reportGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindReportGenerator(IReportGenerator serv) {
		if (reportGenerator == serv) {
			reportGenerator = null;
		}
	}

	public BugReport(Throwable exception) {
		this.exception = exception;
	}

	public BugReport() {
		this.exception = null;
	}

	public void openEditor() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				final Shell shell;
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getShell() != null) {
					shell = new Shell(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell());
				} else {
					shell = new Shell();
				}
				BugReportEditor editor = new BugReportEditor(
						shell,
						reportGenerator.generateReport(
								OdysseusRCPPlugIn.getActiveSession(), exception));
				editor.open();
			}

		});
	}


}
