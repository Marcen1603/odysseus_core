
/* AUTOMATICALLY GENERATED HEADER, DO NOT EDIT! */

/**
 * @file DStreamCars0Views.h
 *
 * Copyright (C) 2007 DLR FS. All rights reserved.
 */

#ifndef __DStreamCars0_VIEWS_H__
#define __DStreamCars0_VIEWS_H__

#include "DominionFUST/include/Macros.h"
#include "DominionFUST/include/Types.h"

/* These structures need to be packed at this point ... */
#pragma pack(1)

/* Type definitions. */
typedef struct
{
    unsigned int Seconds;
    unsigned int Microseconds;
} TViewsTimestamp;

/* Regular Macros. */

#define KIN_X     0x0
#define KIN_Y     0x1
#define KIN_Z     0x2
#define KIN_PHI   0x3
#define KIN_THETA 0x4
#define KIN_PSI   0x5

/* Macros from the hades, shortcuts. */


/* BEGIN: Input shortcuts. */
		
/* No macro shortcut defined for domain 'Environment'. */
		
	/* No macro shortcut defined for structure 'EnvironmentSensorData'. */
			
	/* No macro shortcut defined for structure 'EnvironmentSensorDataDetectedVehicles'. */
			
		/* No macro shortcut defined for entry 'Environment@SensorData.DetectedVehicles.Type'. */
      
		/* No macro shortcut defined for entry 'Environment@SensorData.DetectedVehicles.CarTrafficID'. */
      
		/* No macro shortcut defined for entry 'Environment@SensorData.DetectedVehicles.LaneID'. */
      
		/* No macro shortcut defined for entry 'Environment@SensorData.DetectedVehicles.PositionUTM'. */
      
		/* No macro shortcut defined for entry 'Environment@SensorData.DetectedVehicles.Velocity'. */
      
		/* No macro shortcut defined for entry 'Environment@SensorData.DetectedVehicles.Length'. */
      
		/* No macro shortcut defined for entry 'Environment@SensorData.DetectedVehicles.Width'. */
      
/* END: Input shortcuts. */

	

/* BEGIN: Output shortcuts. */
		
/* END: Output shortcuts. */

  

/* BEGIN: Parameter shortcuts. */
		
/* END: Parameter shortcuts. */

	

/* Definition of data structures. */

/* BEGIN: Input data user views. */

	/*!
	 * @struct TInputEnvironmentSensorDataDetectedVehicles
	 *
	 * This is a Dominion Data Structure object.
	 *
	 * Vehicle Instance
	 */
	typedef struct
	{
		
		/*!
		 * Type
		 *
		 * Size and look of the vehicle.
		 *
		 * Measured in [ - ].
		 * Plausibility range is ( 0, 12 ).
		 */
		SInt32 Type;
			
		/*!
		 * CarTrafficID
		 *
		 * traffic id for car#
		 *
		 * Measured in [ - ].
		 * Plausibility range is ( 0, 60 ).
		 */
		SInt32 CarTrafficID;
			
		/*!
		 * LaneID
		 *
		 * id of the current driven lane
		 *
		 * Measured in [ - ].
		 * Plausibility range is ( -1, 9999 ).
		 */
		SInt32 LaneID;
			
		/*!
		 * PositionUTM
		 *
		 * Actual position of the vehicle.(x-Pos, y-Pos, z Pos, roll, pitch, heading)
		 *
		 * Measured in [ m ].
		 * Plausibility range is ( 0, 10e6 ).
		 */
		Float64 PositionUTM[6];
			
		/*!
		 * Velocity
		 *
		 * Actual speed of the vehicle.
		 *
		 * Measured in [ m/s ].
		 * Plausibility range is ( -50.0, 250.0 ).
		 */
		Float32 Velocity;
			
		/*!
		 * Length
		 *
		 * Length of the vehicle.
		 *
		 * Measured in [ m ].
		 * Plausibility range is ( -1.0, 50.0 ).
		 */
		Float32 Length;
			
		/*!
		 * Width
		 *
		 * Width of the vehicle.
		 *
		 * Measured in [ m/s ].
		 * Plausibility range is ( -1.0, 50.0 ).
		 */
		Float32 Width;
			
	} TInputEnvironmentSensorDataDetectedVehicles;
	
	/*!
	 * @struct TInputEnvironmentSensorData
	 *
	 * This is a Dominion Data Structure object.
	 *
	 * Data produced by various sensors
	 */
	typedef struct
	{
		TInputEnvironmentSensorDataDetectedVehicles DetectedVehicles[50];
						
	} TInputEnvironmentSensorData;
	
