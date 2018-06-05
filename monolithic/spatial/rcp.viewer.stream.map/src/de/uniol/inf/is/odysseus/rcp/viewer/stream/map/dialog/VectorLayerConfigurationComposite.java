package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.VectorLayerConfiguration;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class VectorLayerConfigurationComposite extends Composite {
	
	private VectorLayerConfiguration configuration = null;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public VectorLayerConfigurationComposite(Composite parent, int style, MapEditorModel model, VectorLayerConfiguration config) {
		super(parent, SWT.NONE);
		this.configuration = config;
		setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Name:");
		
		Text text = new Text(this, SWT.BORDER);
		setTextContent(text);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_3 = new Label(this, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("Placement:");
		
		ComboViewer comboViewer = new ComboViewer(this, SWT.NONE);
		setPlacementContent(comboViewer);
		comboViewer.setInput(model);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Source:");
		
		ComboViewer comboViewer_1 = new ComboViewer(this, SWT.NONE);
		Combo combo_1 = comboViewer_1.getCombo();
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		Label lblNewLabel_2 = new Label(this, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("Attribute:");

		ComboViewer comboViewer_2 = new ComboViewer(this, SWT.NONE);
		setAttributeContent(comboViewer_2);
		setSourceContent(comboViewer_1, comboViewer_2);
		comboViewer_1.setInput(model);
		if (configuration.getQuery() != null){
			Object o = comboViewer.getData(configuration.getQuery());
			if (o != null)
				comboViewer.setSelection(new StructuredSelection(o));
		}
		Combo combo_2 = comboViewer_2.getCombo();
		combo_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	
	
	private void setTextContent(final Text text) {
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				configuration.setName(text.getText());
			}
		});
		text.setText(this.configuration.getName());
	}



	private void setPlacementContent(ComboViewer comboViewer) {
		comboViewer.setContentProvider(new IStructuredContentProvider() {
			MapEditorModel input;
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				this.input = (MapEditorModel) newInput;
			}
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			@Override
			public Object[] getElements(Object inputElement) {
				// TODO Auto-generated method stub
				return this.input.getLayers().toArray();
			}
		});
		comboViewer.setLabelProvider(new LabelProvider() {
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				if (element instanceof ILayer)
					return true;
				return false;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof ILayer)
					return ((ILayer)element).getComplexName();
				return null;
			}
		});
		
	}

	private void setSourceContent(ComboViewer comboViewer, final ComboViewer attributeViewer) {
		comboViewer.setContentProvider(new IStructuredContentProvider() {
			MapEditorModel input;
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				this.input = (MapEditorModel) newInput;
			}
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			@Override
			public Object[] getElements(Object inputElement) {
				// TODO Auto-generated method stub
				return this.input.getConnectionCollection().toArray();
			}
		});
		
		comboViewer.setLabelProvider(new LabelProvider() {
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				if (element instanceof LayerUpdater)
					return true;
				return false;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof LayerUpdater)
					return ((LayerUpdater)element).getQuery().getQueryText();
				return null;
			}
		});
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				LayerUpdater layerUpdater = (LayerUpdater) selection.getFirstElement();
				configuration.setQuery(layerUpdater.getQuery().getQueryText());
				attributeViewer.setInput(layerUpdater);
				Object attribute = attributeViewer.getElementAt(0);
				if (attribute != null)
					attributeViewer.setSelection(new StructuredSelection((attribute)));
			}
		});
	}

	private void setAttributeContent(ComboViewer comboViewer) {
		comboViewer.setContentProvider(new IStructuredContentProvider() {
			LayerUpdater input;
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				this.input = (LayerUpdater) newInput;
			}
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			@Override
			public Object[] getElements(Object inputElement) {
				return input.getConnection().getOutputSchema().getAttributes().toArray();
			}
		});
		comboViewer.setLabelProvider(new LabelProvider() {
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				if (element instanceof SDFAttribute)
					if (((SDFAttribute)element).getDatatype() instanceof SDFSpatialDatatype)
						return true;
				return false;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof SDFAttribute)
					return ((SDFAttribute)element).getAttributeName();
				return null;
			}
		});
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
