#ifndef NAVICO_WRAP_H
#define NAVICO_WRAP_H

#include <exception>
#include <ImageClient.h>
#include <ImageClientObserver.h>
#include <TargetTrackingObserver.h>
#include <TargetTrackingClient.h>
#include <MultiRadarClient.h>
#include <PPIController.h>
#include "MastRadarTypes.h"
#include <iostream>
#include <RadarColourLookUpTable.h>

#define NUM_OF_SAMPLES 1024 

/*
class minSpoke
{
private:
	const int *msg;
	int length;

public:
	explicit minSpoke(float startAzimuth,float stopAzimuth,float cellrange,const int  *msg, int length):msg(msg),length(length){
//		
	}

	int getLength() { return length;} 
	signed char getData(int index) 
	{ 
		return msg[index];

 
	}
};*/
/*
class t9174Spoke
{
public:
	const signed char  *msg;
	int length;

public:
	explicit t9174Spoke(const signed char  *msg, int length):msg(msg),length(length){
//		
	}

	int getLength() { return length;} 
	signed char getData(int index) 
	{ 
		return msg[index];

 
	}
};



class cat240Spoke
{
private:
	const signed char  *msg;
	int length;

public:
	explicit cat240Spoke(const signed char  *msg, int length):msg(msg),length(length){
//		
	}

	int getLength() { return length;} 
	signed char getData(int index) 
	{ 
		return msg[index];

 
	}
};



class trackedTarget
{
private:
	const signed char  *msg;
	int length;

public:
	explicit trackedTarget(const signed char  *msg, int length):msg(msg),length(length){
//		
	}

	int getLength() { return length;} 
	signed char getData(int index) 
	{ 
		return msg[index];

 
	}
};


class RadarCallback{
public:
	virtual ~RadarCallback(){}
	virtual void onSpokeUpdate(t9174Spoke spoke)//(minSpoke spoke) 
	{
		std::cout << "Spoke Callback"<<std::endl;
	}
	virtual void onCat240SpokeUpdate(cat240Spoke spoke) 
	{
		std::cout << "Spoke Callback"<<std::endl;
	}
	virtual void onTargetUpdate(trackedTarget target) 
	{
		std::cout << "Target Callback"<<std::endl;
	}
};*/




class NavicoRadarWrapper : public Navico::Protocol::NRP::iImageClientStateObserver, public Navico::Protocol::NRP::iImageClientSpokeObserver, public Navico::Protocol::iRadarListObserver, public Navico::Protocol::iUnlockStateObserver, public Navico::Protocol::iUnlockKeySupplier, public Navico::Protocol::NRP::iTargetTrackingClientObserver, public Navico::Protocol::NRP::iTargetTrackingClientStateObserver
{

public:
	NavicoRadarWrapper(int AntennaHeightMiliMeter, int RangeMeter, std::string RadarSerial,  std::string UnlockKeyStr) throw(std::exception);
	~NavicoRadarWrapper(){ /*delCallback();*/ };

	void	start(); //start Radar 
	void	stop();	 //Stop Radar and delete everything

	bool AcquireTargets(int TargetId, int range, int bearing, int bearingtype); //Acquiere targetTrack
	bool SetBoatSpeed(int speedType, double speed, int headingType, double heading); //if Moving Provide Own Movement
	bool cancelAll(); //cancel All Targets

	/*void delCallback() { delete JavaCallback; JavaCallback = 0; }
	void setCallback(RadarCallback *cb) { delCallback(); JavaCallback = cb; }*/
	virtual void onSpokeUpdate(void *buffer, long size);
	virtual void onCat240SpokeUpdate(void *buffer, long size);
	virtual void onTargetUpdate(void *buffer, long size);
	virtual void onTargetUpdateTTM(std::string TTMmessage); //Testing
	virtual void onPictureUpdate(void *buffer, long size);

	void setCat240Out();
	void delCat240Out();
	bool setAntennaHeight(int HeightInMm);
	int getAntennaHeight();
	bool setRadarRange(int RangeInM);
	int getRadarRange();
	bool setDangerDistance(int DistanceInM);
	int getDangerDistance();
	bool setDangerTime(int TimeInS);
	int getDangerTime();
private:
	
	//Spoke Callback
	void UpdateSpoke( const Navico::Protocol::NRP::Spoke::t9174Spoke *pSpoke );

    // iImageClientStateObserver callbacks
    void UpdateMode( const Navico::Protocol::NRP::tMode* pMode );
    void UpdateSetup( const Navico::Protocol::NRP::tSetup* pSetup );
    void UpdateSetupExtended( const Navico::Protocol::NRP::tSetupExtended* pSetupExtended );
    void UpdateProperties( const Navico::Protocol::NRP::tProperties* pProperties );
    void UpdateConfiguration( const Navico::Protocol::NRP::tConfiguration* pConfiguration );
    void UpdateGuardZoneAlarm( const Navico::Protocol::NRP::tGuardZoneAlarm* pAlarm );
    void UpdateRadarError( const Navico::Protocol::NRP::tRadarError* pAlarm );
    void UpdateAdvancedState( const Navico::Protocol::NRP::tAdvancedSTCState* pState );
	// iTargetTrackingClientObserver callbacks
	void UpdateTarget( const Navico::Protocol::NRP::tTrackedTarget* pTarget );

