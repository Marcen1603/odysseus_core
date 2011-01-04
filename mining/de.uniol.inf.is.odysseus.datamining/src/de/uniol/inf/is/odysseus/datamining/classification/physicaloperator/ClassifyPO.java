package de.uniol.inf.is.odysseus.datamining.classification.physicaloperator;

import de.uniol.inf.is.odysseus.datamining.classification.IClassifier;
import de.uniol.inf.is.odysseus.datamining.classification.RelationalClassificationObject;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class ClassifyPO<T extends IMetaAttribute> extends AbstractClassificationPO<T> {
	
	protected IClassifier<T> classifier ;

	public ClassifyPO(ClassifyPO<T> classifyPO) {
		super(classifyPO);
	}
	
	public ClassifyPO() {
	}

	@Override
	protected void process_next(RelationalTuple<T> object, int port) {
		if(port == 0){
			classifier = object.getAttribute(0);
		}
		else{
				process_next(new RelationalClassificationObject<T>(
						object, restrictList, labelPosition));
			
		}
	}

	@Override
	protected void process_next(RelationalClassificationObject<T> tuple) {
		if(classifier != null){
			tuple.setClassLabel(classifier.getClassLabel(tuple));
		}
		transfer(tuple.getClassifiedTuple());
	}

	@Override
	public AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> clone() {
		return new ClassifyPO<T>(this);
	}

}