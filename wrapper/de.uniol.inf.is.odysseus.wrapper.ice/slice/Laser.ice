 
/**
 *	! 		This is an automatic generated file 		!
 *	
 *	@author scm.comp.generator by Soeren.Schweigert[at]offis.de
 *	
 *	@co-author scm.comp.generator by Soeren.Schweigert[at]offis.de
 *	if you need to modify this file insert an comment in the FIRST line
 * 	containing the words:
 *				 "do not overwrite" 
 *	each file that does not have this comment in the first line will be
 *	overwriten with the next generation step
 */
#ifndef Laser_ICE
#define Laser_ICE

#include "Runtime.ice"

#include "Base.ice"

module scm
{
	module eci { //cause ice is a keyword		
		
	
		module laser
		{
			
			//Module Laser forward declarations
			
			class RangeValue;
			sequence<RangeValue> RangeValueSeq;

			class DebugRangeValue;
			sequence<DebugRangeValue> DebugRangeValueSeq;

			class LaserMeasurement;
			sequence<LaserMeasurement> LaserMeasurementSeq;

			interface Laserscanner;
			sequence<Laserscanner*> LaserscannerSeq;

			//end of forward declarations
			 
			
			/** Datatype
			*	name : RangeValue
			*	description: 
			*/
			class RangeValue
			{
				/** Attribute : distance1
				*	description : 
				*/
				double distance1;		
				/** Attribute : distance2
				*	description : 
				*/
				double distance2;		
				/** Attribute : remission1
				*	description : 
				*/
				double remission1;		
				/** Attribute : remission2
				*	description : 
				*/
				double remission2;		
				/** Attribute : index
				*	description : 
				*/
				int index;		
				/** Attribute : angle
				*	description : 
				*/
				double angle;		
						
				
			};
			class RangeValueMessage extends rt::IMessage { RangeValue value; };
			interface RangeValueInputPort extends rt::InputPort {
				RangeValueMessage getMessage();
				RangeValue getValue();
			};
			interface RangeValueOutputPort extends rt::OutputPort {
				void send(RangeValue msg);
			};
	

			/** Datatype
			*	name : DebugRangeValue
			*	description: 
			*/
			class DebugRangeValue extends laser::RangeValue
			{
				/** Attribute : objectID
				*	description : References UID of the Object if run from Simulation or NULL
				*/
				string objectID;		
						
				
			};
			class DebugRangeValueMessage extends rt::IMessage { DebugRangeValue value; };
			interface DebugRangeValueInputPort extends rt::InputPort {
				DebugRangeValueMessage getMessage();
				DebugRangeValue getValue();
			};
			interface DebugRangeValueOutputPort extends rt::OutputPort {
				void send(DebugRangeValue msg);
			};
	

			/** Datatype
			*	name : LaserMeasurement
			*	description: 
			*/
			class LaserMeasurement
			{
				/** Attribute : serial
				*	description : 
				*/
				string serial;		
				/** Attribute : deviceOK
				*	description : 
				*/
				bool deviceOK;		
				/** Attribute : dirty
				*	description : 
				*/
				bool dirty;		
				/** Attribute : scanCount
				*	description : 
				*/
				int scanCount;		
				/** Attribute : frequency
				*	description : 
				*/
				int frequency;		
				/** Attribute : startAngle
				*	description : 
				*/
				double startAngle;		
				/** Attribute : stepAngle
				*	description : 
				*/
				double stepAngle;		
				/** Attribute : data
				*	description : 
				*/
				laser::DebugRangeValueSeq data;		
						
				
			};
			class LaserMeasurementMessage extends rt::IMessage { LaserMeasurement value; };
			interface LaserMeasurementInputPort extends rt::InputPort {
				LaserMeasurementMessage getMessage();
				LaserMeasurement getValue();
			};
			interface LaserMeasurementOutputPort extends rt::OutputPort {
				void send(LaserMeasurement msg);
			};
	

			
			
			/** Component
			*	name : Laserscanner
			*	description: 
			*/
			interface Laserscanner  extends base::Sensor
			{
				/** Attribute : minRange
				*	description : 
				*/
				base::Distance getMinRange();
				/** Attribute : maxRange
				*	description : 
				*/
				base::Distance getMaxRange();
				/** Attribute : scanAngle
				*	description : 
				*/
				double getScanAngle();
				bool setScanAngle(double newValue);
				/** Attribute : angleResolution
				*	description : 
				*/
				double getAngleResolution();
				bool setAngleResolution(double newValue);
				/** OutputPort: scan
				*	description: 
				*/
				laser::LaserMeasurementOutputPort* getScanOutputPort();
			};

			
			interface LaserFacade
			{
				Laserscanner* createNewLaserscanner();
				Object* getObjectByUID(string uid);
				void releaseObjectByUID(string uid);
			};
		};
	};
};
#endif //Laser_ICE

