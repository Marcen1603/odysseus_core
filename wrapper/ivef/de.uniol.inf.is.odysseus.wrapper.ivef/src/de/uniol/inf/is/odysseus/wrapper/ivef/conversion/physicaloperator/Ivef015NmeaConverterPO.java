/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.wrapper.ivef.conversion.physicaloperator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
//import java.util.Random;
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
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Body;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Header;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.MSG_VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Pos;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.PosReport;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.StaticData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Voyage;
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
//import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.IMO;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.NavigationStatus;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ShipType;

/**
 * @author Mazen salous
 */

@SuppressWarnings({ "rawtypes" })
public class Ivef015NmeaConverterPO<T extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<T, T> {

	private IIvefElement ivef;
	private ConversionType conversionType;
	private int PositionToStaticRatio;
	private Sentence nmea;
	/** Handler for AIS sentences. */
	private AISSentenceHandler aishandler = new AISSentenceHandler();
	/***************************************************
	 * Attributes for coastal ODYSSEUS
	 * *************************************************/
	/** Unique Id for MMSIs */
	private static Map<Long, Integer> mmsiToId = new HashMap<Long, Integer>();
	/** Storing the last position of the ship */
	private static Map<Long, Pos> mmsiToPos = new HashMap<Long, Pos>();

	/***************************************************
	 * Attributes for on-ship ODYSSEUS
	 ***************************************************/
	/** The own-ship */
	private AISSentence ownShip = null;
	private PositionReport ownShipPosition = null;
	@SuppressWarnings("unused")
	private StaticAndVoyageData ownShipStaticData = null;
	/**
	 * Map between the ship MMSI and its frequency. It'll be used to ensure
	 * generating Static&Voyage messages from time to time
	 */
	private static Map<Long, Integer> mmsiToCount = new HashMap<Long, Integer>();

	public Ivef015NmeaConverterPO(Ivef015NmeaConverterPO<T> anotherPO) {
		super();
		this.ivef = anotherPO.ivef;
		this.conversionType = anotherPO.conversionType;
		this.nmea = anotherPO.nmea;
	}

	public Ivef015NmeaConverterPO(ConversionType conversionType,
			int PositionToStaticRatio) {
		super();
		this.ivef = null;
		this.conversionType = conversionType;
		this.PositionToStaticRatio = PositionToStaticRatio;
		this.nmea = null;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T object, int port) {
		KeyValueObject<? extends IMetaAttribute> received = (KeyValueObject<? extends IMetaAttribute>) object;
		switch (conversionType) {
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

	@Override
	public Ivef015NmeaConverterPO<T> clone() {
		return new Ivef015NmeaConverterPO<T>(this);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof Ivef015NmeaConverterPO)) {
			return false;
		}
		Ivef015NmeaConverterPO ivefpo = (Ivef015NmeaConverterPO) ipo;
		return this.conversionType == ivefpo.conversionType
				&& this.PositionToStaticRatio == ivefpo.PositionToStaticRatio;
	}

	/****************************************
	 * *******U T I L I T I E S**************
	 * **************************************/

	/** IVEF Utilities */
	private Header prepareHeader() {
		Header header = new Header();
		header.setVersion("0.1.5");
		header.setMsgRefId("{" + UUID.randomUUID().toString() + "}");
		return header;
	}

