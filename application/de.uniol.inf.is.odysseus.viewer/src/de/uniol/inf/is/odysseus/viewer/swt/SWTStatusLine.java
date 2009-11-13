package de.uniol.inf.is.odysseus.viewer.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;


public class SWTStatusLine {

	private Label statusText;
	
	public SWTStatusLine( Composite parent ) {
		statusText = new Label(parent, SWT.SHADOW_IN );
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		statusText.setLayoutData( data );
	}
	
	public void setText( final String text ) {
		
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				statusText.setForeground( Display.getDefault().getSystemColor( SWT.COLOR_BLACK ) );
			}
		});
		statusText.setText( text );				

	}
	
	public String getText() {
		return statusText.getText();
	}
	
	public void setErrorText( String text ) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				statusText.setForeground( Display.getDefault().getSystemColor( SWT.COLOR_RED ) );
			}
		});

		statusText.setText( text );
	}
	
}
