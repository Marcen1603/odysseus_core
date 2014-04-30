package de.uniol.inf.is.odysseus.mining.physicaloperator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.sweeparea.FastArrayList;
import de.uniol.inf.is.odysseus.sweeparea.FastLinkedList;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.frequentitem.IFrequentPatternMiner;
import de.uniol.inf.is.odysseus.mining.frequentitem.Pattern;

public class FrequentPatternMiningPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>(new FastLinkedList<Tuple<M>>());
	private FastArrayList<PointInTime> points = new FastArrayList<PointInTime>();
	private IFrequentPatternMiner<M> fpm;
	private CombinedMergeFunction<ITimeInterval> metamergeFunction;
	private int maxtransactions;
	private LinkedList<List<Tuple<M>>> transactions = new LinkedList<>();

	public FrequentPatternMiningPO(IFrequentPatternMiner<M> fpm, int maxtransactions) {
		this.fpm = fpm;
		this.maxtransactions = maxtransactions;
	}

	public FrequentPatternMiningPO(FrequentPatternMiningPO<M> old) {
		this.fpm = old.fpm;
		this.maxtransactions = old.maxtransactions;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(Tuple<M> object, int port) {
		if (!this.points.contains(object.getMetadata().getStart())) {
			this.points.add(object.getMetadata().getStart());
		}
		if (!this.points.contains(object.getMetadata().getEnd())) {
			this.points.add(object.getMetadata().getEnd());
		}
		Collections.sort(this.points);
		// we append before, so we only need a "simple" clone
		sweepArea.insert(object);
		int removeTill = 0;
		for (int i = 1; i < this.points.size(); i++) {
			PointInTime endP = this.points.get(i);
			PointInTime startP = this.points.get(i - 1);
			if (endP.beforeOrEquals(object.getMetadata().getStart())) {
				synchronized (this.sweepArea) {
					TimeInterval ti = new TimeInterval(startP, endP);
					List<Tuple<M>> qualifies = this.sweepArea.queryOverlapsAsList(ti);
					transactions.add(qualifies);

					if (transactions.size() > 2) {
//						long tillLearn = System.nanoTime();

						List<Pattern<M>> frequentSets = fpm.createFrequentSets(transactions, object.getMetadata());
//						long afterLearn = System.nanoTime();
						for (Pattern<M> p : frequentSets) {
							@SuppressWarnings("unchecked")
							M metadata = (M) object.getMetadata().clone();
							Tuple<M> newTuple = new Tuple<M>(3, false);
							newTuple.setAttribute(0, p);
							newTuple.setAttribute(1, p.getPattern());
							newTuple.setAttribute(2, p.getSupport());
							newTuple.setMetadata(metadata);
							newTuple.getMetadata().setStartAndEnd(startP, endP);
							// ((ILatency)newTuple.getMetadata()).setLatencyStart(start);
//							 ((ILatency)newTuple.getMetadata()).setLatencyEnd(end);
//							newTuple.setMetadata("LATENCY_BEFORE", tillLearn);
//							newTuple.setMetadata("LATENCY_AFTER", afterLearn);
							transfer(newTuple);
						}
					}
					if (transactions.size() == maxtransactions) {
						transactions.pop();
					}
					// System.out.println("TRANSFER: " + (System.currentTimeMillis() - time - duration));
					removeTill = i;
				}
			} else {
				break;
			}
		}
		if (removeTill != 0) {
			this.points.removeRange(0, removeTill);
			sweepArea.purgeElementsBefore(object.getMetadata().getStart());
		}

	}

	@Override
	public FrequentPatternMiningPO<M> clone() {
		return new FrequentPatternMiningPO<>(this);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.metamergeFunction.init();
	}

	public void setMetadataMerge(CombinedMergeFunction<ITimeInterval> metaDataMerge) {
		this.metamergeFunction = metaDataMerge;
	}

}
