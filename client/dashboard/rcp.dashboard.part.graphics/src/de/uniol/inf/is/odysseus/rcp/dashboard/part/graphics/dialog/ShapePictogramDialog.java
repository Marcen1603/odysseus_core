package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.predicate.TuplePredicate;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.ColorManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractShapePictogram;

public class ShapePictogramDialog extends AbstractPictogramDialog<AbstractShapePictogram> {

	private List<ShapeEntry> entries = new ArrayList<>();
	private ScrolledComposite scroller;
	private Composite container;
	private Spinner widthSpinner;
	private int width;
	private Spinner rotateSpinner;
	private int rotate;
	private Button fillColorCheckButton;
	private Button keepRatioCheckButton;
	private boolean fillColor = false;
	private boolean keepRatio = false;
	

	@Override
	public Control createWidgetAdrea(final Composite parent) {
		Label lblChooseAnImage = new Label(parent, SWT.NONE);
		lblChooseAnImage.setText("Order-dependent list of colors");

		Button btnBrowseToAdd = new Button(parent, SWT.PUSH);
		btnBrowseToAdd.setText("Browse to add a color...");
		scroller = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		GridData gd_scroller = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_scroller.heightHint = 250;
		scroller.setLayoutData(gd_scroller);
		container = new Composite(scroller, SWT.NONE);
		scroller.setContent(container);
		scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		scroller.setMinHeight(250);

		GridLayout containerLayout = new GridLayout(2, false);
		containerLayout.marginWidth = 0;
		containerLayout.marginHeight = 0;
		container.setLayout(containerLayout);

		GridData gd_container = new GridData(GridData.FILL_HORIZONTAL);
		gd_container.heightHint = 250;
		gd_container.minimumHeight = 250;
		container.setLayoutData(gd_container);

		for (ShapeEntry ie : entries) {
			addShapeEntryToContainer(ie);
		}

		btnBrowseToAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {

				final ColorDialog dialog = new ColorDialog(parent.getShell());

				dialog.setText("Choose a color");
				RGB rgb = dialog.open();
				if (rgb != null) {
					ShapeEntry se = new ShapeEntry();
					se.color = ColorManager.createColor(rgb);
					se.predicate = "true";
					addShapeEntryToContainer(se);
					entries.add(se);
				}
			}

		});
		relayout(container, scroller);
		
		Label lblPredicate = new Label(parent, SWT.NONE);
		lblPredicate.setText("Width of the stroke");		

		widthSpinner = new Spinner(parent, SWT.BORDER);
		widthSpinner.setMinimum(0);
		widthSpinner.setMaximum(1000);
		widthSpinner.setSelection(width);
		widthSpinner.setIncrement(1);
		widthSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		
		
		Label lblRotate = new Label(parent, SWT.NONE);
		lblRotate.setText("Rotates the shape by the given counter-clockwise angle");
		
		rotateSpinner = new Spinner(parent, SWT.BORDER);
		rotateSpinner.setMinimum(0);
		rotateSpinner.setMaximum(359);
		rotateSpinner.setSelection(rotate);
		rotateSpinner.setIncrement(1);
		rotateSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));	
		
		keepRatioCheckButton = new Button(parent, SWT.CHECK);
		keepRatioCheckButton.setText("Do not fit the shape to the containers size and keep its ratio");
		keepRatioCheckButton.setSelection(keepRatio);
		
		fillColorCheckButton = new Button(parent, SWT.CHECK);
		fillColorCheckButton.setText("Fill the shape with the color.");
		fillColorCheckButton.setSelection(fillColor);
		return parent;
	}

	private void addShapeEntryToContainer(final ShapeEntry se) {
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label imgLabel = new Label(composite, SWT.NONE);
		GridData colorGridData = new GridData(GridData.FILL_HORIZONTAL);
		colorGridData.widthHint = 287;
		imgLabel.setLayoutData(colorGridData);
//		imgLabel.setText(se.color.toString());
		imgLabel.setBackground(se.color);

		final Text predicateText = new Text(composite, SWT.BORDER);
		GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
		gd_dataFolderText.widthHint = 287;
		predicateText.setLayoutData(gd_dataFolderText);
		predicateText.setText(se.predicate);
		predicateText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				se.predicate = predicateText.getText();

			}
		});

		se.composite = composite;

		final Button removeButton = new Button(container, SWT.PUSH);
		removeButton.setText("X");
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				se.composite.dispose();
				entries.remove(se);
				removeButton.dispose();
				relayout(container, scroller);
			}
		});
		relayout(container, scroller);
	}

	private void relayout(Composite container, ScrolledComposite scroller) {
		scroller.setMinHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		container.layout();
		scroller.layout(true);
	}

	@Override
	public void saveValues(AbstractShapePictogram pg) {
		pg.clearShapeColors();
		for (ShapeEntry se : entries) {
			pg.addShapeColor(se.predicate, se.color);
		}
		pg.setWidth(widthSpinner.getSelection());
		pg.setFillColor(fillColorCheckButton.getSelection());
		pg.setKeepRatio(keepRatioCheckButton.getSelection());
		pg.setRotate(rotateSpinner.getSelection());		
	}

	@Override
	public void loadValues(AbstractShapePictogram pg) {
		entries.clear();
		for (Pair<TuplePredicate, Color> pair : pg.getShapeColors()) {
			ShapeEntry se = new ShapeEntry();
			se.predicate = pair.getE1().toString();
			se.color = pair.getE2();
			entries.add(se);
		}		
		width = pg.getWidth();
		fillColor = pg.getFillColor();
		keepRatio = pg.isKeepRatio();
		rotate = (int) pg.getRotate();
	}

	public boolean isFillColor() {
		return fillColor;
	}

	public void setFillColor(boolean fillColor) {
		this.fillColor = fillColor;
	}

	public boolean isKeepRatio() {
		return keepRatio;
	}

	public void setKeepRatio(boolean keepRatio) {
		this.keepRatio = keepRatio;
	}

	private class ShapeEntry {
		protected Color color;
		protected String predicate;
		protected Composite composite;

	}

}
