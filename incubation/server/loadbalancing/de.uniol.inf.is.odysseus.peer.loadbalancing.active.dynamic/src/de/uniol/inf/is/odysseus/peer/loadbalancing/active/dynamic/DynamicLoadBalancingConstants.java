package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

public class DynamicLoadBalancingConstants {

	
			//Monitoring Thread Constants (Trigger)
			public static final long UPDATE_INTERVAL = 10000;
			public static final double CPU_THRESHOLD = 0.7;
			public static final double MEM_THRESHOLD = 0.25;
			public static final double NET_THRESHOLD = 0.7;
	
			
			//Names
			public static final String BID_PROVIDER_NAME = "OdyLoad";
			public static final String STRATEGY_NAME = "OdyLoad";
			public static final String ALLOCATOR_NAME = "OdyLoad";
			
			
			public static final String INACTIVE_QUERIES_COMMUNICATOR_NAME = "InactiveQuery";
			public static final String STATELESS_QUERIES_COMMUNICATOR_NAME = "ParallelTrack";
			public static final String STATEFUL_QUERIES_COMMUNICATOR_NAME = "MovingState";
			
			
			//Survey Allocator Parameters
			public static final int SURVEY_LATENCY_WEIGHT = 0;
			public static final int SURVEY_BID_WEIGHT = 1;
			public static final String SURVEY_USE_OWN_BID = "notlocal";
			
			
			//Load-Model weights and calculation factors
			//In Costmodel Load is measured in %, we need it between 0 and 1
			public static final double CPU_LOAD_COSTMODEL_FACTOR = 100.0;
			
			public static final double WEIGHT_SENDERS = 0.1;
			public static final double WEIGHT_RECEIVERS = 0.1;
			public static final double WEIGHT_STATE = 0.8;
			
			public static final double WEIGHT_GENERAL_COSTS = 0.3;
			public static final double WEIGHT_INDIVIDUAL_COSTS = 0.7;
			public static final String DEFAULT_QUERY_BUILD_CONFIG = "Standard";
			
			
			//Fallback values for Cost model...
			public static final double FALLBACK_CPU_COSTS = 0.1;
			public static final double FALLBACK_MEM_COSTS = 0.1;
			public static final double FALLBACK_NET_COSTS = 0.1;
			
			//Other
			public static final long WAITING_TIME_FOR_LOCAL_LOCK = 2000;
			

}
