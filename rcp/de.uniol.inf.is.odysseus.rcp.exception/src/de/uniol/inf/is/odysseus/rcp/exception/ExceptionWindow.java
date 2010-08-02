package de.uniol.inf.is.odysseus.rcp.exception;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class ExceptionWindow {

	public ExceptionWindow(final Exception ex) {
		PlatformUI.getWorkbench().getDisplay().asyncExec( new Runnable() {

			@Override
			public void run() {
				createWindow(ex);
			}
			
		});
	}
	
	protected void createWindow(Exception ex) {

		final Shell shell;
		if( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() != null ) 
			shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		else
			shell = new Shell();
		
		shell.setMinimumSize(400, 500);
		shell.setText("Exception");
		

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		shell.setLayout(gl);

		Label label = new Label(shell, SWT.NONE);
		label.setText("An error has occured: " + ex.getClass().getSimpleName());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		label.setLayoutData(data);

		Label message = new Label(shell, SWT.NONE);
		message.setText(ex.getMessage() != null ? ex.getMessage() : "null");
		message.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		message.setLayoutData(data);

		Text stackTrace = new Text(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		stackTrace.setEditable(false);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		stackTrace.setLayoutData(data);
		stackTrace.setText(getStackTraceText(ex));
		
		Button closeButton = new Button( shell, SWT.PUSH );
		data = new GridData( GridData.FILL_HORIZONTAL);
		closeButton.setLayoutData(data);
		closeButton.setText("Close Application");
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				shell.dispose();
				
				PlatformUI.getWorkbench().close();
			}
		});
		
		Button acceptButton = new Button(shell, SWT.PUSH);
		acceptButton.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
		acceptButton.setText("Continue");
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

		sb.append("Caused by: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
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
