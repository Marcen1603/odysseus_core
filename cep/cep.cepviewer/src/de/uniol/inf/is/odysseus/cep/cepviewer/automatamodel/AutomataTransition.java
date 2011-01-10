package de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel;

import org.eclipse.draw2d.ConnectionRouter;

import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

/**
 * This class defines an transition in an automata.
 * 
 * @author Christian
 */
public class AutomataTransition extends AbstractTransition {

	/**
	 * This is the constructor.
	 */
	public AutomataTransition(Anchor sourceAnchor, Anchor targetAnchor,
			Transition transition, AbstractState nextState) {
		super(sourceAnchor, targetAnchor, transition, nextState);

		// set the connection router which directly connects two the anchors
		setConnectionRouter(ConnectionRouter.NULL);
	}
}