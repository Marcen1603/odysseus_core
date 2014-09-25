package de.uniol.inf.is.odysseus.wrapper.ivef.conversion.physicaloperator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.conversion.logicaloperator.ConversionType;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Body;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Construction;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Header;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Identifier;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.MSG_IVEF;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.NavStatus;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.ObjectData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.ObjectDatas;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Pos;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.TrackData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VoyageData;
import de.uniol.inf.is.odysseus.wrapper.ivef.spatial.Ellipsoid;
import de.uniol.inf.is.odysseus.wrapper.ivef.spatial.GeodeticCalculator;
import de.uniol.inf.is.odysseus.wrapper.ivef.spatial.GlobalCoordinates;
import de.uniol.inf.is.odysseus.wrapper.ivef.spatial.Vector2D;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.TargetNumber;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.TargetReference;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.TargetStatus;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Unit;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.AISSentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.Sentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.SentenceFactory;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.TLLSentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.TTMSentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.AISSentenceHandler;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.DecodedAISPayload;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.PositionReport;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.StaticAndVoyageData;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.IMO;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.NavigationStatus;

/**
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class Ivef025NmeaConverterPO<T extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<T, T> {

	private static final double KTS_TO_MS = 0.514444444;
	private ConversionType conversionType;
	private IIvefElement ivef;
	private Sentence nmea;
	private int positionToStaticRatio;
	private PositionReport ownShipPosition;
	private AISSentence ownShip;
	private AISSentenceHandler aishandler = new AISSentenceHandler();;

	/***************************************************
	 * Attributes for coastal ODYSSEUS
	 * *************************************************/
	/** Unique Id for MMSIs */
	private static Map<Long, Integer> mmsiToId = new HashMap<Long, Integer>();
	/** Storing the last position of the ship */
	private static Map<Long, Pos> mmsiToPos = new HashMap<Long, Pos>();

	private static Map<Long, Integer> mmsiToCount = new HashMap<Long, Integer>();

	private StaticAndVoyageData ownShipStaticData;
	private Map<Integer, TTMSentence> cachedTTMSentences = new HashMap<Integer, TTMSentence>();

	public Ivef025NmeaConverterPO(Ivef025NmeaConverterPO<T> anotherPO) {
		super();
		this.ivef = anotherPO.ivef;
		this.conversionType = anotherPO.conversionType;
		this.nmea = anotherPO.nmea;
	}

	public Ivef025NmeaConverterPO(ConversionType conversionType,
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
		Ivef025NmeaConverterPO ivefpo = (Ivef025NmeaConverterPO) ipo;
		return this.conversionType == ivefpo.conversionType
				&& this.positionToStaticRatio == ivefpo.positionToStaticRatio;
	}

	@Override
	protected void process_next(T object, int port) {
		KeyValueObject<? extends IMetaAttribute> received = (KeyValueObject<? extends IMetaAttribute>) object;

		updateOwnShip(received);

		switch (this.conversionType) {
		case TTM_IVEF:
			convertTTMtoIVEF(received);
			break;
		case TLL_IVEF:
			convertTLLtoIVEF(received);
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

	private void updateOwnShip(KeyValueObject<? extends IMetaAttribute> received) {
		// try to get the own position from NMEA VDO Messages
		if (received.getMetadata("originalNMEA") != null) {
			if (received.getMetadata("originalNMEA") instanceof AISSentence) {
				AISSentence ais = (AISSentence) received
						.getMetadata("originalNMEA");
				if (ais.getSentenceId().toUpperCase().equals("VDO")) {
					this.ownShip = ais;
					this.aishandler.handleAISSentence(this.ownShip);
					if (this.aishandler.getDecodedAISMessage() != null) {
						if (this.aishandler.getDecodedAISMessage() instanceof PositionReport) {
							this.ownShipPosition = (PositionReport) this.aishandler
									.getDecodedAISMessage();
						} else if (this.aishandler.getDecodedAISMessage() instanceof StaticAndVoyageData)
							this.ownShipStaticData = (StaticAndVoyageData) this.aishandler
									.getDecodedAISMessage();
						this.aishandler.resetDecodedAISMessage();
					}
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void convertTLLtoIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		KeyValueObject<? extends IMetaAttribute> sent = new KeyValueObject<>();
		if (received.getMetadata("originalNMEA") != null) {
			if (received.getMetadata("originalNMEA") instanceof TTMSentence) {
				TTMSentence ttm = (TTMSentence) received
						.getMetadata("originalNMEA");
				Integer targetNumber = new Integer(ttm.getTargetNumber()
						.getNumber());
				cachedTTMSentences.put(targetNumber, ttm);
			} else if (received.getMetadata("originalNMEA") instanceof TLLSentence) {
				TLLSentence tll = (TLLSentence) received
						.getMetadata("originalNMEA");
				Integer targetNumber = new Integer(tll.getTargetNumber().getNumber());
				TTMSentence ttm = cachedTTMSentences.get(targetNumber);
				if (ttm == null){
					return;
				}
				// create IVEF 0.2.5 Element
				this.ivef = new MSG_IVEF();
				Header header = prepareHeader();
				((MSG_IVEF) this.ivef).setHeader(header);

				TrackData trackData = prepareTrackDataFromTLL(tll, ttm);

				VesselData vesselData = prepareVesselDataFromTLL(tll);

				VoyageData voyageData = prepareVoyageDataFromTLL(tll);

				ObjectDatas objectDatas = new ObjectDatas();
				ObjectData objectData = new ObjectData();
				objectData.setTrackData(trackData);
				objectData.addVesselData(vesselData);
				objectData.addVoyageData(voyageData);
				objectDatas.addObjectData(objectData);

				Body body = new Body();
				body.setObjectDatas(objectDatas);
				((MSG_IVEF) this.ivef).setBody(body);
				this.ivef.fillMap(sent);
				sent.setMetadata("object", this.ivef);
				transfer((T) sent);
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void convertTTMtoIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		/***************************************
		 * Set the own ship (Ship ODYSSEUS)
		 * *************************************/
		KeyValueObject<? extends IMetaAttribute> sent = new KeyValueObject<>();
		if (received.getMetadata("originalNMEA") != null) {
			if (received.getMetadata("originalNMEA") instanceof TTMSentence) {
				// We can't generate IVEF before receiving the ownShipMessage
				this.ivef = new MSG_IVEF();

				if (this.ownShipPosition == null
						|| this.ownShipStaticData == null)
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

				// create IVEF 0.2.5 Element
				Header header = prepareHeader();
				((MSG_IVEF) this.ivef).setHeader(header);

				TrackData trackData = prepareTargetTrackData(ttm,
						targetCoordinates);

				VesselData vesselData = prepareTargetVesselData(ttm);

				VoyageData voyageData = prepareTargetVoyageData(ttm);

				ObjectDatas objectDatas = new ObjectDatas();
				ObjectData objectData = new ObjectData();
				objectData.setTrackData(trackData);
				objectData.addVesselData(vesselData);
				objectData.addVoyageData(voyageData);
				objectDatas.addObjectData(objectData);

				Body body = new Body();
				body.setObjectDatas(objectDatas);
				((MSG_IVEF) this.ivef).setBody(body);

				this.ivef.fillMap(sent);
				sent.setMetadata("object", this.ivef);
				transfer((T) sent);
			}
		}
	}

	private VoyageData prepareTargetVoyageData(TTMSentence ttm) {
		VoyageData voyageData = new VoyageData();
		voyageData.setId(ttm.getTargetNumber().getNumber());

		DateFormat df = new SimpleDateFormat("MM-dd HH:mm");
		Date parsedDate;
		try {
			parsedDate = df.parse(ownShipStaticData.getEta());
			Calendar parsedCalendar = Calendar.getInstance();
			parsedCalendar.setTime(parsedDate);
			parsedCalendar.set(Calendar.YEAR,
					Calendar.getInstance().get(Calendar.YEAR));

			voyageData.setETA(parsedCalendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		voyageData.setSourceId("3");
		voyageData.setSourceType(3);
		voyageData.setSourceName("ODYSSEUS");
		voyageData.setUpdateTime(new Date());
		return voyageData;
	}

	private VesselData prepareTargetVesselData(TTMSentence ttm) {
		VesselData vesselData = new VesselData();
		vesselData.setId(ttm.getTargetNumber().getNumber());
		vesselData.setSourceId("0");
		vesselData.setSourceName("ODYSSEUS");
		vesselData.setSourceType(3);
		vesselData.setUpdateTime(new Date());

		Construction construction = new Construction();
		vesselData.setConstruction(construction);

		Identifier identifier = new Identifier();
		if (ttm.getTargetNumber() != null)
			identifier.setMMSI(ttm.getTargetNumber().getNumber());
		if (ttm.getTargetLabel() != null)
			identifier.setName(ttm.getTargetLabel());
		vesselData.setIdentifier(identifier);
		return vesselData;
	}

	private TrackData prepareTargetTrackData(TTMSentence ttm,
			GlobalCoordinates targetCoordinates) {
		TrackData trackData = new TrackData();
		if (ttm.getTargetNumber() != null)
			trackData.setId(ttm.getTargetNumber().getNumber());
		trackData.setSourceName("ODYSSEUS");
		trackData.setSourceId("0");
		trackData.setTrackStatus(1); // Updated
		if (ttm.getTargetCourse() != null)
			trackData.setCOG(ttm.getTargetCourse());
		if (ttm.getTargetSpeed() != null)
			trackData.setSOG(ttm.getTargetSpeed() * KTS_TO_MS);
		trackData.setUpdateTime(new Date());
		if (targetCoordinates != null) {
			Pos pos = new Pos();
			pos.setLat(targetCoordinates.getLatitude());
			pos.setLong(targetCoordinates.getLongitude());
			trackData.addPos(pos);
		}
		return trackData;
	}

	private VoyageData prepareVoyageDataFromTLL(TLLSentence tll) {
		VoyageData voyageData = new VoyageData();
		voyageData.setId(tll.getTargetNumber().getNumber());
		voyageData.setSourceId("3");
		voyageData.setSourceName("ODYSSEUS");
		voyageData.setSourceType(3);
		voyageData.setUpdateTime(new Date());
		return voyageData;
	}

	private VesselData prepareVesselDataFromTLL(TLLSentence tll) {
		VesselData vesselData = new VesselData();
		vesselData.setId(tll.getTargetNumber().getNumber());
		vesselData.setSourceId("0");
		vesselData.setSourceName("ODYSSEUS");
		vesselData.setSourceType(3);
		vesselData.setUpdateTime(new Date());

		Construction construction = new Construction();
		vesselData.setConstruction(construction);

		Identifier identifier = new Identifier();
		if (tll.getTargetNumber() != null)
			identifier.setMMSI(tll.getTargetNumber().getNumber());
		if (tll.getTargetLabel() != null)
			identifier.setName(tll.getTargetLabel());
		vesselData.setIdentifier(identifier);
		return vesselData;
	}

	private TrackData prepareTrackDataFromTLL(TLLSentence tll, TTMSentence ttm) {
		TrackData trackData = new TrackData();
		if (tll.getTargetNumber() != null)
			trackData.setId(tll.getTargetNumber().getNumber());
		trackData.setSourceName("ODYSSEUS");
		trackData.setSourceId("0");
		trackData.setTrackStatus(1); // Updated
		trackData.setUpdateTime(new Date());
		Pos pos = new Pos();
		pos.setLat(tll.getLatitude());
		pos.setLong(tll.getLongitude());
		trackData.addPos(pos);
		trackData.setCOG(ttm.getTargetCourse());
		trackData.setSOG(ttm.getTargetSpeed());
		return trackData;
	}

	private Header prepareHeader() {
		Header header = new Header();
		header.setVersion("0.2.5");
		header.setMsgRefId("{" + UUID.randomUUID().toString() + "}");
		return header;
	}

	@SuppressWarnings("unchecked")
	private void convertIVEFtoTTM(
			KeyValueObject<? extends IMetaAttribute> received) {
		if (received.getMetadata("object") instanceof MSG_IVEF) {
			KeyValueObject<? extends IMetaAttribute> sent = new KeyValueObject<>();
			MSG_IVEF vesselMsg = (MSG_IVEF) received.getMetadata("object");
			Double ownShipLat = null;
			Double ownShipLong = null;
			if (this.ownShipPosition != null) {
				ownShipLat = this.ownShipPosition.getLatitude();
				ownShipLong = this.ownShipPosition.getLongitude();
			}
			for (int i = 0; i < vesselMsg.getBody().getObjectDatas()
					.countOfObjectDatas(); i++) {
				ObjectData objectData = vesselMsg.getBody().getObjectDatas()
						.getObjectDataAt(i);
				/********************************************************************************************
				 * Data fusion: calculate the track between the ownShip and the
				 * IVEF message (Ship ODYSSEUS).
				 * ******************************************************************************************/
				if (this.ownShipPosition == null)
					return;
				Double targetLat = objectData.getTrackData().getPosAt(0)
						.getLat();
				Double targetLong = objectData.getTrackData().getPosAt(0)
						.getLong();
				if (ownShipLat != null && ownShipLong != null
						&& targetLat != null && targetLong != null) {
					Double targetDistance = IvefConversionUtilities
							.calculateDistance(ownShipLat, ownShipLong,
									targetLat, targetLong);
					Double bearing = IvefConversionUtilities.calculateBearing(
							ownShipLat, ownShipLong, targetLat, targetLong);
					// Speed vectors
					double ownShipCog = this.ownShipPosition
							.getCourseOverGround();
					double ownShipSog = this.ownShipPosition
							.getSpeedOverGround();
					Vector2D ownShipSpeedVector = new Vector2D(-ownShipSog
							* Math.sin(ownShipCog), ownShipSog
							* Math.cos(ownShipCog));
					double targetCog = objectData.getTrackData().getCOG();
					double targetSog = objectData.getTrackData().getSOG();
					Vector2D targetSpeedVector = new Vector2D(-targetSog
							* Math.sin(targetCog), targetSog
							* Math.cos(targetCog));
					// Distance vector
					// First we have to project the P0 and Q0 from spherical
					// spatial reference WGS84(EPSG:4326) into EPSG:3857
					// (GoogleMap)
					GeometryFactory gf = new GeometryFactory();
					Point ownShipInWGS84At0 = gf.createPoint(new Coordinate(
							ownShipLat, ownShipLong));
					ownShipInWGS84At0.setSRID(4326);// WSG84
					Geometry projectedOwnShip = IvefConversionUtilities
							.geometryTransform(ownShipInWGS84At0, 3857);
					Point targetInWGS84At0 = gf.createPoint(new Coordinate(
							targetLat, targetLong));
					targetInWGS84At0.setSRID(4326);// WSG84
					Geometry projectedTarget = IvefConversionUtilities
							.geometryTransform(targetInWGS84At0, 3857);
					// Now we can generate the Distance vector at 0
					Vector2D distanceVector = new Vector2D(
							projectedOwnShip.getCoordinate().x
									- projectedTarget.getCoordinate().x,
							projectedOwnShip.getCoordinate().y
									- projectedTarget.getCoordinate().y);
					// TCPA: Time Closest Position of Approach
					double tcpa = -distanceVector.dot(ownShipSpeedVector
							.subtract(targetSpeedVector))
							/ (ownShipSpeedVector.len() - targetSpeedVector
									.len())
							* (ownShipSpeedVector.len() - targetSpeedVector
									.len());
					// Distances from ownShip to P(tcpa) and from target to
					// Q(tcpa)
					double ownshipToTcpaDist = (ownShipSog * 0.514444444)
							* tcpa;// SOG is converted from Kn/second to
									// M/Second.
					double targetToTcpaDist = targetSog * tcpa;
					// The positions (Lat&Lon) of P(tcpa) and Q(tcpa)
					GeodeticCalculator geoCalculator = new GeodeticCalculator();
					GlobalCoordinates ownShipAtCPA = geoCalculator
							.calculateEndingGlobalCoordinates(Ellipsoid.WGS84,
									new GlobalCoordinates(ownShipLat,
											ownShipLong), ownShipCog,
									ownshipToTcpaDist);
					GlobalCoordinates targetAtCPA = geoCalculator
							.calculateEndingGlobalCoordinates(
									Ellipsoid.WGS84,
									new GlobalCoordinates(targetLat, targetLong),
									targetCog, targetToTcpaDist);
					// CPA: Closest Position of Approach
					Double cpa = IvefConversionUtilities.calculateDistance(
							ownShipAtCPA.getLatitude(),
							ownShipAtCPA.getLongitude(),
							targetAtCPA.getLatitude(),
							targetAtCPA.getLongitude());

					// The TTM message
					TTMSentence ttm = new TTMSentence();
					String targetNumStr = "";
					if (objectData.getVesselDataAt(0) != null) {
						if (objectData.getVesselDataAt(0).getIdentifier() != null) {
							targetNumStr += objectData.getVesselDataAt(0)
									.getIdentifier().getMMSI();
						} else {
							targetNumStr = "0";
						}

						// Maybe the target label is he same of the ship name!
						String targetName = "";
						if (!(targetName = objectData.getVesselDataAt(0)
								.getIdentifier().getName()).equals("")) {
							ttm.setTargetLabel(targetName);
						}

					}
					ttm.setTargetNumber(TargetNumber.parse(targetNumStr));
					ttm.setTargetDistance(targetDistance);
					ttm.setBearing(bearing);
					ttm.setTargetSpeed(targetSog);
					ttm.setTargetCourse(targetCog);
					ttm.setClosestPointOfApproach(cpa);
					ttm.setTimeUntilClosestPoint(tcpa);
					ttm.setTargetStatus(TargetStatus.TRACKING_TARGET);// tracking
					ttm.setReferenceTarget(TargetReference.NOT_DESIGNATED);
					ttm.parse();
					sent.setMetadata("originalNMEA", ttm);
					transfer((T) sent);
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void convertAIStoIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		KeyValueObject<? extends IMetaAttribute> sent = new KeyValueObject<>();
		if (received.getMetadata("decodedAIS") instanceof DecodedAISPayload) {
			/**********************************************************
			 * Convert the received NMEA AIS to IVEF (Coastal ODYSSEUS)
			 * ********************************************************/
			Long mmsi = null;
			this.ivef = new MSG_IVEF();
			mmsi = (Long) received.getAttribute("sourceMmsi");
			int id = 0;
			if (mmsi != null)
				id = getIdFromMMSIMap(mmsi);
			else
				return;
			Pos lastPos = new Pos();
			Double lat = ((Double) received.getAttribute("latitude") != null) ? (Double) received
					.getAttribute("latitude") : null;
			Double lon = ((Double) received.getAttribute("longitude") != null) ? (Double) received
					.getAttribute("longitude") : null;
			if (lat != null && lon != null) {
				lastPos.setLat(lat);
				lastPos.setLong(lon);
				mmsiToPos.put(mmsi, lastPos);
			} else {
				if (mmsiToPos.containsKey(mmsi))
					lastPos = mmsiToPos.get(mmsi);
				else
					return;
			}

			Header header = prepareHeader();
			((MSG_IVEF) this.ivef).setHeader(header);

			TrackData trackData = prepareTrackData(received, id, lastPos);

			VesselData vesselData = prepareVesselData(received, id);

			VoyageData voyageData = prepareVoyageData(received, id);

			ObjectDatas objectDatas = new ObjectDatas();
			ObjectData objectData = new ObjectData();
			objectData.setTrackData(trackData);
			objectData.addVesselData(vesselData);
			objectData.addVoyageData(voyageData);
			objectDatas.addObjectData(objectData);
			if (trackData.getSOG() > 40.0)
				return;

			Body body = new Body();
			body.setObjectDatas(objectDatas);
			((MSG_IVEF) this.ivef).setBody(body);

			this.ivef.fillMap(sent);
			sent.setMetadata("object", this.ivef);
			transfer((T) sent);
		}
	}

	private TrackData prepareTrackData(
			KeyValueObject<? extends IMetaAttribute> received, int id,
			Pos lastPos) {
		TrackData trackData = new TrackData();
		trackData.setId(id);
		// Course over ground
		Double cog = null;
		if ((cog = (Double) received.getAttribute("courseOverGround")) != null)
			trackData.setCOG(cog);
		else
			trackData.setCOG(Double.parseDouble("0"));
		// Rate of turn
		Integer rateOfTurn = null;
		if ((rateOfTurn = (Integer) received.getAttribute("rateOfTurn")) != null)
			trackData.setROT(rateOfTurn);
		// Speed over ground
		Double sog = null;
		if ((sog = (Double) received.getAttribute("speedOverGround")) != null)
			trackData.setSOG(sog * KTS_TO_MS);
		else
			trackData.setSOG(Double.parseDouble("0"));
		trackData.setSourceId("0");
		trackData.setUpdateTime(new Date());
		trackData.setSourceName("ODYSSEUS");
		trackData.setTrackStatus(1); // Updated
		NavigationStatus navigationStatus = null;
		if ((navigationStatus = (NavigationStatus) received
				.getAttribute("navigationStatus")) != null) {
			NavStatus navStatus = new NavStatus();
			navStatus.setValue(navigationStatus.getStatus());
			trackData.addNavStatus(navStatus);
		}
		if (lastPos != null)
			trackData.addPos(lastPos);
		return trackData;
	}

	private VesselData prepareVesselData(
			KeyValueObject<? extends IMetaAttribute> received, int id) {
		VesselData vesselData = new VesselData();
		vesselData.setId(id);
		vesselData.setSourceName("ODYSSEUS");
		vesselData.setSourceType(3);
		vesselData.setUpdateTime(new Date());

		// construction
		Construction construction = new Construction();
		Integer shipType = null;
		if ((shipType = (Integer) received.getAttribute("shipType")) != null)
			construction.setLloydsShipType(shipType);
		Double draught = null;
		if ((draught = (Double) received.getAttribute("draught")) != null)
			construction.setMaxDraught(draught);
		vesselData.setConstruction(construction);

		// identifier
		Identifier identifier = new Identifier();
		String callsign = null;
		if ((callsign = (String) received.getAttribute("callsign")) != null)
			identifier.setCallsign(callsign);
		String shipName = null;
		if ((shipName = (String) received.getAttribute("shipName")) != null)
			identifier.setName(shipName);
		IMO imo = null;
		if ((imo = (IMO) received.getAttribute("imo")) != null)
			identifier.setIMO(imo.getIMO().intValue());
		Long mmsi = null;
		if ((mmsi = (Long) received.getAttribute("sourceMmsi")) != null)
			identifier.setMMSI(mmsi.intValue());
		vesselData.setIdentifier(identifier);

		return vesselData;
	}

	private VoyageData prepareVoyageData(
			KeyValueObject<? extends IMetaAttribute> received, int id) {
		VoyageData voyageData = new VoyageData();
		voyageData.setId(id);
		voyageData.setSourceName("ODYSSEUS");
		voyageData.setSourceType(3);
		voyageData.setUpdateTime(new Date());
		Double draught = null;
		if ((draught = (Double) received.getAttribute("draught")) != null)
			voyageData.setDraught(draught);
		String destination = null;
		if ((destination = (String) received.getAttribute("destination")) != null)
			voyageData.setDestName(destination);
		String eta = null;
		if ((eta = (String) received.getAttribute("eta")) != null)
			voyageData.setETA(IvefConversionUtilities.nmeaTimeToUTC(eta));

		return voyageData;
	}

	private int getIdFromMMSIMap(Long mmsi) {
		if (mmsiToId.containsKey(mmsi))
			return mmsiToId.get(mmsi);
		else {
			int id = mmsiToId.size() + 1;
			mmsiToId.put(mmsi, id);
			return id;
		}
	}

	private void convertIVEFtoAIS(
			KeyValueObject<? extends IMetaAttribute> received) {
		if (received.getMetadata("object") instanceof MSG_IVEF) {
			MSG_IVEF vesselMsg = (MSG_IVEF) received.getMetadata("object");
			for (int i = 0; i < vesselMsg.getBody().getObjectDatas()
					.countOfObjectDatas(); i++) {
				/**********************************************************
				 * Convert the IVEF to AIS NMEA messages (Coastal ODYSSEUS)
				 * ********************************************************/
				String encodedPositionMsg = vesselMsg.getBody()
						.getObjectDatas().getObjectDataAt(i)
						.encodeAISPositionPayload();
				transportNMEA(encodedPositionMsg);
				// Handle multiFragment sentences to send static-And-Voyage
				// messages.
				long mmsi = vesselMsg.getBody().getObjectDatas()
						.getObjectDataAt(i).getVesselDataAt(0).getIdentifier()
						.getMMSI();
				boolean sendStaticVoyage = false;
				if (!mmsiToCount.containsKey(mmsi)) {
					mmsiToCount.put(mmsi, 1);
					sendStaticVoyage = true;
				} else {
					int count = mmsiToCount.get(mmsi) + 1;
					if (count % this.positionToStaticRatio == 0) {
						sendStaticVoyage = true;
						count = 1;
					}
					mmsiToCount.put(mmsi, count);
				}
				if (sendStaticVoyage) {
					String encodedStaticVoyageFragments[] = vesselMsg.getBody()
							.getObjectDatas().getObjectDataAt(i)
							.encodeAISStaticVoyagePayload();
					if (encodedStaticVoyageFragments != null) {
						transportNMEA(encodedStaticVoyageFragments[0]);
						transportNMEA(encodedStaticVoyageFragments[1]);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void transportNMEA(String nmeaString) {
		KeyValueObject<? extends IMetaAttribute> sent = new KeyValueObject<>();
		this.nmea = SentenceFactory.getInstance().createSentence(nmeaString);
		this.nmea.parse();
		// 18.07 Send the original NMEA message
		Map<String, Object> originalEvent = this.nmea.toMap();
		KeyValueObject<? extends IMetaAttribute> originalNmea = new KeyValueObject<>(
				originalEvent);
		originalNmea.setMetadata("originalNMEA", this.nmea);
		transfer((T) originalNmea);
		// Handling AIS Sentences
		if (this.nmea instanceof AISSentence) {
			AISSentence aissentence = (AISSentence) this.nmea;
			this.aishandler.handleAISSentence(aissentence);
			if (this.aishandler.getDecodedAISMessage() != null) {
				// 13.08 We separate the map creation of the decoded message
				// into another method rather than the toMap which will be only
				// for original parts of the message.
				// Here we are quite sure that there is a non-null
				// decodedAISMessage inside the aisSentence because the
				// AIShandler has already set it.
				// Map<String, Object> stringObject = this.nmea.toMap();
				Map<String, Object> stringObject = new HashMap<>();
				aissentence.toDecodedPayloadMap(stringObject);
				sent = new KeyValueObject<>(stringObject);
				// Important to parse the decodedAIS as NMEA sentence in order
				// to prepare the fields which will be used in writing.
				this.aishandler.getDecodedAISMessage().parse();
				sent.setMetadata("decodedAIS",
						this.aishandler.getDecodedAISMessage());
				this.aishandler.resetDecodedAISMessage();
				transfer((T) sent);
			}
		}
	}

}