	private PosReport preparePosReport(
			KeyValueObject<? extends IMetaAttribute> received, int id,
			Pos lastPos) {
		// Pos pos = new Pos();
		// Double lat = ((Double)received.getAttribute("latitude") != null) ?
		// (Double)received.getAttribute("latitude") : null;
		// Double lon = ((Double)received.getAttribute("longitude") != null) ?
		// (Double)received.getAttribute("longitude") : null;
		// if(lat != null)
		// pos.setLat(lat);
		// if(lon != null)
		// pos.setLong(lon);
		PosReport posReport = new PosReport();
		/** Mandatory fields */
		posReport.setPos(lastPos);
		posReport.setId(id);
		posReport.setSourceId(0);// not encoded in AIS NMEA
		Calendar now = Calendar.getInstance();
		// No sense of the second field, it could make problems of wrong order
		// messages, so it should not be used:
		// Integer second = null;
		// if((second = (Integer)received.getAttribute("second")) != null)
		// now.set(Calendar.SECOND, second);
		posReport.setUpdateTime(now.getTime());
		Double sog = null;
		if ((sog = (Double) received.getAttribute("speedOverGround")) != null)
			posReport.setSOG(sog * Double.valueOf("0.514444444"));// Conversion
																	// from Kn/s
																	// to
																	// m/s...Workaround!
																	// posReport.setSOG(Float.valueOf(0));
		else
			posReport.setSOG(Double.parseDouble("0"));
		Double cog = null;
		if ((cog = (Double) received.getAttribute("courseOverGround")) != null)
			posReport.setCOG(cog);
		else
			posReport.setCOG(Double.parseDouble("0"));
		posReport.setLost("no");// not encoded in AIS NMEA
		/** Optional fields */
		NavigationStatus navigationStatus = null;
		if ((navigationStatus = (NavigationStatus) received
				.getAttribute("navigationStatus")) != null)
			posReport.setNavStatus(navigationStatus.getStatus());
		Integer rateOfTurn = null;
		if ((rateOfTurn = (Integer) received.getAttribute("rateOfTurn")) != null)
			posReport.setRateOfTurn(rateOfTurn);
		// posReport.setOrientation(((Integer)received.getAttribute("trueHeading")).floatValue());
		posReport.setUpdSensorType(2);// 2 AIS, or 3 AIS+Radar
		return posReport;
	}

	private PosReport prepareTargetPosReport(TTMSentence ttm,
			GlobalCoordinates targetCoordinates) {
		/** Mandatory fields */
		PosReport posReport = new PosReport();
		Pos pos = new Pos();
		pos.setLat(targetCoordinates.getLatitude());
		pos.setLong(targetCoordinates.getLongitude());
		posReport.setPos(pos);
		posReport.setId(ttm.getTargetNumber().getNumber());
		posReport.setSourceId(0);//
		Calendar now = Calendar.getInstance();
		posReport.setUpdateTime(now.getTime());
		posReport.setSOG(ttm.getTargetSpeed() * 0.514444444);// Conversion from
																// Kn/s to m/s
		posReport.setCOG(ttm.getTargetCourse());
		posReport.setLost("no");//
		/** Optional fields */
		// posReport.setNavStatus(15);//Default, undefined
		posReport.setUpdSensorType(2);// 2 AIS, or 3 AIS+Radar
		return posReport;
	}

	private StaticData prepareStaticData(
			KeyValueObject<? extends IMetaAttribute> received, int id) {
		StaticData staticData = new StaticData();
		/** Mandatory fields */
		staticData.setId(String.valueOf(id));
		staticData.setSourceName("ODYSSEUS");
		staticData.setSource(3);// 3 = manual
		/** Optional fields */
		String callsign = null;
		if ((callsign = (String) received.getAttribute("callsign")) != null)
			staticData.setCallsign(callsign);
		String shipName = null;
		if ((shipName = (String) received.getAttribute("shipName")) != null)
			staticData.setShipName(shipName);
		staticData.setObjectType(2);// 2 = vessel
		ShipType shipType = null;
		if ((shipType = (ShipType) received.getAttribute("shipType")) != null)
			staticData.setShipType(shipType.getType());
		IMO imo = null;
		if ((imo = (IMO) received.getAttribute("imo")) != null)
			staticData.setIMO(imo.getIMO());
		Long mmsi = null;
		if ((mmsi = (Long) received.getAttribute("sourceMmsi")) != null)
			staticData.setMMSI(mmsi);
		// staticData.setATONName("AISTransceiver");//name of the
		// Aids-to-Navigation
		Integer toBow = null;
		if ((toBow = (Integer) received.getAttribute("toBow")) != null)
			staticData.setAntPosDistFromFront(toBow.doubleValue());
		Integer toPort = null;
		if ((toPort = (Integer) received.getAttribute("toPort")) != null)
			staticData.setAntPosDistFromLeft(toPort.doubleValue());
		Float draught = null;
		if ((draught = (Float) received.getAttribute("draught")) != null)
			staticData.setMaxDraught(draught.doubleValue());
		return staticData;
	}

