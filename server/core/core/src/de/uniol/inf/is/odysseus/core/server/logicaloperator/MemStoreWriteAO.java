package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResourceParameter;

@LogicalOperator(name="MemStoreWrite", minInputPorts=1, maxInputPorts =1, doc="This operator writes all elements to a given store. If the store does not exists, it will be created.", category = { LogicalOperatorCategory.SINK })
public class MemStoreWriteAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -5819782325512535615L;
	private Resource store;
	private boolean clearStore = true;
	
	public MemStoreWriteAO(){
	}
	
	public MemStoreWriteAO(MemStoreWriteAO memStoreWriteAO) {
		this.store = memStoreWriteAO.store;
	}

	public Resource getStore() {
		return store;
	}

	@Parameter(type = ResourceParameter.class, optional = false, doc="The name of the memory store to write to")
	public void setStore(Resource store) {
		this.store = store;
	}
	
	public boolean isClearStore() {
		return clearStore;
	}

	@Parameter(type = BooleanParameter.class, optional = true, doc = "The store is cleaned every time the query is started new. If set to false, the elements will be appended without cleaning the store.")
	public void setClearStore(boolean clearStore) {
		this.clearStore = clearStore;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MemStoreWriteAO(this);
	}

}