    // iTargetTrackingClientStateObserver callbacks
    void UpdateAlarmSetup( const Navico::Protocol::NRP::tTargetTrackingAlarmSetup* pAlarmSetup );
    void UpdateProperties( const Navico::Protocol::NRP::tTargetTrackingProperties* pProperties );
	
	void UpdateRadarList( const char* pSerialNumber, iRadarListObserver::eAction action );
    int GetUnlockKey( const char* pSerialNumber, const uint8_t* pLockID, unsigned lockIDSize, uint8_t* pUnlockKey, unsigned maxUnlockKeySize );
    void UpdateUnlockState( const char* pSerialNumber, int lockState );
	
	static inline const std::string computeCheckSum(const std::string& sentence)
{
#define CARRIAGE_RETURN 0x0D
#define LINE_FEED       0x0A
	  unsigned char checksum_value = 0;

	   int string_length = sentence.length();
	   int index = 1; // Skip over the $ at the begining of the sentence

	   while( index < string_length    &&
			  sentence[ index ] != '*' &&
			  sentence[ index ] != CARRIAGE_RETURN &&
			  sentence[ index ] != LINE_FEED )
	   {
		  checksum_value ^= sentence[ index ];
		  index++;
	   }

	   char buffer[20];
	   sprintf(buffer, "%02X\r\n", checksum_value);
  return buffer;
}

	int initState;
	int myRadarLockState;
	char radarList[8][MAX_SERIALNUMBER_SIZE];

		//!@{
	//! Data items for this data record
	uint16_t		m_dataSourceIdentifier;		//!< Data source identifier
	MessageType		m_messageType;				//!< Message Type (MT_VideoSummary or MT_VideoData)
	uint32_t		m_videoRecordHeader;		//!< Message Sequence Identifier (only present if message type is MT_VideoData)
	std::string		m_videoSummary;				//!< Video Summary (only present if the message type is MT_VideoSummary)
	uint16_t		m_startAzimuth;				//!< 16-bit az number multiply by 360/2^16 to get degrees. Part of Video Header nano/femto (only present if the message type is MT_VideoData)
	uint16_t		m_endAzimuth;				//!< 16-bit az number multiply by 360/2^16 to get degrees. Part of Video Header nano/femto (only present if the message type is MT_VideoData)
	uint32_t		m_startRange;				//!< Number of cells. Part of Video Header nano/femto (only present if the message type is MT_VideoData)
	double			m_cellDuration;				//!< Converted to seconds. Part of Video Header nano/femto (only present if the message type is MT_VideoData)
	bool			m_videoCompressionApplied;	//!< Flag indicates if video compression has been applied to the data
	uint16_t		m_videoCellsResolution;		//!< Bit field to specify the resolution of the cell data. (only present if the message type is MT_VideoData)
	uint32_t		m_videoCellsCounter;		//!< Number of valid 'cells' in this video block. (only present if the message type is MT_VideoData)
	uint16_t		m_videoOctetsCounter;		//!< Number of valid video octets. (only present if the message type is MT_VideoData)
	uint32_t		m_videoBlockCount;			//!< Number of bytes contained in the m_videoBlock. (only present if the message type is MT_VideoData)
	uint8_t*		m_videoBlock;				//!< The actual video block. (only present if the message type is MT_VideoData)
	uint32_t		m_timeOfDay;				//!< Number of 1/128s that have elapsed since midnight.
	//!@}

	Navico::Protocol::NRP::Spoke::t9174Spoke m_spoke;
	Navico::Protocol::NRP::tMode m_pMode;
	Navico::Protocol::NRP::tSetup m_pSetup;
	Navico::Protocol::NRP::tSetupExtended m_pSetupExtended;
	Navico::Protocol::NRP::tProperties m_pProperties;
	Navico::Protocol::NRP::tConfiguration m_pConfiguration;
	Navico::Protocol::NRP::tGuardZoneAlarm m_pAlarmGZA;
	Navico::Protocol::NRP::tRadarError m_pAlarmRE;
	Navico::Protocol::NRP::tAdvancedSTCState m_pState;
	Navico::Protocol::NRP::tTargetTrackingAlarmSetup m_pAlarmSetup;
	Navico::Protocol::NRP::tTargetTrackingProperties m_pTrackProperties;
	//RadarCallback* JavaCallback;
	

	bool cat240out;
	double mRangeMeter;
	int mAntennaHeight;
	int mRadarState;
	int mDangerDistance;
	int mDangerTime;

	Navico::Protocol::NRP::tImageClient*       m_pImageClient;
	Navico::Protocol::tMultiRadarClient*       m_pMultiRadar;
	// Target Tracking protocol manager
	Navico::Protocol::NRP::tTargetTrackingClient* m_pTargetTrackingClient;
	Navico::Image::tPPIController* m_pPPIController;
	uint8_t *m_pPPIImage;
	Navico::tRadarColourLookUpTableNavico gNavicoLUT;

	
};

#endif