package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class HelperProvider {
	private static HelperProvider instance = null;
	private final Map<String, IPhysicalOperatorHelper<? extends IPhysicalOperator>> helpers = Maps.newHashMap();

	private HelperProvider() {
		initialize();
	}
	
	private void initialize() {
		IPhysicalOperatorHelper<?> joinTIPOHelper = new JoinTIPOHelper();
		helpers.put(joinTIPOHelper.getOperatorClass().getName(), joinTIPOHelper);
		
		IPhysicalOperatorHelper<?> receiverPOHelper = new ReceiverPOHelper();
		helpers.put(receiverPOHelper.getOperatorClass().getName(), receiverPOHelper);
		
		IPhysicalOperatorHelper<?> relationalProjectPOHelper = new RelationalProjectPOHelper();
		helpers.put(relationalProjectPOHelper.getOperatorClass().getName(), relationalProjectPOHelper);
		
		IPhysicalOperatorHelper<?> renamePOHelper = new RenamePOHelper();
		helpers.put(renamePOHelper.getOperatorClass().getName(), renamePOHelper);
		
		IPhysicalOperatorHelper<?> selectPOHelper = new SelectPOHelper();
		helpers.put(selectPOHelper.getOperatorClass().getName(), selectPOHelper);
		
		IPhysicalOperatorHelper<?> unionPOHelper = new UnionPOHelper();
		helpers.put(unionPOHelper.getOperatorClass().getName(), unionPOHelper);
		
		IPhysicalOperatorHelper<?> metadataUpdatePOHelper = new MetadataUpdatePOHelper();
		helpers.put(metadataUpdatePOHelper.getOperatorClass().getName(), metadataUpdatePOHelper);
		
		IPhysicalOperatorHelper<?> metadataCreationPOHelper = new MetadataCreationPOHelper();
		helpers.put(metadataCreationPOHelper.getOperatorClass().getName(), metadataCreationPOHelper);
		
		IPhysicalOperatorHelper<?> jxtaSenderPOHelper = new JxtaSenderPOHelper();
		helpers.put(jxtaSenderPOHelper.getOperatorClass().getName(), jxtaSenderPOHelper);
		
		IPhysicalOperatorHelper<?> jxtaReceiverPOHelper = new JxtaReceiverPOHelper();
		helpers.put(jxtaReceiverPOHelper.getOperatorClass().getName(), jxtaReceiverPOHelper);
	}
	
	public static HelperProvider getInstance() {
		if(instance == null) {
			instance = new HelperProvider();
		}
		return instance;
	}
	
	public IPhysicalOperatorHelper<? extends IPhysicalOperator> getPhysicalOperatorHelper(IPhysicalOperator operator) {
		if(operator == null) {
			return null;
		} else {
			return this.getPhysicalOperatorHelper(operator.getClass().getName());
		}
	}
	
	public IPhysicalOperatorHelper<? extends IPhysicalOperator> getPhysicalOperatorHelper(String className) {
		return helpers.get(className);
	}
}
