package de.uniol.inf.is.odysseus.wrapper.ivef.conversion.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.conversion.logicaloperator.ConversionType;
import de.uniol.inf.is.odysseus.wrapper.ivef.spatial.Ellipsoid;
import de.uniol.inf.is.odysseus.wrapper.ivef.spatial.GeodeticCalculator;
import de.uniol.inf.is.odysseus.wrapper.ivef.spatial.GlobalCoordinates;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Unit;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.AISSentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.Sentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.TTMSentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.AISSentenceHandler;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.PositionReport;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.StaticAndVoyageData;

/**
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class Ivef104NmeaConverterPO<T extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<T, T> {

	private ConversionType conversionType;
	private IIvefElement ivef;
	private Sentence nmea;
	private int positionToStaticRatio;
	private PositionReport ownShipPosition;
	private AISSentence ownShip;
	private AISSentenceHandler aishandler;
	@SuppressWarnings("unused")
	private StaticAndVoyageData ownShipStaticData;

	public Ivef104NmeaConverterPO(Ivef104NmeaConverterPO<T> anotherPO) {
		super();
		this.ivef = anotherPO.ivef;
		this.conversionType = anotherPO.conversionType;
		this.nmea = anotherPO.nmea;
	}

	public Ivef104NmeaConverterPO(ConversionType conversionType,
			int PositionToStaticRatio) {
		super();
		this.ivef = null;
		this.conversionType = conversionType;
		this.positionToStaticRatio = PositionToStaticRatio;
		this.nmea = null;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof Ivef015NmeaConverterPO)) {
			return false;
		}
		Ivef104NmeaConverterPO ivefpo = (Ivef104NmeaConverterPO) ipo;
		return this.conversionType == ivefpo.conversionType
				&& this.positionToStaticRatio == ivefpo.positionToStaticRatio;
	}

	@Override
	protected void process_next(T object, int port) {
		KeyValueObject<? extends IMetaAttribute> received = (KeyValueObject<? extends IMetaAttribute>) object;
		switch (this.conversionType) {
		case TTM_IVEF:
			convertTTMtoIVEF(received);
			break;
		case IVEF_TTM:
			convertIVEFtoTTM(received);
			break;
		case AIS_IVEF:
			convertAIStoIVEF(received);
			break;
		case IVEF_AIS:
			convertIVEFtoAIS(received);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private void convertTTMtoIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		/***************************************
		 * Set the own ship (Ship ODYSSEUS)
		 * *************************************/
		KeyValueObject<? extends IMetaAttribute> sent = new KeyValueObject<>();
		if (received.getMetadata("originalNMEA") != null) {
			if (received.getMetadata("originalNMEA") instanceof AISSentence) {
				AISSentence ais = (AISSentence) received
						.getMetadata("originalNMEA");
				if (ais.getSentenceId().toUpperCase().equals("VDO")) {
					this.ownShip = ais;
					this.aishandler.handleAISSentence(this.ownShip);
					if (this.aishandler.getDecodedAISMessage() != null) {
						if (this.aishandler.getDecodedAISMessage() instanceof PositionReport)
							this.ownShipPosition = (PositionReport) this.aishandler
									.getDecodedAISMessage();
						else if (this.aishandler.getDecodedAISMessage() instanceof StaticAndVoyageData)
							this.ownShipStaticData = (StaticAndVoyageData) this.aishandler
									.getDecodedAISMessage();
						else {
							// Other decoded messages to be handled later if
							// required.
						}
						this.aishandler.resetDecodedAISMessage();
					}
				}
			}
			/**************************************************************************
			 * Data fusion: Convert the TTM message into IVEF using the OwnShip
			 * message
			 * ************************************************************************/
			else if (received.getMetadata("originalNMEA") instanceof TTMSentence) {
				// We can't generate IVEF before receiving the ownShipMessage
				if (this.ownShipPosition == null)
					return;
				TTMSentence ttm = (TTMSentence) received
						.getMetadata("originalNMEA");
				GeodeticCalculator geoClaculator = new GeodeticCalculator();
				GlobalCoordinates ownshipCoordinates = new GlobalCoordinates(
						this.ownShipPosition.getLatitude(),
						this.ownShipPosition.getLongitude());
				double distanceInMeters = ttm.getTargetDistance();
				if (ttm.getDistanceUnit() == Unit.KILOMETERS)
					distanceInMeters *= 1000;
				else if (ttm.getDistanceUnit() == Unit.NAUTICAL_MILES)
					distanceInMeters *= 1852;
				else if (ttm.getDistanceUnit() == Unit.STATUTE_MILES)
					distanceInMeters *= 1609.344;
				GlobalCoordinates targetCoordinates = geoClaculator
						.calculateEndingGlobalCoordinates(Ellipsoid.WGS84,
								ownshipCoordinates, ttm.getBearing(),
								distanceInMeters);

				// create IVEF 1.0.4 Element

				this.ivef.fillMap(sent);
				sent.setMetadata("object", this.ivef);
				transfer((T) sent);
			}
		}
	}

	private void convertIVEFtoTTM(
			KeyValueObject<? extends IMetaAttribute> received) {
		// TODO Auto-generated method stub

	}

	private void convertAIStoIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		// TODO Auto-generated method stub

	}

	private void convertIVEFtoAIS(
			KeyValueObject<? extends IMetaAttribute> received) {
		// TODO Auto-generated method stub

	}

}
