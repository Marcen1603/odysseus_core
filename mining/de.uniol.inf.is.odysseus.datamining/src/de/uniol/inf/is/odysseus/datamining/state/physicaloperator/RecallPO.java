package de.uniol.inf.is.odysseus.datamining.state.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RecallPO<T extends IMetaAttribute> extends AbstractPipe<RelationalTuple<T>, RelationalTuple<T>>{

	private int hits = 0;
	private int misses = 0;
		
	private int[] checkList;
	
	public RecallPO(int[] schemaToCheck){
		this.checkList = schemaToCheck;
	}
	
	public RecallPO(RecallPO<T> recallPO) {
		this.hits = recallPO.hits;
		this.misses = recallPO.misses;
		this.checkList = recallPO.checkList;
	}	
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {		
		
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(RelationalTuple<T> object, int port) {
		if(allEqual(object)){
			hits++;
		}else{
			misses++;
		}
		RelationalTuple<T> t = object.append(hits);
		t = t.append(misses);
		transfer(t, port);
		
	}

	@Override
	public AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> clone() {
		return new RecallPO<T>(this);
	}
	
	
	public boolean allEqual(RelationalTuple<T> tuple){
		for(int i=0;i<checkList.length;i++){
			for(int j=1;j<checkList.length;j++){
				Object left = tuple.getAttribute(checkList[i]);
				Object right = tuple.getAttribute(checkList[j]);
				if((left instanceof String) && (right instanceof String)){
					if(!((String)left).trim().equals(((String)right).trim())){
						return false;
					}
				}else{
					if(!left.equals(right)){
						return false;
					}
				}
			}
		}		
		return true;
	}

}
