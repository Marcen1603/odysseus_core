package de.uniol.inf.is.odysseus.recommendation.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractEnrichPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;

/**
 * @author Christopher Schwarz
 */
public class LODEnrichPO<T extends IMetaAttribute> extends AbstractEnrichPO<Tuple<T>, T> {
	static Logger logger = LoggerFactory.getLogger(LODEnrichPO.class);

	public LODEnrichPO(ICache cacheManager, IDataMergeFunction<Tuple<T>, T> dataMergeFunction, IMetadataMergeFunction<T> metaMergeFunction, int[] uniqueKey) {
		super(cacheManager, dataMergeFunction, metaMergeFunction, uniqueKey);
	}
	
	/**
	 * Copy constructor.
	 * @param lodEnrichPO
	 */
	public LODEnrichPO(LODEnrichPO<T> lodEnrichPO) {
		super(lodEnrichPO);
	}

	@Override
	protected void internal_process_open() {
		// TODO Auto-generated method stub
	}

	@Override
	protected List<IStreamObject<?>> internal_process(Tuple<T> input) {
		ArrayList<IStreamObject<?>> result = new ArrayList<IStreamObject<?>>(1);
		result.add(input);
		return result;
	}

	@Override
	protected void internal_process_close() {
		// TODO Auto-generated method stub
	}
}
