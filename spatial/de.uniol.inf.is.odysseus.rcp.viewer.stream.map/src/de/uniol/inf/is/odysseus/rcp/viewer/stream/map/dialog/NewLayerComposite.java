package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.VectorLayerConfiguration;

public class NewLayerComposite extends Composite {
	
	LayerConfiguration configuration;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NewLayerComposite(Composite parent, int style, MapEditorModel model) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Type:");
		
		final Button btnRadioButton = new Button(this, SWT.RADIO);
		btnRadioButton.setText("Vector");
		btnRadioButton.setSelection(true);
		
		final Button btnRadioButton_1 = new Button(this, SWT.RADIO);
		btnRadioButton_1.setText("Raster");
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Name:");
		
		final Text text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		text.setText(model.getNextLayerName());
		final Button btnCreate = new Button(this, SWT.NONE);
		btnCreate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
		btnCreate.setText("Create");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		btnCreate.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (event.widget == btnCreate){
					if(btnRadioButton.getSelection()){
						configuration = new VectorLayerConfiguration(text.getText());
						notifyListeners(SWT.CHANGED, event);
					}
					else if (btnRadioButton_1.getSelection()){
						configuration = new VectorLayerConfiguration(text.getText());
						notifyListeners(SWT.CHANGED, event);
					}
				}
			}
		});
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public LayerConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return this.configuration;
	}
}
