package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import java.util.Hashtable;

import org.eclipse.draw2d.Figure;

/**
 * This abstract class defines a state.
 * 
 * @author Christian
 */
public abstract class State extends Figure {

	// contains the anchors that are used as a source for a transition
	protected Hashtable<String, Anchor> transTargets = new Hashtable<String, Anchor>();

	// contains the anchors that are used as a target for a transition
	protected Hashtable<String, Anchor> transSources = new Hashtable<String, Anchor>();

	// holds the name of a state
	protected String name;

	/**
	 * This constant holds the size of a state.
	 */
	public static final int SIZE = 65;

	/**
	 * This method sets the name of a state.
	 * 
	 * @param name
	 *            is the new name 
	 */
	public void setText(String name) {
		this.name = name;
		repaint();
	}
	
}