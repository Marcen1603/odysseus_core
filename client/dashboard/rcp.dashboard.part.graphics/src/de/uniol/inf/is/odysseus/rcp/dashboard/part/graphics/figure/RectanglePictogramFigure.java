package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.RectanglePictogram;

public class RectanglePictogramFigure extends AbstractPictogramFigure<RectanglePictogram> {

	private boolean visibile = true;
	private Color color;
	private int width = 3;

	@Override
	public void updateValues(RectanglePictogram node) {
		this.visibile = node.isVisibile();
		this.color = node.getCurrentColor();
		this.width = node.getWidth();

	}

	@Override
	public void paintGraphic(Graphics g) {
		Rectangle r = getContentBounds().getCopy();		
		g.setForegroundColor(color);
		g.setLineWidth(this.width);
		if (this.visibile) {
			g.drawRectangle(r.x, r.y, r.width-1, r.height);
		}

	}

	@Override
	public Dimension getContentSize() {
		return getPreferredSize();
	}

}
