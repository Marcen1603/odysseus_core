package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator.OdysseusMapPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.RasterLayerConfiguration;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.UpdateListStrategy;

public class RasterLayerConfigurationComposite extends Composite {
	@SuppressWarnings("unused")
	private DataBindingContext m_bindingContext;
	RasterLayerConfiguration configuration = null;
	MapEditorModel model = null;
	private Text text;
	private Combo combo;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RasterLayerConfigurationComposite(Composite parent, int style, MapEditorModel model, RasterLayerConfiguration config) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		this.model = model;
		this.configuration = config;
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Name:");
		
		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_3 = new Label(this, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("Placement:");
		
		combo = new Combo(this, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		combo.setItems(model.getLayerNameList());
		
		Label lblNewLabel_2 = new Label(this, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("URL:");
		
		Combo combo_2 = new Combo(this, SWT.NONE);
		combo_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		String[] items = OdysseusMapPlugIn.getProperties().listTileServer();
		combo_2.setItems(items);

		Label lblProtocol = new Label(this, SWT.NONE);
		lblProtocol.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProtocol.setText("Protocol:");
		
		Combo combo_3 = new Combo(this, SWT.NONE);
		combo_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_bindingContext = initDataBindings();
		

	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(text);
		IObservableValue nameNewConfigObserveValue = PojoProperties.value("name").observe(configuration);
		bindingContext.bindValue(observeTextTextObserveWidget, nameNewConfigObserveValue, null, null);
		//
		IObservableList itemsComboObserveWidget = WidgetProperties.items().observe(combo);
		IObservableList layersModelObserveList = BeanProperties.list("layers").observe(model);
		bindingContext.bindList(itemsComboObserveWidget, layersModelObserveList, new UpdateListStrategy(UpdateListStrategy.POLICY_NEVER), null);
		//
		return bindingContext;
	}
}
