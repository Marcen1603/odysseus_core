package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class CompareSinkPO
		extends
		AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {

	boolean sameOutputAsInputPort = true;

	@SuppressWarnings("unchecked")
	private CompareSinkSweepArea<Tuple<? extends ITimeInterval>>[] sweepArea = new CompareSinkSweepArea[2];

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		sweepArea[0] = new CompareSinkSweepArea<>();
		sweepArea[1] = new CompareSinkSweepArea<>();
	}

	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
		synchronized (sweepArea) {
			//System.err.println("Testing " + object + " from port " + port);
			int otherport = port ^ 1;
			// 1. find elements in other area that cannot be joined
			// these are error elements!
			Iterator<Tuple<? extends ITimeInterval>> elems = sweepArea[otherport]
					//.extractElementsBefore(object.getMetadata().getStart());
					.extractElementsStartingBefore(object.getMetadata().getStart());
			while (elems.hasNext()) {
				Tuple<? extends ITimeInterval> e = elems.next();
				if (sameOutputAsInputPort) {
					transfer(e, otherport);
				} else {
					transfer(e);
				}
				//System.err.println(e + " from port " + otherport
				//		+ " has not counterpart");
			}
			List<Tuple<? extends ITimeInterval>> startSame = sweepArea[otherport]
					.extractEqualElementsStartingEquals(object,0,true);

			if (startSame.size() == 0) {
				sweepArea[port].insert(object);
				// System.err.println("Not found inserting");
			}
			//else {
//				int i = 0;
//				for (Tuple<? extends ITimeInterval> t : startSame) {
//					System.err.print("Found " + t + " for " + object);
//					if (!(t.getMetadata().getEnd().equals(object.getMetadata()
//							.getEnd()))) {
//						System.err.print(" with DIFFERENT TIMESTAMP!!");
//					}
//					i++;
//					if (i > 1) {
//						System.err.print(" NOT REMOVED." + i + ". element");
//					}
//					System.err.println();
//				}
				// System.err.println("Remove corresponding element" +
				// startSame);
			//}

//			if (sweepArea[0].size() > 0 || sweepArea[1].size() > 0) {
//				System.err
//						.println("AREA 0 --------------------------------------------------------------------------------");
//				System.err.println(sweepArea[0]);
//				System.err
//						.println("AREA 1--------------------------------------------------------------------------------");
//				System.err.println(sweepArea[1]);
//				System.err
//						.println("--------------------------------------------------------------------------------");
//			}else{
//				System.err
//				.println("--------------------------------------------------------------------------------");
//			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
			// ignore punctuations
	}


	@Override
	protected void process_close() {
		testRemaingTuples();
	}

	@Override
	protected void process_done() {
		testRemaingTuples();
	}

	private void testRemaingTuples() {
		synchronized (sweepArea) {
			for (int i = 0; i < 2; i++) {
				Iterator<Tuple<? extends ITimeInterval>> iter = sweepArea[i]
						.extractAllElements();
				while (iter.hasNext()) {
					//System.err.println("UNASSIGNED TUPLES FROM PORT " + i);
					Tuple<? extends ITimeInterval> e = iter.next();
					if (sameOutputAsInputPort) {
						transfer(e, i);
					} else {
						transfer(e);
					}
					//System.err.println("UNASSIGNED TUPLES FROM PORT " + i + " "
					//		+ e);
				}
			}

		}
	}


}
