package de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.MarginBorder;

import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;

/**
 * This class defines the layout of an automata diagram.
 * 
 * @author Christian
 */
public class AutomataDiagram extends FreeformLayeredPane {

	// the CEPInstance which holds the currently shown automata
	private CEPInstance instance;

	/**
	 * This is the constructor of this class. It set the LayoutManager and
	 * determines the background color.
	 */
	public AutomataDiagram() {
		this.setLayoutManager(new FreeformLayout());
		this.setBorder(new MarginBorder(5));
		this.setBackgroundColor(ColorConstants.white);
		this.setOpaque(true);
	}

	/**
	 * This method add the AbstractStates and AbstractTransitions to the
	 * diagram.
	 * 
	 * @param instance
	 *            is the class that holds the elements for the automata.
	 */
	public void setContent(CEPInstance instance) {
		this.removeAll();
		this.instance = instance;
		// add the states
		for (AbstractState state : instance.getStateList()) {
			this.add(state);
		}
		// add the transitions
		for (AbstractTransition state : instance.getTransitionList()) {
			this.add(state);
		}
	}

	/**
	 * This is the getter for the CEPInstance of the Automata that is currently
	 * shown.
	 * 
	 * @return
	 */
	public CEPInstance getInstance() {
		return instance;
	}

}