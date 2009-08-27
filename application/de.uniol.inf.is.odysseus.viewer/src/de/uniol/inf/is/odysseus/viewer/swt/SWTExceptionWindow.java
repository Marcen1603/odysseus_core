package de.uniol.inf.is.odysseus.viewer.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class SWTExceptionWindow {

	public SWTExceptionWindow( final Shell baseShell, Exception ex ) {
		
		final Shell shell;
		if( baseShell != null ) {
			shell = new Shell( baseShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		} else {
			shell = new Shell( Display.getCurrent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		}
		shell.setText( "Unerwartete Ausnahme" );
		shell.setSize( 600, 300 );
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shell.setLayout( gridLayout );
		
		Label label = new Label(shell, SWT.SHADOW_IN);
		label.setText( "Es wurde eine unbehandelte Ausnahme vom Typ " + ex.getClass().getSimpleName() + " geworfen." );
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		label.setLayoutData( data );
		
		ScrolledComposite comp = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		comp.setLayoutData( data );
		
		Text exceptionText = new Text(comp, SWT.MULTI | SWT.BORDER);
		StringBuilder sb = new StringBuilder();
		sb.append( ex.getClass().toString() );
		sb.append( ": " );
		sb.append( ex.getMessage() );
		sb.append( "\n" );
		for( StackTraceElement ele : ex.getStackTrace() ) {
			sb.append( "\tat " );
			sb.append( ele.getClassName() );
			sb.append( "." );
			sb.append( ele.getMethodName());
			if( ele.getFileName() != null ) {
				sb.append( "(" );
				sb.append( ele.getFileName() );
				sb.append( ":" );
				sb.append( ele.getLineNumber() );
				sb.append( ")" );
			} else {
				sb.append( "(Unknown ISource)" );
			}
			sb.append( "\n" );
		}
		exceptionText.setText( sb.toString() );
		exceptionText.setEditable( false );
		exceptionText.pack();
		comp.setContent( exceptionText );
		comp.setMinSize( exceptionText.getClientArea().width, exceptionText.getClientArea().height );
		comp.setExpandHorizontal( true );
		comp.setExpandVertical( true );

		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText( "OK" );
		okButton.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
		okButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent arg0 ) {
				shell.close();
				shell.dispose();
			}
		});
		
		Button endButton = new Button(shell, SWT.PUSH);
		endButton.setText( "Programm beenden" );
		endButton.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
		endButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent arg0 ) {
				shell.close();
				shell.dispose();
				baseShell.dispose();
			}
		});
		
		shell.open();

	}
}
