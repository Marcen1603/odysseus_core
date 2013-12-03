package de.uniol.inf.is.odysseus.peer.distribute.partition.survey;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.adv.CostQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.adv.CostQueryAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.adv.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.adv.CostResponseAdvertisementInstantiator;

public class SurveyBasedPartitionerPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(CostQueryAdvertisement.getAdvertisementType(), new CostQueryAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(CostResponseAdvertisement.getAdvertisementType(), new CostResponseAdvertisementInstantiator());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

}
