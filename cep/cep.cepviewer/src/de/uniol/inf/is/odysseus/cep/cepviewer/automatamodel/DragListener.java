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

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPStateView;

/**
 * This class defines the drag and drop listener used by the automata view.
 * 
 * @author Christian
 */
public class DragListener extends MouseMotionListener.Stub implements
		MouseListener {

	// is the state that is currently draged
	private AbstractState draggingState;
	// is the initial point for calculating the new position
	private Point initialPoint;

	/**
	 * This is the constructor.
	 * 
	 * @param state
	 *            is the state which should feature drag and drop
	 */
	public DragListener(AbstractState state) {
		state.addMouseMotionListener(this);
		state.addMouseListener(this);
	}

	/**
	 * This method is called if the mouse button was clicked.
	 * 
	 * @param event
	 *            is the mouse event which happened
	 */
	public void mouseClicked(MouseEvent event) {
		// do nothing
	}

	/**
	 * This method is called if the mouse button was clicked two times.
	 * 
	 * @param event
	 *            is the mouse event which happened
	 */
	@Override
    public void mouseDoubleClicked(MouseEvent event) {
		// do nothing
	}

	/**
	 * This method is called if the mouse button is pressed and the mouse is
	 * dragged.
	 * 
	 * @param event
	 *            is the mouse event which happened
	 */
	@Override
    public void mouseDragged(MouseEvent event) {
		// null check to avoid exception
		if (this.initialPoint != null) {
			// calculate the new position of the state
			Point newPoint = event.getLocation();
			Dimension d = newPoint.getDifference(this.initialPoint);
			this.initialPoint = newPoint;
			// set new location of the state
			this.draggingState.setBounds(this.draggingState.getBounds()
					.getTranslated(d.width, d.height));
		}
	}

	/**
	 * This method is called if the mouse button is pressed.
	 * 
	 * @param event
	 *            is the mouse event which happened
	 */
	@Override
    public void mousePressed(MouseEvent event) {
		// If the is no state registered to be dragged, register it.
		if (initialPoint == null) {
			this.initialPoint = event.getLocation();
			this.draggingState = (AbstractState) event.getSource();
		}
	}

	/**
	 * This method is called if the mouse button was released.
	 * 
	 * @param event
	 *            is the mouse event which happened
	 */
	@Override
    public void mouseReleased(MouseEvent event) {
		// delete
		this.initialPoint = null;
		this.draggingState = null;
		for (IViewReference reference : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
			if (reference.getId().equals(CEPStateView.ID)) {
				if (event.getSource() instanceof AbstractState) {
					AbstractState state = (AbstractState) event.getSource();
					((CEPStateView) reference.getView(false)).setContent(
							(AbstractState) event.getSource(),
							((AutomataDiagram) state.getParent()).getInstance()
									.getTransitionList());
					break;
				}
			}
		}
	}
}