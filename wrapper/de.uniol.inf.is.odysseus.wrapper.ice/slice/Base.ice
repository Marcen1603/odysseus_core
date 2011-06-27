 
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
#ifndef Base_ICE
#define Base_ICE

#include "Runtime.ice"

module scm
{
	module eci { //cause ice is a keyword		
		
	
		module base
		{
			
			/**
			*	Enumeration: DistanceUnit
			*	description: 
			*/
			enum DistanceUnit
			{
			
				/**	Literal
				*	METER
				*	description: 
				*/
				METER
			, 
				/**	Literal
				*	CENTIMETER
				*	description: 
				*/
				CENTIMETER
			, 
				/**	Literal
				*	MILLIMETER
				*	description: 
				*/
				MILLIMETER
			, 
				/**	Literal
				*	NANOMETER
				*	description: 
				*/
				NANOMETER
			, 
				/**	Literal
				*	DEZIMETER
				*	description: 
				*/
				DEZIMETER
			, 
				/**	Literal
				*	KILOMETER
				*	description: 
				*/
				KILOMETER
			
			};

			//Module Base forward declarations
			
			class File;
			sequence<File> FileSeq;

			class Matrix4x4;
			sequence<Matrix4x4> Matrix4x4Seq;

			class Matrix3x3;
			sequence<Matrix3x3> Matrix3x3Seq;

			class Vector3;
			sequence<Vector3> Vector3Seq;

			class Vector2;
			sequence<Vector2> Vector2Seq;

			class Polygon3;
			sequence<Polygon3> Polygon3Seq;

			class Polygon2;
			sequence<Polygon2> Polygon2Seq;

			class Pose;
			sequence<Pose> PoseSeq;

			class Distance;
			sequence<Distance> DistanceSeq;

			interface Sensor;
			sequence<Sensor*> SensorSeq;

			//end of forward declarations
			 
			
			/** Datatype
			*	name : File
			*	description: 
			*/
			class File
			{
				/** Attribute : path
				*	description : 
				*/
				string path;		
				/** Attribute : length
				*	description : 
				*/
				int length;		
						
				/** Operation: getData
				*	description: 
				*/	
				byteSeq getData() ;
				/** Operation: existLocal
				*	description: 
				*/	
				bool existLocal() ;
				
			};
			class FileMessage extends rt::IMessage { File value; };
			interface FileInputPort extends rt::InputPort {
				FileMessage getMessage();
				File getValue();
			};
			interface FileOutputPort extends rt::OutputPort {
				void send(File msg);
			};
	

			/** Datatype
			*	name : Matrix4x4
			*	description: 
			*/
			class Matrix4x4
			{
				/** Attribute : values
				*	description : 
				*/
				doubleSeq values;		
						
				
			};
			class Matrix4x4Message extends rt::IMessage { Matrix4x4 value; };
			interface Matrix4x4InputPort extends rt::InputPort {
				Matrix4x4Message getMessage();
				Matrix4x4 getValue();
			};
			interface Matrix4x4OutputPort extends rt::OutputPort {
				void send(Matrix4x4 msg);
			};
	

			/** Datatype
			*	name : Matrix3x3
			*	description: 
			*/
			class Matrix3x3
			{
				/** Attribute : values
				*	description : 
				*/
				doubleSeq values;		
						
				
			};
			class Matrix3x3Message extends rt::IMessage { Matrix3x3 value; };
			interface Matrix3x3InputPort extends rt::InputPort {
				Matrix3x3Message getMessage();
				Matrix3x3 getValue();
			};
			interface Matrix3x3OutputPort extends rt::OutputPort {
				void send(Matrix3x3 msg);
			};
	

			/** Datatype
			*	name : Vector3
			*	description: 
			*/
			class Vector3
			{
				/** Attribute : x
				*	description : 
				*/
				double x;		
				/** Attribute : y
				*	description : 
				*/
				double y;		
				/** Attribute : z
				*	description : 
				*/
				double z;		
						
				
			};
			class Vector3Message extends rt::IMessage { Vector3 value; };
			interface Vector3InputPort extends rt::InputPort {
				Vector3Message getMessage();
				Vector3 getValue();
			};
			interface Vector3OutputPort extends rt::OutputPort {
				void send(Vector3 msg);
			};
	

			/** Datatype
			*	name : Vector2
			*	description: 
			*/
			class Vector2
			{
				/** Attribute : x
				*	description : 
				*/
				double x;		
				/** Attribute : y
				*	description : 
				*/
				double y;		
						
				
			};
			class Vector2Message extends rt::IMessage { Vector2 value; };
			interface Vector2InputPort extends rt::InputPort {
				Vector2Message getMessage();
				Vector2 getValue();
			};
			interface Vector2OutputPort extends rt::OutputPort {
				void send(Vector2 msg);
			};
	

			/** Datatype
			*	name : Polygon3
			*	description: 
			*/
			class Polygon3
			{
				/** Attribute : points
				*	description : 
				*/
				base::Vector3Seq points;		
				/** Attribute : isClosed
				*	description : 
				*/
				bool isClosed;		
						
				
			};
			class Polygon3Message extends rt::IMessage { Polygon3 value; };
			interface Polygon3InputPort extends rt::InputPort {
				Polygon3Message getMessage();
				Polygon3 getValue();
			};
			interface Polygon3OutputPort extends rt::OutputPort {
				void send(Polygon3 msg);
			};
	

			/** Datatype
			*	name : Polygon2
			*	description: 
			*/
			class Polygon2
			{
				/** Attribute : points
				*	description : 
				*/
				base::Vector2Seq points;		
				/** Attribute : isClosed
				*	description : 
				*/
				bool isClosed;		
						
				
			};
			class Polygon2Message extends rt::IMessage { Polygon2 value; };
			interface Polygon2InputPort extends rt::InputPort {
				Polygon2Message getMessage();
				Polygon2 getValue();
			};
			interface Polygon2OutputPort extends rt::OutputPort {
				void send(Polygon2 msg);
			};
	

			/** Datatype
			*	name : Pose
			*	description: 
			*/
			class Pose
			{
				/** Attribute : position
				*	description : 
				*/
				base::Vector3 position;		
				/** Attribute : rotationEuler
				*	description : 
				*/
				base::Vector3 rotationEuler;		
						
				
			};
			class PoseMessage extends rt::IMessage { Pose value; };
			interface PoseInputPort extends rt::InputPort {
				PoseMessage getMessage();
				Pose getValue();
			};
			interface PoseOutputPort extends rt::OutputPort {
				void send(Pose msg);
			};
	

			/** Datatype
			*	name : Distance
			*	description: 
			*/
			class Distance
			{
				/** Attribute : value
				*	description : 
				*/
				double value;		
				/** Attribute : unit
				*	description : 
				*/
				base::DistanceUnit unit;		
						
				
			};
			class DistanceMessage extends rt::IMessage { Distance value; };
			interface DistanceInputPort extends rt::InputPort {
				DistanceMessage getMessage();
				Distance getValue();
			};
			interface DistanceOutputPort extends rt::OutputPort {
				void send(Distance msg);
			};
	

			
			
			/** Component
			*	name : Sensor
			*	description: 
			*/
			/*abstract*/ interface Sensor  extends rt::Component 
			{
				/** Attribute : frequency
				*	description : 
				*/
				int getFrequency();
				bool setFrequency(int newValue);
			};

			
			interface BaseFacade
			{
				Object* getObjectByUID(string uid);
				void releaseObjectByUID(string uid);
			};
		};
	};
};
#endif //Base_ICE

