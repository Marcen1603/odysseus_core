
#include <iostream> // C++ Standard Input / Output Streams Library
#include <sstream> // C++ Standard string Stream Library
#include <vector> // STL dynamic array
#include <string> // STL string class
#include <iomanip>      // std::setprecision

#include <ImageClient.h>
#include <ImageClientObserver.h>
#include <TargetTrackingObserver.h>
#include <TargetTrackingClient.h>
#include <MultiRadarClient.h>
#include <PPIController.h>

#include <RadarColourLookUpTable.h>
#include <ClientErrors.h>
#include <time.h>
#include "NavicoWrap.h"

enum radarSensorState_en
{
	noinitRadar = 0,
	configuredRadar = 1,
	runningRadar = 2
};


using namespace std;
using namespace Navico::Protocol;

uint8_t* hexstringToArray(std::string hexString) //The Unlock Key is provided by an Hex String and has to be converted to a byte Array. Two Ascii letters must be transformed to one Byte Date
{
 uint8_t hexArray[sizeof(hexString.size()/2)]; //create an array ín size of the Unlock Key in Byte 
 int len = sizeof(hexString.size()/2); 
		 if (len <= 0 || (len & 1) != 0 || (len >>= 1) > (hexString.size()/2))return NULL; //Check size of  String 
		 else {
				for (int i = 0; i < len; ++i)
				{
					bool ok = false;
					std::string buffer = hexString.substr( 2*i, 2 ); //get ASCII pair
					if(strtol(buffer.c_str(), NULL, 16) < 256) ok = true; //The value should be not bigger then 255
					if (ok)	hexArray[ i ] = strtol(buffer.c_str(), NULL, 16); //fill array
					else{ return NULL;} //if something goes wrong return null
				}
		 }
  return &hexArray[0];
}

