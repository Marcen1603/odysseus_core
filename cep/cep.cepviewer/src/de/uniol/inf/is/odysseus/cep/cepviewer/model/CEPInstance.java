package de.uniol.inf.is.odysseus.cep.cepviewer.model;

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

public class CEPInstance {
	
	private StateMachineInstance<?> instance;
	private ArrayList<AbstractState> stateList;
	private ArrayList<AbstractTransition> transitionList;
	private ArrayList<AbstractState> finalStateList;
	private AutomataState currentState;
	private CEPStatus status;
	private Image image;
	
	public CEPInstance(StateMachineInstance<?> instance) {
		this.instance = instance;
		this.stateList = new ArrayList<AbstractState>();
		this.transitionList = new ArrayList<AbstractTransition>();
		this.finalStateList = new ArrayList<AbstractState>();
		Bundle bundle = Activator.getDefault().getBundle();
		if(instance.getCurrentState().isAccepting()) {
			this.status = CEPStatus.FINISHED;
			this.image = ImageDescriptor.createFromURL(bundle.getEntry(StringConst.PATH_TO_FINISHED_IMAGE)).createImage();
		} else {
			this.status = CEPStatus.RUNNING;
			this.image = ImageDescriptor.createFromURL(bundle.getEntry(StringConst.PATH_TO_RUNNING_IMAGE)).createImage();
		}
		this.createAutomata(instance);
	}
	
	public void createAutomata(StateMachineInstance<?> instance) {
		// create the initial state of the automata
		AutomataState state = this.createState(instance.getStateMachine()
				.getInitialState());
		this.createTransitions(state);
	}
	
	private void createTransitions(AutomataState automataState) {
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
				this.createTransitions(newState);
			}
		}
	}

	private AutomataState createState(State state) {
		AutomataState newState;
		if(this.instance.getCurrentState().equals(state)){
			newState = new AutomataState(this.stateList.size(), state, true);
			this.currentState = newState;
		} else {
			newState = new AutomataState(this.stateList.size(), state, false);
		}
		if(state.isAccepting()) {
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
	
	public void currentStateChanged() {
		this.currentState.setActive(false);
		for(AbstractState astate : this.stateList) {
			if(astate.getState().equals(this.instance.getCurrentState())) {
				this.currentState = (AutomataState)astate;
				this.currentState.setActive(true);
			}
		}	
	}
	
	public AutomataState getCurrentState() {
		return this.currentState;
	}
	
	public long getStartTimestamp() {
		return this.instance.getStartTimestamp();
	}
	
	public StateMachine<?> getStateMachine() {
		return this.instance.getStateMachine();
	}

	public StateMachineInstance<?> getInstance() {
		return instance;
	}

	public void setInstance(StateMachineInstance<?> instance) {
		this.instance = instance;
	}

	public CEPStatus getStatus() {
		return status;
	}

	public void setStatus(CEPStatus status) {
		Bundle bundle = Activator.getDefault().getBundle();
		if(status.equals(CEPStatus.FINISHED)) {
			this.image = ImageDescriptor.createFromURL(bundle.getEntry(StringConst.PATH_TO_FINISHED_IMAGE)).createImage();
		} else if(status.equals(CEPStatus.ABORTED)){
			this.image = ImageDescriptor.createFromURL(bundle.getEntry(StringConst.PATH_TO_ABORTED_IMAGE)).createImage();
		}
		this.status = status;
	}

	public ArrayList<AbstractState> getStateList() {
		return stateList;
	}

	public ArrayList<AbstractTransition> getTransitionList() {
		return transitionList;
	}

	public ArrayList<AbstractState> getFinalStateList() {
		return finalStateList;
	}

	public Image getImage() {
		return image;
	}
	

}