/*!
 * @struct Environment
 *
 * Dominion Data Domain object.
 *
 * 
 */
typedef struct
{
	TInputEnvironmentSensorData SensorData;
	
} TInputEnvironment;
	
typedef struct
{
	TInputEnvironment Environment;
	
} TInput;
      
/* END: Input data user views. */

  
/* BEGIN: Output data user views. */

typedef struct
{
    int null;

} TOutput;
      
/* END: Output data user views. */

  
/* BEGIN: Input data user views. */

typedef struct
{
    int null;

} TParameter;
      
/* END: Parameter data user views. */

  

/* Definition of meta data structures. */

/* BEGIN: InputMeta meta user views. */

	/*!
	 * @struct TInputMetaEnvironmentSensorDataDetectedVehicles
	 *
	 * This is a Dominion Meta Structure object.
	 */
	typedef struct
	{
		
		/*!
		* @struct TInputMetaEnvironmentSensorDataDetectedVehiclesType
		*
		* This is a Dominion Meta Entry object.
		*/
		struct
		{
			/*!
			 * Multiplicity of 'Type'.
			 */
			unsigned int _multiplicity;
		} Type;
	
		/*!
		* @struct TInputMetaEnvironmentSensorDataDetectedVehiclesCarTrafficID
		*
		* This is a Dominion Meta Entry object.
		*/
		struct
		{
			/*!
			 * Multiplicity of 'CarTrafficID'.
			 */
			unsigned int _multiplicity;
		} CarTrafficID;
	
		/*!
		* @struct TInputMetaEnvironmentSensorDataDetectedVehiclesLaneID
		*
		* This is a Dominion Meta Entry object.
		*/
		struct
		{
			/*!
			 * Multiplicity of 'LaneID'.
			 */
			unsigned int _multiplicity;
		} LaneID;
	
		/*!
		* @struct TInputMetaEnvironmentSensorDataDetectedVehiclesPositionUTM
		*
		* This is a Dominion Meta Entry object.
		*/
		struct
		{
			/*!
			 * Multiplicity of 'PositionUTM'.
			 */
			unsigned int _multiplicity;
		} PositionUTM;
	
		/*!
		* @struct TInputMetaEnvironmentSensorDataDetectedVehiclesVelocity
		*
		* This is a Dominion Meta Entry object.
		*/
		struct
		{
			/*!
			 * Multiplicity of 'Velocity'.
			 */
			unsigned int _multiplicity;
		} Velocity;
	
		/*!
		* @struct TInputMetaEnvironmentSensorDataDetectedVehiclesLength
		*
		* This is a Dominion Meta Entry object.
		*/
		struct
		{
			/*!
			 * Multiplicity of 'Length'.
			 */
			unsigned int _multiplicity;
		} Length;
	
		/*!
		* @struct TInputMetaEnvironmentSensorDataDetectedVehiclesWidth
		*
		* This is a Dominion Meta Entry object.
		*/
		struct
		{
			/*!
			 * Multiplicity of 'Width'.
			 */
			unsigned int _multiplicity;
		} Width;
	
		/*!
		 * Multiplicity of 'DetectedVehicles'.
		 */
		unsigned int _multiplicity;
	} TInputMetaEnvironmentSensorDataDetectedVehicles;
	
	/*!
	 * @struct TInputMetaEnvironmentSensorData
	 *
	 * This is a Dominion Meta Structure object.
	 */
	typedef struct
	{
		TInputMetaEnvironmentSensorDataDetectedVehicles DetectedVehicles;
				
		/*!
		 * Multiplicity of 'SensorData'.
		 */
		unsigned int _multiplicity;
	} TInputMetaEnvironmentSensorData;
	
/*!
 * @struct Environment
 *
 * Dominion Meta Domain object.
 */
typedef struct
{
	TInputMetaEnvironmentSensorData SensorData;
	
	/*!
	 * Multiplicity of 'Environment'.
	 */
	unsigned int _multiplicity;
} TInputMetaEnvironment;
	
typedef struct
{
TInputMetaEnvironment Environment;

} TInputMeta;
      
/* END: InputMeta data user views. */

  
/* BEGIN: OutputMeta data user views. */

typedef struct
{
    int null;
} TOutputMeta;
      
/* END: OutputMeta data user views. */

  
/* BEGIN: ParameterMeta data user views. */

typedef struct
{
    int null;
} TParameterMeta;
      
/* END: ParameterMeta data user views. */

  

/* Stop the packing madness ... */
#pragma pack()

#endif /* __StreamCars0_VIEWS_H__ */

/* AUTOMATICALLY GENERATED HEADER, DO NOT EDIT! */
