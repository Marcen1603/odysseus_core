package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

public class DynamicLoadBalancingConstants {

	
	//In Costmodel Load is measured in %, we need it between 0 and 1
			public static final double CPU_LOAD_COSTMODEL_FACTOR = 100.0;
			
			public static final double WEIGHT_SENDERS = 0.1;
			public static final double WEIGHT_RECEIVERS = 0.1;
			public static final double WEIGHT_STATE = 0.8;
			
			public static final double WEIGHT_GENERAL_COSTS = 0.3;
			public static final double WEIGHT_INDIVIDUAL_COSTS = 0.7;

}
