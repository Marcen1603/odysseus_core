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
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.MSG_VesselData;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.AISSentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.AISSentenceHandler;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.StaticAndVoyageData;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.logicaloperator.ConversionType;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteRootElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;

public class ShipRouteConverterPO<T extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<T, T> {
	private final Logger LOG = LoggerFactory
			.getLogger(ShipRouteConverterPO.class);
	private ConversionType conversionType;
	private AISSentenceHandler aishandler = new AISSentenceHandler();
	private List<IShipRouteRootElement> cachedJSONElements = new ArrayList<IShipRouteRootElement>();
	private List<IECRoute> cachedIECElements = new ArrayList<IECRoute>();
	private StaticAndVoyageData staticAndVoyageData = null;

	public ShipRouteConverterPO(ShipRouteConverterPO<T> anotherPO) {
		super();
		this.conversionType = anotherPO.conversionType;
	}

	public ShipRouteConverterPO(ConversionType conversionType) {
		super();
		this.conversionType = conversionType;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public ShipRouteConverterPO<T> clone() {
		return new ShipRouteConverterPO<T>(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ShipRouteConverterPO)) {
			return false;
		}
		ShipRouteConverterPO converterPO = (ShipRouteConverterPO) ipo;
		return this.conversionType == converterPO.conversionType;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		KeyValueObject<? extends IMetaAttribute> received = (KeyValueObject<? extends IMetaAttribute>) object;

		IECRoute iec = null;
		MSG_VesselData ivef = null;
		IShipRouteRootElement shipRouteElement = null;

		switch (conversionType) {
		case JSON_TO_IEC:
			iec = convertJSONToIEC(received);
			break;
		case JSON_NMEA_TO_IVEF:
			ivef = convertJSONToIVEF(received);
			break;
		case IEC_TO_JSON_ROUTE:
			shipRouteElement = convertIECToJSONRoute(received);
			break;
		case IEC_TO_JSON_PREDICTION:
			shipRouteElement = convertIECToJSONPrediction(received);
			break;
		case IEC_TO_JSON_MANOEUVRE:
			shipRouteElement = convertIECToJSONManoeuvre(received);
			break;
		case IEC_NMEA_TO_IVEF:
			ivef = convertIECToIVEF(received);
			break;
		default:
			break;
		}

		if (iec != null) {
			KeyValueObject<? extends IMetaAttribute> next = iec.toMap();
			next.setMetadata("object", iec);
			transfer((T) next);
		} else if (ivef != null) {
			KeyValueObject<? extends IMetaAttribute> next = ivef.toMap();
			next.setMetadata("object", ivef);
			transfer((T) next);
		} else if (shipRouteElement != null) {
			KeyValueObject<? extends IMetaAttribute> next = shipRouteElement
					.toMap();
			next.setMetadata("object", shipRouteElement);
			transfer((T) next);
		}
	}

