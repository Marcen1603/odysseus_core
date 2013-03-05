package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

public class SelectValueDialog extends Dialog{

	private int minValue;
	private int maxValue;
	private int selectedValue;
	
	private Shell parent;
	private Label valueLabel;
	private Shell shell;
	
	public SelectValueDialog(Shell parent, int minValue, int maxValue, int selectedValue) {
		super(parent);
		this.parent = parent;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.selectedValue = selectedValue;
	}
	public int open(){
		shell = new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		shell.setText("SelectValueDialog");
		shell.setLayout(new GridLayout(1, true));
		
		final Scale scale = new Scale(shell, SWT.NONE);
		scale.setMinimum(minValue);
		scale.setMaximum(maxValue);
		scale.setPageIncrement(1);
		scale.setSelection(selectedValue);
		scale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		valueLabel = new Label(shell, SWT.BORDER);
		valueLabel.setText(Integer.toString(selectedValue));
		valueLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		Button buttonOK = new Button(shell, SWT.PUSH);
	    buttonOK.setText("Ok");
	    buttonOK.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
	    buttonOK.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	          shell.dispose();
	        }
	    });
	    scale.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				selectedValue = scale.getSelection();
				valueLabel.setText(Integer.toString(selectedValue));
				valueLabel.pack();
				shell.layout();
				shell.pack();
			}
		});
		
	    shell.pack();
	    shell.open();
	    
	    centerScreen();
	    
	    Display display = parent.getDisplay();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }

	    return selectedValue;
	}
	private void centerScreen() {
		 Rectangle shellBounds = parent.getBounds();
	     Point dialogSize = shell.getSize();

	     shell.setLocation(
	     shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
	     shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
	}

}
