package de.uniol.inf.is.odysseus.wrapper.urg.dashboard.part;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;

public class UrgScannConfigurer extends
		AbstractDashboardPartConfigurer<UrgScannPart> {
	private UrgScannPart dashboardPart;

	@Override
	public void init(UrgScannPart dashboardPartToConfigure,
			Collection<IPhysicalOperator> roots) {
		dashboardPart = dashboardPartToConfigure;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createLineColorChooser(topComposite);
		createPointColorChooser(topComposite);
	}

	@Override
	public void dispose() {
	}

	private void createLineColorChooser(final Composite topComposite) {
		createLabel(topComposite, "Lines Color");
		
		final Color col = new Color(topComposite.getShell().getDisplay(),
				intToRgb(dashboardPart.getLineColor()));
		Composite topComposite2 = new Composite(topComposite, SWT.NONE);
		topComposite2.setLayout(new GridLayout(2, false));
		topComposite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final Button colorButton = createButton(topComposite2, "...");
		final Label colorLabel = createColorLabel(topComposite2, col);
		colorButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog dlg = new ColorDialog(topComposite.getShell());
				dlg.setRGB(colorLabel.getBackground().getRGB());
				dlg.setText("Choose a Color");
				RGB rgb = dlg.open();
				if (rgb != null) {
					dashboardPart.setLineColor(rgbToInt(rgb));
					fireListener();
					col.dispose();
					Color col2 = new Color(topComposite.getShell().getDisplay(), rgb);
					colorLabel.setBackground(col2);
				}
			}
		});
	}
	
	private void createPointColorChooser(final Composite topComposite) {
		createLabel(topComposite, "Point Color");
		
		final Color col = new Color(topComposite.getShell().getDisplay(),
				intToRgb(dashboardPart.getPointColor()));
		Composite topComposite2 = new Composite(topComposite, SWT.NONE);
		topComposite2.setLayout(new GridLayout(2, false));
		topComposite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final Button colorButton = createButton(topComposite2, "...");
		final Label colorLabel = createColorLabel(topComposite2, col);
		colorButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog dlg = new ColorDialog(topComposite.getShell());
				dlg.setRGB(colorLabel.getBackground().getRGB());
				dlg.setText("Choose a Color");
				RGB rgb = dlg.open();
				if (rgb != null) {
					dashboardPart.setPointColor(rgbToInt(rgb));
					fireListener();
					col.dispose();
					Color col2 = new Color(topComposite.getShell().getDisplay(), rgb);
					colorLabel.setBackground(col2);
				}
			}
		});
	}

	private static Label createColorLabel(Composite parent, Color color) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(" ");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setBackground(color);
		return label;
	}

	private static void createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}

	private static Button createButton(Composite tableComposite, String text) {
		Button button = new Button(tableComposite, SWT.PUSH);
		button.setText(text);
		return button;
	}

	public static int rgbToInt(RGB rgb) {
		return ((rgb.red & 0xff) << 16) | ((rgb.green & 0xff) << 8)
				| ((rgb.blue & 0xff));
	}

	public static RGB intToRgb(int i) {
		int red = (i >> 16) & 0xff;
		int green = (i >> 8) & 0xff;
		int blue = i & 0xff;
		return new RGB(red, green, blue);
	}
}