NavicoRadarWrapper::NavicoRadarWrapper(int AntennaHeightMiliMeter, int RangeMeter, std::string RadarSerial, std::string UnlockKeyStr) 
{
	this->mAntennaHeight = AntennaHeightMiliMeter; // Antenna Height in mm
	this->mRangeMeter = RangeMeter; //Radar Scan Range in meter
	initState = noinitRadar; //init State
	myRadarLockState = 0; //Set Radar Lock state 0 (locked)
	m_pMultiRadar = NULL;
	m_pPPIImage = NULL;
	m_pImageClient = new NRP::tImageClient();
	m_pTargetTrackingClient = new NRP::tTargetTrackingClient();
	
	cat240out = false;

	// Initialise record data
	m_dataSourceIdentifier = 0; 
	m_messageType = MT_VideoData;
	m_videoRecordHeader = 0;
	m_startAzimuth = 0;
	m_endAzimuth = 0;
	m_startRange = 0;
	m_cellDuration = 0;
	m_videoCompressionApplied = false;
	m_videoCellsResolution = 0;
	m_videoCellsCounter = 0;
	m_videoOctetsCounter = 0;
	m_videoBlock = NULL;
	m_timeOfDay = 0;
	
	mDangerDistance = 20; //Distance in m to alarm at dangerous CPA
	mDangerTime = 5; //Time in seconds from time to CPS to raise alarm

	eErrors image; //Error type holder
	const unsigned cMaxRadars = 8; 
	int ret = -20; //return holder
	bool retB = 0; //return holder B
	unsigned int numberOfRadars; 
	std::string serNumbLock = "1304303409"; //Default Radar serial (Mobile Bridge)
	if(!RadarSerial.empty()) serNumbLock.assign(RadarSerial); //Radar Serial provided from API
	uint8_t unlockKey[] = { 0xEE, 0xB8, 0x1B, 0xBE, 0x12, 0x0F, 0xE0, 0x04, 0x1B, 0xE3, 0x22, 0xA7, 0xEA, 0xA3, 0x8E, 0x46, 0x21, 0xE7, 0xB3, 0xA9, 0xD3, 0x4D, 0x3B, 0x6B, 0x6C, 0x7B, 0xF9, 0x72,
							0x39, 0xF6, 0x66, 0xBE, 0x79, 0x09, 0x58, 0xC7, 0x25, 0xDF, 0x70, 0x83, 0xF9, 0x19, 0xE4, 0xF9, 0x98, 0x88, 0x92, 0xEA, 0xA4, 0xB7, 0x6F, 0xEB, 0xD1, 0x29, 0x02, 0x73, 0x79, 0xEB, 0xD4,
							0x08, 0xFF, 0x91, 0x3C, 0xA7 }; //Default Radar Unlock Key (Mobile Bridge)
	//if(NULL != UnlockKeyStr && 0 != UnlockKeylength) memcpy(&unlockKey,UnlockKey,UnlockKeylength);
	
	if(!UnlockKeyStr.empty()) //hexString conversion from Navico //replace with function hexstringToArray
	{
		 int len = sizeof(unlockKey);
		 if (len <= 0 || (len & 1) != 0 || (len >>= 1) > (UnlockKeyStr.size()/2));
		 else {
				for (int i = 0; i < len; ++i)
				{
					bool ok = false;
					std::string buffer = UnlockKeyStr.substr( 2*i, 2 );
					if(strtol(buffer.c_str(), NULL, 16) < 256) ok = true;
					if (ok)	unlockKey[ i ] = strtol(buffer.c_str(), NULL, 16);
					else{ uint8_t unlockKey[] = { 0xEE, 0xB8, 0x1B, 0xBE, 0x12, 0x0F, 0xE0, 0x04, 0x1B, 0xE3, 0x22, 0xA7, 0xEA, 0xA3, 0x8E, 0x46, 0x21, 0xE7, 0xB3, 0xA9, 0xD3, 0x4D, 0x3B, 0x6B, 0x6C, 0x7B, 0xF9, 0x72,
								0x39, 0xF6, 0x66, 0xBE, 0x79, 0x09, 0x58, 0xC7, 0x25, 0xDF, 0x70, 0x83, 0xF9, 0x19, 0xE4, 0xF9, 0x98, 0x88, 0x92, 0xEA, 0xA4, 0xB7, 0x6F, 0xEB, 0xD1, 0x29, 0x02, 0x73, 0x79, 0xEB, 0xD4,
								0x08, 0xFF, 0x91, 0x3C, 0xA7 };break;}
				}
		 }
	}
	m_pMultiRadar = tMultiRadarClient::GetInstance(); //Acces single global Instance 
	m_pMultiRadar->AddRadarListObserver(this); //Get Observer to observe list of Radars
	m_pMultiRadar->AddUnlockStateObserver(this); //Get Observer to observe Radar Unlock States
	m_pMultiRadar->SetUnlockKeySupplier(this); //That the NavicoRadarWrapper as Unlockkey provider
	while (0 > ret)ret = m_pMultiRadar->Connect(); //Connect to Radar
	ret = 0;
	std::cout << "Navico Radar: Multi Radar Client Connected"<<std::endl;
	while (false == retB) retB = m_pMultiRadar->QueryRadars(); //Query Radar Configuration
	retB = false;
	std::cout << "Navico Radar: Multi Radar Query succesful"<<std::endl;

	if (m_pImageClient) {
		numberOfRadars = tMultiRadarClient::GetInstance()->GetRadars(radarList, cMaxRadars); //Check number of Radars

		if (1 > myRadarLockState) { //0 locked 
			tMultiRadarClient::GetInstance()->SetUnlockKey(serNumbLock.c_str(), unlockKey, sizeof(unlockKey)); //Set Unlockkey
			while (0 >= ret) //Try to Unlock Radar
			{
				ret = tMultiRadarClient::GetInstance()->UnlockRadar(serNumbLock.c_str(), &unlockKey[0], sizeof(unlockKey), tMultiRadarClient::GetInstance()->cDefaultUnlockPeriod); //Unlock Radar
			}
			ret = 0;
			std::cout << "Navico Radar: Radar " << serNumbLock.c_str() << " Unlocked Succesfull"<<std::endl;
		}
		

		while (false == retB) retB = m_pImageClient->AddSpokeObserver(this); //Set up the Radar Spoke Callback
		retB = false;
		std::cout << "Navico Radar: Multi Radar Image SpokeObserver added"<<std::endl;
		while (false == retB) retB = m_pImageClient->AddStateObserver(this); //Set up State Callbacks Radar Image client
		retB = false;
		std::cout << "Navico Radar: Multi RadarImage StateObserver added"<<std::endl;
		while (false == retB) retB = m_pTargetTrackingClient->AddStateObserver(this);//Set up Radar Track state Callback
		retB = false;
		std::cout << "Navico Radar: Multi Radar Target StateObserver added"<<std::endl;
		while (false == retB) retB = m_pTargetTrackingClient->AddTargetTrackingObserver(this); //Set up the Radar Spoke Callback
		retB = false;
		std::cout << "Navico Radar: Multi Radar Target TrackingObserver added"<<std::endl;
		
		
		int instances = tMultiRadarClient::GetInstance()->GetImageStreamCount(serNumbLock.c_str()); //Get Radar Instances
		if (0 < instances) {//Check Instances
			for (int j = 1; j <= instances; j++) {

				image = (eErrors) m_pImageClient->Connect(serNumbLock.c_str(), j); //Connect to Radar Image Client
				eErrors err2 = (eErrors) m_pTargetTrackingClient->Connect(serNumbLock.c_str(), j); // Connect Radar Tracking Client
				switch (image) {
				case EOK:
					while (false == retB)  retB = m_pImageClient->SetPower(1); //From Power off to Standby
					retB = false;
					initState = configuredRadar;
					if (15 > this->mRangeMeter) { //15m is minimum Range
						this->mRangeMeter = 15; //Set minimum Range
					}

					if (false == retB) retB = m_pImageClient->SetRange(this->mRangeMeter);//Set Image Radius 
					if(retB == false) std::cout << "Navico Radar: Radar Range Not set" <<std::endl;
					else std::cout << "Navico Radar: Radar Range Set to "<< this->mRangeMeter <<std::endl; retB = false;
					if (false == retB) retB = m_pImageClient->SetAntennaHeight(this->mAntennaHeight); //Set Radarhead height
					if(retB == false) std::cout << "Navico Radar: Radar Height Not Set" <<std::endl;
					else std::cout << "Navico Radar: Radar Height Set to "<< this->mAntennaHeight/1000.0 <<"m" <<std::endl; retB = false;
		
					//m_pImageClient->SetGuardZoneEnable(0,1);
					//m_pImageClient->SetGuardZoneSetup(0, getStartRangeMeter(), getEndRangeMeter(), getBearingDegree(), getWidthDegree());
					
					if(false == retB) retB = m_pTargetTrackingClient->SetDangerDistance((uint32_t) mDangerDistance); //Set Dangerzone Radius
					if(retB == false) std::cout << "Navico Radar: Radar Tracking Danger Distance Not Set" <<std::endl;
					else std::cout << "Navico Radar: Radar Tracking Danger Distance Set to "<< mDangerDistance <<std::endl; retB = false;
		
					if(false == retB) retB = m_pTargetTrackingClient->SetDangerTime((uint32_t) mDangerTime); // Set Time to CPA Alarm time
					if(retB == false) std::cout << "Navico Radar: Radar Tracking Danger Time Not set" <<std::endl;
					else	std::cout << "Navico Radar: Radar Tracking Danger Time Set to "<< mDangerTime <<std::endl; retB = false;

					std::cout << "Radar Image Client EOK"<<std::endl;
					break;
				case ELocked:
					std::cout << "Radar Image Client ELocked"<<std::endl;
					break;
				case EPending:
					std::cout << "Radar Image Client EPending"<<std::endl;
					break;
				case ETimedOut:
					std::cout << "Radar Image Client ETimedOut"<<std::endl;
					break;
				case EBusy:
					std::cout << "Radar Image Client EBusy"<<std::endl;;
					break;
				case EBadSerialNumber:
					std::cout << "Radar Image Client EBadSerialNumber"<<std::endl;
					break;
				case ENoUnlockKey:
					std::cout << "Radar Image Client ENoUnlockKey"<<std::endl;
					break;
				case EBadUnlockKey:
					std::cout << "Radar Image EBadUnlockKey"<<std::endl;
					break;
				case EWrongUnlockKey:
					std::cout << "Radar Image Client EWrongUnlockKey"<<std::endl;
					break;
				case ENotRunning:
					std::cout << "Radar Image Client ENotRunning"<<std::endl;
					break;
				case EUnknownRadar:
					std::cout << "Radar Image Client EUnknownRadar"<<std::endl;
					break;
				case ENonStdAddress:
					std::cout << "Radar Image Client ENonStdAddress"<<std::endl;
					break;
				case ECommsFailure:
					std::cout << "Radar Image Client ECommsFailure"<<std::endl;
					break;
				case EThreadCreation:
					std::cout << "Radar Image Client EThreadCreation"<<std::endl;
					break;
				case EBadParameter:
					std::cout << "Radar Image Client EBadParameter"<<std::endl;
					break;
				case EUnused:
					std::cout << "Radar Image Client EUnused"<<std::endl;
					break;
				default:
					break;
				}
				switch (err2) {
				case EOK:
					std::cout << "Target Track Client EOK"<<std::endl;
					break;
				case ELocked:
					std::cout << "Target Track Client ELocked"<<std::endl;
					break;
				case EPending:
					std::cout << "Target Track Client EPending"<<std::endl;
					break;
				case ETimedOut:
					std::cout << "Target Track Client ETimedOut"<<std::endl;
					break;
				case EBusy:
					std::cout << "Target Track Client EBusy"<<std::endl;
					break;
				case EBadSerialNumber:
					std::cout << "Target Track Client EBadSerialNumber"<<std::endl;
					break;
				case ENoUnlockKey:
					std::cout << "Target Track Client ENoUnlockKey"<<std::endl;
					break;
				case EBadUnlockKey:
					std::cout << "Target Track Client EBadUnlockKey"<<std::endl;
					break;
				case EWrongUnlockKey:
					std::cout << "Target Track Client EWrongUnlockKey" <<std::endl;
					break;
				case ENotRunning:
					std::cout << "Target Track Client ENotRunning"<<std::endl;
					break;
				case EUnknownRadar:
					std::cout << "Target Track Client EUnknownRadar"<<std::endl;
					break;
				case ENonStdAddress:
					std::cout << "Target Track Client ENonStdAddress"<<std::endl;
					break;
				case ECommsFailure:
					std::cout << "Target Track Client ECommsFailure"<<std::endl;
					break;
				case EThreadCreation:
					std::cout << "Target Track Client EThreadCreation"<<std::endl;
					break;
				case EBadParameter:
					std::cout << "Target Track Client EBadParameter"<<std::endl;
					break;
				case EUnused:
					std::cout << "Target Track Client EUnused"<<std::endl;
					break;
				default:
					break;
				}
			}
		} else {
			throw exception("RADAR ERROR: Get Stream error");
		}
		//}
	} else {
		throw exception("RADAR ERROR: No Imageclient Instance created");
	}	
}



