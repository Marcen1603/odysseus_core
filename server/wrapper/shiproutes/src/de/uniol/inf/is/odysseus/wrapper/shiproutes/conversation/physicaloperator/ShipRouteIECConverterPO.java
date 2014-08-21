package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.logicaloperator.ConversionType;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteRootElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;

public class ShipRouteIECConverterPO<T extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<T, T> {
	private final Logger LOG = LoggerFactory
			.getLogger(ShipRouteIECConverterPO.class);
	private ConversionType conversionType;

	public ShipRouteIECConverterPO(ShipRouteIECConverterPO<T> anotherPO) {
		super();
		this.conversionType = anotherPO.conversionType;
	}

	public ShipRouteIECConverterPO(ConversionType conversionType) {
		super();
		this.conversionType = conversionType;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public ShipRouteIECConverterPO<T> clone() {
		return new ShipRouteIECConverterPO<T>(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ShipRouteIECConverterPO)) {
			return false;
		}
		ShipRouteIECConverterPO converterPO = (ShipRouteIECConverterPO) ipo;
		return this.conversionType == converterPO.conversionType;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		KeyValueObject<? extends IMetaAttribute> received = (KeyValueObject<? extends IMetaAttribute>) object;

		IECRoute iec = null;
		IShipRouteRootElement shipRouteElement = null;

		switch (conversionType) {
		case SHIPROUTETOIEC:
			if (received.getMetadata("object") instanceof RouteDataItem) {
				RouteDataItem dataItem = (RouteDataItem) received
						.getMetadata("object");
				iec = ToIECConverterHelper.convertShipRouteToIEC(dataItem);

			} else if (received.getMetadata("object") instanceof PredictionDataItem) {
				PredictionDataItem dataItem = (PredictionDataItem) received
						.getMetadata("object");
				iec = ToIECConverterHelper.convertPredictionToIEC(dataItem);

			} else if (received.getMetadata("object") instanceof ManoeuvrePlanDataItem) {
				ManoeuvrePlanDataItem dataItem = (ManoeuvrePlanDataItem) received
						.getMetadata("object");
				iec = ToIECConverterHelper.convertManoeuvreToIEC(dataItem);

			} else {
				LOG.error("Cannot convert ShipRoute to IEC, because Datastream contains "
						+ "no ShipRoutes, Predictions or ManoeuvrePlans");
			}
			break;
		case IECTOROUTE:
			if (received.getMetadata("object") instanceof IECRoute) {
				IECRoute route = (IECRoute) received.getMetadata("object");
				shipRouteElement = ToShipRouteConverter
						.convertIECToShipRoute(route);
			} else {
				LOG.error("Cannot convert IEC to Route, because Datastream contains "
						+ "no IEC Elements");
			}
			break;
		case IECTOPREDICTION:
			if (received.getMetadata("object") instanceof IECRoute) {
				IECRoute route = (IECRoute) received.getMetadata("object");
				shipRouteElement = ToShipRouteConverter
						.convertIECToPrediction(route);
			} else {
				LOG.error("Cannot convert IEC to Prediction, because Datastream contains "
						+ "no IEC Elements");
			}
			break;
		case IECTOMANOEUVRE:
			if (received.getMetadata("object") instanceof IECRoute) {
				IECRoute route = (IECRoute) received.getMetadata("object");
				shipRouteElement = ToShipRouteConverter
						.convertIECToManoeuvre(route);
			} else {
				LOG.error("Cannot convert IEC to ManoeuvrePlan, because Datastream contains "
						+ "no IEC Elements");
			}
			break;
		default:
			break;
		}

		if (iec != null) {
			KeyValueObject<? extends IMetaAttribute> next = iec.toMap();
			next.setMetadata("object", iec);
			transfer((T) next);
		} else if (shipRouteElement != null) {
			KeyValueObject<? extends IMetaAttribute> next = shipRouteElement
					.toMap();
			next.setMetadata("object", shipRouteElement);
			transfer((T) next);
		}
	}

}
