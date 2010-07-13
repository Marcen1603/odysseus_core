package de.uniol.inf.is.odysseus.rcp.editor.parts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;

public class OperatorFigure extends Figure implements IFigure {

	private Label label; 
	
	public OperatorFigure() {
		setLayoutManager(new ToolbarLayout());
		
		label = new Label();
		label.setBorder(new LineBorder());
		add(label);
	}
	
	public void setText( String text ) {
		label.setText(text);
		layout();
	}

}
