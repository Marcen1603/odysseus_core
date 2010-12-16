package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.cep.metamodel.State;

/**
 * This class defines a normal state.
 * 
 * @author Christian
 */
public class AutomataState extends AbstractState {

	// holds the widget that inherits this state
	private Composite parent;

	// the anchors of this state
	private Anchor inAnchor;
	private Anchor outAnchor;
	private Anchor takeInAnchor;
	private Anchor takeOutAnchor;
	private Anchor ignoreInAnchor;
	private Anchor ignoreOutAnchor;
	
	private boolean isActive;
	
	private boolean isEndState;

	/**
	 * This is the constructor
	 * 
	 * @param parent
	 *            is the widget which contains this state.
	 */
	public AutomataState(Composite parent, int id, State state, boolean isActive, boolean isAccepting) {
		super(id, state);
		this.setOpaque(false);
		this.parent = parent;
		this.isActive = isActive;
		this.isEndState = isAccepting;
		
		// create an name all anchors
		this.inAnchor = new Anchor(this);
		this.inAnchor.place = new Point(0, 1);
		transTargets.put("in_term", this.inAnchor);
		this.outAnchor = new Anchor(this);
		this.outAnchor.place = new Point(2, 1);
		transSources.put("out_term", this.outAnchor);
		this.takeInAnchor = new Anchor(this);
		this.takeInAnchor.place = new Point(1, 0);
		transTargets.put("take_in_term", this.takeInAnchor);
		this.takeOutAnchor = new Anchor(this);
		this.takeOutAnchor.place = new Point(1, 0);
		transSources.put("take_out_term", this.takeOutAnchor);
		this.ignoreInAnchor = new Anchor(this);
		this.ignoreInAnchor.place = new Point(1, 2);
		transTargets.put("take_in_term", this.ignoreInAnchor);
		this.ignoreOutAnchor = new Anchor(this);
		this.ignoreOutAnchor.place = new Point(1, 2);
		transSources.put("take_out_term", this.ignoreOutAnchor);
	}

	/**
	 * This method paints the shape and the name of the state.
	 * 
	 * @param grafic
	 *            is the object that allows to draw on a surface.
	 */
	@Override
	public void paintFigure(Graphics grafic) {
		Display display = this.parent.getDisplay();
		Rectangle r = bounds;
		Font f = new Font(display, "Arial", 15,
				SWT.BOLD | SWT.ITALIC);
		grafic.setFont(f);
		grafic.setLineWidth(3);
		if(isActive) {
			grafic.setBackgroundColor(display.getSystemColor(SWT.COLOR_BLUE));
		}
		if(this.isEndState) {
			grafic.fillOval(r.x + 5, r.y + 5, r.width - 11, r.height - 11);
			grafic.drawOval(r.x + 5, r.y + 5, r.width - 11, r.height - 11);
		} else {
			grafic.fillOval(r.x + 1, r.y + 1, r.width - 3, r.height - 3);
		}
		grafic.drawOval(r.x + 1, r.y + 1, r.width - 3, r.height - 3);
		if(isActive) {
			grafic.setForegroundColor(display.getSystemColor(SWT.COLOR_WHITE));
		}
		// paint the name on the location based on its length
		if (name.length() < 2) {
			grafic.drawText(name, r.x + (r.width / 5), r.y + (r.height / 3));
		} else {
			grafic.drawText(name, r.x + (r.width / 3), r.y + (r.height / 3));
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

	/**
	 * This method return the specific anchor
	 * 
	 * @return the take_in_anchor
	 */
	public Anchor getTakeInAnchor() {
		return takeInAnchor;
	}

	/**
	 * This method return the specific anchor
	 * 
	 * @return the take_out_anchor
	 */
	public Anchor getTakeOutAnchor() {
		return takeOutAnchor;
	}

	/**
	 * This method return the specific anchor
	 * 
	 * @return the ignore_in_anchor
	 */
	public Anchor getIgnoreInAnchor() {
		return ignoreInAnchor;
	}

	/**
	 * This method return the specific anchor
	 * 
	 * @return the ignore_out_anchor
	 */
	public Anchor getIgnoreOutAnchor() {
		return ignoreOutAnchor;
	}
	
}