	private StaticData prepareTargetStaticData(TTMSentence ttm) {
		StaticData staticData = new StaticData();
		/** Mandatory fields */
		staticData.setId(String.valueOf(ttm.getTargetNumber()));
		staticData.setSourceName("ODYSSEUS");
		staticData.setSource(3);// 3 = manual
		/** Optional fields */
		String shipName = null;
		if ((shipName = ttm.getTargetLabel()) != null)
			staticData.setShipName(shipName);
		staticData.setObjectType(2);// 2 = vessel
		staticData.setMMSI((long) ttm.getTargetNumber().getNumber());// We may
																		// extract
																		// the
																		// MMSI
																		// and
																		// IMO
																		// from
																		// TargetNumber..
		return staticData;
	}

	private Voyage prepareVoyage(
			KeyValueObject<? extends IMetaAttribute> received, int id) {
		Voyage voyage = new Voyage();
		/** Mandatory fields */
		voyage.setId(String.valueOf(id));
		voyage.setSourceName("ODYSSEUS");
		voyage.setSource(3);// 3 manual
		/** Optional fields */
		// voyage.setAirDraught(((Float)received.getAttribute("draught")).doubleValue());
		// not encoded in AIS NMEA
		Float draught = null;
		if ((draught = (Float) received.getAttribute("draught")) != null)
			voyage.setDraught(draught.doubleValue());
		String destination = null;
		if ((destination = (String) received.getAttribute("destination")) != null)
			voyage.setDestination(destination);
		String eta = null;
		if ((eta = (String) received.getAttribute("eta")) != null)
			voyage.setETA(IvefConversionUtilities.toUTC(IvefConversionUtilities
					.nmeaTimeToUTC(eta)));
		return voyage;
	}

	private Voyage prepareTargetVoyage(TTMSentence ttm) {
		Voyage voyage = new Voyage();
		/** Mandatory fields */
		voyage.setId(String.valueOf(ttm.getTargetNumber()));
		voyage.setSourceName("ODYSSEUS");
		voyage.setSource(3);// 3 manual
		/** Optional fields */
		return voyage;
	}

	private PosReport preparePosReportFromTLL(TLLSentence tll) {
		PosReport posReport = new PosReport();
		posReport.setId(tll.getTargetNumber().getNumber());
		posReport.setSourceId(0);
		Calendar now = Calendar.getInstance();
		posReport.setUpdateTime(now.getTime());
		Pos pos = new Pos();
		pos.setLat(tll.getLatitude());
		pos.setLong(tll.getLongitude());
		posReport.setPos(pos);
		posReport.setLost("no");
		posReport.setSOG(0.0);
		posReport.setCOG(0.0);
		return posReport;
	}

	private StaticData prepareStaticDataFromTLL(TLLSentence tll) {
		StaticData staticData = new StaticData();
		staticData.setId(String.valueOf(tll.getTargetNumber()));
		staticData.setSourceName("ODYSSEUS");
		staticData.setSource(3);// 3 = manual
		return staticData;
	}