void NavicoRadarWrapper::start() //Start Radar
{
	if (m_pImageClient) {

		if (m_pImageClient->SetRotate(1) && m_pImageClient->SetTransmit(1)) //enable Rotating and Transmitting
				{
					m_pImageClient->SetAutoSendClientWatchdog(true); //Send automatically Watchdog Checks
			initState = runningRadar; //Set Transmit on
		   std::cout << "Navico Radar: Radar Transmit start" <<std::endl;
		}
		else {	
			throw exception("RADAR ERROR: Radar won't Start");
		}
	}
	else {	
			throw exception("RADAR ERROR: Client not initialized");
		}
	
	
		
}

bool NavicoRadarWrapper::AcquireTargets(int TargetId, int range, int bearing, int bearingtype)   //Try to acquire Target
{
	uint32_t id = TargetId; //Target ID vom Client
	uint32_t range_m = range; //Distance to Target
	uint16_t bearing_deg = bearing; //bearing in degrees
	Navico::Protocol::NRP::eBearingType bearingType = (Navico::Protocol::NRP::eBearingType)bearingtype; //True or Magnetic
	if(m_pTargetTrackingClient)
	{
		std::cout << "Navico Radar: try to Acquire target" <<std::endl;
		return m_pTargetTrackingClient->Acquire(id, range_m, bearing_deg, bearingType); //Request Acquire Target
		
	}
	return false;
}

bool NavicoRadarWrapper::SetBoatSpeed(int speedType, double speed, int headingType, double heading) //Set Own Ship Movement
{
	if(m_pTargetTrackingClient)
	{
		//std::cout << "Navico Radar: Set Boat Speed and Heading" <<std::endl;
		return m_pTargetTrackingClient->SetBoatSpeed((Navico::Protocol::NRP::eSpeedType) speedType, speed,(Navico::Protocol::NRP::eDirectionType) headingType, heading); //Set boat Speed and heading
	}
	return 0;}

