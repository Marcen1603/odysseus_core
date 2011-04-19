//package de.uniol.inf.is.odysseus.sparql.physicalops.interval.convert;
//
//import java.util.Iterator;
//import java.util.LinkedList;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.ITimeInterval;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.PointInTime;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.TimeInterval;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.container.Container;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.container.IMetaAttribute;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.container.MetaAttribute;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.predicate.intervalbased.LiesInPredicate;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.sweeparea.ISweepArea.Order;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.sweeparea.intervalbased.DefaultTISweepArea;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.AbstractPipe;
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.streaming.sparql.Ask;
//
///**
// * This is the ask operator for fixed windows in sparql stream processing.
// * 
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// *
// */
//public class AskFixedPO<T extends IMetaAttribute<? extends ITimeInterval>> extends AbstractPipe<T, Container<Boolean, ? extends ITimeInterval>>{
//
//	/**
//	 * defined to use it directly and not by using the
//	 * logical algebra operator
//	 */
//	private long range;
//	
//	/**
//	 * the number of the last window for which elements have
//	 * been returned
//	 */
//	private long numberOfLastWindow;
//	
//	/**
//	 * the sweep area for this operator
//	 */
//	private DefaultTISweepArea<IMetaAttribute<? extends ITimeInterval>> sa;
//	
//	/**
//	 * the next elements to return
//	 */
//	private LinkedList<Container<Boolean, ? extends ITimeInterval>> nextElems;
//	
//	/**
//	 * 
//	 * @param logical
//	 */
//	public AskFixedPO(AbstractLogicalOperator logical){
//		this.range = ((Ask)logical).getFixedWidth();
//		this.numberOfLastWindow = 0;
//		this.nextElems = new LinkedList<Container<Boolean, ? extends ITimeInterval>>();
//		
//		this.sa = new DefaultTISweepArea<IMetaAttribute<? extends ITimeInterval>>();
//		this.sa.setRemovePredicate(new LiesInPredicate());
//
//	}
//	
//	public AskFixedPO(AskFixedPO<T> original){
//		this.range = original.range;
//		this.numberOfLastWindow = original.numberOfLastWindow;
//		this.nextElems = original.nextElems;
//	}
//	
//	public AskFixedPO<T> clone(){
//		return new AskFixedPO<T>(this);
//	}
//	
//	public synchronized void process_next(T object, int port, boolean isReadOnly){
//		this.doIntervalBased(object);
//		while(!this.nextElems.isEmpty()){
//			this.transfer(this.nextElems.removeFirst());
//		}
//	}
//	
//	private void doIntervalBased(IMetaAttribute<? extends ITimeInterval> next){
//		long numberOfNewWindow = next.getMetadata().getStart().getMainPoint() / this.range;
//		
//		while(this.numberOfLastWindow < numberOfNewWindow){
//			PointInTime t_start = new PointInTime(numberOfLastWindow * this.range, 0);
//			PointInTime t_end = new PointInTime((numberOfLastWindow+1) * this.range, 0);
//			IMetaAttribute<ITimeInterval> reference =
//				new MetaAttribute<ITimeInterval>();
//			reference.setMetadata(new TimeInterval(t_start, t_end));
//			
//			Iterator<IMetaAttribute<? extends ITimeInterval>> iter = this.sa.extractElements(reference, Order.LeftRight);
//			if(iter.hasNext()){
//				Container<Boolean, ITimeInterval> toAdd = new Container<Boolean, ITimeInterval>(new Boolean(true));
//				toAdd.setMetadata(new TimeInterval(t_start, t_end));
//				this.nextElems.addLast(toAdd);
//			}
//			else{
//				// Das in dieser Iteration betrachtete Fenster enthaelt keine Elemente.
//				// Daher enthalten alle weiteren bereits in der Vergangenheit liegenden Fenster
//				// ebenfalls keine Elemente, denn sonst waeren bereits Antworten fuer dieses Fenster und die
//				// die folgenden Fenster in den Ausgabedatenstrom geschrieben worden. Damit kann man die folgenden Fenster
//				// alle zusammenfassen und muss nicht fuer jedes Fenster ein eigenes Element in den
//				// Ausgabedatenstrom schreiben.
//				Container<Boolean, ITimeInterval> toAdd = new Container<Boolean, ITimeInterval>(new Boolean(false));
//				t_end = new PointInTime(numberOfNewWindow * this.range, 0);
//				toAdd.setMetadata(new TimeInterval(t_start, t_end));
//				this.nextElems.addLast(toAdd);
//				this.numberOfLastWindow = numberOfNewWindow;
//				break;
//			}
//			this.numberOfLastWindow++;
//		}
//		
//		this.sa.insert(next);		
//	}
//	
//	public void process_open(){
//	}
//	
//	public void process_close(){
//	}
//	
//}
