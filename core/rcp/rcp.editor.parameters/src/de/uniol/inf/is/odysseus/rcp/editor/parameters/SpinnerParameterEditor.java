package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.SimpleParameterEditor;

public class SpinnerParameterEditor extends SimpleParameterEditor<Long> {

	@Override
	public Long convertFromString(String txt) {
		return Long.valueOf(txt);
	}

	@Override
	public String convertToString(Long object) {
		if( object == null ) 
			return "";
		return object.toString();
	}
	
	@Override
	protected Control createInputControl(Composite parent) {
		final Spinner spinner = new Spinner(parent, SWT.BORDER);

		spinner.setSelection(0);
		spinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					setValue(spinner.getSelection());
				} catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
			
		});
		
		return spinner;
	}

}