bool NavicoRadarWrapper::cancelAll()  //Cancel all Radar Tracks
{
	if(m_pTargetTrackingClient)
	{
		return m_pTargetTrackingClient->CancelAll(); //Cancel all Radar Tracks
	}
	return false;
}




void NavicoRadarWrapper::UpdateSpoke(const NRP::Spoke::t9174Spoke *pSpoke) { //Callback for Radar Spokes
//IMAGE Start
	if (!m_pPPIImage) //Cheick if Image Client is initialized
	{
		m_pPPIImage = new uint8_t[ (2*NUM_OF_SAMPLES)*(2*NUM_OF_SAMPLES)*sizeof(Navico::tColor) ];  //Create Array Image Array
		m_pPPIController = new Navico::Image::tPPIController(); //Create Image Controller
 
		m_pPPIController->SetFrameBuffer((intptr_t)m_pPPIImage,(2*NUM_OF_SAMPLES), (2*NUM_OF_SAMPLES), &gNavicoLUT,(2 * NUM_OF_SAMPLES) / 2, (2 * NUM_OF_SAMPLES) / 2 ); //Initialize Fream Buffer
		m_pPPIController->SetTrailsTime(300);//Set Trail of Radar Targets in seconds
		m_pPPIController->SetRangeInterpolation(Navico::Image::tPPIRangeInterpolation::eRangeInterpolationPeak); //Range Interpolation method see PPI documentation
		m_pPPIController->SetRangeResolution(mRangeMeter/((float)(2*NUM_OF_SAMPLES))); //Set Range to max Range
	}

	m_pPPIController->Process( pSpoke ); //Update Image Buffer 
	static int count123 = 0;
	if(count123>=100) //Send every 100 Spokes the Picture to Odysseus
	{
			onPictureUpdate(m_pPPIImage, ((2*NUM_OF_SAMPLES)*(2*NUM_OF_SAMPLES)*sizeof(Navico::tColor)));
        	count123=0;
	}
	count123++;
//Image END

		//Calculate Azimuths
		uint32_t azimuth; 
		uint32_t startAzimuth;
		uint32_t stopAzimuth;
		uint32_t halfBeamWidth = 29; //Beamwidth = 5,2° +- 10% ca 2,6 /360 *4096 = ca, 29.5
		if(4095<pSpoke->NRP::Spoke::t9174Spoke::header.spokeAzimuth)
		{
			azimuth = 4095;
		}
		else azimuth = pSpoke->NRP::Spoke::t9174Spoke::header.spokeAzimuth;

		if(azimuth < halfBeamWidth) 
		{
			startAzimuth = 4095 + azimuth - halfBeamWidth;
			stopAzimuth = azimuth + halfBeamWidth;
		}
		else if(azimuth > (4095 - halfBeamWidth))
		{
			startAzimuth = azimuth - halfBeamWidth;
			stopAzimuth = azimuth + halfBeamWidth - 4095;
		}
		else {
			startAzimuth = azimuth - halfBeamWidth;
			stopAzimuth = azimuth + halfBeamWidth;
		}

//Cat240 Send Start
	if(cat240out)//If Transmission of Cat 240 is Requestet
	{
		packedcat240Spoke catSpoke;
		
		//Category
		catSpoke.catMsgHead.asterixCategory = 240;
		//LEN
		catSpoke.catMsgHead.dataLength = 26 + 3; //Mandatory Btes
		catSpoke.catMsgHead.dataLength += 2 + sizeof pSpoke->NRP::Spoke::t9174Spoke::data;
		//FSPEC UAP listed
		catSpoke.catMsgHead.FSPEC = 0x02CE; // 000XXX10 10101110 / first bits of each byte is reserved
		if (sizeof pSpoke->NRP::Spoke::t9174Spoke::data < 1020)
			catSpoke.catMsgHead.FSPEC += 0x0400;        //00000110 10101110
		else if (sizeof pSpoke->NRP::Spoke::t9174Spoke::data < 16320)
			catSpoke.catMsgHead.FSPEC += 0x0800;  //00001010 10101110
		else
			catSpoke.catMsgHead.FSPEC += 0x1000; //00010010 10101110

		//UAP_MessageType = 2
		catSpoke.cat240Header.videoMsgType = MT_VideoData;
		//UAP_DataSourceIdentifier	= 1
		catSpoke.cat240Header.dataSourceIdentifier.systemAreaCode = 61; //Germany System Area Code List 61/62
		catSpoke.cat240Header.dataSourceIdentifier.systemIdentificationCode = 0; //SIC= System Identication Code obtained by local air control
		//UAP_VideoRecordHeader
		catSpoke.cat240Header.videoRecordHeader = pSpoke->NRP::Spoke::t9174Spoke::header.sequenceNumber;
		// UAP_VideoHeaderNano = 5,
		catSpoke.cat240Header.videoHeaderNano.startAzimuth = startAzimuth * 16; //Azimuth Cat240 nano 360*x/2^16 / Azimuth t9174Spoke 360*x/2^12      half Azimuth 360*y/2^16 = 360*x/2^12 - 1/2*360/2^12 ->  y=16x-8
		catSpoke.cat240Header.videoHeaderNano.endAzimuth = stopAzimuth * 16;	//same but plus
		catSpoke.cat240Header.videoHeaderNano.startRange = 0;
		catSpoke.cat240Header.videoHeaderNano.cellDuration = (uint32_t)(1.0 / ((((double) m_pSetupExtended.rpmX10) / 6.0) * 4096.0) * 1000000000.0); //1/(rpm*10/60 * 2^12) (*10^-9?) //ToDo Check if it hast to be multiplied by 10^-9
		//UAP_VideoCellsResolutionAndCompressionInd	= 7,
		catSpoke.cat240Header.cellResAndDataCompression.dataCompressIndicator = (uint8_t) false * 0x80;
		catSpoke.cat240Header.cellResAndDataCompression.resolution = VR_MediumResolution; //4 Bit
		//UAP_VideoOctetsAndCellsCounter			= 8,
		catSpoke.cat240Header.OctetAndCellNumbers.numberOfValidOctets = pSpoke->NRP::Spoke::t9174Spoke::header.nOfSamples / 2; //No of samples by 4 Bits are half number of samples = Bytes
		m_videoCellsCounter = 2 * pSpoke->NRP::Spoke::t9174Spoke::header.rangeCellsDiv2;		 //RangeCells Mulitplied by 2
		catSpoke.cat240Header.OctetAndCellNumbers.numberOfValidCells1 = (uint8_t) m_videoCellsCounter >> 16;
		catSpoke.cat240Header.OctetAndCellNumbers.numberOfValidCells2 = (uint8_t) m_videoCellsCounter >> 8;
		catSpoke.cat240Header.OctetAndCellNumbers.numberOfValidCells3 = (uint8_t) m_videoCellsCounter;
		
		//UAP_VideoBlockLowDataVolume				= 9,
		if (sizeof pSpoke->NRP::Spoke::t9174Spoke::data < 1020) {
			catSpoke.repetitionFactor = (sizeof pSpoke->NRP::Spoke::t9174Spoke::data) / 4;

			memcpy(catSpoke.videoBlock, pSpoke->data, sizeof pSpoke->NRP::Spoke::t9174Spoke::data);
		}
		//UAP_VideoBlockMediumDataVolume				= 10,
		else if (sizeof pSpoke->NRP::Spoke::t9174Spoke::data < 16320) {
			catSpoke.repetitionFactor = (sizeof pSpoke->NRP::Spoke::t9174Spoke::data) / 64;
			memcpy(catSpoke.videoBlock, pSpoke->data, sizeof pSpoke->NRP::Spoke::t9174Spoke::data);
		}
		//UAP_VideoBlockHighDataVolume				= 11,
		else {
			catSpoke.repetitionFactor = (sizeof pSpoke->NRP::Spoke::t9174Spoke::data) / 256;
			memcpy(catSpoke.videoBlock, pSpoke->data, sizeof pSpoke->NRP::Spoke::t9174Spoke::data);
		}
		onCat240SpokeUpdate(&catSpoke, sizeof(catSpoke));
	}
//Cat240 Send END

//T9174 Spoke Send START
 	packedT9174Spoke packSpoke;
	packSpoke.header.spokeLength_bytes =  pSpoke->NRP::Spoke::t9174Spoke::header.spokeLength_bytes;  //!< length of the whole spoke in bytes
	packSpoke.header.sequenceNumber =  pSpoke->NRP::Spoke::t9174Spoke::header.sequenceNumber;      //!< spoke sequence number
    packSpoke.header.nOfSamples =  pSpoke->NRP::Spoke::t9174Spoke::header.nOfSamples;          //!< number of samples present in the spoke
	packSpoke.header.bitsPerSample =  pSpoke->NRP::Spoke::t9174Spoke::header.bitsPerSample;         //!< number of bits per sample, normally is set to 4
    packSpoke.header.rangeCellSize_mm =  pSpoke->NRP::Spoke::t9174Spoke::header.rangeCellSize_mm;    //!< Distance represented by each range-cell. sample size is computed as: rangeCellSize_mm * 2*rangeCellsDiv2 / nOfSamples;
	
	packSpoke.header.spokeAzimuth =  azimuth;         //!< Azimuth of the spoke in the range 0-4095. Values greater than 4095 must be mapped to 4095. This represents a full circle 0-360 degrees
      
    packSpoke.header.bearingZeroError =  pSpoke->NRP::Spoke::t9174Spoke::header.bearingZeroError;      //!< Set if there is malfunctioning bearing zero
     
    packSpoke.header.spokeCompass =  pSpoke->NRP::Spoke::t9174Spoke::header.spokeCompass;         //!< Heading of the boat when this spoke was sampled. It is represented in the 0-4095 range for 0-360degrees of heading
    packSpoke.header.trueNorth =  pSpoke->NRP::Spoke::t9174Spoke::header.trueNorth;            //!< The connected heading sensor is reporting true north (1) or magnetic north (0)
    packSpoke.header.compassInvalid=  pSpoke->NRP::Spoke::t9174Spoke::header.compassInvalid;       //!< If this bit is 1, the compass information are invalid

    packSpoke.header.rangeCellsDiv2 =  pSpoke->NRP::Spoke::t9174Spoke::header.rangeCellsDiv2;      //!< Number of range-cells represented by the data in this spoke, divided by 2
	memcpy(packSpoke.data, pSpoke->data, sizeof pSpoke->NRP::Spoke::t9174Spoke::data);
	onSpokeUpdate(&packSpoke, sizeof(packSpoke));
//T9174 Spoke Send END

	/*
	float setStartAzimuth((float) (azimuth * 16 - 8) * 360.0 / 65536.0); //x*360/2^16
	float setEndAzimuth((float) (azimuth * 16 + 8) * 360.0 / 65536.0);
	float setCellRange((float) pSpoke->NRP::Spoke::t9174Spoke::header.rangeCellSize_mm / 1000.0);

	
	
	Navico::tRadarColourLookUpTable gNavicoLUT;
	uint32_t colour[(sizeof pSpoke->NRP::Spoke::t9174Spoke::data) * 2];
	if (4 == pSpoke->NRP::Spoke::t9174Spoke::header.bitsPerSample) {
		for (unsigned int i = 0; i < pSpoke->NRP::Spoke::t9174Spoke::header.nOfSamples / 2; i++) {
			//cellList->add(gNavicoLUT.GetColour(((pSpoke->data[i]     ) & 0xf)));
			//cellList->add(gNavicoLUT.GetColour(((pSpoke->data[i] >> 4) & 0xf)));
			colour[(i * 2)] = (gNavicoLUT.GetColour(((pSpoke->NRP::Spoke::t9174Spoke::data[i]) & 0xf)));
			colour[(i * 2) + 1] = (gNavicoLUT.GetColour(((pSpoke->NRP::Spoke::t9174Spoke::data[i] >> 4) & 0xf)));
		}
	}else throw exception("Radar Bits per Sample is wrong");
	JavaCallback->onSpokeUpdate(minSpoke(setStartAzimuth,setEndAzimuth,setCellRange,(int*)&colour, sizeof(colour)));
	*/
		

	
}

