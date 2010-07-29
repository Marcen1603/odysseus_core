/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;



/**
 * @author dtwumasi
 *
 */
public class InitializationPO<M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer> {
	
	
	
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object, int[] newObjListPath, MVRelationalTuple<M> initial ) {
		
		MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, newObjListPath)).getAttributes();
		
		MVRelationalTuple<M>[] oldList = newList.clone();
		
		// traverse connection list and filter
		int i=0;
		for(MVRelationalTuple<M> measurement : newList ) {
			oldList[i]=measurement;
		}
		
		// this in object reinsetzten
		
		return object;
		
		}





	

	
}


