package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.MSG_IVEF;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.AISSentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.AISSentenceHandler;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.StaticAndVoyageData;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.logicaloperator.ConversionType;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteRootElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;

public class ShipRoute025ConverterPO <T extends IStreamObject<IMetaAttribute>>
extends AbstractPipe<T, T>{
	private final Logger LOG = LoggerFactory
			.getLogger(ShipRoute025ConverterPO.class);
	private ConversionType conversionType;
	private AISSentenceHandler aishandler = new AISSentenceHandler();
	private List<IShipRouteRootElement> cachedJSONElements = new ArrayList<IShipRouteRootElement>();
	private List<IECRoute> cachedIECElements = new ArrayList<IECRoute>();
	private StaticAndVoyageData staticAndVoyageData = null;

	public ShipRoute025ConverterPO(ShipRoute025ConverterPO<T> anotherPO) {
		super();
		this.conversionType = anotherPO.conversionType;
	}

	public ShipRoute025ConverterPO(ConversionType conversionType) {
		super();
		this.conversionType = conversionType;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public ShipRoute025ConverterPO<T> clone() {
		return new ShipRoute025ConverterPO<T>(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ShipRoute015ConverterPO)) {
			return false;
		}
		ShipRoute025ConverterPO converterPO = (ShipRoute025ConverterPO) ipo;
		return this.conversionType == converterPO.conversionType;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		KeyValueObject<? extends IMetaAttribute> received = (KeyValueObject<? extends IMetaAttribute>) object;

		MSG_IVEF ivef = null;

		switch (conversionType) {
		case JSON_NMEA_TO_IVEF:
			ivef = convertJSONToIVEF(received);
			break;
		case IEC_NMEA_TO_IVEF:
			ivef = convertIECToIVEF(received);
			break;
		case IVEF_TO_JSON_ROUTE:
			convertIVEFToJSONRoute(received);
			break;
		case IVEF_TO_JSON_PREDICTION:
			convertIVEFToJSONPrediction(received);
			break;
		case IVEF_TO_JSON_MANOEUVRE:
			convertIVEFToJSONManoeuvre(received);
			break;
		case IVEF_TO_IEC:
			convertIVEFToIEC(received);
			break;
		default:
			break;
		}

		if (ivef != null) {
			KeyValueObject<? extends IMetaAttribute> next = ivef.toMap();
			next.setMetadata("object", ivef);
			transfer((T) next);
		} 
	}

	@SuppressWarnings("unchecked")
	private MSG_IVEF convertJSONToIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		MSG_IVEF ivef = null;
		if (received.getMetadata("originalNMEA") != null) {
			if (staticAndVoyageData == null) {
				staticAndVoyageData = getMMSIFromNMEA(received);
				// process cached elements
				if (staticAndVoyageData != null) {
					for (IShipRouteRootElement element : cachedJSONElements) {
						if (element instanceof RouteDataItem) {
							ivef = ToIVEF025ConverterHelper
									.convertShipRouteToIVEF(
											(RouteDataItem) element,
											staticAndVoyageData);
						} else if (element instanceof ManoeuvrePlanDataItem) {
							ivef = ToIVEF025ConverterHelper
									.convertManoeuvreToIVEF(
											(ManoeuvrePlanDataItem) element,
											staticAndVoyageData);
						} else if (element instanceof PredictionDataItem) {
							ivef = ToIVEF025ConverterHelper
									.convertPredictionToIVEF(
											(PredictionDataItem) element,
											staticAndVoyageData);
						}
						if (ivef != null) {
							KeyValueObject<? extends IMetaAttribute> next = ivef
									.toMap();
							next.setMetadata("object", ivef);
							transfer((T) next);
						}
					}
					ivef = null;
					cachedJSONElements.clear();
				}
			}
		} else if (received.getMetadata("object") instanceof RouteDataItem) {
			RouteDataItem dataItem = (RouteDataItem) received
					.getMetadata("object");
			if (staticAndVoyageData != null) {
				ivef = ToIVEF025ConverterHelper.convertShipRouteToIVEF(dataItem,
						staticAndVoyageData);
			} else {
				cachedJSONElements.add(dataItem);
			}
		} else if (received.getMetadata("object") instanceof ManoeuvrePlanDataItem) {
			ManoeuvrePlanDataItem dataItem = (ManoeuvrePlanDataItem) received
					.getMetadata("object");
			if (staticAndVoyageData != null) {
				ivef = ToIVEF025ConverterHelper.convertManoeuvreToIVEF(dataItem,
						staticAndVoyageData);
			} else {
				cachedJSONElements.add(dataItem);
			}
		} else if (received.getMetadata("object") instanceof PredictionDataItem){
			PredictionDataItem dataItem = (PredictionDataItem) received.getMetadata("object");
			if (staticAndVoyageData != null){
				ivef = ToIVEF025ConverterHelper
						.convertPredictionToIVEF(dataItem,
								staticAndVoyageData);
			} else {
				cachedJSONElements.add(dataItem);
			}
		}
		
		else {
			LOG.debug("Element contains no Route or ManoeuvrePlan");
		}
		return ivef;
	}

	@SuppressWarnings("unchecked")
	private MSG_IVEF convertIECToIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		MSG_IVEF ivef = null;
		if (received.getMetadata("originalNMEA") != null) {
			if (staticAndVoyageData == null) {
				staticAndVoyageData = getMMSIFromNMEA(received);
				// process cached elements
				if (staticAndVoyageData != null) {
					for (IECRoute cachedIECRoute : cachedIECElements) {
						ivef = ToIVEF025ConverterHelper.convertIECToIVEF025(
								cachedIECRoute, staticAndVoyageData);
						KeyValueObject<? extends IMetaAttribute> next = ivef
								.toMap();
						next.setMetadata("object", ivef);
						transfer((T) next);
					}
					ivef = null;
					cachedIECElements.clear();
				}
			}
		} else if (received.getMetadata("object") instanceof IECRoute) {
			IECRoute route = (IECRoute) received.getMetadata("object");
			if (staticAndVoyageData != null) {
				ivef = ToIVEF025ConverterHelper.convertIECToIVEF025(route,
						staticAndVoyageData);
			} else {
				cachedIECElements.add(route);
			}
		} else {
			LOG.error("Cannot convert IEC to IVEF, because Datastream contains "
					+ "no IEC Elements");
		}
		return ivef;
	}

	@SuppressWarnings("unchecked")
	private void convertIVEFToJSONRoute(
			KeyValueObject<? extends IMetaAttribute> received) {
		if (received.getMetadata("object") instanceof MSG_IVEF) {
			MSG_IVEF msg_ivef = (MSG_IVEF) received
					.getMetadata("object");
			List<IShipRouteRootElement> shipRouteElements = ToJSONConverter
					.convertIVEF025ToRoute(msg_ivef);
			for (IShipRouteRootElement iShipRouteRootElement : shipRouteElements) {
				KeyValueObject<? extends IMetaAttribute> next = iShipRouteRootElement
						.toMap();
				next.setMetadata("object", iShipRouteRootElement);
				transfer((T) next);
			}
		} else {
			LOG.error("Cannot convert IVEF to Route, because Datastream contains "
					+ "no IVEF Elements");
		}
	}

	@SuppressWarnings("unchecked")
	private void convertIVEFToJSONPrediction(
			KeyValueObject<? extends IMetaAttribute> received) {
		if (received.getMetadata("object") instanceof MSG_IVEF) {
			MSG_IVEF msg_ivef = (MSG_IVEF) received
					.getMetadata("object");
			List<IShipRouteRootElement> shipRouteElements = ToJSONConverter
					.convertIVEF025ToPrediction(msg_ivef);
			for (IShipRouteRootElement iShipRouteRootElement : shipRouteElements) {
				KeyValueObject<? extends IMetaAttribute> next = iShipRouteRootElement
						.toMap();
				next.setMetadata("object", iShipRouteRootElement);
				transfer((T) next);
			}
		} else {
			LOG.error("Cannot convert IVEF to Prediction, because Datastream contains "
					+ "no IVEF Elements");
		}
	}

	@SuppressWarnings("unchecked")
	private void convertIVEFToJSONManoeuvre(
			KeyValueObject<? extends IMetaAttribute> received) {
		if (received.getMetadata("object") instanceof MSG_IVEF) {
			MSG_IVEF msg_ivef = (MSG_IVEF) received
					.getMetadata("object");
			List<IShipRouteRootElement> shipRouteElements = ToJSONConverter
					.convertIVEF025ToManoeuvre(msg_ivef);
			for (IShipRouteRootElement iShipRouteRootElement : shipRouteElements) {
				KeyValueObject<? extends IMetaAttribute> next = iShipRouteRootElement
						.toMap();
				next.setMetadata("object", iShipRouteRootElement);
				transfer((T) next);
			}
		} else {
			LOG.error("Cannot convert IVEF to ManoeuvrePlan, because Datastream contains "
					+ "no IVEF Elements");
		}
	}

	@SuppressWarnings("unchecked")
	private void convertIVEFToIEC(
			KeyValueObject<? extends IMetaAttribute> received) {
		if (received.getMetadata("object") instanceof MSG_IVEF) {
			MSG_IVEF msg_ivef = (MSG_IVEF) received
					.getMetadata("object");
			List<IECRoute> iecs = ToIECConverterHelper
					.convertIVEF025ToIEC(msg_ivef);
			for (IECRoute iecRoute : iecs) {
				if (iecRoute.isValid()) {
					KeyValueObject<? extends IMetaAttribute> next = iecRoute
							.toMap();
					next.setMetadata("object", iecRoute);
					transfer((T) next);
				} else {
					LOG.warn("IEC Element is invalid => not processed");
				}
			}
		} else {
			LOG.error("Cannot convert IVEF to IEC, because Datastream contains "
					+ "no IVEF Elements");
		}
	}
	
	private StaticAndVoyageData getMMSIFromNMEA(
			KeyValueObject<? extends IMetaAttribute> received) {
		if (received.getMetadata("originalNMEA") instanceof AISSentence) {
			AISSentence ais = (AISSentence) received
					.getMetadata("originalNMEA");
			if (ais.getSentenceId().toUpperCase().equals("VDO")
					|| ais.getSentenceId().toUpperCase().equals("VDM")) {
				this.aishandler.handleAISSentence(ais);
				if (this.aishandler.getDecodedAISMessage() != null) {
					if (this.aishandler.getDecodedAISMessage() instanceof StaticAndVoyageData) {
						StaticAndVoyageData ownShipStaticData = (StaticAndVoyageData) this.aishandler
								.getDecodedAISMessage();
						return ownShipStaticData;
					}
					this.aishandler.resetDecodedAISMessage();
				}
			}
		}
		return null;
	}
}
