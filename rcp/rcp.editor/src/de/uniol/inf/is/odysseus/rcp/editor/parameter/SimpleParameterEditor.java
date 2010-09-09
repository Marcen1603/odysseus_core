package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class SimpleParameterEditor<T> extends AbstractParameterEditor {
	
	private Text text;
	private Label label;
	
	@Override
	public void createControl(Composite parent) {
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		parent.setLayout(gl);
		
		label = new Label( parent, SWT.CENTER );
		label.setText(getAttributeTitle());
				
		text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					if( text.getText().length() == 0 )
						setValue(null);
					else 
						setValue(convertFromString(text.getText()));
					
					if( !validate() ) 
						markErrors(getErrorText());
					else
						unmarkErrors();
					
				} catch( Exception ex ) {
					markErrors( ex.getMessage() );
				}
			}
			
		});

		try {
			@SuppressWarnings("unchecked")
			String txt = convertToString((T)getValue());
			text.setText(txt == null ? "" : txt );
		} catch( Exception ex ) {
			text.setText("");
		}
	}
	
	public abstract T convertFromString( String txt );
	public abstract String convertToString( T object );
	
	protected void markErrors( String txt ) {
		text.setForeground(ColorConstants.red);
		label.setForeground(ColorConstants.red);
		text.setToolTipText(txt);
		label.setToolTipText(txt);
	}
	
	protected void unmarkErrors() {
		text.setForeground(ColorConstants.black);
		label.setForeground(ColorConstants.black);
		text.setToolTipText("");
		label.setToolTipText("");
	}
	
	protected String getAttributeTitle() {
		return getParameter().getName();
	}

	@Override
	public void close() {

	}

}
