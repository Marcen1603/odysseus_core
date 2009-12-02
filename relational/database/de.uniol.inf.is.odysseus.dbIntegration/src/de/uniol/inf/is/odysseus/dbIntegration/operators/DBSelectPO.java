package de.uniol.inf.is.odysseus.dbIntegration.operators;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.dbIntegration.control.Controller;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class DBSelectPO extends AbstractPipe<Object, Object> {

	
	protected Logger logger;
	private DBQuery query;
	private SDFAttributeList outputSchema;
	private List<String> options;
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
	
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
//		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Object object, int port) {
		List<RelationalTuple<?>> result = controller.getData(object).getResult();
		
		RelationalMergeFunction dataMerge = new RelationalMergeFunction<IMetaAttribute>(outputSchema);
		
//		[db.id db.name db.email db.creditcard db.city db.state a.timestamp a.id a.itemname a.description a.initialbid a.reserve a.expires a.seller a.category ]
		List objOut = new LinkedList<RelationalTuple<?>>();
		RelationalTuple<?> obj = (RelationalTuple<?>) object;
		for (RelationalTuple<?> tuple : result) {
			RelationalTuple<IMetaAttribute> addTuple = dataMerge.merge(obj, tuple);
			objOut.add(addTuple);
		}
		for (Object tuple : objOut) {
			System.out.println("### " + tuple + "###");
		}
		System.out.println(outputSchema.size());
		System.out.println(((RelationalTuple<IMetaAttribute> )objOut.get(0)).getAttributeCount());
		
		transfer(objOut);
		
		


		
	}

	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
	}
	

}
