#ifndef MASTRADARTYPES_H_
#define MASTRADARTYPES_H_

#include <NavTypes.h>

		//! Enumeration of the support asterix category messages
	enum AsterixCategory
	{
		AC_CatUnknown  = 999,		//!< Default value speicifying an invalid category number
		AC_Cat010Track = 10,		//!< Category for track updates (Transmission of Monosensor Surface Movement Data)
		AC_Cat048Track = 48,		//!< Category for track updates (Transmission of Monoradar Targets Reports)
		AC_Cat240Video = 240,		//!< Category for azimuths of video data (Radar Video Transmission)
		AC_Cat253Ctrl  = 253		//!< Category for radar status and control (Remote Monitoring and Control)
	};

		//! The User Application Protocol (UAP) for this data record.
		//! This provides the mapping between a data item and the
		//! Field Reference Number (FRN). The FRN is essentially the position
		//! in the data that the field will appear (if the item is present).
		//! The FPSEC in the AsterixDataRecord base class can be checked using the
		//! FRN number (as a bit number) for each field to determine if that field is
		//! provided in the data.
			enum UAP
		{
			// Data Item								// FRN
			UAP_DataSourceIdentifier					= 1,
			UAP_MessageType								= 2,
			UAP_VideoRecordHeader						= 3,
			UAP_VideoSummary							= 4,
			UAP_VideoHeaderNano							= 5,
			UAP_VideoHeaderFemto						= 6,
			UAP_VideoCellsResolutionAndCompressionInd	= 7,
			UAP_VideoOctetsAndCellsCounter				= 8,
			UAP_VideoBlockLowDataVolume					= 9,
			UAP_VideoBlockMediumDataVolume				= 10,
			UAP_VideoBlockHighDataVolume				= 11,
			UAP_TimeOfDay								= 12,
			UAP_ReservedExpansionField					= 13,
			UAP_SpecialPurposeField						= 14
		};

		//! Enumeration describing the message types
		//! that a cat 240 data record can contain
		enum MessageType
		{
			MT_VideoSummary = 1,
			MT_VideoData	= 2
		};

		//! Enumeration of the bit fileds in the
		//! m_videoCellsResolution data item
		enum VideoCellsResolution
		{
			VR_NotSet				= 0,	//!< default indicating no bits are set
			VR_MonobitResolution	= 1,	//!< specifies 1-bit video cells
			VR_LowResolution		= 2,	//!< specifies 2-bit video cells
			VR_MediumResolution		= 3,	//!< specifies 4-bit video cells
			VR_HighResolution		= 4,	//!< specifies 8-bit video cells
			VR_VeryHighResolution	= 5,	//!< specifies 16-bit video cells
			VR_UltraHighResolution	= 6,	//!< specifies 32-bit video cells
		};