// iImageClientStateObserver callbacks
void NavicoRadarWrapper::UpdateMode(const NRP::tMode* pMode) {
	m_pMode = *pMode;
}
void NavicoRadarWrapper::UpdateSetup(const NRP::tSetup* pSetup) {
	m_pSetup = *pSetup;
}
void NavicoRadarWrapper::UpdateSetupExtended(const NRP::tSetupExtended* pSetupExtended) {
	m_pSetupExtended = *pSetupExtended;
}
void NavicoRadarWrapper::UpdateProperties(const NRP::tProperties* pProperties) {
	m_pProperties = *pProperties;
}
void NavicoRadarWrapper::UpdateConfiguration(const NRP::tConfiguration* pConfiguration) {
	m_pConfiguration = *pConfiguration;
}
void NavicoRadarWrapper::UpdateGuardZoneAlarm(const NRP::tGuardZoneAlarm* pAlarm) {
	m_pAlarmGZA = *pAlarm;
}
void NavicoRadarWrapper::UpdateRadarError(const NRP::tRadarError* pAlarm) {
	m_pAlarmRE = *pAlarm;
}
void NavicoRadarWrapper::UpdateAdvancedState(const NRP::tAdvancedSTCState* pState) {
	m_pState = *pState;
}

void NavicoRadarWrapper::UpdateRadarList(const char* pSerialNumber, iRadarListObserver::eAction action) {
	const unsigned cMaxRadars = 8;

	switch (action) {//Nothing Implemented
	case Navico::Protocol::iRadarListObserver::Added:
		break;
	case Navico::Protocol::iRadarListObserver::Removed:
		break;
	case Navico::Protocol::iRadarListObserver::Changed:
		break;
	default:
		break;
	}

}

