package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class HelperProvider {
	private static HelperProvider instance = null;
	private final Map<Class<? extends IPhysicalOperator>, IPhysicalOperatorHelper<? extends IPhysicalOperator>> helpers = Maps.newHashMap();

	private HelperProvider() {
		initialize();
	}
	
	private void initialize() {
		IPhysicalOperatorHelper<?> joinTIPOHelper = new JoinTIPOHelper();
		helpers.put(joinTIPOHelper.getOperatorClass(), joinTIPOHelper);
		
		IPhysicalOperatorHelper<?> receiverPOHelper = new ReceiverPOHelper();
		helpers.put(receiverPOHelper.getOperatorClass(), receiverPOHelper);
		
		IPhysicalOperatorHelper<?> relationalProjectPOHelper = new RelationalProjectPOHelper();
		helpers.put(relationalProjectPOHelper.getOperatorClass(), relationalProjectPOHelper);
		
		IPhysicalOperatorHelper<?> renamePOHelper = new RenamePOHelper();
		helpers.put(renamePOHelper.getOperatorClass(), renamePOHelper);
		
		IPhysicalOperatorHelper<?> selectPOHelper = new SelectPOHelper();
		helpers.put(selectPOHelper.getOperatorClass(), selectPOHelper);
		
		IPhysicalOperatorHelper<?> unionPOHelper = new UnionPOHelper();
		helpers.put(unionPOHelper.getOperatorClass(), unionPOHelper);
	}
	
	public static HelperProvider getInstance() {
		if(instance == null) {
			instance = new HelperProvider();
		}
		return instance;
	}
	
	public IPhysicalOperatorHelper<? extends IPhysicalOperator> getPhysicalOperatorHelper(IPhysicalOperator operator) {
		Preconditions.checkNotNull(operator, "Operator mustn't be null");
		
		IPhysicalOperatorHelper<? extends IPhysicalOperator> h = helpers.get(operator.getClass());
		return h;
	}
	
	public IPhysicalOperatorHelper<? extends IPhysicalOperator> getPhysicalOperatorHelper(String className) {
		for(Class<?> c : helpers.keySet()) {
			if(c.toString().equals(className)) {
				return helpers.get(c);
			}
		}
		return null;
	}
}
