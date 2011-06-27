 
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
#ifndef Vision_ICE
#define Vision_ICE

#include "Runtime.ice"

		#include "Base.ice"

module scm
{
	module eci { //cause ice is a keyword		
		
	
		module vision
		{
			
			/**
			*	Enumeration: SIUnit
			*	description: 
			*/
			enum SIUnit
			{
			
				/**	Literal
				*	Meter
				*	description: 
				*/
				Meter
			, 
				/**	Literal
				*	Centimeter
				*	description: 
				*/
				Centimeter
			, 
				/**	Literal
				*	Millimeter
				*	description: 
				*/
				Millimeter
			
			};

			/**
			*	Enumeration: ThresholdMode
			*	description: 
			*/
			enum ThresholdMode
			{
			
				/**	Literal
				*	BINARY
				*	description: 
				*/
				BINARY
			, 
				/**	Literal
				*	INV_BINARY
				*	description: 
				*/
				INVBINARY
			
			};

			//Module Vision forward declarations
			
			interface InputHandler;
			sequence<InputHandler*> InputHandlerSeq;

			class Matrix;
			sequence<Matrix> MatrixSeq;

			class Image;
			sequence<Image> ImageSeq;

			class GrayImage;
			sequence<GrayImage> GrayImageSeq;

			class RGBImage;
			sequence<RGBImage> RGBImageSeq;

			class DistanceImage;
			sequence<DistanceImage> DistanceImageSeq;

			interface Algorithm;
			sequence<Algorithm*> AlgorithmSeq;

			interface Threshold;
			sequence<Threshold*> ThresholdSeq;

			interface GrayThreshold;
			sequence<GrayThreshold*> GrayThresholdSeq;

			interface RGBThreshold;
			sequence<RGBThreshold*> RGBThresholdSeq;

			interface ImageServer;
			sequence<ImageServer*> ImageServerSeq;

			interface RGBImageServer;
			sequence<RGBImageServer*> RGBImageServerSeq;

			interface GrayImageServer;
			sequence<GrayImageServer*> GrayImageServerSeq;

			class Point2i;
			sequence<Point2i> Point2iSeq;

			interface Kinect;
			sequence<Kinect*> KinectSeq;

			//end of forward declarations
			 
			
			/** Datatype
			*	name : Matrix
			*	description: 
			*/
			class Matrix
			{
				/** Attribute : rows
				*	description : 
				*/
				int rows;		
				/** Attribute : cols
				*	description : 
				*/
				int cols;		
				/** Attribute : data
				*	description : 
				*/
				byteSeq data;		
				/** Attribute : step
				*	description : 
				*/
				int step;		
				/** Attribute : flags
				*	description : 
				*/
				int flags;		
						
				/** Operation: depth
				*	description: 
				*/	
				int depth() ;
				/** Operation: channels
				*	description: 
				*/	
				int channels() ;
				
			};
			class MatrixMessage extends rt::IMessage { Matrix value; };
			interface MatrixInputPort extends rt::InputPort {
				MatrixMessage getMessage();
				Matrix getValue();
			};
			interface MatrixOutputPort extends rt::OutputPort {
				void send(Matrix msg);
			};
	

			/** Datatype
			*	name : Image
			*	description: 
			*/
			class Image extends vision::Matrix
			{
						
				
			};
			class ImageMessage extends rt::IMessage { Image value; };
			interface ImageInputPort extends rt::InputPort {
				ImageMessage getMessage();
				Image getValue();
			};
			interface ImageOutputPort extends rt::OutputPort {
				void send(Image msg);
			};
	

			/** Datatype
			*	name : GrayImage
			*	description: 
			*/
			class GrayImage extends vision::Image
			{
						
				
			};
			class GrayImageMessage extends rt::IMessage { GrayImage value; };
			interface GrayImageInputPort extends rt::InputPort {
				GrayImageMessage getMessage();
				GrayImage getValue();
			};
			interface GrayImageOutputPort extends rt::OutputPort {
				void send(GrayImage msg);
			};
	

			/** Datatype
			*	name : RGBImage
			*	description: 
			*/
			class RGBImage extends vision::Image
			{
						
				
			};
			class RGBImageMessage extends rt::IMessage { RGBImage value; };
			interface RGBImageInputPort extends rt::InputPort {
				RGBImageMessage getMessage();
				RGBImage getValue();
			};
			interface RGBImageOutputPort extends rt::OutputPort {
				void send(RGBImage msg);
			};
	

			/** Datatype
			*	name : DistanceImage
			*	description: 
			*/
			class DistanceImage extends vision::GrayImage
			{
				/** Attribute : unit
				*	description : 
				*/
				vision::SIUnit unit;		
						
				
			};
			class DistanceImageMessage extends rt::IMessage { DistanceImage value; };
			interface DistanceImageInputPort extends rt::InputPort {
				DistanceImageMessage getMessage();
				DistanceImage getValue();
			};
			interface DistanceImageOutputPort extends rt::OutputPort {
				void send(DistanceImage msg);
			};
	

