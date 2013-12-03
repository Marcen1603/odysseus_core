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
package de.uniol.inf.is.odysseus.rcp.exception;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

public class ExceptionWindow {

	private Text stackTrace = null;
	
	public ExceptionWindow(final Exception ex) {
		PlatformUI.getWorkbench().getDisplay().asyncExec( new Runnable() {

			@Override
			public void run() {
				createWindow(ex);
			}
			
		});
	}
	
	protected void createWindow(final Exception ex) {

		final Shell shell;
		if( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() != null ) 
			shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		else
			shell = new Shell();
		
		shell.setText(OdysseusNLS.Exception);

		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		shell.setLayout(gl);

		Label label = new Label(shell, SWT.NONE);
		label.setText(OdysseusNLS.AnErrorHasOccured+": " + ex.getClass().getSimpleName());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		label.setLayoutData(data);

		Label message = new Label(shell, SWT.NONE);
		message.setText(ex.getMessage() != null ? ex.getMessage() : "null");
		message.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		message.setLayoutData(data);
		
		final Composite placeHolder = new Composite( shell, SWT.NONE);
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 3;
		placeHolder.setLayoutData(data);
		placeHolder.setLayout(new FillLayout());
		
		Button closeButton = new Button( shell, SWT.PUSH );
		data = new GridData( GridData.FILL_HORIZONTAL);
		closeButton.setLayoutData(data);
		closeButton.setText(OdysseusNLS.CloseApplication);
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				shell.dispose();
				
				PlatformUI.getWorkbench().close();
			}
		});
		
		final Button stackTraceButton = new Button( shell, SWT.PUSH );
		data = new GridData( GridData.FILL_HORIZONTAL);
		stackTraceButton.setLayoutData(data);
		stackTraceButton.setText(OdysseusNLS.ShowStackTrace);
		stackTraceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( stackTrace != null ) {
					// Stacktrace wird schon angezeigt --> ausblenden
					hideStackTrace(placeHolder);
					stackTraceButton.setText(OdysseusNLS.ShowStackTrace);
				} else {
					// Stacktrace noch unsichtbar --> einblenden
					showStackTrace(placeHolder, ex);
					stackTraceButton.setText(OdysseusNLS.HideStackTrace);
				}
				layoutShell(shell);
			}
		});
		
		Button acceptButton = new Button(shell, SWT.PUSH);
		acceptButton.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
		acceptButton.setText(OdysseusNLS.Continue);
		acceptButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				shell.dispose();
			}
		});
		
		shell.pack();
		shell.open();
	}
	
	protected void layoutShell(Shell shell) {
		shell.pack();
		shell.layout();
	}
	
	protected void showStackTrace(Composite comp, Exception ex) {
		Text stackTrace = new Text(comp, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY | SWT.BORDER);
		stackTrace.setText(getStackTraceText(ex));
		
		this.stackTrace = stackTrace;
	}
	
	protected void hideStackTrace( Composite comp ) {
		if( stackTrace != null ) {
			stackTrace.dispose();
			stackTrace = null;
		}
	}
	
	protected String getStackTraceText(Exception ex) {
		StackTraceElement[] stack = ex.getStackTrace();

		StringBuilder sb = new StringBuilder();

		for (StackTraceElement s : stack) {
			sb.append("\tat ");
			sb.append(s);
			sb.append("\n");
		}

		// cause
		Throwable cause = ex.getCause();
		if (cause != null) {
			printStackTraceAsCause( cause, sb, stack);
		}

		return sb.toString();
	}

	private void printStackTraceAsCause(Throwable ex, StringBuilder sb, StackTraceElement[] causedTrace) {
		// assert Thread.holdsLock(s);

		// Compute number of frames in common between this and caused
		StackTraceElement[] trace = ex.getStackTrace();
		int m = trace.length - 1, n = causedTrace.length - 1;
		while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
			m--;
			n--;
		}
		int framesInCommon = trace.length - 1 - m;

		sb.append(OdysseusNLS.CausedBy+": " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
		sb.append("\n");
		for (int i = 0; i <= m; i++)
			sb.append("\tat " + trace[i]).append("\n");
		if (framesInCommon != 0)
			sb.append("\t... " + framesInCommon + " more").append("\n");

		// Recurse if we have a cause
		Throwable ourCause = ex.getCause();
		if (ourCause != null)
			printStackTraceAsCause(ourCause, sb, trace);
	}
}