	@SuppressWarnings("unchecked")
	private MSG_VesselData convertJSONToIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		MSG_VesselData ivef = null;
		if (received.getMetadata("originalNMEA") != null) {
			if (staticAndVoyageData == null) {
				staticAndVoyageData = getMMSIFromNMEA(received);
				// process cached elements
				if (staticAndVoyageData != null) {
					for (IShipRouteRootElement element : cachedJSONElements) {
						if (element instanceof RouteDataItem) {
							ivef = ToIVEFConverterHelper
									.convertShipRouteToIVEF(
											(RouteDataItem) element, staticAndVoyageData);
						} else if (element instanceof ManoeuvrePlanDataItem) {
							ivef = ToIVEFConverterHelper
									.convertManoeuvreToIVEF(
											(ManoeuvrePlanDataItem) element,
											staticAndVoyageData);
						}
						if (ivef != null) {
							KeyValueObject<? extends IMetaAttribute> next = ivef
									.toMap();
							next.setMetadata("object", ivef);
							transfer((T) next);
						}
					}
					cachedJSONElements.clear();
				}
			}
		} else if (received.getMetadata("object") instanceof RouteDataItem) {
			RouteDataItem dataItem = (RouteDataItem) received
					.getMetadata("object");
			if (staticAndVoyageData != null) {
				ivef = ToIVEFConverterHelper.convertShipRouteToIVEF(
						dataItem, staticAndVoyageData);
			} else {
				cachedJSONElements.add(dataItem);
			}
		} else if (received.getMetadata("object") instanceof ManoeuvrePlanDataItem) {
			ManoeuvrePlanDataItem dataItem = (ManoeuvrePlanDataItem) received
					.getMetadata("object");
			if (staticAndVoyageData != null) {
				ivef = ToIVEFConverterHelper.convertManoeuvreToIVEF(
						dataItem, staticAndVoyageData);
			} else {
				cachedJSONElements.add(dataItem);
			}
		} else {
			LOG.debug("Element contains no Route or ManoeuvrePlan");
		}
		return ivef;
	}

	@SuppressWarnings("unchecked")
	private MSG_VesselData convertIECToIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		MSG_VesselData ivef = null;
		if (received.getMetadata("originalNMEA") != null) {
			if (staticAndVoyageData == null) {
				staticAndVoyageData = getMMSIFromNMEA(received);
				// process cached elements
				if (staticAndVoyageData != null) {
					for (IECRoute cachedIECRoute : cachedIECElements) {
						ivef = ToIVEFConverterHelper.convertIECToIVEF(cachedIECRoute, staticAndVoyageData);
						KeyValueObject<? extends IMetaAttribute> next = ivef.toMap();
						next.setMetadata("object", ivef);
						transfer((T) next);
					}
					cachedIECElements.clear();
				}
			}
		} else if (received.getMetadata("object") instanceof IECRoute) {
			IECRoute route = (IECRoute) received.getMetadata("object");
			if (staticAndVoyageData != null) {
				ivef = ToIVEFConverterHelper.convertIECToIVEF(route, staticAndVoyageData);					
			} else {
				cachedIECElements.add(route);
			}
		} else {
			LOG.error("Cannot convert IEC to IVEF, because Datastream contains "
					+ "no IEC Elements");
		}
		return ivef;
	}

	private IShipRouteRootElement convertIECToJSONManoeuvre(
			KeyValueObject<? extends IMetaAttribute> received) {
		IShipRouteRootElement shipRouteElement = null;
		if (received.getMetadata("object") instanceof IECRoute) {
			IECRoute route = (IECRoute) received.getMetadata("object");
			shipRouteElement = ToJSONConverter
					.convertIECToManoeuvre(route);
		} else {
			LOG.error("Cannot convert IEC to ManoeuvrePlan, because Datastream contains "
					+ "no IEC Elements");
		}
		return shipRouteElement;
	}

	private IShipRouteRootElement convertIECToJSONPrediction(
			KeyValueObject<? extends IMetaAttribute> received) {
		IShipRouteRootElement shipRouteElement = null;
		if (received.getMetadata("object") instanceof IECRoute) {
			IECRoute route = (IECRoute) received.getMetadata("object");
			shipRouteElement = ToJSONConverter
					.convertIECToPrediction(route);
		} else {
			LOG.error("Cannot convert IEC to Prediction, because Datastream contains "
					+ "no IEC Elements");
		}
		return shipRouteElement;
	}

	private IShipRouteRootElement convertIECToJSONRoute(
			KeyValueObject<? extends IMetaAttribute> received) {
		IShipRouteRootElement shipRouteElement = null;
		if (received.getMetadata("object") instanceof IECRoute) {
			IECRoute route = (IECRoute) received.getMetadata("object");
			shipRouteElement = ToJSONConverter
					.convertIECToJSON(route);
		} else {
			LOG.error("Cannot convert IEC to Route, because Datastream contains "
					+ "no IEC Elements");
		}
		return shipRouteElement;
	}

	private IECRoute convertJSONToIEC(
			KeyValueObject<? extends IMetaAttribute> received) {
		IECRoute iec = null;
		
		if (received.getMetadata("object") instanceof RouteDataItem) {
			RouteDataItem dataItem = (RouteDataItem) received
					.getMetadata("object");
			iec = ToIECConverterHelper.convertJSONShipRouteToIEC(dataItem);

		} else if (received.getMetadata("object") instanceof PredictionDataItem) {
			PredictionDataItem dataItem = (PredictionDataItem) received
					.getMetadata("object");
			iec = ToIECConverterHelper.convertJSONPredictionToIEC(dataItem);

		} else if (received.getMetadata("object") instanceof ManoeuvrePlanDataItem) {
			ManoeuvrePlanDataItem dataItem = (ManoeuvrePlanDataItem) received
					.getMetadata("object");
			iec = ToIECConverterHelper.convertJSONManoeuvreToIEC(dataItem);

		} else {
			LOG.error("Cannot convert ShipRoute to IEC, because Datastream contains "
					+ "no ShipRoutes, Predictions or ManoeuvrePlans");
		}
		return iec;
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
