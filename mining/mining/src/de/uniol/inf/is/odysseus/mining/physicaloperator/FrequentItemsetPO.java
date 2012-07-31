package de.uniol.inf.is.odysseus.mining.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.mining.frequentitem.FrequentItemSet;
import de.uniol.inf.is.odysseus.mining.frequentitem.FrequentItemSetContainer;
import de.uniol.inf.is.odysseus.mining.frequentitem.Transaction;

public class FrequentItemsetPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private List<Transaction<Tuple<M>>> transactions = new ArrayList<Transaction<Tuple<M>>>();
	private PointInTime lastCut = PointInTime.getZeroTime();
	private int minsupport = 2;
	private int maxlength = 5;
	private int counter = 0;
	private long lastTime = 0L;
	private long startTime = 0L;

	public FrequentItemsetPO() {

	}

	public FrequentItemsetPO(FrequentItemsetPO<M> old) {

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();		
		this.transactions.clear();
		this.lastCut = PointInTime.getZeroTime();
		sweepArea.clear();
		startTime = System.currentTimeMillis();
		lastTime = startTime;
	}
	
	
	@Override
	protected void process_next(Tuple<M> object, int port) {
//		System.out.println("---------------------------------------NEW ELEMENT----------------------------------------------");
//		System.out.println(object);		
		if(counter%100==0){
			long now = System.currentTimeMillis();
			System.out.println("current: "+counter+" needed: "+(now-lastTime)+" ms and total "+(now-startTime)+" ms");
			System.out.println("number of transactions: "+transactions.size());
			lastTime = now;
		}
		synchronized (sweepArea) {
			// daten werden verarbeitet, weil wir einen zeitfortschritt haben
			processData(object.getMetadata().getStart());
			// anschließend kann das aktuelle Element rein
			// das aktuelle element wird nicht berücksichtigt, weil ggf. andere
			// elemente denselben startzeitstempel haben können
			// und die sind noch unbekannt.

			sweepArea.insert(object);			
		}
		counter++;
	}

	private void processData(PointInTime currentTime) {

		if (currentTime.after(lastCut)) {
			// hole alle elemente, die definitiv bearbeitet werden können, weil
			// sie echt vor der aktuellen zeit sind
			Iterator<Tuple<M>> qualifies = sweepArea.queryElementsStartingBefore(currentTime);
			// wir betrachten den zeitraum zwischen der letzten berechnung und
			// der aktuellen zeit
			PointInTime start = PointInTime.getZeroTime();
			PointInTime end = PointInTime.getInfinityTime();
			// die betrachtete zeit ist die maximale startzeit und die minimale
			// endzeit
			Transaction<Tuple<M>> transaction = new Transaction<Tuple<M>>();
			while (qualifies.hasNext()) {
				Tuple<M> next = qualifies.next();
				// wir nehmen den maximalen startzeitstempel (alles davor wurde
				// schon in der vorherigen iteration berechnet!)
				if (start.before(next.getMetadata().getStart())) {
					start = next.getMetadata().getStart();
				}
				// der endzeitstempel ist der kleinste endzeitstempel
				if (end.after(next.getMetadata().getEnd())) {
					end = next.getMetadata().getEnd();
				}
				transaction.addElement(next);
			}
			// korrigiere TI, wenn die aktuelle zeit innerhalb des TI ist
			end = PointInTime.min(end, currentTime);
			transaction.setTimeInterval(new TimeInterval(start, end));
			lastCut = currentTime;
//			System.out.println("adding");
//			System.out.println(transaction);
			this.transactions.add(transaction);
			long startStep = System.currentTimeMillis();
			List<FrequentItemSetContainer<Tuple<M>>> fisses = createFrequentItems();
			System.out.println(System.currentTimeMillis()-startStep);
			System.out.println("SA: "+sweepArea.size());
//			for (FrequentItemSetContainer<Tuple<M>> fis : fisses) {
//				System.out.println("----");
//				System.out.println(fis);
//			}

			// als letztes können wir noch alle Elemente rauswerfen, die in
			// Zukunft unwichtig sind
			sweepArea.purgeElementsBefore(currentTime);
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		synchronized (sweepArea) {
			processData(timestamp);
		}
	}

	private List<FrequentItemSetContainer<Tuple<M>>> createFrequentItems() {
		List<FrequentItemSetContainer<Tuple<M>>> allFrequentItemSets = new ArrayList<FrequentItemSetContainer<Tuple<M>>>();

		FrequentItemSetContainer<Tuple<M>> oneItemSet = new FrequentItemSetContainer<Tuple<M>>();
		for (Transaction<Tuple<M>> trans : this.transactions) {
			for (Tuple<M> t : trans.getElements()) {

				FrequentItemSet<Tuple<M>> fis = new FrequentItemSet<Tuple<M>>(t);

				if (!oneItemSet.containsFrequentItemSet(fis)) {
					oneItemSet.addItemSet(fis);
				} else {
					oneItemSet.increaseCount(fis);
				}
			}
		}
		oneItemSet.purgeFrequentItemWithoutMinimumSupport(minsupport);
		allFrequentItemSets.add(oneItemSet);
		FrequentItemSetContainer<Tuple<M>> currentSet = oneItemSet;
		for (int i = 2; i <= maxlength; i++) {
			currentSet = aprioriGen(currentSet, i);
			allFrequentItemSets.add(currentSet);
			if (currentSet.size() == 0) {
				break;
			}
		}
		return allFrequentItemSets;
	}

	private FrequentItemSetContainer<Tuple<M>> aprioriGen(FrequentItemSetContainer<Tuple<M>> smallerLength, int length) {
		FrequentItemSetContainer<Tuple<M>> candidates = new FrequentItemSetContainer<Tuple<M>>();
		ArrayList<FrequentItemSet<Tuple<M>>> list = new ArrayList<FrequentItemSet<Tuple<M>>>(smallerLength.getFrequentItemSets());

		for (int i = 0; i < list.size(); i++) {
			FrequentItemSet<Tuple<M>> current = list.get(i);
			for (int k = i + 1; k < list.size(); k++) {

				FrequentItemSet<Tuple<M>> other = list.get(k);
				int commonItems = other.intersectCount(current);
				if (commonItems == (length - 2)) {
					FrequentItemSet<Tuple<M>> newFis = new FrequentItemSet<Tuple<M>>(current);
					newFis.addFrequentItemSet(other);
					if (!candidates.containsFrequentItemSet(newFis)) {
						// check, if each subset is a frequent item set
						List<FrequentItemSet<Tuple<M>>> subsets = newFis.splitIntoSmallerSubset();
						if (smallerLength.containsAll(subsets)) {
							candidates.addItemSet(newFis);
						}
					}
				}
			}
		}

		candidates.calcSupportCount(this.transactions);
		candidates.purgeFrequentItemWithoutMinimumSupport(2);
		return candidates;
	}

	@Override
	public FrequentItemsetPO<M> clone() {
		return new FrequentItemsetPO<M>(this);
	}

}
