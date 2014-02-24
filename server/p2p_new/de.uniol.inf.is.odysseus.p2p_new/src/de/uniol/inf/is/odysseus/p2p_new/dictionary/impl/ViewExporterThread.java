package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class ViewExporterThread extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(ViewExporterThread.class);
	private final IP2PDictionary dictionary;
	private final int advLifeTime;
	
	public ViewExporterThread(int intervalMillis, int advLifeTime, IP2PDictionary dictionary) {
		super(intervalMillis, "P2PDictionary exporter thread");
		Preconditions.checkNotNull(dictionary, "P2PDictionary must not be null!");
		Preconditions.checkArgument(advLifeTime > 0, "Lifetime for advertisements must be positive");
		
		this.dictionary = dictionary;
		this.advLifeTime = advLifeTime;
	}
	
	@Override
	public void doJob() {
		ImmutableList<SourceAdvertisement> exportedSources = dictionary.getExportedSources();
		for( SourceAdvertisement exportedSource : exportedSources ) {
			// only views must be exported regulary
			if( exportedSource.isView() ) {
				try {
					JxtaServicesProvider.getInstance().getDiscoveryService().flushAdvertisement(exportedSource);
					JxtaServicesProvider.getInstance().getDiscoveryService().remotePublish(exportedSource, advLifeTime);
				} catch (IOException e) {
					LOG.error("Could not locally publish exported source {}", exportedSource.getName(), e);
				}
			}
		}
	}
}
