package de.uniol.inf.is.odysseus.pnapproach.pn.physicaloperator.relational;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * This is an operator for removing duplicates that are valid
 * at the same point in time.
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 *
 */
public class DuplicateEliminationPNPO<T extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPipe<T, T>{

	
	/**
	 * the attributelist to emulate the left schema for the
	 * equality predicate
	 */
	//private SDFAttributeList leftPredicateSchema;
	
	/**
	 * the attributelist to emulate the right schema for the
	 * equality predicate
	 */
	//private SDFAttributeList rightPredicateSchema;
	
	/**
	 * the sweep area for this operator;
	 * FIXME: wird noch gar nicht benutzt
	 */
//	private DefaultPNIDSweepArea<NodeList<IPosNeg>> sa;
	
	/**
	 * In the positive negative approach we need
	 * a hash map, that contains all positive elements
	 * that have to be written out.
	 */
	private HashMap<T, List<T>> hash;
	
	public DuplicateEliminationPNPO(){
		this.hash = new HashMap<T, List<T>>();
	}
	
	public DuplicateEliminationPNPO(DuplicateEliminationPNPO<T> original){
//		this.sa = original.sa;
		this.hash = original.hash;
	}
	
	public DuplicateEliminationPNPO<T> cloneMe(){
		return new DuplicateEliminationPNPO<T>(this);
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	public synchronized void process_next(T next, int port) {		
		doPositiveNegative(next);
	}

	private void doPositiveNegative(T next){
		// first check if there is such an element
		// if there is one, nothing has to be done
		// except that the new element has to be written
		// into the hash table.
		// The ordering by timestamped will be ensured,
		// because the timestamp of the out elements wil
		// always be the same as the one of the input elems
		List<T> actElems = this.hash.get(next);
		if(actElems == null){
			if(next.getMetadata().getElementType().equals(ElementType.POSITIVE)){
				this.transfer(next);
				actElems = new ArrayList<T>();
				actElems.add(next);
				this.hash.put(next, actElems);
			}
			else{
				throw new RuntimeException("A negative element should not arrive before a positive one.");
			}
		}
		else if(actElems.isEmpty()){
			if(next.getMetadata().getElementType().equals(ElementType.POSITIVE)){
				this.transfer(next);
				actElems.add(next);
				this.hash.put(next, actElems);
			}
			else{
				throw new RuntimeException("A negative element should not arrive before a positive one.");
			}
		}
		else{
			if(next.getMetadata().getElementType().equals(ElementType.POSITIVE)){
				actElems.add(next);
				this.hash.put(next, actElems);
			}
			// it's a negative element that will remove some other element
			else{
				// wirklich nur ein Element entfernen, oder doch alle?
				actElems.remove(0);
				// Warum muss das neue Element dort reingeschrieben werden?
				this.hash.put(next, actElems);
			}
		}
	}
	
	@Override
	public final void process_open(){
	}
	
	public final void process_close(){
	}
	
	@Override
	public DuplicateEliminationPNPO<T> clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