int NavicoRadarWrapper::GetUnlockKey(const char* pSerialNumber, const uint8_t* pLockID, unsigned lockIDSize, uint8_t* pUnlockKey, unsigned maxUnlockKeySize) {//Get Unlock KEy Callback
	int retval;
	if(NULL == pSerialNumber) return -1;
	std::string unlockString;
	std::string serNumb(pSerialNumber);
	if(serNumb.compare("1304303409")) //Mobile NaviBox
	unlockString = "EEB81BBE120FE0041BE322A7EAA38E4621E7B3A9D34D3B6B6C7BF97239F666BE790958C725DF7083F919E4F9988892EAA4B76FEBD129027379EBD408FF913CA7";
	/*uint8_t unlockKey[] = { 0xEE, 0xB8, 0x1B, 0xBE, 0x12, 0x0F, 0xE0, 0x04, 0x1B, 0xE3, 0x22, 0xA7, 0xEA, 0xA3, 0x8E, 0x46, 0x21, 0xE7, 0xB3, 0xA9, 0xD3, 0x4D, 0x3B, 0x6B, 0x6C, 0x7B, 0xF9, 0x72,
			0x39, 0xF6, 0x66, 0xBE, 0x79, 0x09, 0x58, 0xC7, 0x25, 0xDF, 0x70, 0x83, 0xF9, 0x19, 0xE4, 0xF9, 0x98, 0x88, 0x92, 0xEA, 0xA4, 0xB7, 0x6F, 0xEB, 0xD1, 0x29, 0x02, 0x73, 0x79, 0xEB, 0xD4,
			0x08, 0xFF, 0x91, 0x3C, 0xA7 };*/
	if(serNumb.compare("1405303984")) //Navibox Station x 
	unlockString = "22EA4981EC17C268E421BBACDE3F16D560F0864320A4EF253525A2895B713B6B16DF892FDC924CE69011EB3DB2F0F5F4FEC6493968C0016B58A9EBE1C9B17291";
	if(serNumb.compare("1405303985"))//Navibox Station y
	unlockString = "A6CFDB8E64F4A770DB48740BE0EDB5732F355C688CE3EC2C6CC3EADC49C430E5F49A92C5CFBB67AC7E1A643F0B7545FEC55CA5777DE85A4BAF5345B802427DC3";
	if(serNumb.compare("1406302796"))//Navibox Station z
	unlockString = "5540A0503B12479AC8AA18BD9CFE9F8C088353AFFBF0ADE3D3E4B803B6FD53CADB66EDF69E3C7788059DA88DD4A2D713FD1CD1856582DD38B03E41E2F28CE6DF";
	if(unlockString.empty()) return -1;//No Unlock Key supllied in this functions
	uint8_t  *unlockKey = hexstringToArray(unlockString); //Change String to Unlockkey Array

	if (maxUnlockKeySize >= sizeof unlockKey) { //Check if Key does not exceed Max Size
		for (int i = 0; i < sizeof unlockKey; i++) {
			pUnlockKey[i] = unlockKey[i];
		}
		retval = sizeof unlockKey;
	} else
		retval = -1;//No Unlock Key supllied in this functions
	return retval; 
}

