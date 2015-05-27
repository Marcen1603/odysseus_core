package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.condition.datatypes.CounterNode;
import de.uniol.inf.is.odysseus.condition.logicaloperator.RarePatternAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator searches for rare patterns in data. It's especially designed to
 * find rare state sequences. E.g. if state "b" nearly always follows state "a",
 * but very seldom a "c" follows "a". This operator should find the seldom pattern
 * "a" -> "c".
 * 
 * @author Tobias Brandt
 */
@SuppressWarnings("rawtypes")
public class RarePatternPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private CounterNode<T> root;
	private int depthCounter;
	private int maxDepth;
	private CounterNode<T> currentNode;
	private double minRelativeFrequency;
	
	private boolean firstTupleIsRoot;
	private boolean firstTuple;

	public RarePatternPO(RarePatternAO ao) {
		this.root = new CounterNode<T>(null);
		this.maxDepth = ao.getDepth();
		this.minRelativeFrequency = ao.getMinRelativeFrequency();
		this.firstTuple = true;
		this.firstTupleIsRoot = ao.isFirstTupleIsRoot();
	}

	@Override
	protected void process_next(T tuple, int port) {

		if (firstTuple && firstTupleIsRoot) {
			root.setObject(tuple);
			firstTuple = false;
		}
		
		if (firstTupleIsRoot && root.getObject().equals(tuple)) {
			// Start from the root again
			depthCounter = 0;
		}
		
		// Put the tuple into the tree
		if (depthCounter == 0) {
			// Start again from root
			currentNode = root.addChild(tuple);
			depthCounter++;
		} else {
			currentNode = currentNode.addChild(tuple);
			depthCounter++;
			if (depthCounter >= maxDepth) {
				depthCounter = 0;
			}
		}

		double relativeFrequencyOfPath = currentNode.calcRelativeFrequencyPath();
		if (relativeFrequencyOfPath < minRelativeFrequency) {
			// In this case, it was a seldom pattern
			String path = getPath(currentNode);
			Tuple newTuple = tuple.append(1.0 - relativeFrequencyOfPath).append(relativeFrequencyOfPath).append(path);
			transfer(newTuple);
			return;
		}
	}

	/**
	 * Creates a string that shows the path from the root to the given node.
	 * 
	 * @param node
	 *            The node that is the end of the path
	 * @return A string that shows the path from root to the given node
	 */
	private String getPath(CounterNode node) {

		// Collect nodes to get correct order
		List<String> nodes = new ArrayList<String>();
		while (node != null && node != root) {
			nodes.add(node.toString());
			node = node.getParent();
		}

		String path = "";
		String[] nodeString = nodes.toArray(new String[1]);
		for (int i = nodeString.length - 1; i >= 0; i--) {
			path += nodeString[i];
			if (i != 0)
				path += " -> ";
		}

		return path;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
}
