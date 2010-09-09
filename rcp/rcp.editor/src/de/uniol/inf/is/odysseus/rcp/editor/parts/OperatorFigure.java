package de.uniol.inf.is.odysseus.rcp.editor.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;

public class OperatorFigure extends Figure implements IFigure {

	private Label label; 
	
	public OperatorFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		layout.setSpacing(10);
		setLayoutManager(layout);
		
		label = new Label();
		label.setBorder(new LineBorder(2));
		add(label);
	}
	
	public void setText( String text ) {
		label.setText(text);
		layout();
	}

	public void markError() {
		label.setForegroundColor(ColorConstants.red);
	}
	
	public void unmarkError() {
		label.setForegroundColor(ColorConstants.black);
	}
}
