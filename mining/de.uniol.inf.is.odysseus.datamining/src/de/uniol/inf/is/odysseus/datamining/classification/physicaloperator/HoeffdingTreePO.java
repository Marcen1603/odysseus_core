package de.uniol.inf.is.odysseus.datamining.classification.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.datamining.classification.HoeffdingNode;
import de.uniol.inf.is.odysseus.datamining.classification.RelationalClassificationObject;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class HoeffdingTreePO<T extends IMetaAttribute> extends AbstractClassificationPO<T>{
		
	
	private HoeffdingNode<T> tree;

	public HoeffdingTreePO(HoeffdingTreePO<T> hoeffdingTreePO) {
		super(hoeffdingTreePO);
		tree = hoeffdingTreePO.tree.clone();
		attributeEvaluationMeasure = hoeffdingTreePO.attributeEvaluationMeasure;
			}

	public HoeffdingTreePO() {
		super();
	}
	
	public void initTree(){
		tree = new HoeffdingNode<T>();
		tree.setLeaf(true);
		tree.initDataCube(restrictList.length);
		tree.setAttributeEvaluationMeasure(attributeEvaluationMeasure);
		ArrayList<Integer> splitAttributes = new ArrayList<Integer>();
		for(int i = 0; i< restrictList.length; i++){
			splitAttributes.add(i);
		}
		tree.setSplitAttributes(splitAttributes);
	}

	

	@Override
	public HoeffdingTreePO<T> clone() {
		// TODO Auto-generated method stub
		return new HoeffdingTreePO<T>(this);
	}
	


	


	@Override
	protected void process_next(RelationalClassificationObject<T> tuple) {
		boolean changed = tree.addTuple(tuple);
		if(changed){
			transferClassifier(tree.getSnapshot());
		}
		
	}

}
