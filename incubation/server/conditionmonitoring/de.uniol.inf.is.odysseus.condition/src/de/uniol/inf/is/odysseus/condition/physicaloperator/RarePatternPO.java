package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.condition.datatypes.CounterNode;
import de.uniol.inf.is.odysseus.condition.logicaloperator.RarePatternAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

@SuppressWarnings("rawtypes")
public class RarePatternPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private CounterNode<T> root;
	private int depthCounter;
	private int maxDepth;
	private CounterNode<T> currentNode;

	public RarePatternPO(RarePatternAO ao) {
		root = new CounterNode<T>(null);
		maxDepth = 2;
	}

	@Override
	protected void process_next(T tuple, int port) {

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
		if (relativeFrequencyOfPath < 0.3) {
			String path = getPath(currentNode);
			Tuple newTuple = tuple.append(1.0 - relativeFrequencyOfPath).append(path);
			transfer(newTuple);
			return;
		}
	}

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
		return OutputMode.INPUT;
	}
}
