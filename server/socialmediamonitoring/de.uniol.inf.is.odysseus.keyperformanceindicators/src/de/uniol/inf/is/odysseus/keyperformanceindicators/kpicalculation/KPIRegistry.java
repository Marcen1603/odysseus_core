package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class KPIRegistry {
	
	static Logger logger = LoggerFactory.getLogger(KPIRegistry.class);
	static Map<String, IKeyPerformanceIndicators> classifierAlgoTypes = new HashMap<String, IKeyPerformanceIndicators>();
	
	public static IKeyPerformanceIndicators getKPIByName(String kpiType) 
	{	
		if(classifierAlgoTypes.containsKey(kpiType))
		{
			IKeyPerformanceIndicators kpi = classifierAlgoTypes.get(kpiType);
			
			if(kpi.getType().equals(kpiType))		
			   return kpi;
			else
				return null;
		}
		else
		{
			IKeyPerformanceIndicators kpi = classifierAlgoTypes.get(kpiType.toLowerCase());
			IKeyPerformanceIndicators newKPI = kpi.getInstance(kpiType.toLowerCase());
			classifierAlgoTypes.put(kpiType.toLowerCase(), newKPI);

			return newKPI;
		}
	}
	
	public static void registerKPIType(IKeyPerformanceIndicators kpiType) {

		if (!classifierAlgoTypes.containsKey(kpiType.getType().toLowerCase())) {
			classifierAlgoTypes.put(kpiType.getType().toLowerCase(), kpiType);
		} else {
			logger.debug("KPIType " + kpiType.getType().toLowerCase() + " already added.");
		}
	}

	public static void unregisterKPIType(IKeyPerformanceIndicators kpiType) {
		if (classifierAlgoTypes.containsKey(kpiType.getType().toLowerCase())) {
			classifierAlgoTypes.remove(kpiType.getType().toLowerCase());
		}

	}

}
