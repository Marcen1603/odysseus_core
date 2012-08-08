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
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
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
public class FrequentItemsetFPGrowthPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<ITimeInterval>> {

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

	public FrequentItemsetFPGrowthPO() {

	}

	public FrequentItemsetFPGrowthPO(FrequentItemsetFPGrowthPO<M> old) {
		this.minsupport = old.minsupport;
	}

	public FrequentItemsetFPGrowthPO(int minSupport) {
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
		println("#################################################################### NEW ELEMENT ####################################################################");
		println(object.toString());
		if (counter % 100 == 0) {
			long now = System.currentTimeMillis();
			System.out.println("current: " + counter + " needed: " + (now - lastTime) + " ms and total " + (now - startTime) + " ms");
			System.out.println("number of transactions: " + transactions.size());
			System.out.println("number in sweeparea: "+this.sweepArea.size());
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
			Iterator<Tuple<M>> qualifies = sweepArea.queryElementsStartingBeforeFIFO(currentTime);
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
				this.flist.insertTuple(next);

			}
			// korrigiere TI, wenn die aktuelle zeit innerhalb des TI ist
			end = PointInTime.min(end, currentTime);
			transaction.setTimeInterval(start, end);			
			
			println("adding new transaction: "+transaction);			
			if(this.transactions.size()==this.maxTransactions){
				Transaction<M> removed = this.transactions.remove(0);
				this.flist.remove(removed.getElements());
			}
			this.transactions.add(transaction);
			// unser gesamter zeitraum um das es hier geht, 
			// startet bei dem letzten cut (oder der transaktion)			
			PointInTime totalMin = start;
			// und endet mit der letzten transaction
			PointInTime totalMax = end;
			
			
			println("f-List: " + this.flist);
			synchronized (this.flist) {

				List<Pair<Tuple<M>, Integer>> currentfList = this.flist.getSortedList(minsupport);
				println("sorted f-List: " + currentfList);

				if (!currentfList.isEmpty()) {
					FPTree<M> tree = new FPTree<M>();
					for (Transaction<M> trans : this.transactions) {						
						println("actual transaction: "+trans);
						List<Tuple<M>> sortedList = trans.getFBasedList(currentfList);
						if(!sortedList.isEmpty()){
							println("insert ordered frequent items: " + sortedList);
							tree.insertTree(sortedList, tree.getRoot());
						}
						totalMin = PointInTime.min(totalMin, trans.getMetadata().getStart());
						totalMax = PointInTime.max(totalMax, trans.getMetadata().getEnd());						
					}
					println("FP-TREE: ");
					tree.printTree();
					ArrayList<Pattern<M>> results = fpgrowth(tree);
					
					println("-----------------------------------------------------------------");
					println("-----------------------------------------------------------------");
					println("ERGEBNIS:");
					int i=0;
					for (Pattern<M> p : results) {
						println(p.toString());
						Tuple<ITimeInterval> newtuple = new Tuple<ITimeInterval>(2, false);
						newtuple.setMetadata(p.getMetadata());
						newtuple.getMetadata().setStartAndEnd(totalMin, totalMax);	
						newtuple.setAttribute(0, i);
						newtuple.setAttribute(1, p.toString());
						i++;
						//System.out.println("new Tuple: "+newtuple);
						transfer(newtuple);
					}
					println("-----------------------------------------------------------------");
					
					
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
			println("-----------------------------------------------------------------");
			println("NEW CALL: Added " + tuple + " to pattern...");
			newPattern.add(tuple, tree.getCount(tuple));
			allPatterns.add(newPattern);
			println("INPUT TREE: ");
			tree.printTree();
			// get conditional prefix pathes
			List<Pattern<M>> paths = tree.getPrefixPaths(tuple);
			println("BUILD NEW CONDITIONAL FP-TREE FOR " + newPattern);
			// build conditional fp tree
			FPTree<M> condTree = new FPTree<M>();
			for (Pattern<M> path : paths) {
				println("INSERT PATH: " + path);
				condTree.insertTree(path);
			}
			condTree.printTree();
			// REMOVE INFREQUENT (bei e wäre es b)
			condTree.removeWithoutMinSupport(minsupport);
			condTree.printTree();
			// if conditional tree is not empty, call recursively fpgrowth
			if (!condTree.isEmpty()) {
				Tuple<M> nextKey = condTree.getHeaderTable().lastKey();
				if (!newPattern.contains(nextKey)) {

					// newPattern.add(nextKey, condTree.getCount(nextKey));
					println("call recursively for " + newPattern + " with tree:");
					condTree.printTree();
					println("...");
					fpgrowth(condTree, newPattern, allPatterns);
				}
			}
		}
	}

	private void println(String s) {
		//System.out.println(s);
	}

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

}
