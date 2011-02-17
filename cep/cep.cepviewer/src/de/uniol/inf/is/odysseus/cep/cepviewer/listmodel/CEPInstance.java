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
package de.uniol.inf.is.odysseus.cep.cepviewer.listmodel;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.cep.cepviewer.Activator;
import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AbstractState;
import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AbstractTransition;
import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AutomataState;
import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AutomataTransition;
import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.DragListener;
import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.TransitionLoop;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.IntConst;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

/**
 * This class defines a CEPInstance which is added to the TreeViewer of all
 * lists within the CEPViewer.
 * 
 * @author Christian
 */
public class CEPInstance {

	// the instance which a instance of this class represents
	private StateMachineInstance<?> instance;
	// holds the states of the automata diagram
	private ArrayList<AbstractState> stateList;
	// holds the transitions of the automata diagram
	private ArrayList<AbstractTransition> transitionList;
	// holds the final states of the automata diagram
	private ArrayList<AbstractState> finalStateList;
	// holds the current states of the automata diagram
	private AutomataState currentState;
	// holds the status of the instance
	private CEPStatus status;
	// the image of the current status
	private Image image;

	/**
	 * This is the constructor of this class.
	 * 
	 * @param instance
	 *            the StateMachineInstance an instance of this class represents
	 */
	public CEPInstance(StateMachineInstance<?> instance) {
		this.instance = instance;
		// create the fields
		this.stateList = new ArrayList<AbstractState>();
		this.transitionList = new ArrayList<AbstractTransition>();
		this.finalStateList = new ArrayList<AbstractState>();
		// set the current status and status image
		Bundle bundle = Activator.getDefault().getBundle();
		if (instance.getCurrentState().isAccepting()) {
			this.status = CEPStatus.FINISHED;
			this.image = ImageDescriptor.createFromURL(
					bundle.getEntry(StringConst.PATH_TO_FINISHED_IMAGE))
					.createImage();
		} else {
			this.status = CEPStatus.RUNNING;
			this.image = ImageDescriptor.createFromURL(
					bundle.getEntry(StringConst.PATH_TO_RUNNING_IMAGE))
					.createImage();
		}
	}

	/**
	 * This method is called if an entrywithin the CEPListView is selected. If
	 * its the first time this method is called, the automata diagram will be
	 * created, else it does nothing.
	 */
	public void createAutomata() {
		if (this.stateList.isEmpty()) {
			// create the initial state of the automata
			AutomataState state = this.createState(instance.getStateMachine()
					.getInitialState());
			this.createTransition(state);
		}
	}

	/**
	 * This method creates an transition of the automata diagram.
	 * 
	 * @param automataState
	 *            is the source state of the transition
	 */
	private void createTransition(AutomataState automataState) {
		for (Transition nextTrans : automataState.getState().getTransitions()) {
			if (nextTrans.getNextState().equals(automataState.getState())) {
				// if the transition is a loop ...
				TransitionLoop loop = null;
				if (nextTrans.getAction() == EAction.consumeBufferWrite) {
					// if the action of the transition is to consume an event
					loop = new TransitionLoop(automataState.getTakeOutAnchor(),
							automataState.getTakeInAnchor(), nextTrans,
							automataState);
				} else if (nextTrans.getAction() == EAction.discard) {
					// if the action of the transition is to discard an event
					loop = new TransitionLoop(
							automataState.getIgnoreOutAnchor(),
							automataState.getIgnoreInAnchor(), nextTrans,
							automataState);
				}
				// add the loop to the diagram
				this.transitionList.add(loop);
			} else {
				// if the transition is no loop create its end-state
				AutomataState newState = createState(nextTrans.getNextState());
				AutomataTransition path = new AutomataTransition(
						automataState.getOutAnchor(), newState.getInAnchor(),
						nextTrans, newState);
				// add the transition to the diagramm
				this.transitionList.add(path);
				this.createTransition(newState);
			}
		}
	}

