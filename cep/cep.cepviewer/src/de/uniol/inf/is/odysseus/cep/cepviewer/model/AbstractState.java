package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import java.util.Hashtable;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.metamodel.State;

/**
 * This abstract class defines a state within the diagram.
 * 
 * @author Christian
 */
public abstract class AbstractState extends Figure {

	// contains the anchors that are used as a source for a transition
	protected Hashtable<String, Anchor> transTargets = new Hashtable<String, Anchor>();
	// contains the anchors that are used as a target for a transition
	protected Hashtable<String, Anchor> transSources = new Hashtable<String, Anchor>();
	// the corresponding state in a StateMachine
	protected State state;
	// the label of the state
	protected String name;
	// is the state active
	protected boolean isActive;

	/**
	 * This is the constructor of this class
	 * 
	 * @param id
	 *            is the id of this state
	 * @param state
	 *            is the corresponding state in a StateMachine
	 */
	public AbstractState(int id, State state, boolean isActive) {
		this.state = state;
		this.name = StringConst.STATE_LABEL_PREFIX + id;
		this.isActive = isActive;
		this.setToolTip(new Label("State " + this.name));
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
	 * This is the getter of the corresponding state.
	 * 
	 * @return the corresponding state of a StateMachine
	 */
	public State getState() {
		return this.state;
	}
	
	/**
	 * This method returns if the state is active.
	 * 
	 * @return true if the state is active, else false
	 */
	public boolean isActive() {
		return this.isActive;
	}
}