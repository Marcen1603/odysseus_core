/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.mining.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.frequentitem.Transaction;
import de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth.FList;
import de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth.FPTree;
import de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth.Pattern;

/**
 * @author Dennis Geesen
 * 
 */
public class FrequentItemsetFPGrowthPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private List<Transaction<M>> transactions = new ArrayList<Transaction<M>>();
	private PointInTime lastCut = PointInTime.getZeroTime();
	private int minsupport = 2;
	// private int maxlength = 5;
	private int maxTransactions = 25;
	private int counter = 0;
	private long lastTime = 0L;
	private long startTime = 0L;	

	private FList<M> flist = new FList<M>();
	
	private IMetadataMergeFunction<M> metadatamergefunction;
	private M lastMetadata;

	public FrequentItemsetFPGrowthPO() {

	}

	public FrequentItemsetFPGrowthPO(FrequentItemsetFPGrowthPO<M> old) {
		this.minsupport = old.minsupport;
		this.maxTransactions = old.maxTransactions;
		this.metadatamergefunction = old.metadatamergefunction.clone();
	}

	public FrequentItemsetFPGrowthPO(int minSupport, int maxTransactions) {
		this.minsupport = minSupport;
		this.maxTransactions = maxTransactions;
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

	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<M> object, int port) {
		lastMetadata = (M) object.getMetadata().clone();
		if (counter % 100 == 0) {
			long now = System.currentTimeMillis();
			long needed = (now - lastTime);
			long total = (now - startTime);

			lastTime = now;
			Tuple<M> countTuple = new Tuple<M>(3, false);
			countTuple.setAttribute(0, counter);
			countTuple.setAttribute(1, needed);
			countTuple.setAttribute(2, total);						
			countTuple.setMetadata(lastMetadata);
			transfer(countTuple, 1);
		}
		// daten werden verarbeitet, weil wir einen zeitfortschritt haben
		processData(object.getMetadata().getStart());
		// anschließend kann das aktuelle Element rein
		// das aktuelle element wird nicht berücksichtigt, weil ggf. andere
		// elemente denselben startzeitstempel haben können
		// und die sind noch unbekannt.
		sweepArea.insert(object);
		counter++;
	}

	private synchronized void processData(PointInTime currentTime) {
		if (currentTime.after(lastCut)) {
			// hole alle elemente, die definitiv bearbeitet werden können, weil
			// sie echt vor der aktuellen zeit sind
			Iterator<Tuple<M>> qualifies = sweepArea.queryElementsStartingBefore(currentTime);

			// baue daraus eine transaktion und zähle die vorkommen für die
			// flist
			Transaction<M> transaction = new Transaction<M>();			
			while (qualifies.hasNext()) {				
				// wir holen eine kopie
				Tuple<M> next = qualifies.next().clone();
				// transaction sorgt dafür, dass alle elemente dieselben zeiten haben
				transaction.addElement(next);
				this.flist.insertTuple(next);
			}

			// wenn wir mehr transactions als benötigt angeschaut haben, dann
			// entfernen wir zu alte wieder
			if (this.transactions.size() == this.maxTransactions) {
				Transaction<M> removed = this.transactions.remove(0);
				this.flist.remove(removed.getElements());
			}
			this.transactions.add(transaction);

			PointInTime totalMin = lastCut;
			PointInTime totalMax = currentTime;
			
			
			synchronized (this.flist) {
				// create a new fp-tree
				FPTree<M> thetree = new FPTree<M>();
				// and use the current f-list with minimum support
				List<Pair<Tuple<M>, Integer>> currentfList = this.flist.getSortedList(minsupport);

 
				// do we have at least one frequent item with min-support?
				if (!currentfList.isEmpty()) {

					// the validity of the fptree
					
							
					// then - insert all into the tree
					for (Transaction<M> trans : this.transactions) {

						List<Tuple<M>> sortedList = trans.getFBasedList(currentfList);
						if (!sortedList.isEmpty()) {
							// we strech the whole fptree-timeintervall to min and max of all transactions
//							totalMin = PointInTime.min(totalMin, trans.getMinTime());
//							totalMax = PointInTime.max(totalMax, trans.getMaxTime());
							thetree.insertTree(sortedList, thetree.getRoot());
						}
						
					}

					ArrayList<Pattern<M>> results = fpgrowth(thetree);

					int i = 0;
					for (Pattern<M> p : results) {
						Tuple<M> newtuple = new Tuple<M>(3, false);
						@SuppressWarnings("unchecked")
						M left = (M) p.getMetadata().clone();
						left.setStartAndEnd(totalMin, totalMax);
						@SuppressWarnings("unchecked")
						M right = (M) lastMetadata.clone();		
						right.setStartAndEnd(totalMin, totalMax);
						M meta = this.metadatamergefunction.mergeMetadata(left, right);
						newtuple.setMetadata(meta);						
						newtuple.setAttribute(0, i);
						newtuple.setAttribute(1, p.getPattern());
						newtuple.setAttribute(2, p.getSupport());
						i++;

						transfer(newtuple);
					}

				}
			}

			lastCut = currentTime;
			// als letztes können wir noch alle Elemente rauswerfen, die in
			// Zukunft unwichtig sind
			sweepArea.purgeElementsBefore(currentTime);
		}
	}

	private ArrayList<Pattern<M>> fpgrowth(FPTree<M> tree) {
		Pattern<M> pattern = new Pattern<M>();
		ArrayList<Pattern<M>> allPatterns = new ArrayList<Pattern<M>>();
		fpgrowth(tree, pattern, allPatterns);
		return allPatterns;
	}

	private void fpgrowth(FPTree<M> tree, Pattern<M> pattern, ArrayList<Pattern<M>> allPatterns) {

		// if(tree.hasSingePrefixPath()){
		// List<FPTreeNode<M>> p = tree.getSinglePrefixPath();
		// for(FPTreeNode<M> n : p){
		// System.out.println("P: "+n);
		// }
		// tree = tree.getMultiPathTree();
		// }
		for (Tuple<M> tuple : tree.getDescendingHeaderList()) {
			Pattern<M> newPattern = new Pattern<M>(pattern);
			newPattern.add(tuple, tree.getCount(tuple));
			allPatterns.add(newPattern);
			// tree.printTree();
			// get conditional prefix pathes
			List<Pattern<M>> paths = tree.getPrefixPaths(tuple);
			// build conditional fp tree
			FPTree<M> condTree = new FPTree<M>();
			for (Pattern<M> path : paths) {
				condTree.insertTree(path);
			}
			// condTree.printTree();
			// REMOVE INFREQUENT (bei e wäre es b)
			condTree.removeWithoutMinSupport(minsupport);
			// condTree.printTree();
			// if conditional tree is not empty, call recursively fpgrowth
			if (!condTree.isEmpty()) {
				Tuple<M> nextKey = condTree.getHeaderTable().lastKey();
				if (!newPattern.contains(nextKey)) {
					fpgrowth(condTree, newPattern, allPatterns);
				}
			}
		}
	}
//
	@Override
	public FrequentItemsetFPGrowthPO<M> clone() {
		return new FrequentItemsetFPGrowthPO<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		synchronized (sweepArea) {
			processData(timestamp);
		}
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof FrequentItemsetFPGrowthPO)) {
			return false;
		}
		if (this.hasSameSources(ipo)) {
			FrequentItemsetFPGrowthPO<?> po = (FrequentItemsetFPGrowthPO<?>) ipo;
			if (this.maxTransactions == po.maxTransactions && this.minsupport == po.minsupport) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param combinedMergeFunction
	 */
	public void setMetadataMerge(IMetadataMergeFunction<M> combinedMergeFunction) {
		this.metadatamergefunction = combinedMergeFunction;
		
	}

	/**
	 * @return
	 */
	public IMetadataMergeFunction<M> getMetadataMerge() {
		return this.metadatamergefunction;
	}

}
