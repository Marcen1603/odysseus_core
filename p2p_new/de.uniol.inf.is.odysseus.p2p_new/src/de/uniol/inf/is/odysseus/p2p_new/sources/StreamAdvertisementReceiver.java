package de.uniol.inf.is.odysseus.p2p_new.sources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;

public class StreamAdvertisementReceiver implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(StreamAdvertisementReceiver.class);
	
	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if( adv instanceof StreamAdvertisement ) {
			final StreamAdvertisement srcAdv = (StreamAdvertisement) adv;
			LOG.debug("Got source advertisement of source {}", srcAdv.getAccessAO());
	
			final AccessAO accessAO = srcAdv.getAccessAO();
			final IDataDictionary dataDictionary = DataDictionaryService.get();
			if (!dataDictionary.containsViewOrStream(accessAO.getSourcename(), SessionManagementService.getActiveSession())) {
	
				final ILogicalOperator timestampAO = addTimestampAO(accessAO, null);
				dataDictionary.addEntitySchema(accessAO.getSourcename(), accessAO.getOutputSchema(), SessionManagementService.getActiveSession());
				dataDictionary.setStream(accessAO.getSourcename(), timestampAO, SessionManagementService.getActiveSession());
	
				LOG.debug("Registered {} in local data dictionary", accessAO);
			} else {
				LOG.debug("Source {} already registered in data dictionary", accessAO);
			}
		}
	}
	
	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		if( adv instanceof StreamAdvertisement ) {
			final StreamAdvertisement srcAdv = (StreamAdvertisement) adv;
			
			DataDictionaryService.get().removeViewOrStream(srcAdv.getAccessAO().getSourcename(), SessionManagementService.getActiveSession());
		}
	}
	

	private static ILogicalOperator addTimestampAO(ILogicalOperator operator, String dateFormat) {
		final TimestampAO timestampAO = new TimestampAO();
		timestampAO.setDateFormat(dateFormat);
		if (operator.getOutputSchema() != null) {

			for (final SDFAttribute attr : operator.getOutputSchema()) {
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
