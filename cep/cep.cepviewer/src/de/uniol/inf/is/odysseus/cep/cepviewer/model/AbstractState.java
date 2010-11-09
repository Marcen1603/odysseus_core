package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import java.util.Hashtable;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;

import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.State;

/**
 * This abstract class defines a state.
 * 
 * @author Christian
 */
public abstract class AbstractState extends Figure {

	// contains the anchors that are used as a source for a transition
	protected Hashtable<String, Anchor> transTargets = new Hashtable<String, Anchor>();

	// contains the anchors that are used as a target for a transition
	protected Hashtable<String, Anchor> transSources = new Hashtable<String, Anchor>();
	
	protected State state;
	protected String name;
	
	/**
	 * This constant holds the size of a state.
	 */
	public static final int SIZE = 65;
	
	public AbstractState(String name, State state) {
		this.state = state;
		this.name = "S" +this.state.getId();
		this.setToolTip(new Label("State " + name));
	}

	public String getName() {
		return name;
	}
}