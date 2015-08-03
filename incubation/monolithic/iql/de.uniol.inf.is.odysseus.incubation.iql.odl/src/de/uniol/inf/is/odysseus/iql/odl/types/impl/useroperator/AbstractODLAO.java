package de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLAO;


public abstract class AbstractODLAO extends AbstractLogicalOperator implements IODLAO {


	private static final long serialVersionUID = 5292551007394987585L;
	
	private Map<String, List<Object>> metadata = new HashMap<>();

	public AbstractODLAO() {
		super();
	}
	
	public AbstractODLAO(AbstractODLAO ao) {
		super(ao);
		
	}
	
	@Override
	public boolean isAllPhysicalInputSet() {
		return super.isAllPhysicalInputSet() && validate();
	}
	
	protected boolean validate() {
		return true;
	}
	
	@Override
	public Map<String, List<Object>> getMetadata() {
		return metadata;
	}

	@Override
	public void addMetadata(String key, Object value) {
		List<Object> valueList = metadata.get(key);
		if (valueList == null) {
			valueList = new ArrayList<>();
		}
		valueList.add(value);
	}

}
