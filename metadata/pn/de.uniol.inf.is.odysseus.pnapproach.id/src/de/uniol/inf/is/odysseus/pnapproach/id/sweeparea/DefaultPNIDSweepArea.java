package de.uniol.inf.is.odysseus.pnapproach.id.sweeparea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.MetadataComparator;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.id.metadata.IDGenerator;

/**
 * @author Andre Bolles
 */
public class DefaultPNIDSweepArea<T extends IMetaAttributeContainer<? extends IPosNeg>>
		extends SweepArea<T> {
	
	public DefaultPNIDSweepArea() {
		super(new MetadataComparator<IPosNeg>());
	}
	
	public DefaultPNIDSweepArea(DefaultPNIDSweepArea<T> sa) {
		super(sa.comparator);
	}

	/**
	 * Keine Exception werfen, da sonst alle Operatoren doch neu implementiert werden muessen.
	 * @deprecated Diese Methode sollte beim PN-Ansatz eigentlich nie aufgerufen werden.
	 */
	@Override
	public void purgeElements(T element, Order order) {
	}

	/**
	 * Diese Methode sollte beim PN-Ansatz eigentlich nie aufgerufen werden. Doch: Bei Praedikatfenstern mit Tupelfenstern
	 */
	@Override
	public Iterator<T> extractElements(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		Iterator<T> it = this.elements.iterator();
		while (it.hasNext()) {
			T next = it.next();
			switch (order) {
			case LeftRight:
				if (getRemovePredicate().evaluate(element, next)) {
					it.remove();
					result.add(next);
					break;
				} else {
					return result.iterator();
				}
			case RightLeft:
				if (getRemovePredicate().evaluate(next, element)) {
					it.remove();
					result.add(next);
					break;
				} else {
					return result.iterator();
				}
			}
		}
		return result.iterator();
	}
	
	/**
	 * This method returns and removes all elements from the sweep area that
	 * have a timestamp that lies totally before the passed point.
	 * 
	 * @param validity
	 *            All elements with a time interval that lies totally before
	 *            this interval will be returned and removed from this
	 *            sweeparea.
	 * 
	 * @return
	 */
	public Iterator<T> extractElements(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		Iterator<T> li = elements.iterator();
		while (li.hasNext()) {
			T s_hat = li.next();
			// Alle Elemente entfernen, die nicht mehr verschnitten werden
			// k�nnen (also davor liegen)
			if (PointInTime.before(s_hat.getMetadata().getTimestamp(), validity)) {
				retval.add(s_hat);
				li.remove();
			}
			// Die Elemente sind nach Zeitstempeln sortiert, also werden
			// alle folgenden Elemente auch nicht vor dem Zeitpunkt liegen.
			else{
				return retval.iterator();
			}
		}
		return retval.iterator();
	}
	
	
	/**
	 * Inserting an element into the sweep area also causes some elements
	 * to be removed if they are corresponding negative to the inserted element.
	 * Negative Elementes will not be inserted.
	 * 
	 * @param element
	 * @param order
	 */
	public void insert(T element, Order order){
		this.insertAndReturnRemoved(element);
	}
	
	/**
	 * Positive and negative Elements will be inserted. Negative elements will not
	 * remove any other elements. Will be used for transferFunctions in Join-Operators.
	 * If you want to remove elements by inserting a negative element use
	 * insert(element, null) instead.
	 */
	@Override
	public void insert(T element){
		super.insert(element);
	}
	
	/**
	 * This method works like insert in the P/N-approach,
	 * but it also returns all the elements that have been
	 * removed by a negative element.
	 * 
	 * @return If s is negative, all elements that have been removed
	 * by inserting s, will be returned, othterwise an empty set will be returned
	 */
	public List<T> insertAndReturnRemoved(T s){
		List<T> removed = new ArrayList<T>();

		if(s.getMetadata().getElementType() == ElementType.NEGATIVE){
			Iterator<T> iter = this.elements.iterator();
			for(int i = 0; iter.hasNext(); i++){
				T old = iter.next();
				// only the ids have to be checked and in the id only
				// the real values have to be checked
				if(this.checkIDs(s.getMetadata().getID(), old.getMetadata().getID()) && old.getMetadata().getElementType() == ElementType.POSITIVE){
					removed.add(old);
					iter.remove();
				}
			}
		}
		else{
			super.insert(s);
		}
		return removed;
	}
	
	/**
	 * There is a problem with the join in the pn-approach.
	 * If an element occurs in stream left and then the scheduler
	 * only schedules the right stream, there may occur elements
	 * that join with the element from the left. In this case
	 * there will be a join result. But in some cases the element
	 * from the left will not be valid anymore, but because of
	 * the scheduling the corresponding negative element has not
	 * occured yet.
	 * 
	 * Becaused of the timestamp ordering this error can be
	 * corrected in the heap of the join. In this case, if
	 * there are positive and negative elements that together
	 * have a 0 or negative validity will be deleted.
	 * 
	 * For this reason you need this method.
	 * 
	 * @param s
	 * 
	 * @return Die Anzahl der falsch erzeugten Ergebnisse
	 *  
	 */
	public int insertAndRemovedWrongScheduled(T s){
		int count = 0;
		if(s.getMetadata().getElementType() == ElementType.NEGATIVE){
			//T removed = null;
			boolean removedWrongScheduled = false;
			Iterator<T> iter = this.elements.iterator();
			for(int i = 0; iter.hasNext(); i++){
				T old = iter.next();
				// only the ids have to be checked and in the id only
				// the real values have to be checked
				if(this.checkIDs(s.getMetadata().getID(), old.getMetadata().getID()) && old.getMetadata().getElementType() == ElementType.POSITIVE &&
						s.getMetadata().getTimestamp().beforeOrEquals(old.getMetadata().getTimestamp())){
					iter.remove();
					//removed = old;
					removedWrongScheduled = true;
					count++;
				}
			}
			
//			Den auskommentierten Code bitte nicht entfernen. Wird noch ben�tigt.
			
//			// Falls ein falsch positives Ergebnis entfernt wurde, kann es sein, dass es zu diesem Ergebnis auch noch ein falsch negatives gibt.
//			// Auch dieses muss entfernt werden.
//			// Falls es jedoch kein falsch positives Ergebnis gibt, kann es immernoch sein, dass es ein falsch negatives Ergebnis gibt. Dieses
//			// muss dann durch das neue negative Ergebnis ausgetauscht werden, wenn das neue negative vor dem alten negativen liegt
//			
//			iter = this.elements.iterator();
//			while(iter.hasNext()){
//				T old = iter.next();
//				// TODO: Ich meine, dass es egal ist, welches der falsch positiven entfernt wird, weil es nur auf die Anzahl der negtiven Elemente
//				// ankommt. Kann aber auch sein, dass ich mich irre
//				if(this.checkIDs(s.getMetadata().getID(), old.getMetadata().getID()) && old.getMetadata().getElementType() == ElementType.NEGATIVE){
//					
//					// falls kein falsch positives entfernt werden musste und das neue negative vor dem bereits vorhandenen negativen liegt,
//					// so muss, dass alte negative durch das neue negative ersetzt werden.
//					// falls kein falsch positives entfernt werden musste und das neue negative nach dem bereits vorhandenen negativen liegt,
//					// so kann das alte negative beibehalten werden.
//					if(!removedWrongScheduled && s.getMetadata().getTimestamp().before(old.getMetadata().getTimestamp())){
//						iter.remove();
//						super.insert(s);
//					}
//					// falls ein falsch positives entfernt werden musste und ein negatives dazu vorhanden ist, so muss das negative ebenfalls entfernt
//					// werden. Das neue negative darf jedoch  nicht eingef�gt werden.
//					else if(removedWrongScheduled){
//						iter.remove();
//					}
//				}
//			}
			
//			// Falls ein falsch positives Ergebnis entfernt wurde, kann es sein, dass es zu diesem Ergebnis auch noch ein falsch negatives gibt.
//			// Auch dieses muss entfernt werden.
//			if(removedWrongScheduled){
//				iter = this.elements.iterator();
//				while(iter.hasNext()){
//					T old = iter.next();
//					// TODO: Ich meine, dass es egal ist, welches der falsch positiven entfernt wird, weil es nur auf die Anzahl der negtiven Elemente
//					// ankommt. Kann aber auch sein, dass ich mich irre
//					if(this.checkIDs(s.getMetadata().getID(), old.getMetadata().getID()) && old.getMetadata().getElementType() == ElementType.NEGATIVE){
//						iter.remove();
//					}
//				}
//			}
			
			// Nur negative Elemente, die entweder keine falsch erzeugten Ergebnisse gel�scht haben oder
			// Wildcards in der ID enthalten werden hinzugef�gt.
			if( !(removedWrongScheduled && !s.getMetadata().getID().contains(IDGenerator.getWildcard()))){
				super.insert(s);
			}
		}
		else{
			super.insert(s);
		}
		return count;
	}

	
	/**
	 * @return the min start timestamp of all elements currently in the sweep
	 *         area. Should be the start timestamp of the first element in the
	 *         linked list.
	 */
	public PointInTime getMinTs() {
		if (!this.elements.isEmpty()) {
			return this.elements.peek().getMetadata().getTimestamp();
		}
		return null;
	}

	@Override
	public void setRemovePredicate(IPredicate<? super T> removePredicate) {
		super.setRemovePredicate(removePredicate);
	}
	
	public int getPos(List<Long> id){
		Iterator<T> iter = this.elements.iterator();
		for(int i = 0; iter.hasNext(); i++){
			if(iter.next().getMetadata().getID().equals(id)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * An ID comp matches an ID ref, if for every value in ref
	 * that is not wildcard the value in comp is the same.
	 * 
	 * ex: (8, ?) matches (8, 12)
	 * 
	 * @param ref the reference id
	 * @param comp the id to be compared
	 * @return true, if both ids matches corresponding to the given
	 * description of matching, false otherwise
	 */
	protected boolean checkIDs(List<Long> ref, List<Long> comp){
		for(int i = 0; i<ref.size(); i++){
			Long l = ref.get(i);
			Long c = comp.get(i);
			if( !( (   (l == null) && (c == null)   ) ||
				   (   (l != null) && l.equals(IDGenerator.getWildcard())   ) ||
				   (   (l != null) && l.equals(c)   ) 
				 ) 
			  ){
				return false;
			}
		}
		return true;
	}
}