	private Voyage prepareVoyageFromTLL(TLLSentence tll) {
		Voyage voyage = new Voyage();
		voyage.setId(String.valueOf(tll.getTargetNumber()));
		voyage.setSourceName("ODYSSEUS");
		voyage.setSource(3);// 3 manual
		return voyage;
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

	// private int getIdRandomly() {
	// Random random = new Random();
	// return random.nextInt(maxNumOfShips) + maxNumOfShips + 1;
	// }

	/******************************************************************
	 * ********************Conversion Utilities************************
	 * ****************************************************************
	 */

	@SuppressWarnings("unchecked")
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
				this.ivef = new MSG_VesselData();
				// Header
				Header header = prepareHeader();
				((MSG_VesselData) this.ivef).setHeader(header);
				// PosReport
				PosReport posReport = prepareTargetPosReport(ttm,
						targetCoordinates);
				// StaticData
				StaticData staticData = prepareTargetStaticData(ttm);
				// Voyage
				Voyage voyage = prepareTargetVoyage(ttm);
				// VesseData
				VesselData vesseData = new VesselData();
				vesseData.setPosReport(posReport);
				vesseData.addStaticData(staticData);
				vesseData.addVoyage(voyage);
				// Body
				Body body = new Body();
				body.addVesselData(vesseData);
				((MSG_VesselData) this.ivef).setBody(body);
				this.ivef.fillMap(sent);
				sent.setMetadata("object", this.ivef);
				transfer((T) sent);
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void convertTLLtoIVEF(
			KeyValueObject<? extends IMetaAttribute> received) {
		KeyValueObject<? extends IMetaAttribute> sent = new KeyValueObject<>();
		if (received.getMetadata("originalNMEA") != null) {
			if (received.getMetadata("originalNMEA") instanceof TLLSentence) {
				TLLSentence tll = (TLLSentence) received
						.getMetadata("originalNMEA");
				
				this.ivef = new MSG_VesselData();
				// Header
				Header header = prepareHeader();
				((MSG_VesselData) this.ivef).setHeader(header);
				// PosReport
				PosReport posReport = preparePosReportFromTLL(tll);
				// StaticData
				StaticData staticData = prepareStaticDataFromTLL(tll);
				// Voyage
				Voyage voyage = prepareVoyageFromTLL(tll);
				// VesseData
				VesselData vesseData = new VesselData();
				vesseData.setPosReport(posReport);
				vesseData.addStaticData(staticData);
				vesseData.addVoyage(voyage);
				
				Body body = new Body();
				body.addVesselData(vesseData);
				((MSG_VesselData) this.ivef).setBody(body);
				this.ivef.fillMap(sent);
				sent.setMetadata("object", this.ivef);
				transfer((T) sent);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void convertIVEFtoTTM(
			KeyValueObject<? extends IMetaAttribute> received) {
		KeyValueObject<? extends IMetaAttribute> sent = new KeyValueObject<>();
		if (received.getMetadata("object") instanceof MSG_VesselData) {
			MSG_VesselData vesselMsg = (MSG_VesselData) received
					.getMetadata("object");
			Double ownShipLat = null;
			Double ownShipLong = null;
			if (this.ownShipPosition != null) {
				ownShipLat = this.ownShipPosition.getLatitude();
				ownShipLong = this.ownShipPosition.getLongitude();
			}
			for (int i = 0; i < vesselMsg.getBody().countOfVesselDatas(); i++) {
				/********************************************************************************************
				 * Data fusion: calculate the track between the ownShip and the
				 * IVEF message (Ship ODYSSEUS).
				 * ******************************************************************************************/
				if (this.ownShipPosition == null)
					return;
				Double targetLat = vesselMsg.getBody().getVesselDataAt(i)
						.getPosReport().getPos().getLat();
				Double targetLong = vesselMsg.getBody().getVesselDataAt(i)
						.getPosReport().getPos().getLong();
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
					double targetCog = vesselMsg.getBody().getVesselDataAt(i)
							.getPosReport().getCOG();
					double targetSog = vesselMsg.getBody().getVesselDataAt(i)
							.getPosReport().getSOG();
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
					Long mmsi = null;
					if ((mmsi = vesselMsg.getBody().getVesselDataAt(i)
							.getStaticDataAt(0).getMMSI()) != null)
						targetNumStr += mmsi.toString();
					else
						targetNumStr = "0";
					ttm.setTargetNumber(TargetNumber.parse(targetNumStr));
					ttm.setTargetDistance(targetDistance);
					ttm.setBearing(bearing);
					ttm.setTargetSpeed(targetSog);
					ttm.setTargetCourse(targetCog);
					ttm.setClosestPointOfApproach(cpa);
					ttm.setTimeUntilClosestPoint(tcpa);
					// Maybe the target label is he same of the ship name!
					String targetName = "";
					if (!(targetName = vesselMsg.getBody().getVesselDataAt(i)
							.getStaticDataAt(0).getShipName()).equals(""))
						ttm.setTargetLabel(targetName);
					ttm.setTargetStatus(TargetStatus.TRACKING_TARGET);// tracking
					ttm.setReferenceTarget(TargetReference.NOT_DESIGNATED);// The
																			// ownSip
																			// position
																			// and/or
																			// velocity
																			// are
																			// independent
																			// from
																			// target.
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
			// DecodedAISPayload decodedAIS = (DecodedAISPayload)
			// received.getMetadata("decodedAIS");
			// AISMessageType messageType = decodedAIS.getMessageType();
			// if(received.getMetadata("decodedAIS") instanceof
			// DecodedAISPayload) {
			this.ivef = new MSG_VesselData();
			mmsi = (Long) received.getAttribute("sourceMmsi");
			int id = 0;
			if (mmsi != null)
				id = getIdFromMMSIMap(mmsi);
			else
				return;// Workaround to avoid sending null MMSI...id =
						// getIdRandomly();
			// Updating Or Getting the last position of the ship
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
			// Header
			Header header = prepareHeader();
			((MSG_VesselData) this.ivef).setHeader(header);
			// PosReport
			PosReport posReport = preparePosReport(received, id, lastPos);
			// StaticData
			StaticData staticData = prepareStaticData(received, id);
			// Voyage
			Voyage voyage = prepareVoyage(received, id);
			// VesseData
			VesselData vesseData = new VesselData();
			vesseData.setPosReport(posReport);
			vesseData.addStaticData(staticData);
			vesseData.addVoyage(voyage);
			// Body
			Body body = new Body();
			body.addVesselData(vesseData);
			((MSG_VesselData) this.ivef).setBody(body);
			// }
			// else {
			// System.out.println("Not converted AIS message:");
			// AISSentence sentence = (AISSentence)
			// received.getMetadata("object");
			// System.out.println(sentence.getMessageId());
			// System.out.println(sentence.getNmeaString());
			// }
			// Workaround: Exclude strange speeds > 40 M/s
			if (posReport.getSOG() > 40.0)
				return;
			this.ivef.fillMap(sent);
			sent.setMetadata("object", this.ivef);
			transfer((T) sent);
		}
	}

	private void convertIVEFtoAIS(
			KeyValueObject<? extends IMetaAttribute> received) {
		if (received.getMetadata("object") instanceof MSG_VesselData) {
			MSG_VesselData vesselMsg = (MSG_VesselData) received
					.getMetadata("object");
			for (int i = 0; i < vesselMsg.getBody().countOfVesselDatas(); i++) {
				/**********************************************************
				 * Convert the IVEF to AIS NMEA messages (Coastal ODYSSEUS)
				 * ********************************************************/
				String encodedPositionMsg = vesselMsg.getBody()
						.getVesselDataAt(i).encodeAISPositionPayload();
				transportNMEA(encodedPositionMsg);
				// Handle multiFragment sentences to send static-And-Voyage
				// messages.
				long mmsi = vesselMsg.getBody().getVesselDataAt(i)
						.getStaticDataAt(0).getMMSI();
				boolean sendStaticVoyage = false;
				if (!mmsiToCount.containsKey(mmsi)) {
					mmsiToCount.put(mmsi, 1);
					sendStaticVoyage = true;
				} else {
					int count = mmsiToCount.get(mmsi) + 1;
					if (count % this.PositionToStaticRatio == 0) {
						sendStaticVoyage = true;
						count = 1;
					}
					mmsiToCount.put(mmsi, count);
				}
				if (sendStaticVoyage) {
					String encodedStaticVoyageFragments[] = vesselMsg.getBody()
							.getVesselDataAt(i).encodeAISStaticVoyagePayload();
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
