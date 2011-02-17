/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.metamodel.State;

/**
 * This class defines a normal state.
 * 
 * @author Christian
 */
public class AutomataState extends AbstractState {

	// the anchors of this state
	private Anchor ignoreInAnchor;
	private Anchor ignoreOutAnchor;
	private Anchor inAnchor;
	private Anchor outAnchor;
	private Anchor takeInAnchor;
	private Anchor takeOutAnchor;
	
	/**
	 * This is the constructor
	 * 
	 * @param parent
	 *            is the widget which contains this state.
	 */
	public AutomataState(int id, State state, boolean isActive) {
		super(id, state, isActive);
		this.setOpaque(false);
		
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
	 * @param graphic
	 *            is the object that allows to draw on a surface.
	 */
	public void paintFigure(Graphics graphic) {
		Display display = PlatformUI.getWorkbench().getDisplay();
		Rectangle r = bounds;
		Font f = new Font(display, "Arial", 15,
				SWT.BOLD | SWT.ITALIC);
		graphic.setFont(f);
		graphic.setLineWidth(3);
		if(isActive) {
			// if the state is the active one
			graphic.setBackgroundColor(display.getSystemColor(SWT.COLOR_BLUE));
		}
		if(this.state.isAccepting()) {
			// if the state is the end state
			graphic.fillOval(r.x + 5, r.y + 5, r.width - 11, r.height - 11);
			graphic.drawOval(r.x + 5, r.y + 5, r.width - 11, r.height - 11);
		} else {
			// if the state is a normal state
			graphic.fillOval(r.x + 1, r.y + 1, r.width - 3, r.height - 3);
		}
		graphic.drawOval(r.x + 1, r.y + 1, r.width - 3, r.height - 3);
		if(isActive) {
			// if the state is the active one
			graphic.setForegroundColor(display.getSystemColor(SWT.COLOR_WHITE));
		}
		// paint the name on the location based on its length
		if (name.length() < 2) {
			graphic.drawText(name, r.x + (r.width / 5), r.y + (r.height / 3));
		} else {
			graphic.drawText(name, r.x + (r.width / 3), r.y + (r.height / 3));
		}
	}
	
	/**
	 * This method is the getter for the specific anchor.
	 * 
	 * @return the ignore_in_anchor
	 */
	public Anchor getIgnoreInAnchor() {
		return ignoreInAnchor;
	}

	/**
	 * This method is the getter for the specific anchor.
	 * 
	 * @return the ignore_out_anchor
	 */
	public Anchor getIgnoreOutAnchor() {
		return ignoreOutAnchor;
	}

	/**
	 * This method is the getter for the specific anchor.
	 * 
	 * @return the in_anchor
	 */
	public Anchor getInAnchor() {
		return this.inAnchor;
	}

	/**
	 * This method is the getter for the specific anchor.
	 * 
	 * @return the out_anchor
	 */
	public Anchor getOutAnchor() {
		return this.outAnchor;
	}

	/**
	 * This method is the getter for the specific anchor.
	 * 
	 * @return the take_in_anchor
	 */
	public Anchor getTakeInAnchor() {
		return takeInAnchor;
	}

	/**
	 * This method is the getter for the specific anchor.
	 * 
	 * @return the take_out_anchor
	 */
	public Anchor getTakeOutAnchor() {
		return takeOutAnchor;
	}
	
}