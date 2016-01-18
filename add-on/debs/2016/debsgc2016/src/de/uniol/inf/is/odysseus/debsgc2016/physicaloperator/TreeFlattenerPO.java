package de.uniol.inf.is.odysseus.debsgc2016.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.server.intervalapproach.TIInputStreamSyncArea;

public class TreeFlattenerPO extends AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> implements IProcessInternal<Tuple<ITimeInterval>> {

	// ID pos of the root key in root object
	final private int rootNodeKeyPos;

	// ID pos of the non root key in non root object
	final private int nRootNodeKeyPos;
	// Pos of reference to non root node in non root object
	final private int nRootRefToNRootPos;
	// Pos of reference to root node in non root object
	final private int nRootRefToRootPos;

	// -----------------------------------------------------------

	final protected IInputStreamSyncArea<Tuple<ITimeInterval>> inputStreamSyncArea;

	final private Map<Long, Tuple<ITimeInterval>> roots = new HashMap<>();
	final private Map<Long, List<Tuple<ITimeInterval>>> nodeLists = new HashMap<>();

	final private Map<Long, Long> nodeToRoot = new HashMap<>();

	public TreeFlattenerPO(int rootNodeKeyPos, int nRootNodeKeyPos, int nRootRefToRootPos, int nRootRefToNRootPos) {
		this.rootNodeKeyPos = rootNodeKeyPos;
		this.nRootNodeKeyPos = nRootNodeKeyPos;
		this.nRootRefToRootPos = nRootRefToRootPos;
		this.nRootRefToNRootPos = nRootRefToNRootPos;
		inputStreamSyncArea = new TIInputStreamSyncArea<>();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void process_punctuation_intern(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
		
	}

	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void process_open() throws OpenFailedException {
		roots.clear();
		nodeLists.clear();
		nodeToRoot.clear();
		inputStreamSyncArea.init(this, getSubscribedToSource().size());
	}

	@Override
	protected synchronized void process_next(Tuple<ITimeInterval> object, int port) {
		inputStreamSyncArea.transfer(object, port);
	}
	
	@Override
	public void process_internal(Tuple<ITimeInterval> object, int port) {
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
	
	@Override
	protected void process_done(int port) {
		inputStreamSyncArea.done(port);
	} 
		

	private void cleanup() {
		// TODO Auto-generated method stub

	}

	private void processRoot(Tuple<ITimeInterval> object) {

		Long key = object.getAttribute(rootNodeKeyPos);
		// this must be a new root
		roots.put(key, object);
		// the could already be some children read from the other port
		if (nodeLists.get(key) == null) {
			nodeLists.put(key, new ArrayList<Tuple<ITimeInterval>>());
		}
		createOutput(key, object);
	}

	private void processNonRoot(Tuple<ITimeInterval> object) {
		Long nRootKey = object.getAttribute(nRootNodeKeyPos);
		// two cases
		// 1) a direct reference to a root node
		Long rootNodeRef = object.getAttribute(nRootRefToRootPos);
		if (rootNodeRef == null || rootNodeRef < 0) {
			// 2) a reference to a node
			// in this case there must already be a reference to the root node
			Long nRootNodeRef = object.getAttribute(nRootRefToNRootPos);
			rootNodeRef = nodeToRoot.get(nRootNodeRef);
		}
		nodeToRoot.put(nRootKey, rootNodeRef);
		List<Tuple<ITimeInterval>> l = nodeLists.get(rootNodeRef);
		if (l == null) {
			l = new ArrayList<Tuple<ITimeInterval>>();
			nodeLists.put(rootNodeRef, l);
		}
		l.add(object);

		// REMOVE AGAIN LATER
		object.setAttribute(nRootRefToRootPos, rootNodeRef);
		
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
