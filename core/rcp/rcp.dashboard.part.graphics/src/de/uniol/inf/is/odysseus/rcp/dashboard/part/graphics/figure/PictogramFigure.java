package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

public class PictogramFigure extends Figure {
	private Label label;
	private RectangleFigure rectangle;	

	public PictogramFigure() {
		setLayoutManager(new XYLayout());
		rectangle = new RectangleFigure();
		setOpaque(false);
		add(rectangle);
		label = new Label();			
		add(label);
	}

	public Label getLabel() {
		return label;
	}

	public void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		setConstraint(rectangle, new Rectangle(0, 0, r.width, r.height));
		setConstraint(label, new Rectangle(0, 0, r.width, r.height));
		rectangle.invalidate();
		label.invalidate();
	}	

}
