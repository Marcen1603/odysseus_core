package de.uniol.inf.is.odysseus.pnapproach.id.sweeparea;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;


/**
 * Diese SweepArea funktioniert genau wie die DefaultPNIDSweepArea. Lediglich die Methode
 * insertAndRemoveWrongScheduled ist anders implementiert, weil sie von ResultAwareJoinPNIDPO
 * anders ben�tigt wird.
 * 
 * @author abolles
 *
 * @param <T>
 */
public class ResultAwarePNIDSweepArea<T extends IMetaAttributeContainer<? extends IPosNeg>> extends DefaultPNIDSweepArea<T>{

	public ResultAwarePNIDSweepArea(
			ResultAwarePNIDSweepArea<T> resultAwarePNIDSweepArea) {
		super(resultAwarePNIDSweepArea);
	}

	public ResultAwarePNIDSweepArea() {
		super();	
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
	 * Because of the timestamp ordering this error can be
	 * corrected in the heap of the join. In this case, if
	 * there are positive and negative elements that together
	 * have a 0 or negative validity will be deleted.
	 * 
	 * For this reason you need this method.
	 * 
	 * If an element removed a wrong scheduled one, it will not be inserted
	 * into the sweep area. This must be done explicitly by the ResultAwareJoinPNIDPO
	 * 
	 * @param s
	 * 
	 * @return Die Anzahl der falschen Ergebnisse
	 * 
	 */
	@Override
	public int insertAndRemovedWrongScheduled(T s){
		int count = 0;
		if(s.getMetadata().getElementType() == ElementType.NEGATIVE){
			Iterator<T> iter = this.elements.iterator();
			for(int i = 0; iter.hasNext(); i++){
				T old = iter.next();
				// only the ids have to be checked and in the id only
				// the real values have to be checked
				if(this.checkIDs(s.getMetadata().getID(), old.getMetadata().getID()) && old.getMetadata().getElementType() == ElementType.POSITIVE &&
						s.getMetadata().getTimestamp().beforeOrEquals(old.getMetadata().getTimestamp())){
					iter.remove();
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
		}
		else{
			super.insert(s);
		}
		return count;
	}
	
	@Override
	public ResultAwarePNIDSweepArea<T> clone() throws CloneNotSupportedException {
		return new ResultAwarePNIDSweepArea<T>(this);
	}
}
