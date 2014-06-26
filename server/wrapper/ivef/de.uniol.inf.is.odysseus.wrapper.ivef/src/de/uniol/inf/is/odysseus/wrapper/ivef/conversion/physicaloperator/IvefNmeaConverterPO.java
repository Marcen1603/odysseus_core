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
import java.util.Random;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.MSG_VesselData;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.*;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.AISSentenceHandler;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.*;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.IMO;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.NavigationStatus;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ShipType;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Header;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Body;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Pos;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.PosReport;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.StaticData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Voyage;



/**
 * @author Mazen salous
 */

@SuppressWarnings({ "rawtypes" })
public class IvefNmeaConverterPO<T extends IStreamObject<IMetaAttribute>> extends AbstractPipe<T, T> {

	private IIvefElement ivef;
	private boolean directionIvefToNmea;
	private Sentence nmea;
	/** Handler for AIS sentences. */
	private AISSentenceHandler aishandler = new AISSentenceHandler();
	private static Map<Long, Integer> mmsiToId = new HashMap<Long, Integer>();
	/** The maximum number of ships. */
	private static int maxNumOfShips = 10000;

	public IvefNmeaConverterPO(IIvefElement ivef) {
		super();
		this.ivef = ivef;
		this.directionIvefToNmea = true;
	}
	
	public IvefNmeaConverterPO(Sentence nmea) {
		super();
		this.nmea = nmea;
		this.directionIvefToNmea = false;
	}

