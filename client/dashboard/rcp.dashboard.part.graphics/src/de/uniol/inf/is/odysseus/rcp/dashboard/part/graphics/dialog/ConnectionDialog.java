package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.predicate.TuplePredicate;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.ColorManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection;

public class ConnectionDialog extends AbstractPartDialog<Connection> {

	private ScrolledComposite scroller;
	private Composite container;

	private List<ColorContainer> entries = new ArrayList<>();
	private Text widthText;
	private String width;
	
	private Text sourceTextText;
	private String sourceText = "";

	private Text targetTextText;
	private String targetText = "";
	
	private Text topTextText;
	private String topText = "";

	private Text bottomTextText;
	private String bottomText = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPartDialog#createWidgetAdrea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createWidgetAdrea(final Composite parent) {
		Label lblWidth = new Label(parent, SWT.NONE);
		lblWidth.setText("Width of the connection");

		widthText = new Text(parent, SWT.BORDER);
		widthText.setText(width);
		widthText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		
		Label lblTextTop = new Label(parent, SWT.NONE);
		lblTextTop.setText("Text at the top of the connection");

		topTextText = new Text(parent, SWT.BORDER);
		topTextText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		topTextText.setText(topText);

		Label lblTextBottom = new Label(parent, SWT.NONE);
		lblTextBottom.setText("Text at the bottom of the connection");

		bottomTextText = new Text(parent, SWT.BORDER);
		bottomTextText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		bottomTextText.setText(bottomText);
		
		
		Label lblTextSource = new Label(parent, SWT.NONE);
		lblTextSource.setText("Text at the source of the connection");

		sourceTextText = new Text(parent, SWT.BORDER);
		sourceTextText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sourceTextText.setText(sourceText);

		Label lblTextTarget= new Label(parent, SWT.NONE);
		lblTextTarget.setText("Text at the target of the connection");

		targetTextText = new Text(parent, SWT.BORDER);
		targetTextText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		targetTextText.setText(targetText);
		
		
		Label lblChooseAnColor = new Label(parent, SWT.NONE);
		lblChooseAnColor.setText("Order-dependent list of colors");

		Button btnBrowseToAdd = new Button(parent, SWT.PUSH);
		btnBrowseToAdd.setText("Add a color...");
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

		for (ColorContainer ce : entries) {
			addColorEntryToContainer(ce);
		}

		btnBrowseToAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {

				ColorSelector colorSelector = new ColorSelector(parent);
				colorSelector.open();
				if (colorSelector.getColorValue() != null) {

					ColorContainer ce = new ColorContainer();
					ce.color = ColorManager.createColor(colorSelector.getColorValue());
					ce.predicate = "true";
					addColorEntryToContainer(ce);
					entries.add(ce);
				}
			}

		});
		relayout(container, scroller);
		return parent;
	}

	private void relayout(Composite container, ScrolledComposite scroller) {
		scroller.setMinHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		container.layout();
		scroller.layout(true);
	}

	private void addColorEntryToContainer(final ColorContainer ce) {
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label imgLabel = new Label(composite, SWT.NONE);
		imgLabel.setBackground(ce.color);
		GridData gd_imgLabel = new GridData(GridData.FILL_HORIZONTAL);
		gd_imgLabel.widthHint = 50;
		gd_imgLabel.grabExcessHorizontalSpace = true;
		imgLabel.setLayoutData(gd_imgLabel);

		final Text predicateText = new Text(composite, SWT.BORDER);
		GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
		gd_dataFolderText.widthHint = 287;
		predicateText.setLayoutData(gd_dataFolderText);
		predicateText.setText(ce.predicate);
		predicateText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				ce.predicate = predicateText.getText();

			}
		});

		ce.composite = composite;

		final Button removeButton = new Button(container, SWT.PUSH);
		removeButton.setText("X");
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ce.composite.dispose();
				entries.remove(ce);
				removeButton.dispose();				
				relayout(container, scroller);
			}
		});
		relayout(container, scroller);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPartDialog#saveValues(de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart)
	 */
	@Override
	public void saveValues(Connection connection) {
		connection.setWidth(Integer.parseInt(widthText.getText()));
		connection.clearColors();
		for (ColorContainer ce : entries) {
			connection.addColor(ce.color, ce.predicate);
		}
		connection.setBottomText(bottomTextText.getText());
		connection.setTargetText(targetTextText.getText());
		connection.setTopText(topTextText.getText());
		connection.setSourceText(sourceTextText.getText());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPartDialog#loadValues(de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart)
	 */
	@Override
	public void loadValues(Connection connection) {
		width = Integer.toString(connection.getWidth());
		for (Pair<Color, TuplePredicate> co : connection.getColorPredicates()) {
			ColorContainer cc = new ColorContainer();
			cc.color = co.getE1();
			cc.predicate = co.getE2().getExpression().getExpressionString();
			this.entries.add(cc);
		}
		bottomText = connection.getBottomText();
		topText = connection.getTopText();
		sourceText = connection.getSourceText();
		targetText = connection.getTargetText();

	}

	private class ColorContainer {
		protected Color color;
		protected String predicate;
		protected Composite composite;

	}

}