//Lockstate Callback
void NavicoRadarWrapper::UpdateUnlockState(const char* pSerialNumber, int lockState) {
		int myRadarLockState = lockState;

}


void NavicoRadarWrapper::UpdateTarget( const NRP::tTrackedTarget* pTarget )//Radar Track Callback
{
		if ((pTarget->infoAbsoluteValid) && (500000<pTarget->infoAbsolute.distance_m)||(0 == pTarget->infoRelative.distance_m) && (500000<pTarget->infoAbsolute.distance_m)) return;
		

			packedTrackedTarget target;
			
			target.targetValid = pTarget->targetValid;        ///< 0x00 invalid, 0x01 valid
			target.trueTarget = pTarget->trueTarget;         ///< 0x00 simulated target, 0x01 true/real target
			target.targetType = pTarget->targetType;         ///< One of eTargetType enum values
			target.targetID = pTarget->targetID;           ///< Client assigned target-ID (provided by user in manual-acquire0
			target.serverTargetID = pTarget->serverTargetID;     ///< Server assigned target-ID (negative when invalid acquire - eFailAcquire* states)
			target.targetState = pTarget->targetState;        ///< One of eTargetState enum values
			target.autoAcquired = pTarget->autoAcquired;       ///< One of eAcquireType enum values (manually or automatically acquired)

			///< Target details expressed relative to the boats speed and direction
			target.infoRelative.distance_m = pTarget->infoRelative.distance_m; ///< Distance to target in metres
			target.infoRelative.bearing_ddeg = pTarget->infoRelative.bearing_ddeg;    ///< Target bearing in 10ths of a degree (deci-degrees)
			target.infoRelative.course_ddeg = pTarget->infoRelative.course_ddeg;     ///< Target course in 10ths of a degree (deci-degrees)
			target.infoRelative.speed_dmps = pTarget->infoRelative.speed_dmps;      ///< Target speed in 10ths of a metre per second (deci-metres/second)
			///< Target details expressed independant of the boat (relative to true north)
			target.infoAbsolute.distance_m = pTarget->infoAbsolute.distance_m; ///< Distance to target in metres
			target.infoAbsolute.bearing_ddeg = pTarget->infoAbsolute.bearing_ddeg;    ///< Target bearing in 10ths of a degree (deci-degrees)
			target.infoAbsolute.course_ddeg = pTarget->infoAbsolute.course_ddeg;     ///< Target course in 10ths of a degree (deci-degrees)
			target.infoAbsolute.speed_dmps = pTarget->infoAbsolute.speed_dmps;      ///< Target speed in 10ths of a metre per second (deci-metres/second)
			target.infoAbsoluteValid = pTarget->infoAbsoluteValid;  ///< Whether 'infoAbsolute' is valid (0 absolute info invalid, 1 valid)

			target.CPA_m = pTarget->CPA_m;              ///< Closest point of approach expressed in meters
			target.TCPA_sec = pTarget->TCPA_sec;           ///< Time to the closest point of approach expressed in seconds
			target.towardsCPA = pTarget->towardsCPA;         ///< CPA direction (0 target moving away from CPA, 1 towards CPA)
		if (60000<pTarget->TCPA_sec)target.TCPA_sec=0;
		onTargetUpdate(&target, sizeof(target));
		
		std::stringstream ttm;
		ttm << "$RATTM,"; // Beginning Sender ID RA = Radar, Sentence ID TTM = Tracked Target Status (Message)
		if(10>pTarget->targetID)ttm << "0"; //Target ID < 10 begin with a Zero
		ttm << (pTarget->targetID) << ","; //Fill Target ID ARPA Assigned Target Number 00-99
		if (pTarget->infoAbsoluteValid) //Orientation True
		{
			//ttm.precision(1);
			ttm << std::fixed << std::setprecision(1) << (pTarget->infoAbsolute.distance_m/1852.0) << ","; //Target Distance in NM from m
			ttm << (pTarget->infoAbsolute.bearing_ddeg / 10.0)<< ","; //Target Bearing from Ownship in degrees from deci degrees
			ttm << "T,";
			ttm << ((pTarget->infoAbsolute.speed_dmps / 10.0) * 1.94384449)<< ","; // Target speed in knots from deci m/s
			ttm << (pTarget->infoAbsolute.course_ddeg / 10.0)<< ","; //Target course in degrees rom deci degrees
			ttm << "T,";
		}
		else //Orientation Relative
		{
			ttm << std::fixed << std::setprecision(1) << (pTarget->infoRelative.distance_m/1852.0) << ","; //Target Distance in NM from m
			ttm << (pTarget->infoRelative.bearing_ddeg / 10.0)<< ","; //Target Bearing from Ownship in degrees rom deci degrees
			ttm << "R,";
			ttm << ((pTarget->infoRelative.speed_dmps / 10.0) * 1.94384449)<< ",";// Target speed in knots from deci m/s
			ttm << (pTarget->infoRelative.course_ddeg / 10.0)<< ","; //Target course in degrees rom deci degrees
			ttm << "R,";
		}		
		ttm << std::fixed << std::setprecision(1) << (pTarget->CPA_m)/1000.0 << ","; //Closest point of Approach in km from m
		if(pTarget->towardsCPA) ttm << (target.TCPA_sec)/60.0 << ","; //Time to closest point of approach 
		else ttm << -1.0*(target.TCPA_sec)/60.0 << ","; // a negative number indicates the target is past its CPA
		ttm << "K,"; //Distance and Speed Units N is NM and knots, K is Kilometres, S is statute miles
		ttm /*<< (int)pTarget->serverTargetID*/ << ","; //Target Label on ARPA (TAR01)

		switch(pTarget->targetState) // Target Status T = Tracking / L = Lost / Q = Acquiring
		{
			case Navico::Protocol::NRP::eAcquiringTarget:	ttm << "Q,";
															break;     ///< Attempting to acquire target
			case Navico::Protocol::NRP::eSafeTarget:		ttm << "T,";
															break;     ///< Target acquired and not on a collision course
			case Navico::Protocol::NRP::eDangerousTarget:	ttm << "T,";
															break;     ///< Target acquired and may be on a collision course
			case Navico::Protocol::NRP::eLostTarget:		ttm << "L,";
															break;     ///< Target has been lost and needs to be cancelled an reacquired
			case Navico::Protocol::NRP::eAcquireFailure:	ttm << "L,";
															break;     ///< Failed to acquire a target
			case Navico::Protocol::NRP::eOutOfRange:		ttm << "L,";
															break;     ///< Target is now out of range
			case Navico::Protocol::NRP::eLostOutOfRange:	ttm << "L,";
															break;     ///< Target lost due to staying out of range
			case Navico::Protocol::NRP::eFailAcquireMax:	ttm << "L,";
															break;     ///< Acquire failed because no target IDs were free
			case Navico::Protocol::NRP::eFailAcquirePos:	ttm << "L,";
															break;     ///< Acquire failed because the position was invalid
				default:									ttm << "L,";
				break;
		}
		if(pTarget->trueTarget)ttm << ","; //Reference Targets are Calibration Targets
		else ttm << "R,";

		//ttm.precision(2);
		ttm << ","; // UTC Time Stamp
		if(Navico::Protocol::NRP::eAcquireType::eAutoAcquired == pTarget->autoAcquired) ttm << "A"; // Target acqusition automatic
		else if(Navico::Protocol::NRP::eAcquireType::eManualAcquired == pTarget->autoAcquired) ttm << "M"; //Target acquisition Manual
		ttm << "*" << computeCheckSum(ttm.str()); //Add Checksum
	    onTargetUpdateTTM(ttm.str());
}



