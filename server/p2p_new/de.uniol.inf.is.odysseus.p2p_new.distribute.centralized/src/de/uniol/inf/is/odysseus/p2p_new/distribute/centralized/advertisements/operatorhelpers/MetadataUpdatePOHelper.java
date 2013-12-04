package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SameTimeFactory;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SystemTimeIntervalFactory;

@SuppressWarnings("rawtypes")
public class MetadataUpdatePOHelper extends AbstractPhysicalOperatorHelper<MetadataUpdatePO> {
	private static String METADATA_FACTORY_TAG = "metadata_factory";
	private static String RELATIONAL_TIMESTAMP_FACTORY_YEAR = "year";
	private static String RELATIONAL_TIMESTAMP_FACTORY_MONTH = "month";
	private static String RELATIONAL_TIMESTAMP_FACTORY_DAY = "day";
	private static String RELATIONAL_TIMESTAMP_FACTORY_HOUR = "hour";
	private static String RELATIONAL_TIMESTAMP_FACTORY_MINUTE = "minute";
	private static String RELATIONAL_TIMESTAMP_FACTORY_SECOND = "second";
	private static String RELATIONAL_TIMESTAMP_FACTORY_MILLISECOND = "millisecond";
	private static String RELATIONAL_TIMESTAMP_FACTORY_FACTOR = "factor";
	private static String RELATIONAL_TIMESTAMP_FACTORY_CLEAREND = "clearend";
	private static String RELATIONAL_TIMESTAMP_FACTORY_TIMEZONE = "timezone";
	
	@Override
	public Class<MetadataUpdatePO> getOperatorClass() {
		return MetadataUpdatePO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType, StructuredDocument rootDoc, Element toAppendTo) {
		MetadataUpdatePO<?,?> mdcpo = (MetadataUpdatePO<?,?>)o;
		IMetadataUpdater<?,?> updater = mdcpo.getMetadataFactory();
		toAppendTo.appendChild(rootDoc.createElement(METADATA_FACTORY_TAG,updater.getName()));
		if(updater.getClass().getSimpleName().equals(RelationalTimestampAttributeTimeIntervalMFactory.class.getSimpleName())) {
			RelationalTimestampAttributeTimeIntervalMFactory r = (RelationalTimestampAttributeTimeIntervalMFactory)updater;
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_YEAR,Integer.toString(r.getStartTimestampYearPos())));
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_MONTH,Integer.toString(r.getStartTimestampMonthPos())));
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_DAY,Integer.toString(r.getStartTimestampDayPos())));
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_HOUR,Integer.toString(r.getStartTimestampHourPos())));
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_MINUTE,Integer.toString(r.getStartTimestampMinutePos())));
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_SECOND,Integer.toString(r.getStartTimestampSecondPos())));
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_MILLISECOND,Integer.toString(r.getStartTimestampMillisecondPos())));
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_FACTOR,Integer.toString(r.getFactor())));
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_CLEAREND,Boolean.toString(r.isClearEnd())));
			toAppendTo.appendChild(rootDoc.createElement(RELATIONAL_TIMESTAMP_FACTORY_TIMEZONE,r.getTimezone().getID()));
		}
		return rootDoc;
	}

	@SuppressWarnings("unchecked")
	@Override
	SimpleImmutableEntry<Integer, MetadataUpdatePO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		IMetadataUpdater<?,?> updater = null;
		String metadataFactoryString = "";
		int year = -1;
		int month = -1;
		int day = -1;
		int hour = -1;
		int minute = -1;
		int second = -1;
		int millisecond = -1;
		int factor = -1;
		boolean clearEnd = false;
		String timeZoneID = "";
		
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(METADATA_FACTORY_TAG)) {
				metadataFactoryString = elem.getTextValue();
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_YEAR)) {
				year = Integer.parseInt(elem.getTextValue());
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_MONTH)) {
				month = Integer.parseInt(elem.getTextValue());
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_DAY)) {
				day = Integer.parseInt(elem.getTextValue());
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_HOUR)) {
				hour = Integer.parseInt(elem.getTextValue());
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_MINUTE)) {
				minute = Integer.parseInt(elem.getTextValue());
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_SECOND)) {
				second = Integer.parseInt(elem.getTextValue());
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_MILLISECOND)) {
				millisecond = Integer.parseInt(elem.getTextValue());
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_FACTOR)) {
				factor = Integer.parseInt(elem.getTextValue());
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_CLEAREND)) {
				clearEnd = Boolean.parseBoolean(elem.getTextValue());
			} else if(elem.getName().equals(RELATIONAL_TIMESTAMP_FACTORY_TIMEZONE)) {
				timeZoneID = elem.getTextValue();
			}
		}
		if(metadataFactoryString.equals(SameTimeFactory.class.getSimpleName())) {
			updater = new SameTimeFactory<ITimeInterval, IStreamObject<ITimeInterval>>();
		} else if (metadataFactoryString.equals(SystemTimeIntervalFactory.class.getSimpleName())) {
			updater = new SystemTimeIntervalFactory<ITimeInterval, IStreamObject<ITimeInterval>>();;
		} else if (metadataFactoryString.equals(RelationalTimestampAttributeTimeIntervalMFactory.class.getSimpleName())) {
			updater = new RelationalTimestampAttributeTimeIntervalMFactory(year, month, day, hour, minute, second, millisecond,factor, clearEnd, timeZoneID);
		}

		MetadataUpdatePO result = new MetadataUpdatePO(updater);
		return new SimpleImmutableEntry<Integer, MetadataUpdatePO>(operatorId,result);
	}
}
