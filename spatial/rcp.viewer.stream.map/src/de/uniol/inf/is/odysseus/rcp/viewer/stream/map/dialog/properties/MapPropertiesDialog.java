package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap.Heatmap;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.tracemap.TraceLayer;

/**
 * 
 * Dialog to edit the properties of a map
 * 
 * @author Stefan Bothe
 * 
 */
public class MapPropertiesDialog extends TitleAreaDialog {

	private static final Logger LOG = LoggerFactory
			.getLogger(TitleAreaDialog.class);

	private TreeViewer treeViewer;
	private PropertiesLabelProvider lblProvider = new PropertiesLabelProvider();
	HashMap<Integer, Style> hashStyles;

	private MapEditorModel map;
	@SuppressWarnings("unused")
	private StreamMapEditorPart editor;

	private LayerConfiguration layerConfiguration = null;
	private Text txtSridInput;
	private Text txtQueriesInput;

	public LayerConfiguration getLayerConfiguration() {
		return layerConfiguration;
	}

	public void setLayerConfiguration(LayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param editor
	 */
	public MapPropertiesDialog(Shell parentShell,
			LinkedList<ILayer> layerOrder, MapEditorModel map,
			Collection<LayerUpdater> connections, StreamMapEditorPart editor) {
		super(parentShell);
		this.setShellStyle(SWT.MAX | SWT.RESIZE);
		this.map = map;
		this.editor = editor;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Map Properties: " + map.getFile().getName());
		setMessage("Edit map parameters.", IMessageProvider.INFORMATION);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		GridData gd_container = new GridData(GridData.FILL, SWT.FILL, true,
				true);
		gd_container.heightHint = 317;
		container.setLayoutData(gd_container);

		treeViewer = new TreeViewer(container, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		treeViewer.setContentProvider(new PropertiesContentProvider());
		treeViewer.setLabelProvider(lblProvider);
		treeViewer.setInput(new PropertiesModel(map.getLayers().toArray(), map
				.getFile()));
		treeViewer.refresh();
		treeViewer.expandAll();

		treeViewer.getTree().setSelection(treeViewer.getTree().getItem(0));
		treeViewer.setSelection(treeViewer.getSelection(), true);

		// Add a listener -> shows right settings for the layers
		treeViewer
				.addSelectionChangedListener(new TreeListener(container, this));

		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		// Fill the right area with standard-content
		createStandardContent(container);

		return area;
	}

	/**
	 * Fills the container with standardcontent (srid and query) for
	 * non-thematic maps
	 * 
	 * @param container
	 */
	public void createStandardContent(Composite container) {
		Group grpMap = new Group(container, SWT.NONE);
		grpMap.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpMap.setText("Map");
		grpMap.setLayout(new GridLayout(2, false));

		Label lblSrid = new Label(grpMap, SWT.NONE);
		lblSrid.setText("Srid:");

		txtSridInput = new Text(grpMap, SWT.BORDER);
		txtSridInput.setText(Integer.toString(map.getSRID()));

		Label lblQueries = new Label(grpMap, SWT.NONE);
		lblQueries.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1));
		lblQueries
				.setText("Query: " + map.getQryFileList().getLast().getName());
		new Label(grpMap, SWT.NONE);

		txtQueriesInput = new Text(grpMap, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		txtQueriesInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		try {
			txtQueriesInput.setText(convertStreamToString(map.getQryFileList()
					.getLast().getContents()));
		} catch (CoreException e) {
			e.printStackTrace();
		}

		container.layout();
	}

	@SuppressWarnings("resource")
	public static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createOkButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// Check for valid input, if standard-properties are shown
				if (!txtSridInput.isDisposed() && isValidInput()) {
					// Non-thematic
					try {
						map.getQryFileList()
								.getLast()
								.setContents(
										new ByteArrayInputStream(
												txtQueriesInput.getText()
														.getBytes("UTF-8")),
										true, true, null);
						map.setSrid(Integer.parseInt(txtSridInput.getText()));
					} catch (UnsupportedEncodingException | CoreException e) {
						LOG.debug("Changing Map Porperties failed");
						e.printStackTrace();
					}					
				} else {

					IStructuredSelection selection = (IStructuredSelection) treeViewer
							.getSelection();
					if (selection.getFirstElement() instanceof Heatmap) {
						// OK-Options for the heatmap menu
						Heatmap heatmap = (Heatmap) selection.getFirstElement();
						// Should have been set in the TreeListener
						if(layerConfiguration != null && layerConfiguration instanceof HeatmapLayerConfiguration)
							heatmap.setConfiguration((HeatmapLayerConfiguration)layerConfiguration);

					} else if (selection.getFirstElement() instanceof TraceLayer) {
						// OK-Options for the linemap menu

					}
				}
				okPressed();
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		boolean valid = true;

		if (txtSridInput.getText().isEmpty()
				|| txtSridInput.getText().contains(" ")) {
			super.setErrorMessage("Please enter a valid number: No whitespaces");
			valid = false;
		}

		try {
			Integer.parseInt(txtSridInput.getText());
		} catch (NumberFormatException e) {
			super.setErrorMessage("Please enter only valid numbers");
			valid = false;
		}

		return valid;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(748, 522);
	}
}
 