			/** Datatype
			*	name : Point2i
			*	description: 
			*/
			class Point2i
			{
				/** Attribute : x
				*	description : 
				*/
				int x;		
				/** Attribute : y
				*	description : 
				*/
				int y;		
						
				
			};
			class Point2iMessage extends rt::IMessage { Point2i value; };
			interface Point2iInputPort extends rt::InputPort {
				Point2iMessage getMessage();
				Point2i getValue();
			};
			interface Point2iOutputPort extends rt::OutputPort {
				void send(Point2i msg);
			};
	

			
			
			/** Component
			*	name : InputHandler
			*	description: 
			*/
			/*abstract*/ interface InputHandler  extends base::Sensor
			{
			};

			/** Component
			*	name : Algorithm
			*	description: 
			*/
			/*abstract*/ interface Algorithm  extends rt::Component 
			{
			};

			/** Component
			*	name : Threshold
			*	description: 
			*/
			/*abstract*/ interface Threshold  extends vision::Algorithm
			{
				/** Attribute : threshold
				*	description : 
				*/
				int getThreshold();
				bool setThreshold(int newValue);
				/** Attribute : mode
				*	description : 
				*/
				vision::ThresholdMode getMode();
				bool setMode(vision::ThresholdMode newValue);
			};

			/** Component
			*	name : GrayThreshold
			*	description: 
			*/
			interface GrayThreshold  extends vision::Threshold
			{
				/** OutputPort: threshImg
				*	description: 
				*/
				vision::GrayImageOutputPort* getThreshImgOutputPort();
				/** InputPort: img
				*	description: 
				*/
				vision::GrayImageInputPort* getImgInputPort();
			};

			/** Component
			*	name : RGBThreshold
			*	description: 
			*/
			interface RGBThreshold  extends vision::Threshold
			{
				/** OutputPort: threshImg
				*	description: 
				*/
				vision::RGBImageOutputPort* getThreshImgOutputPort();
				/** InputPort: img
				*	description: 
				*/
				vision::RGBImageInputPort* getImgInputPort();
			};

			/** Component
			*	name : ImageServer
			*	description: 
			*/
			/*abstract*/ interface ImageServer  extends vision::InputHandler
			{
				/** Attribute : resolution
				*	description : nur ne test beschreibung, die relativ lang ist und verschiedene "Zeichen" enthält \n und das in zwei zeilen.
				*/
				vision::Point2i getResolution();
				bool setResolution(vision::Point2i newValue);
				/** OutputPort: img
				*	description: 
				*/
				vision::ImageOutputPort* getImgOutputPort();
			};

			/** Component
			*	name : RGBImageServer
			*	description: 
			*/
			interface RGBImageServer  extends vision::ImageServer
			{
				/** OutputPort: colorImage
				*	description: 
				*/
				vision::RGBImageOutputPort* getColorImageOutputPort();
			};

			/** Component
			*	name : GrayImageServer
			*	description: 
			*/
			interface GrayImageServer  extends vision::ImageServer
			{
				/** OutputPort: grayImage
				*	description: 
				*/
				vision::GrayImageOutputPort* getGrayImageOutputPort();
			};

			/** Component
			*	name : Kinect
			*	description: 
			*/
			interface Kinect  extends vision::RGBImageServer
			{
				/** Attribute : motorPos
				*	description : 
				*/
				int getMotorPos();
				bool setMotorPos(int newValue);
				/** Attribute : led
				*	description : 
				*/
				int getLed();
				bool setLed(int newValue);
				/** Attribute : regMat
				*	description : 
				*/
				vision::Matrix getRegMat();
				bool setRegMat(vision::Matrix newValue);
				/** OutputPort: regRGBImg
				*	description: 
				*/
				vision::RGBImageOutputPort* getRegRGBImgOutputPort();
				/** OutputPort: depthImg
				*	description: 
				*/
				vision::GrayImageOutputPort* getDepthImgOutputPort();
				/** OutputPort: distImg
				*	description: 
				*/
				vision::DistanceImageOutputPort* getDistImgOutputPort();
			};

			
			interface VisionFacade
			{
				GrayThreshold* createNewGrayThreshold();
				RGBThreshold* createNewRGBThreshold();
				RGBImageServer* createNewRGBImageServer();
				GrayImageServer* createNewGrayImageServer();
				Kinect* createNewKinect();
				Object* getObjectByUID(string uid);
				void releaseObjectByUID(string uid);
			};
		};
	};
};
#endif //Vision_ICE

