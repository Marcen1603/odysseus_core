/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import java.util.ArrayList;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.geometry.Point;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.IntConst;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

/**
 * This class defines an transition loop in an automata.
 * 
 * @author Christian
 */
public class TransitionLoop extends AbstractTransition {

	// a list of all Bendpoints
	private ArrayList<AbsoluteBendpoint> bendpointList;
	// a list of the Bendpoint position relative to the state
	private ArrayList<Point> pointList;
	// the AbstractState which is the source/target of this transition
	private AutomataState state;

	/**
	 * This is the constructor.
	 * 
	 * @param state
	 *            is the state which this loop belongs to.
	 * @param eAction
	 *            defines the layout of the loop
	 */
	public TransitionLoop(Anchor sourceAnchor, Anchor targetAnchor,
			Transition transition, AutomataState state) {
		super(sourceAnchor, targetAnchor, transition, state);
		this.state = state;
		this.pointList = createLocationPoints();
		// set the connection router to connect two anchors via some bend
		// points
		this.setConnectionRouter(new BendpointConnectionRouter());
		this.bendpointList = new ArrayList<AbsoluteBendpoint>();
		for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
			this.bendpointList.add(new AbsoluteBendpoint(new Point(0, 0)));
		}
		this.setLocations();
		// add the bend points to the connection
		this.setRoutingConstraint(bendpointList);
	}

	/**
	 * This method creates the location points for the Bendpoints
	 * 
	 * @return a list of loaction points
	 */
	private static ArrayList<Point> createLocationPoints() {
		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
			points.add(new Point((IntConst.STATE_SIZE * i / 2),
					IntConst.Y_SPACE_BETWEEN_BENDPOINTS * (1 + i % 2)));
		}
		return points;
	}

	/**
	 * This method is called, if the AbstractState is dragged to another
	 * position within the diagram to update the positions of the Bendpoints.
	 */
	@Override
    public void repaint() {
//		super.repaint();
		if (this.bendpointList != null) {
			this.setLocations();
		}
	}

	/**
	 * This method computes the new location of the Bendpoints relativly to the
	 * AbstracteState.
	 */
	private void setLocations() {
		if (this.transition.getAction() == EAction.consumeBufferWrite) {
			for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
				this.bendpointList.get(i).setLocation(
						this.state.getLocation().x + this.pointList.get(i).x,
						this.state.getLocation().y - this.pointList.get(i).y);
			}
		} else if (this.transition.getAction() == EAction.discard) {
			for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
				this.bendpointList.get(i).setLocation(
						this.state.getLocation().x + this.pointList.get(i).x,
						this.state.getLocation().y + this.pointList.get(i).y
								+ IntConst.STATE_SIZE);
			}
		}
	}

}