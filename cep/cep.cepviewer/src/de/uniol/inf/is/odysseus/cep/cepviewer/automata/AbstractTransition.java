package de.uniol.inf.is.odysseus.cep.cepviewer.automata;

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

	// the corresponding transition within a StateMachine
	protected Transition transition;
	// the label of this transition
	protected String name;
	// the next State in the automata
	protected AbstractState nextState;

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

	public Transition getTransition() {
		return transition;
	}

	public AbstractState getNextState() {
		return nextState;
	}

}