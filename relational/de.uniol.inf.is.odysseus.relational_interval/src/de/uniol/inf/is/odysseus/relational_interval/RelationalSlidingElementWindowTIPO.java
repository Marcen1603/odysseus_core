package de.uniol.inf.is.odysseus.relational_interval;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;

public class RelationalSlidingElementWindowTIPO extends
		SlidingElementWindowTIPO<RelationalTuple<ITimeInterval>> {

	private int[] gRestrict;
	Map<RelationalTuple<ITimeInterval>, Integer> keyMap = null;
	int maxId = 0;
	private Map<Integer, List<RelationalTuple<ITimeInterval>>> buffers = null;

	public RelationalSlidingElementWindowTIPO(
			SlidingElementWindowTIPO<RelationalTuple<ITimeInterval>> po) {
		super(po);
		init();
	}

	public RelationalSlidingElementWindowTIPO(WindowAO ao) {
		super(ao);
		init();
	}
	
	@Override
	public void process_open() {
		init();
	}

	private void init() {
		List<SDFAttribute> grAttribs = windowAO.getPartitionBy();
		if (grAttribs != null && grAttribs.size() > 0) {
			gRestrict = new int[grAttribs.size()];
			for (int i = 0; i < grAttribs.size(); i++) {
				gRestrict[i] = windowAO.getInputSchema().indexOf(
						grAttribs.get(i));
			}
		}
		keyMap = new HashMap<RelationalTuple<ITimeInterval>, Integer>();
		buffers = new HashMap<Integer, List<RelationalTuple<ITimeInterval>>>();
	}

	@Override
	protected synchronized void process_next(
			RelationalTuple<ITimeInterval> object, int port) {
		if (windowAO.isPartitioned()){
			int bufferId = getGroupID(object);
			List<RelationalTuple<ITimeInterval>> buffer = buffers.get(bufferId);
			if (buffer == null){
				buffer = new LinkedList<RelationalTuple<ITimeInterval>>();
				buffers.put(bufferId, buffer);
			}
			buffer.add(object);
			processBuffer(buffer, object);
		}else{
			super.process_next(object, port);
		}
	}
	
	public int getGroupID(RelationalTuple<ITimeInterval> elem) {
		// Wenn es keine Gruppierungen gibt, ist der Schl�ssel immer gleich 0
		if (gRestrict == null || gRestrict.length == 0)
			return 0;
		// Ansonsten das Tupel auf die Gruppierungsattribute einschr�nken
		RelationalTuple<ITimeInterval> gTuple = elem.restrict(gRestrict, true);
		// Gibt es diese Kombination schon?
		Integer id = keyMap.get(gTuple);
		// Wenn nicht, neu eintragen
		if (id == null) {
			id = ++maxId;
			keyMap.put(gTuple, id);
		}
		return id;
	}
	
	@Override
	public void transfer(RelationalTuple<ITimeInterval> object) {
		// FIXME: Hier muss jetzt eine Queue erstellt werden, sortiert nach
		// dem Startzeitstempel --> analog zum GroupBy
		throw new RuntimeException("Transfer not implemented now");
		//super.transfer(object);
	}
	
}
