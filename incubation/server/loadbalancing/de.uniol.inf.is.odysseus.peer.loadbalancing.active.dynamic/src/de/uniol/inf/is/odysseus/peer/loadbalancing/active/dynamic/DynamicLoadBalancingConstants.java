package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

public class DynamicLoadBalancingConstants {

	
			//Monitoring Thread Constants (Trigger)
			public static final long UPDATE_INTERVAL = 10000;
			public static final double CPU_THRESHOLD = 0.7;
			public static final double MEM_THRESHOLD = 0.4;
			public static final double NET_THRESHOLD = 0.7;
	
			
			//Names
			public static final String BID_PROVIDER_NAME = "OdyLoad";
			public static final String STRATEGY_NAME = "OdyLoad";
			public static final String ALLOCATOR_NAME = "OdyLoad";
			
			
			//Survey Allocator Parameters
			public static final int SURVEY_LATENCY_WEIGHT = 0;
			public static final int SURVEY_BID_WEIGHT = 1;
			public static final String SURVEY_USE_OWN_BID = "false";
	
			
			//Load-Model weights and calculation factors
			//In Costmodel Load is measured in %, we need it between 0 and 1
			public static final double CPU_LOAD_COSTMODEL_FACTOR = 100.0;
			
			public static final double WEIGHT_SENDERS = 0.1;
			public static final double WEIGHT_RECEIVERS = 0.1;
			public static final double WEIGHT_STATE = 0.8;
			
			public static final double WEIGHT_GENERAL_COSTS = 0.3;
			public static final double WEIGHT_INDIVIDUAL_COSTS = 0.7;
			public static final String DEFAULT_QUERY_BUILD_CONFIG = "Standard";

}