	public IvefNmeaConverterPO(IvefNmeaConverterPO<T> anotherPO) { 
		super();
		this.ivef = anotherPO.ivef;
		this.directionIvefToNmea = anotherPO.directionIvefToNmea;
		this.nmea = anotherPO.nmea;
	}


	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		KeyValueObject<? extends IMetaAttribute> received = (KeyValueObject<? extends IMetaAttribute>) object;
		KeyValueObject<? extends IMetaAttribute> sent = new KeyValueObject<>(); 
		if (received.getMetadata("object") instanceof IIvefElement) {
			//TODO: handle multiFragment sentences.
			this.directionIvefToNmea = true;
			if(received.getMetadata("object") instanceof MSG_VesselData){
			MSG_VesselData vesselMsg = (MSG_VesselData)received.getMetadata("object");
				for(int i=0; i<vesselMsg.getBody().countOfVesselDatas(); i++){
					String encodedNmeaStr = vesselMsg.getBody().getVesselDataAt(i).encodeAISPayload();
					this.nmea = SentenceFactory.getInstance().createSentence(encodedNmeaStr);
					this.nmea.parse();
					//Handling AIS Sentences
					if (this.nmea instanceof AISSentence) {
						AISSentence aissentence = (AISSentence) this.nmea;
						this.aishandler.handleAISSentence(aissentence);
						if(this.aishandler.getDecodedAISMessage() != null) {
							Map<String, Object> stringObject = this.nmea.toMap();
							sent = new KeyValueObject<>(stringObject);
							//Important to parse the decodedAIS as NMEA sentence in order to prepare the fields which will be used in writing.
							this.aishandler.getDecodedAISMessage().parse();
							sent.setMetadata("decodedAIS", this.aishandler.getDecodedAISMessage());
							this.aishandler.resetDecodedAISMessage();
							transfer((T) sent);
						}
					}
				}
			}
		}
		else if (received.getMetadata("decodedAIS") instanceof DecodedAISPayload)
		{
			this.directionIvefToNmea = false;
			/**Convert the received NMEA AIS to IVEF*/
			Long mmsi = null;
//			if(received.getMetadata("decodedAIS") instanceof DecodedAISPayload) {
			this.ivef = new MSG_VesselData();
			mmsi = (Long)received.getAttribute("sourceMmsi");
			//test
//				if (mmsi == 211625860)
//					System.out.println("211625860 received");
			//
			int id = 0;
			if(mmsi != null)
				id = getIdFromMMSIMap(mmsi);
			else
				id = getIdRandomly();
			//Header
			Header header = prepareHeader();
			((MSG_VesselData) this.ivef).setHeader(header);
			//PosReport
			PosReport posReport = preparePosReport(received, id);
			//StaticData
			StaticData staticData = prepareStaticData(received, id);
//				////Just for Test:
//				staticData.setShipName(String.valueOf(id));
//				////
			//Voyage
			Voyage voyage = prepareVoyage(received, id);
			//VesseData
			VesselData vesseData = new VesselData();
			vesseData.setPosReport(posReport);
			vesseData.addStaticData(staticData);
			vesseData.addVoyage(voyage);
			//Body
			Body body = new Body();
			body.addVesselData(vesseData);
			((MSG_VesselData) this.ivef).setBody(body);
//			}
//			else {
//				System.out.println("Not converted AIS message:");
//				AISSentence sentence = (AISSentence) received.getMetadata("object");
//				System.out.println(sentence.getMessageId());
//				System.out.println(sentence.getNmeaString());
//			}
			this.ivef.fillMap(sent);
			sent.setMetadata("object", this.ivef);
			transfer((T) sent);
		}
	}

	@Override
	public IvefNmeaConverterPO<T> clone() {
		return new IvefNmeaConverterPO<T>(this);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof IvefNmeaConverterPO)) {
			return false;
		}
		IvefNmeaConverterPO ivefpo = (IvefNmeaConverterPO) ipo;
		if(ivefpo.directionIvefToNmea)
			return ivefpo.ivef == this.ivef;
		else		
			return ivefpo.nmea == this.nmea;
	}
	
	/**Utilities*/
	private Header prepareHeader(){
		Header header = new Header();
		header.setVersion("0.5.1");
		header.setMsgRefId("{" + UUID.randomUUID().toString() + "}");
		return header;
	}
	
	private PosReport preparePosReport(KeyValueObject<? extends IMetaAttribute> received, int id){
		Pos pos = new Pos();
		Float lat = ((Float)received.getAttribute("latitude") != null) ? (Float)received.getAttribute("latitude") : null;
		Float lon = ((Float)received.getAttribute("longitude") != null) ? (Float)received.getAttribute("longitude") : null;
		if(lat != null)
			pos.setLat(lat);
		if(lon != null)
			pos.setLong(lon);
		PosReport posReport = new PosReport();
		/**Mandatory fields*/
		posReport.setPos(pos);
		posReport.setId(id);
		posReport.setSourceId(0);//not encoded in AIS NMEA
		Calendar now = Calendar.getInstance();
		Integer second = null;
		if((second = (Integer)received.getAttribute("second")) != null)
			now.set(Calendar.SECOND, second);
		posReport.setUpdateTime(now.getTime());
		@SuppressWarnings("unused")
		Float sog = null;
		if((sog = (Float)received.getAttribute("speedOverGround")) != null)
			posReport.setSOG(Float.valueOf(0));//Workaround! posReport.setSOG(sog);
		else
			posReport.setSOG(Float.parseFloat("0"));
		Float cog = null;
		if ((cog = (Float)received.getAttribute("courseOverGround")) != null)
			posReport.setCOG(cog);
		else
			posReport.setCOG(Float.parseFloat("0"));
		posReport.setLost("no");//not encoded in AIS NMEA
		/**Optional fields*/
		NavigationStatus navigationStatus = null;
		if((navigationStatus = (NavigationStatus)received.getAttribute("navigationStatus")) != null)
			posReport.setNavStatus(navigationStatus.getStatus());
		Integer rateOfTurn = null;
		if((rateOfTurn = (Integer)received.getAttribute("rateOfTurn")) != null)
			posReport.setRateOfTurn(rateOfTurn);
		//posReport.setOrientation(((Integer)received.getAttribute("trueHeading")).floatValue());
		posReport.setUpdSensorType(2);// 2 AIS
		return posReport;
	}
	
	private StaticData prepareStaticData(KeyValueObject<? extends IMetaAttribute> received, int id){
		StaticData staticData = new StaticData();
		/**Mandatory fields*/
		staticData.setId(String.valueOf(id));
		staticData.setSourceName("OFFIS");
		staticData.setSource(3);//3 = manual
		/**Optional fields*/
		String callsign = null;
		if ((callsign = (String)received.getAttribute("callsign")) != null)
			staticData.setCallsign(callsign);
		String shipName = null;
		if((shipName = (String)received.getAttribute("shipName")) != null)
			staticData.setShipName(shipName);
		staticData.setObjectType(2);// 2 = vessel
		ShipType shipType = null;
		if((shipType = (ShipType)received.getAttribute("shipType")) != null)
			staticData.setShipType(shipType.getType());
		IMO imo = null;
		if((imo = (IMO)received.getAttribute("imo")) != null)
			staticData.setIMO(imo.getIMO());
		Long mmsi = null;
		if((mmsi = (Long)received.getAttribute("sourceMmsi")) != null)
			staticData.setMMSI(mmsi);
		//staticData.setATONName("AISTransceiver");//name of the Aids-to-Navigation
		Integer toBow = null;
		if((toBow = (Integer)received.getAttribute("toBow")) != null)
			staticData.setAntPosDistFromFront(toBow.doubleValue());
		Integer toPort = null;
		if((toPort = (Integer)received.getAttribute("toPort")) != null)
			staticData.setAntPosDistFromLeft(toPort.doubleValue());
		Float draught = null;
		if((draught = (Float)received.getAttribute("draught")) != null)
			staticData.setMaxDraught(draught.doubleValue());
		return staticData;
	}
	
	private Voyage prepareVoyage(KeyValueObject<? extends IMetaAttribute> received, int id){
		Voyage voyage = new Voyage();
		/**Mandatory fields*/
		voyage.setId(String.valueOf(id));
		voyage.setSourceName("OFFIS");
		voyage.setSource(3);//3 manual
		/**Optional fields*/
		//voyage.setAirDraught(((Float)received.getAttribute("draught")).doubleValue()); not encoded in AIS NMEA
		Float draught = null;
		if((draught = (Float)received.getAttribute("draught")) != null)
			voyage.setDraught(draught.doubleValue());
		String destination = null;
		if((destination = (String)received.getAttribute("destination")) != null)
			voyage.setDestination(destination);
		String eta = null;
		if((eta = (String)received.getAttribute("eta")) != null)
			voyage.setETA(nmeaTimeToISO8601(eta));
		return voyage;
	}
	
	private int getIdFromMMSIMap(Long mmsi){
		if (mmsiToId.containsKey(mmsi))
			return mmsiToId.get(mmsi);
		else
		{
			int id = mmsiToId.size() + 1;
			mmsiToId.put(mmsi, id);
			return id;
		}
	}
	
	private String nmeaTimeToISO8601(String eta) {
		String day = eta.substring(0, eta.indexOf("-"));
		String month = eta.substring(eta.indexOf("-")+1, eta.indexOf(" "));
		String hour = eta.substring(eta.indexOf(" ")+1, eta.indexOf(":"));
		String minute = eta.substring(eta.indexOf(":")+1);
		String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		return year+"-"+month+"-"+day+"T"+hour+":"+minute+":00.000-0000";
	}
	
	private int getIdRandomly() {
		Random random = new Random();
		return random.nextInt(maxNumOfShips) + maxNumOfShips + 1;
	}

}
