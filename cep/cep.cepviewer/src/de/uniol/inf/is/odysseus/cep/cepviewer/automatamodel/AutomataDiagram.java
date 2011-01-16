package de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.MarginBorder;

import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;

/**
 * This class defines the layout of an automata diagram.
 * 
 * @author Christian
 */
public class AutomataDiagram extends FreeformLayeredPane {

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

	public CEPInstance getInstance() {
		return instance;
	}

}