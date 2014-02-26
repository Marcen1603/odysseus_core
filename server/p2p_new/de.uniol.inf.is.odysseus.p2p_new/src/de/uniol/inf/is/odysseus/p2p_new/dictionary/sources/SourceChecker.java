package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class SourceChecker extends RepeatingJobThread {

	private static final String THREAD_NAME = "Source check thread";
	private static final long CHECK_INTERVAL_MILLIS = 10 * 1000;
	
	private final P2PDictionary dictionary;
	
	public SourceChecker(P2PDictionary dictionary) {
		super(CHECK_INTERVAL_MILLIS, THREAD_NAME);
		Preconditions.checkNotNull(dictionary, "P2PDictionary to check sources must not be null!");
		
		this.dictionary = dictionary;
	}
	
	@Override
	public void doJob() {
		ImmutableList<SourceAdvertisement> sources = dictionary.getSources();
		for( SourceAdvertisement source : sources ) {
			if( !dictionary.isExported(source.getName()) ) {
				if( !dictionary.checkSource(source)) {
					dictionary.removeSource(source);
				}
			}
		}
	}
}