// iTargetTrackingClientStateObserver callbacks
void NavicoRadarWrapper::UpdateAlarmSetup( const NRP::tTargetTrackingAlarmSetup* pAlarmSetup ){
	m_pAlarmSetup = *pAlarmSetup;
}
void NavicoRadarWrapper::UpdateProperties( const NRP::tTargetTrackingProperties* pProperties ){
	m_pTrackProperties = *pProperties;
}


void NavicoRadarWrapper::stop() //Stop Radar
{
	if (m_pImageClient) {
		m_pImageClient->SetRotate(0); //StopRotating
		m_pImageClient->SetTransmit(0); //disable Transmit
		m_pImageClient->Disconnect();
		m_pImageClient->RemoveSpokeObserver(this);
		m_pImageClient->RemoveStateObserver(this);
	}
	if (m_pMultiRadar) {
		m_pMultiRadar->Disconnect();
	}
	if(m_pTargetTrackingClient){
		m_pTargetTrackingClient->Disconnect();
		m_pTargetTrackingClient->RemoveStateObserver(this);
		m_pTargetTrackingClient->RemoveTargetTrackingObserver(this);
	}
	cout <<"Radar Destroyed" << endl;
	initState = noinitRadar;
}

void NavicoRadarWrapper::setCat240Out()
{
	cat240out = true;

}

void NavicoRadarWrapper::delCat240Out()
{
    cat240out = false;

}

//Set Radar Configuration Functions for API
bool NavicoRadarWrapper::setAntennaHeight(int HeightInMm)
{
	if(NULL != m_pImageClient) return m_pImageClient->SetAntennaHeight((uint32_t)HeightInMm);
	else return false;
}
int NavicoRadarWrapper::getAntennaHeight()
{
	return m_pConfiguration.antennaHeight_mm;
}


bool NavicoRadarWrapper::setRadarRange(int RangeInM)
{
	if(NULL != m_pImageClient) return m_pImageClient->SetRange((uint32_t)RangeInM);
	else return false;
}
int NavicoRadarWrapper::getRadarRange()
	{
	return m_pSetup.range_dm/10;
}
	bool NavicoRadarWrapper::setDangerDistance(int DistanceInM)
{
	if(NULL != m_pTargetTrackingClient) return m_pTargetTrackingClient->SetDangerDistance((uint32_t)DistanceInM);
	else return false;
}
int	NavicoRadarWrapper::getDangerDistance()
	{
		return m_pAlarmSetup.safeZoneDistance_m;
}
	bool NavicoRadarWrapper::setDangerTime(int TimeInS)
{
	if(NULL != m_pTargetTrackingClient) return m_pTargetTrackingClient->SetDangerTime((uint32_t)TimeInS);
	else return false;
}

int	NavicoRadarWrapper::getDangerTime()
		{
	return m_pAlarmSetup.safeZoneTime_sec;
}

//Callback Functions for Odysseus
void NavicoRadarWrapper::onSpokeUpdate(void *buffer, long size){}
void NavicoRadarWrapper::onCat240SpokeUpdate(void *buffer, long size){}
void NavicoRadarWrapper::onTargetUpdate(void *buffer, long size){}
void NavicoRadarWrapper::onTargetUpdateTTM(std::string TTMmessage){}
void NavicoRadarWrapper::onPictureUpdate(void *buffer, long size){}