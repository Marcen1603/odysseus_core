package de.uniol.inf.is.odysseus.rcp.logging.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public class LogEntryDetailsWindow {
	
	public static void show( RCPLogEntry entry ) {
		// Preconditions.checkNotNull(entry, "Entry must not be null!");
		
		createWindowAsync(entry);
	}

	private static void createWindowAsync(final RCPLogEntry entry) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				createWindow(entry);
			}
		});
	}

	private static void createWindow(RCPLogEntry entry) {
		final Shell shell = createShell();
		
		shell.setText("Log details");
		shell.setLayout(new GridLayout(2, false));
		
		createLabelTextPair(shell, "Timestamp", String.valueOf(entry.getTimestamp()));
		createLabelTextPair(shell, "Level", entry.getLevel().toString());
		createLabelTextPair(shell, "Logger", entry.getLoggerName());
		createLabelTextPair(shell, "Method name", entry.getClassName() + "." + entry.getMethodName() + ":" + entry.getLineNumber());
		createLabelTextPair(shell, "Simple class name", entry.getSimpleClassName());
		createLabelTextPair(shell, "Thread name", entry.getThreadName());
		
		createLabel(shell, "Message");
		Text messageText = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
		messageText.setText(entry.getMessage());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 400;
		messageText.setLayoutData(gd);
		
		createButtons(shell);
		
		shell.pack();
		shell.open();
	}

	private static void createLabelTextPair(Shell shell, String title, String text) {
		createLabel(shell, title);
		createReadonlyText(shell, text);
	}

	private static Composite createButtons(final Shell shell) {
		Composite buttonComposite = new Composite(shell, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		buttonComposite.setLayoutData(gd);
		buttonComposite.setLayout(new GridLayout(2,false));
		
		Composite emptyButtonSpaceComposite = new Composite(buttonComposite, SWT.NONE);
		emptyButtonSpaceComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Button okButton = new Button(buttonComposite, SWT.PUSH);
		okButton.setText("OK");
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				shell.dispose();
			}
		});
		
		return buttonComposite;
	}

	private static Label createLabel(Shell shell, String text) {
		Label label = new Label(shell, SWT.NONE);
		label.setText(text);
		
		return label;
	}
	
	private static Text createReadonlyText(Shell shell, String txt) {
		Text text = new Text(shell, SWT.READ_ONLY | SWT.BORDER);
		text.setText(txt);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		return text;
	}

	private static Shell createShell() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() != null) {
			return new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		}
		
		return new Shell();
	}

}