#pragma pack(push) 
#pragma pack(1)
struct dataSourceIdent{
	uint8_t systemAreaCode;
	uint8_t systemIdentificationCode;
};
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct videoRecordHeader{
	uint8_t systemAreaCode;
	uint8_t systemIdentificationCode;
};
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct videoRecordHeaderNano{
	uint16_t startAzimuth;
	uint16_t endAzimuth;
	uint32_t startRange;
	uint32_t cellDuration;
};
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct videoCellResAndDataComp{
	uint8_t dataCompressIndicator; //128 Compression applied or  0 No Compression Applied
	uint8_t resolution; //see Enum VideoCellsresoltuion
};
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct videoOctetsAndCellCounters{
	uint16_t numberOfValidOctets;
	uint8_t	numberOfValidCells1;
	uint8_t numberOfValidCells2;
	uint8_t numberOfValidCells3;
};
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct cat240Msg{
	uint8_t asterixCategory; // Category 240;
	uint16_t dataLength; // total lenghts of octets in Data Block including CAT and LEN Fields
	uint16_t FSPEC; //Field Specification (UAP with FRN)
};
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct cat240VideoHeader{
	uint8_t videoMsgType; // Cat 240 Radar Video
	dataSourceIdent dataSourceIdentifier;
	uint32_t videoRecordHeader;
	videoRecordHeaderNano videoHeaderNano;
	videoCellResAndDataComp cellResAndDataCompression;
	videoOctetsAndCellCounters OctetAndCellNumbers;
};
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct packedcat240Spoke {
	cat240Msg catMsgHead;
	cat240VideoHeader cat240Header;
	uint8_t repetitionFactor;
	uint8_t	videoBlock[SAMPLES_PER_SPOKE/2]; //only for this Radar 512 Byte ATTENTION
} ;
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct packedtTargetInfo             ///  Structure for conveying target-tracking target information (eg. course and location)
{                              ///
    uint32_t  distance_m;      ///< Distance to target in metres
    uint32_t  bearing_ddeg;    ///< Target bearing in 10ths of a degree (deci-degrees)
    uint32_t  course_ddeg;     ///< Target course in 10ths of a degree (deci-degrees)
    uint32_t  speed_dmps;      ///< Target speed in 10ths of a metre per second (deci-metres/second)
};
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct packedTrackedTarget                 ///  Structure for conveying all target-tracking target information
{                                     ///
    uint8_t       targetValid;        ///< 0x00 invalid, 0x01 valid
    uint8_t       trueTarget;         ///< 0x00 simulated target, 0x01 true/real target
    uint32_t	  targetType;         ///< One of eTargetType enum values
    uint32_t      targetID;           ///< Client assigned target-ID (provided by user in manual-acquire0
    int32_t       serverTargetID;     ///< Server assigned target-ID (negative when invalid acquire - eFailAcquire* states)
    uint32_t	  targetState;        ///< One of eTargetState enum values
    uint32_t	  autoAcquired;       ///< One of eAcquireType enum values (manually or automatically acquired)

	packedtTargetInfo   infoRelative;       ///< Target details expressed relative to the boats speed and direction
    packedtTargetInfo   infoAbsolute;       ///< Target details expressed independant of the boat (relative to true north)
    uint8_t       infoAbsoluteValid;  ///< Whether 'infoAbsolute' is valid (0 absolute info invalid, 1 valid)

    uint32_t      CPA_m;              ///< Closest point of approach expressed in meters
    uint32_t      TCPA_sec;           ///< Time to the closest point of approach expressed in seconds
    uint8_t       towardsCPA;         ///< CPA direction (0 target moving away from CPA, 1 towards CPA)
};
#pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
 struct packedt9174SpokeHeader
    {
        uint32_t spokeLength_bytes : 12;   //!< length of the whole spoke in bytes
        uint32_t : 4;                      //!< reserved
        uint32_t sequenceNumber : 12;      //!< spoke sequence number
        uint32_t : 4;                      //!< reserved

        uint32_t nOfSamples : 12;          //!< number of samples present in the spoke
        uint32_t bitsPerSample: 4;         //!< number of bits per sample, normally is set to 4
        uint32_t rangeCellSize_mm : 16;    //!< Distance represented by each range-cell. sample size is computed as: rangeCellSize_mm * 2*rangeCellsDiv2 / nOfSamples;

        uint32_t spokeAzimuth: 13;         //!< Azimuth of the spoke in the range 0-4095. Values greater than 4095 must be mapped to 4095. This represents a full circle 0-360 degrees
        uint32_t : 1;                      //!< reserved
        uint32_t bearingZeroError: 1;      //!< Set if there is malfunctioning bearing zero
        uint32_t : 1;                      //!< reserved
        uint32_t spokeCompass: 14;         //!< Heading of the boat when this spoke was sampled. It is represented in the 0-4095 range for 0-360degrees of heading
        uint32_t trueNorth : 1;            //!< The connected heading sensor is reporting true north (1) or magnetic north (0)
        uint32_t compassInvalid : 1;       //!< If this bit is 1, the compass information are invalid

        uint32_t rangeCellsDiv2 : 16;      //!< Number of range-cells represented by the data in this spoke, divided by 2
        uint32_t : 16;                     //!< reserved

        uint32_t : 16;                     //!< reserved
        uint32_t : 16;                     //!< reserved
        uint32_t : 16;                     //!< reserved
        uint32_t : 16;                     //!< reserved
    } ;
 #pragma pack(pop)

#pragma pack(push) 
#pragma pack(1)
struct packedT9174Spoke                
{                                     

        packedt9174SpokeHeader header;
        uint8_t data[ SAMPLES_PER_SPOKE/2 ];
   
};
#pragma pack(pop)







#endif