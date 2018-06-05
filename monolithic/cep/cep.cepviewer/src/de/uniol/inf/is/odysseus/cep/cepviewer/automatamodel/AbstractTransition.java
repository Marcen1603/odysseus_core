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

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;

import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

/**
 * This abstract class defines a transition within the diagram.
 * 
 * @author Christian
 */
public class AbstractTransition extends PolylineConnection {

	// the label of this transition
	protected String name;
	// the next State in the automata
	protected AbstractState nextState;
	// the corresponding transition within a StateMachine
	protected Transition transition;

	/**
	 * This is the constructor of this class. IT sets the source and target
	 * Anchor, determines the layout of the transition and sets the tooltip
	 * 
	 * @param sourceAnchor
	 *            is the anchor of the start state
	 * @param targetAnchor
	 *            is the anchor of the end state
	 * @param transition
	 *            ins the corresponding transition within a StateMachine
	 */
	public AbstractTransition(Anchor sourceAnchor, Anchor targetAnchor,
			Transition transition, AbstractState nextState) {
		this.name = transition.getCondition().getLabel();
		this.nextState = nextState;
		this.transition = transition;
		this.setSourceAnchor(sourceAnchor);
		this.setTargetAnchor(targetAnchor);
		// draw the transition as a line with an arrow pointing to the end state
		setTargetDecoration(new PolylineDecoration());
		this.setToolTip(new Label(transition.getCondition().getLabel() + " / "
				+ transition.getAction().toString()));
	}

	/**
	 * This is the getter of the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * This is the getter for the AbstractState which is the target of this
	 * transition.
	 * 
	 * @return the targeted AbstractState
	 */
	public AbstractState getNextState() {
		return nextState;
	}

	/**
	 * This is the getter for the represented transition.
	 * 
	 * @return the represented transition
	 */
	public Transition getTransition() {
		return transition;
	}

}