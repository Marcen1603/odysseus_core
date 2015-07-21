package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.condition.datatypes.CounterNode;
import de.uniol.inf.is.odysseus.condition.logicaloperator.RareSequenceAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator searches for rare sequences in data. It's especially designed to
 * find rare state sequences. E.g. if state "b" nearly always follows state "a",
 * but very seldom a "c" follows "a". This operator should find the seldom
 * pattern "a" -> "c".
 * 
 * @author Tobias Brandt
 */
@SuppressWarnings("rawtypes")
public class RareSequencePO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private CounterNode root;
	private int depthCounter;
	private int maxDepth;
	private CounterNode currentNode;
	private double minRelativeFrequencyPath;
	private double minRelativeFrequencyNode;

	private boolean firstTupleIsRoot;
	private boolean firstTuple;

	private static final int DATA_PORT = 0;
	private static final int BACKUP_PORT = 1;

	private static final String BACKUP_ATTRIBUTE_TREE = "tree";
	private static final String BACKUP_ATTRIBUTE_ID = "backupId";
	private Gson gson;
	private String uniqueBackupId;

	public RareSequencePO(RareSequenceAO ao) {
		this.root = new CounterNode(null);
		this.maxDepth = ao.getDepth();
		this.minRelativeFrequencyPath = ao.getMinRelativeFrequencyPath();
		this.minRelativeFrequencyNode = ao.getMinRelativeFrequencyNode();
		this.firstTuple = true;
		this.firstTupleIsRoot = ao.isFirstTupleIsRoot();
		this.gson = new Gson();
		this.uniqueBackupId = ao.getUniqueBackupId();
	}

	@Override
	protected void process_next(T tuple, int port) {

		if (port == DATA_PORT) {
			if (firstTuple && firstTupleIsRoot) {
				root.setObject(tuple.getAttributes());
				firstTuple = false;
			}

			if (firstTupleIsRoot && Arrays.equals(root.getObject(), tuple.getAttributes())) {
				// Start from the root again
				depthCounter = 0;
			}

			// Put the tuple into the tree
			if (depthCounter == 0) {
				// Start again from root
				currentNode = root.addChild(tuple.getAttributes());
				depthCounter++;
			} else {
				currentNode = currentNode.addChild(tuple.getAttributes());
				depthCounter++;
				if (depthCounter >= maxDepth) {
					depthCounter = 0;
				}
			}

			// Backup the tree
			String tree = gson.toJson(root);
			// Transfer learned tree to backup
			Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(2, false);
			output.setMetadata(tuple.getMetadata());
			output.setAttribute(0, tree);
			output.setAttribute(1, this.uniqueBackupId);
			transfer(output, BACKUP_PORT);

			double relativeFrequencyOfPath = currentNode.calcRelativeFrequencyPath();
			boolean hasLowNode = currentNode.pathHasSeldomNode(this.minRelativeFrequencyNode);
			if (relativeFrequencyOfPath < minRelativeFrequencyPath || hasLowNode) {
				// In this case, it was a seldom pattern
				String path = getPath(currentNode);
				Tuple newTuple = tuple.append(1.0 - relativeFrequencyOfPath).append(relativeFrequencyOfPath)
						.append(path);
				transfer(newTuple);
			}
		} else if (port == BACKUP_PORT) {
			// Get backup data
			String backupId = getBackupString(tuple, BACKUP_ATTRIBUTE_ID);
			// Check, if this data is meant for this operator
			if (backupId.equals(this.uniqueBackupId)) {
				String treeJson = getBackupString(tuple, BACKUP_ATTRIBUTE_TREE);
				if (!treeJson.isEmpty()) {
					CounterNode newRoot = gson.fromJson(treeJson, CounterNode.class);
					setParents(newRoot);
					this.root = newRoot;
				}
			}
		}
	}

	/**
	 * As the parents can't be serialized, they will be set again recursively
	 * 
	 * @param root
	 *            The root to start with
	 */
	private void setParents(CounterNode root) {
		for (CounterNode node : root.getChildren()) {
			node.setParent(root);
			setParents(node);
		}
	}

	/**
	 * Searches for the backup string and returns it
	 * 
	 * @param tuple
	 *            The tuple that holds the backup string
	 * @param attribute
	 *            The attribute to search for
	 * @return the string from the backup tuple
	 */
	private String getBackupString(T tuple, String attribute) {
		int valueIndex = getInputSchema(BACKUP_PORT).findAttributeIndex(attribute);
		if (valueIndex >= 0) {
			String treeJson = tuple.getAttribute(valueIndex);
			return treeJson;
		}
		return "";
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