	/**
	 * This method create an state of the automata diagram and returns it.
	 * 
	 * @param state
	 *            is a State object from the metamodel of the represented
	 *            instance
	 * 
	 * @return the created AutomataState object
	 */
	private AutomataState createState(State state) {
		AutomataState newState;
		if (this.instance.getCurrentState().equals(state)) {
			newState = new AutomataState(this.stateList.size(), state, true);
			this.currentState = newState;
		} else {
			newState = new AutomataState(this.stateList.size(), state, false);
		}
		if (state.isAccepting()) {
			this.finalStateList.add(newState);
		}
		// set location in the diagram
		newState.setBounds(new Rectangle(IntConst.X_SPACE_BETWEEN_STATES
				* (this.stateList.size() + 1),
				(IntConst.Y_GAP_TO_TOP - IntConst.STATE_SIZE) / 2,
				IntConst.STATE_SIZE, IntConst.STATE_SIZE));
		new DragListener(newState);
		this.stateList.add(newState);
		return newState;
	}

	/**
	 * This method changed the current state of the automata diagram
	 */
	public void currentStateChanged() {
		if (this.currentState != null) {
			this.currentState.setActive(false);
			for (AbstractState astate : this.stateList) {
				if (astate.getState().equals(this.instance.getCurrentState())) {
					this.currentState = (AutomataState) astate;
					this.currentState.setActive(true);
				}
			}
		}
	}

	/**
	 * This method changes the status and the status image of a instance of this
	 * class.
	 * 
	 * @param status
	 *            is the new status
	 */
	public void setStatus(CEPStatus status) {
		Bundle bundle = Activator.getDefault().getBundle();
		if (status.equals(CEPStatus.FINISHED)) {
			this.image = ImageDescriptor.createFromURL(
					bundle.getEntry(StringConst.PATH_TO_FINISHED_IMAGE))
					.createImage();
		} else if (status.equals(CEPStatus.ABORTED)) {
			this.image = ImageDescriptor.createFromURL(
					bundle.getEntry(StringConst.PATH_TO_ABORTED_IMAGE))
					.createImage();
		}
		this.status = status;
	}

	/**
	 * This is the getter for the current state.
	 * 
	 * @return the current AutomataState
	 */
	public AutomataState getCurrentState() {
		return this.currentState;
	}

	/**
	 * This is the getter for the time stamp the represented instance was
	 * created.
	 * 
	 * @return the time stamp the representedinstance was created
	 */
	public long getStartTimestamp() {
		return this.instance.getStartTimestamp();
	}

	/**
	 * This is the getter for the StateMachine of the represented instance.
	 * 
	 * @return the StateMachine of the represented instance
	 */
	public StateMachine<?> getStateMachine() {
		return this.instance.getStateMachine();
	}

	/**
	 * This is the getter for the represented instance.
	 * 
	 * @return the represented instance
	 */
	public StateMachineInstance<?> getInstance() {
		return instance;
	}

	/**
	 * This is the getter for the status of the instance.
	 * 
	 * @return the status of the instance
	 */
	public CEPStatus getStatus() {
		return status;
	}

	/**
	 * This is the getter for the list of states within the automata diagram.
	 * 
	 * @return the list of states
	 */
	public ArrayList<AbstractState> getStateList() {
		return stateList;
	}

	/**
	 * This is the getter for the list of transitions within the automata diagram.
	 * 
	 * @return the list of transitions
	 */
	public ArrayList<AbstractTransition> getTransitionList() {
		return transitionList;
	}

	/**
	 * This is the getter for the list of final states within the automata diagram.
	 * 
	 * @return the list of final states
	 */
	public ArrayList<AbstractState> getFinalStateList() {
		return finalStateList;
	}

	/**
	 * This is the getter for the status image
	 * 
	 * @return the status image
	 */
	public Image getImage() {
		return image;
	}

}
