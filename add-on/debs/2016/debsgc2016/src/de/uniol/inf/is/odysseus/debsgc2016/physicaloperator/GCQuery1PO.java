package de.uniol.inf.is.odysseus.debsgc2016.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class GCQuery1PO extends AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> {

	// ID pos of the root key in root object
	final private int rootNodeKeyPos = 1;

	// ID pos of the non root key in non root object
	final private int nRootNodeKeyPos = 1;
	// Pos of reference to non root node in non root object
	final private int nRootNodeNodeKeyPos = 5;
	// Pos of reference to root node in non root object
	final private int nRootNodeRootKeyPos = 6;

	// -----------------------------------------------------------

	final private Map<Long, Tuple<ITimeInterval>> roots = new HashMap<>();
	final private Map<Long, List<Tuple<ITimeInterval>>> nodeLists = new HashMap<>();

	final private Map<Long, Long> nodeToRoot = new HashMap<>();

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		roots.clear();
		nodeLists.clear();
		nodeToRoot.clear();
	}

	@Override
	protected synchronized void process_next(Tuple<ITimeInterval> object, int port) {
		cleanup();
		switch (port) {
		case 0:
			processRoot(object);
			break;
		case 1:
			processNonRoot(object);
			break;
		default:

		}
	}

	private void cleanup() {
		// TODO Auto-generated method stub

	}

	private void processRoot(Tuple<ITimeInterval> object) {
		Long key = object.getAttribute(rootNodeKeyPos);
		// this must be a new root
		roots.put(key, object);
		nodeLists.put(key, new ArrayList<Tuple<ITimeInterval>>());
		createOutput(key, object);
	}

	private void processNonRoot(Tuple<ITimeInterval> object) {
		Long nRootKey = object.getAttribute(nRootNodeKeyPos);
		// two cases
		// 1) a direct reference to a root node
		Long rootNodeRef = object.getAttribute(nRootNodeRootKeyPos);
		if (rootNodeRef == null || rootNodeRef < 0) {
			// 2) a reference to a node
			// in this case there must already be a reference to the root node
			Long nRootNodeRef = object.getAttribute(nRootNodeNodeKeyPos);
			rootNodeRef = nodeToRoot.get(nRootNodeRef);
		}
		nodeToRoot.put(nRootKey, rootNodeRef);
		List<Tuple<ITimeInterval>> l = nodeLists.get(rootNodeRef);
		if (l == null){
			l = new ArrayList<Tuple<ITimeInterval>>();
			nodeLists.put(rootNodeRef, l);
		}
		l.add(object);

		createOutput(rootNodeRef, object);
	}

	private void createOutput(Long key, Tuple<ITimeInterval> object) {
		Tuple<ITimeInterval> root = roots.get(key);
		// Could be a node for a root not seen
		if (root != null) {
			List<Tuple<ITimeInterval>> outList = new ArrayList<>(nodeLists.get(key));
			// Transfer only object with at leat one node
			if (outList != null && outList.size() > 0) {
				Tuple<ITimeInterval> out = new Tuple<>(root);
				out = out.append(outList);
				out.setMetadata(object.getMetadata().clone());
				transfer(out);
			}
		}
	}

}
