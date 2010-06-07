package de.uniol.inf.is.odysseus.dbIntegration.operators;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.dbIntegration.control.Controller;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class DBSelectPO extends AbstractPipe<Object, Object> {

	
	protected Logger logger = LoggerFactory.getLogger(DBSelectPO.class);
	private SDFAttributeList outputSchema;
	private Controller controller;
	
	public DBSelectPO(Controller controller) {
		super();
		this.controller = controller;
	}
	
	public DBSelectPO(Controller controller, SDFAttributeList outputSchema) {
		super();
		this.controller = controller;
		this.outputSchema = outputSchema;
	}
	
	
	public DBSelectPO(DBSelectPO dbSelectPO) {
		super();
		// TODO: Muss der Controller gecloned werden?
		this.controller = dbSelectPO.controller; 
		this.outputSchema = dbSelectPO.outputSchema.clone();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Object object, int port) {
		
		List<RelationalTuple<?>> result = controller.getData(object);
		if (result != null) {
			if(result.size() > 0) {
				RelationalMergeFunction dataMerge = new RelationalMergeFunction<IMetaAttribute>(outputSchema.size());
				
				List objOut = new LinkedList<RelationalTuple<?>>();
				RelationalTuple<?> obj = (RelationalTuple<?>) object;
				for (RelationalTuple<?> tuple : result) {
					RelationalTuple<IMetaAttribute> addTuple = dataMerge.merge(obj, tuple);
					objOut.add(addTuple);
				}
				
				transfer(objOut);
			}
		}
	}

	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
	}
	
	@Override
	public DBSelectPO clone() {
		return new DBSelectPO(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	

}
