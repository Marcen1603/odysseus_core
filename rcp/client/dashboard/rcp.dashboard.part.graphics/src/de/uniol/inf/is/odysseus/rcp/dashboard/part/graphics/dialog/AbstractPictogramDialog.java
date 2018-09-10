package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;

public abstract class AbstractPictogramDialog<T extends AbstractPictogram> extends AbstractPartDialog<T> {

	
	private Text topTextText;
	private String topText = "";
	
	private Text middleTextText;
	private String middleText = "";

	private Text bottomTextText;
	private String bottomText = "";

	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPartDialog#init(de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart)
	 */
	@Override
	public void init(T part) {	
		super.init(part);
		bottomText = part.getTextBottom();
		topText = part.getTextTop();
		middleText = part.getTextMiddle();
	}
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPartDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Composite createDialogArea(Composite parent) {
		Composite container = super.createDialogArea(parent);
		Label lblTextTop = new Label(container, SWT.NONE);
		lblTextTop.setText("Text at the top of the figure");

		topTextText = new Text(container, SWT.BORDER);
		topTextText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		topTextText.setText(topText);
		
		Label lblTextMiddle = new Label(container, SWT.NONE);
		lblTextMiddle.setText("Text at the middle of the figure");

		middleTextText = new Text(container, SWT.BORDER);
		middleTextText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		middleTextText.setText(middleText);

		Label lblTextBottom = new Label(container, SWT.NONE);
		lblTextBottom.setText("Text at the bottom of the figure");

		bottomTextText = new Text(container, SWT.BORDER);
		bottomTextText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		bottomTextText.setText(bottomText);
		return parent;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPartDialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		getPart().setTextBottom(bottomTextText.getText());
		getPart().setTextMiddle(middleTextText.getText());
		getPart().setTextTop(topTextText.getText());
		super.okPressed();
	}
}