package de.uniol.inf.is.odysseus.scars.objecttracking.initialization.physicaloperator;


import de.uniol.inf.is.odysseus.base.ISubscription;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.AbstractFilterPO;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author dtwumasi
 * 
 */
public class InitializationPO<M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer> extends AbstractFilterPO<M> {

	
	public InitializationPO() {
		super();
	}
	
	public InitializationPO(InitializationPO<M> copy) {
		super(copy);
		
	}
	
	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
	
	
	
 /*   MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>) OrAttributeResolver.resolveTuple(
        object, this.getNewObjListPath())).getAttributes(); 

    MVRelationalTuple<M>[] oldList = newList.clone();

    // traverse connection list and filter
    int i = 0;
    for (MVRelationalTuple<M> measurement : newList) {
      oldList[i] = measurement;
    }

    // this in object reinsetzen
*/
    return object;

  }

@Override
public AbstractPipe clone() {
	return new InitializationPO<M>(this);
}

@Override
public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
  return OutputMode.NEW_ELEMENT;
}




@Override
public void processPunctuation(PointInTime timestamp, int port) {
	this.sendPunctuation(timestamp);
	
}






}
