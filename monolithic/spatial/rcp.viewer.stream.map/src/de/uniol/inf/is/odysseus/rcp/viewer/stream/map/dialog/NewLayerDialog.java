package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TabItem;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.VectorLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.RasterLayerConfiguration;

public class NewLayerDialog extends TitleAreaDialog {

	MapEditorModel model = null;
	LayerConfiguration configuration = null;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewLayerDialog(Shell parentShell, MapEditorModel model, LayerConfiguration configuration) {
		super(parentShell);
		this.model = model;
		this.configuration = configuration;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		final Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		
		final TabItem tbtmInput = new TabItem(tabFolder, SWT.NONE);
		tbtmInput.setText("Layer");
		if (configuration == null){
			final NewLayerComposite newLayerComposite = new NewLayerComposite(tabFolder, SWT.NONE, model);
			tbtmInput.setControl(newLayerComposite);
			newLayerComposite.addListener(SWT.CHANGED, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
					configuration = newLayerComposite.getConfiguration();
					newLayerComposite.dispose();
					if (configuration instanceof VectorLayerConfiguration){
						createNewVectorLayerConfiguration(tabFolder, tbtmInput);
					}else if (configuration instanceof RasterLayerConfiguration){
						createNewRasterLayerConfiguration(tabFolder, tbtmInput);
					}
					container.redraw();
				}
			});
		}
		else if (configuration instanceof VectorLayerConfiguration){
			createNewVectorLayerConfiguration(tabFolder, tbtmInput);
		}else if (configuration instanceof RasterLayerConfiguration){
			createNewRasterLayerConfiguration(tabFolder, tbtmInput);
		}
		
		return area;
	}

	private void createNewVectorLayerConfiguration(TabFolder tabFolder, TabItem tbtmInput){
		VectorLayerConfigurationComposite vectorLayerConfigurationComposite_1 = new VectorLayerConfigurationComposite(tabFolder, SWT.NONE, model, (VectorLayerConfiguration) configuration);
		tbtmInput.setControl(vectorLayerConfigurationComposite_1);

		TabItem tbtmStyle = new TabItem(tabFolder, SWT.NONE);
		tbtmStyle.setText("Style");
		
		StyleComposite styleComposite = new StyleComposite(tabFolder, SWT.NONE, configuration);
		tbtmStyle.setControl(styleComposite);
	}
	
	private void createNewRasterLayerConfiguration(TabFolder tabFolder, TabItem tbtmInput){
		RasterLayerConfigurationComposite vectorLayerConfigurationComposite_1 = new RasterLayerConfigurationComposite(tabFolder, SWT.NONE, model, (RasterLayerConfiguration) configuration);
		tbtmInput.setControl(vectorLayerConfigurationComposite_1);
		TabItem tbtmStyle = new TabItem(tabFolder, SWT.NONE);
		tbtmStyle.setText("Style");
		
		StyleComposite styleComposite = new StyleComposite(tabFolder, SWT.NONE, configuration);
		tbtmStyle.setControl(styleComposite);
	}
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 423);
	}

}
