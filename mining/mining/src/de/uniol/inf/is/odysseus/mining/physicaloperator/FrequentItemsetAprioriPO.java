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
import de.uniol.inf.is.odysseus.mining.frequentitem.Transaction;
import de.uniol.inf.is.odysseus.mining.frequentitem.apriori.FrequentItemSet;
import de.uniol.inf.is.odysseus.mining.frequentitem.apriori.FrequentItemSetContainer;

public class FrequentItemsetAprioriPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<ITimeInterval>> {

	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private List<Transaction<M>> transactions = new ArrayList<Transaction<M>>();
	private PointInTime lastCut = PointInTime.getZeroTime();
	private int minsupport = 2;
	private int maxlength = 5;
	private int counter = 0;
	private long lastTime = 0L;
	private long startTime = 0L;

	public FrequentItemsetAprioriPO() {

	}

	public FrequentItemsetAprioriPO(FrequentItemsetAprioriPO<M> old) {
		this.minsupport = old.minsupport;
	}

	
	public FrequentItemsetAprioriPO(int minSupport) {
		this.minsupport = minSupport;
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
		System.out.println("---------------------------------------NEW ELEMENT----------------------------------------------");
		System.out.println(object);		
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
			Transaction<M> transaction = new Transaction<M>();			
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
			lastCut = currentTime;
//			System.out.println("adding");
//			System.out.println(transaction);
			this.transactions.add(transaction);
			
			List<FrequentItemSetContainer<Tuple<M>, M>> fisses = createFrequentItems();
						
			
			for (FrequentItemSetContainer<Tuple<M>, M> fisc : fisses) {
				for(FrequentItemSet<Tuple<M>, M> fis : fisc.getFrequentItemSets()){
					Tuple<ITimeInterval> newtuple = new Tuple<ITimeInterval>(1, false);
					newtuple.setMetadata(fis.getMetadata());
					newtuple.setAttribute(0, fis.toString());					
					transfer(newtuple);
				}
				System.out.println("----");
				System.out.println(fisc);
			}

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

	private List<FrequentItemSetContainer<Tuple<M>, M>> createFrequentItems() {
		
		List<FrequentItemSetContainer<Tuple<M>, M>> allFrequentItemSets = new ArrayList<FrequentItemSetContainer<Tuple<M>, M>>();
		
		FrequentItemSetContainer<Tuple<M>, M> oneItemSet = new FrequentItemSetContainer<Tuple<M>, M>();
		for (Transaction<M> trans : this.transactions) {
			for (Tuple<M> t : trans.getElements()) {

				FrequentItemSet<Tuple<M>, M> fis = new FrequentItemSet<Tuple<M>, M>(t);

				if (!oneItemSet.containsFrequentItemSet(fis)) {
					oneItemSet.addItemSet(fis);
				} else {
					oneItemSet.increaseCount(fis);
				}
				// TODO: set metadata
				//fis.setMetadata(trans.getMetadata());				
			}
		}
		oneItemSet.purgeFrequentItemWithoutMinimumSupport(minsupport);		
		allFrequentItemSets.add(oneItemSet);
		FrequentItemSetContainer<Tuple<M>, M> currentSet = oneItemSet;
		for (int i = 2; i <= maxlength; i++) {
			long startStep = System.currentTimeMillis();
			debug("Gen: "+i+"...");
			currentSet = aprioriGen(currentSet, i);
			debug("Gesamt: "+(System.currentTimeMillis()-startStep));
			allFrequentItemSets.add(currentSet);
			if (currentSet.size() == 0) {
				break;
			}
		}
		return allFrequentItemSets;
	}

	private FrequentItemSetContainer<Tuple<M>, M> aprioriGen(FrequentItemSetContainer<Tuple<M>, M> smallerLength, int length) {
		FrequentItemSetContainer<Tuple<M>, M> candidates = new FrequentItemSetContainer<Tuple<M>, M>();
		ArrayList<FrequentItemSet<Tuple<M>, M>> list = new ArrayList<FrequentItemSet<Tuple<M>, M>>(smallerLength.getFrequentItemSets());

		long startStep = System.currentTimeMillis();
		int counti = 0;
		for (int i = 0; i < list.size(); i++) {
			FrequentItemSet<Tuple<M>, M> current = list.get(i);
			long startStep2 = System.currentTimeMillis();
			for (int k = i + 1; k < list.size(); k++) {				
				FrequentItemSet<Tuple<M>, M> other = list.get(k);
				int commonItems = other.intersectCount(current);
				if (commonItems == (length - 2)) {
					FrequentItemSet<Tuple<M>, M> newFis = new FrequentItemSet<Tuple<M>, M>(current);
					newFis.addFrequentItemSet(other);
					if (!candidates.containsFrequentItemSet(newFis)) {
						// check, if each subset is a frequent item set						
						List<FrequentItemSet<Tuple<M>, M>> subsets = newFis.splitIntoSmallerSubset();
						if (smallerLength.containsAll(subsets)) {
							candidates.addItemSet(newFis);
						}
						
					}
				}
			}
			debug("        check: "+(System.currentTimeMillis()-startStep2));
			counti++;
		}
		debug("  count: "+counti);
		debug("  combination: "+(System.currentTimeMillis()-startStep));
		startStep = System.currentTimeMillis();
		candidates.calcSupportCount(this.transactions);
		debug("  Calcsupport: "+(System.currentTimeMillis()-startStep));
		startStep = System.currentTimeMillis();
		candidates.purgeFrequentItemWithoutMinimumSupport(2);
		debug("  purging: "+(System.currentTimeMillis()-startStep));
		return candidates;
	}

	@Override
	public FrequentItemsetAprioriPO<M> clone() {
		return new FrequentItemsetAprioriPO<M>(this);
	}
	
	
	private void debug(String str){
	//	System.out.println(str);
	}

}
