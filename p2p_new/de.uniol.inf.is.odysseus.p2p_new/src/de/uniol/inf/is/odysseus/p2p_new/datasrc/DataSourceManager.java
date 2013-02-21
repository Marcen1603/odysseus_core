package de.uniol.inf.is.odysseus.p2p_new.datasrc;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.adv.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;

public class DataSourceManager implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(DataSourceManager.class);

	private DataSourcePublisher dataSourcePublisher;
	private IDataDictionary dataDictionary;

	@Override
	public void advertisementOccured(IAdvertisementManager sender, Advertisement adv) {
		SourceAdvertisement srcAdv = (SourceAdvertisement) adv;
		LOG.debug("Got source advertisement of source {}", srcAdv.getAccessAO());

		AccessAO accessAO = srcAdv.getAccessAO();
		ILogicalOperator timestampAO = addTimestampAO(accessAO, null);
		
		if( !dataDictionary.containsViewOrStream(accessAO.getSourcename(), SessionManagementService.getActiveSession())) {
			dataSourcePublisher.addSourceAdvertisement(srcAdv);
			dataDictionary.addEntitySchema(accessAO.getSourcename(), accessAO.getOutputSchema(), SessionManagementService.getActiveSession());
			dataDictionary.setStream(accessAO.getSourcename(), timestampAO, SessionManagementService.getActiveSession());
			
			LOG.debug("Registered {} in local data dictionary", accessAO);
		} else {
			LOG.debug("Source {} already registered in data dictionary", accessAO);
		}
	}

	@Override
	public boolean isSelected(Advertisement advertisement) {
		return (advertisement instanceof SourceAdvertisement);
	}

	// called by OSGi-DS
	public void bindAdvertisementManager(IAdvertisementManager manager) {
		manager.addAdvertisementListener(this);

		LOG.debug("Bound AdvertisementManager {}", manager);
	}

	// called by OSGi-DS
	public void unbindAdvertisementManager(IAdvertisementManager manager) {
		manager.removeAdvertisementListener(this);

		LOG.debug("Unbound AdvertisementManager {}", manager);
	}

	// called by OSGi-DS
	public void bindDataDictionary(IDataDictionary dd) {
		dataSourcePublisher = new DataSourcePublisher(dd, P2PNewPlugIn.getDiscoveryService());
		dataSourcePublisher.start();

		dataDictionary = dd;
		LOG.debug("Bound DataDictionary {}", dd);
	}

	// called by OSGi-DS
	public void unbindDataDictionary(IDataDictionary dd) {
		if( dd == dataDictionary ) {
			dataSourcePublisher.stopRunning();
			dataDictionary = null;
	
			LOG.debug("Unbound DataDictionary {}", dd);
		}
	}

	private static ILogicalOperator addTimestampAO(ILogicalOperator operator, String dateFormat) {
		TimestampAO timestampAO = new TimestampAO();
		timestampAO.setDateFormat(dateFormat);
		if (operator.getOutputSchema() != null) {

			for (SDFAttribute attr : operator.getOutputSchema()) {
				if (SDFDatatype.START_TIMESTAMP.toString().equalsIgnoreCase(attr.getDatatype().getURI()) || SDFDatatype.START_TIMESTAMP_STRING.toString().equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setStartTimestamp(attr);
				}

				if (SDFDatatype.END_TIMESTAMP.toString().equalsIgnoreCase(attr.getDatatype().getURI()) || SDFDatatype.END_TIMESTAMP_STRING.toString().equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setEndTimestamp(attr);
				}

			}
		}
		timestampAO.subscribeTo(operator, operator.getOutputSchema());
		timestampAO.setName(timestampAO.getStandardName());
		return timestampAO;
	}
}
