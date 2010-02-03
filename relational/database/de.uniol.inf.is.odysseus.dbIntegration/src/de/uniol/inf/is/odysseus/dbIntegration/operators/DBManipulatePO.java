package de.uniol.inf.is.odysseus.dbIntegration.operators;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.dbIntegration.control.Controller;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DBManipulatePO<T> extends AbstractSink<T> {

	private Controller controller;
	private DBQuery query;
	private SDFAttributeList inputSchema;
	
	public DBManipulatePO() {
		super();
	}
	
	public DBManipulatePO(DBQuery query, SDFAttributeList inputSchema) {
		super();
		this.query = query;
		this.inputSchema = inputSchema;
	}

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		controller.getData(object);
		
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		controller = new Controller(query, inputSchema);
	}
	
	@Override
	protected void process_close() {
		super.process_close();
		controller.closeQuery();
	}

	@Override
	public DBManipulatePO<T> clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
