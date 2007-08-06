package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This preprocessor caches the results of previous preprocessing phases to improve the performance.
 * 
 * When the metadata for a planoperator is requested it first checks, wether it
 * is already cached and only if it isn't, the preprocessor falls back to the database query. 
 * @author Jonas Jacobi
 */
public class CachingPreprocessor extends DefaultPreprocessor {
	private static final long serialVersionUID = -146932504880220679L;
	
	Map<String, PreprocessedMetadata> metadataCache;

	public CachingPreprocessor(IPOMetadatarepository metadatarepository) {
		super(metadatarepository);

		this.metadataCache = new HashMap<String, PreprocessedMetadata>();
	}

	@Override
	public PreprocessedMetadata getMetadata(String poGuid) throws Exception {
		PreprocessedMetadata data = metadataCache.get(poGuid);
		if (data == null) {
			data = super.getMetadata(poGuid);
			this.metadataCache.put(poGuid, data);
		}
		return data;
	}

	@Override
	public Map<PlanOperator, PreprocessedMetadata> getMetadata(
			Collection<PlanOperator> pos) throws Exception {
		Map<PlanOperator, PreprocessedMetadata> data = new HashMap<PlanOperator, PreprocessedMetadata>();
		List<PlanOperator> nonAvailablePOs = new ArrayList<PlanOperator>();
		for (PlanOperator po : pos) {
			PreprocessedMetadata curData = metadataCache.get(po.getGuid());
			if (curData == null) {
				nonAvailablePOs.add(po);
			} else {
				data.put(po, curData);
				metadataCache.put(po.getGuid(), curData);
			}
		}
		Map<PlanOperator, PreprocessedMetadata> newData = super
				.getMetadata(nonAvailablePOs);
		data.putAll(newData);

		return data;
	}

}
