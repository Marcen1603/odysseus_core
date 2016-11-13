package de.uniol.inf.is.odysseus.incubation.graph.listener;

import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;

/**
 * Interface for graph listeners.
 * 
 * @author Kristian Bruns
 */
public interface IGraphListener {
	void graphDataStructureChange(Graph graph);
}
