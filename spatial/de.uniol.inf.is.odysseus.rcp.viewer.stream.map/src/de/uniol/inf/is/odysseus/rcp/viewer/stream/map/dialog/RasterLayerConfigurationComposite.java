package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator.OdysseusMapPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.RasterLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.internal.databinding.conversion.StringToBooleanConverter;

public class RasterLayerConfigurationComposite extends Composite {
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
