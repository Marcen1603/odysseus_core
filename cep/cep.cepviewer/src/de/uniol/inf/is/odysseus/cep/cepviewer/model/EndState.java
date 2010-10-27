package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;

/**
 * This class defines an end state.
 * 
 * @author Christian
 */
public class EndState extends State {

	// holds the widget that inherits this state
	private Composite parent;

	// the anchors of this state
	private Anchor inAnchor;
	private Anchor outAnchor;

	/**
	 * This is the constructor
	 * 
	 * @param parent
	 *            is the widget which contains this state.
	 */
	public EndState(Composite parent) {
		this.parent = parent;

		// create an name all anchors
		this.inAnchor = new Anchor(this);
		this.inAnchor.place = new Point(0, 1);
		transTargets.put("in_term", this.inAnchor);
		this.outAnchor = new Anchor(this);
		this.outAnchor.place = new Point(2, 1);
		transSources.put("out_term", this.outAnchor);
	}

	/**
	 * This method paints the shape and the name of the state.
	 * 
	 * @param g
	 *            is the object that allows to draw on a surface.
	 */
	@Override
	public void paintFigure(Graphics g) {
		Rectangle r = bounds;
		Font f = new Font(this.parent.getShell().getDisplay(), "Arial", 15,
				SWT.BOLD | SWT.ITALIC);
		g.setFont(f);
		g.setLineWidth(3);
		g.drawOval(r.x + 1, r.y + 1, r.width - 3, r.height - 3);
		g.drawOval(r.x + 5, r.y + 5, r.width - 11, r.height - 11);

		// paint the name on the location based on its length
		if (name.length() > 2) {
			g.drawText(name, r.x + (r.width / 5), r.y + (r.height / 3));
		} else {
			g.drawText(name, r.x + (r.width / 3), r.y + (r.height / 3));
		}
	}

	/**
	 * This method return the specific anchor
	 * 
	 * @return the in_anchor
	 */
	public Anchor getInAnchor() {
		return this.inAnchor;
	}

	/**
	 * This method return the specific anchor
	 * 
	 * @return the out_anchor
	 */
	public Anchor getOutAnchor() {
		return this.outAnchor;
	}

}