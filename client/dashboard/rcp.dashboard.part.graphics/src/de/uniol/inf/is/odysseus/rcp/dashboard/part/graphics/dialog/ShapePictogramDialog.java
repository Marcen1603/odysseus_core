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
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.predicate.TuplePredicate;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.ColorManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.RectanglePictogram;

public class ShapePictogramDialog extends AbstractPictogramDialog<RectanglePictogram> {

	private List<ShapeEntry> entries = new ArrayList<>();
	private ScrolledComposite scroller;
	private Composite container;
	private Text widthText;
	private String width;

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

		widthText = new Text(parent, SWT.BORDER);
		widthText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		widthText.setText(width);
		
		
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
	public void saveValues(RectanglePictogram pg) {
		pg.clearShapeColors();
		for (ShapeEntry se : entries) {
			pg.addShapeColor(se.predicate, se.color);
		}
		pg.setWidth(Integer.parseInt(widthText.getText()));

	}

	@Override
	public void loadValues(RectanglePictogram pg) {
		entries.clear();
		for (Pair<TuplePredicate, Color> pair : pg.getShapeColors()) {
			ShapeEntry se = new ShapeEntry();
			se.predicate = pair.getE1().toString();
			se.color = pair.getE2();
			entries.add(se);
		}
		width = Integer.toString(pg.getWidth());
	}

	private class ShapeEntry {
		protected Color color;
		protected String predicate;
		protected Composite composite;

	}

}
