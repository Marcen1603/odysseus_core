 
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
#ifndef Simulation_ICE
#define Simulation_ICE

#include "Runtime.ice"

		#include "Scheduler2.ice"
		#include "Base.ice"
		#include "Vision.ice"
		#include "Laser.ice"
	

module scm
{
	module eci { //cause ice is a keyword		
		
	
		module simulation
		{
			
			//Module Simulation forward declarations
			
			interface SimObject;
			sequence<SimObject*> SimObjectSeq;

			interface SimAgent;
			sequence<SimAgent*> SimAgentSeq;

			interface SimBehavior;
			sequence<SimBehavior*> SimBehaviorSeq;

			interface SimSensor;
			sequence<SimSensor*> SimSensorSeq;

			interface SimCamera;
			sequence<SimCamera*> SimCameraSeq;

			interface SimLaserscanner;
			sequence<SimLaserscanner*> SimLaserscannerSeq;

			class TransformState;
			sequence<TransformState> TransformStateSeq;

			class BoundingVolume;
			sequence<BoundingVolume> BoundingVolumeSeq;

			class AABB;
			sequence<AABB> AABBSeq;

			class BoundingSphere;
			sequence<BoundingSphere> BoundingSphereSeq;

			interface Scene;
			sequence<Scene*> SceneSeq;

			class Resource;
			sequence<Resource> ResourceSeq;

			interface Scenario;
			sequence<Scenario*> ScenarioSeq;

			interface SimulationControler;
			sequence<SimulationControler*> SimulationControlerSeq;

			//end of forward declarations
			  
			
			/** Datatype
			*	name : TransformState
			*	description: 
			*/
			class TransformState
			{
				/** Attribute : transformMatrix
				*	description : 
				*/
				base::Matrix4x4 transformMatrix;		
						
				/** Operation: getPosition
				*	description: 
				*/	
				base::Vector3 getPosition() ;
				/** Operation: getEuler
				*	description: 
				*/	
				base::Vector3 getEuler() ;
				/** Operation: getScale
				*	description: 
				*/	
				base::Vector3 getScale() ;
				
			};
			class TransformStateMessage extends rt::IMessage { TransformState value; };
			interface TransformStateInputPort extends rt::InputPort {
				TransformStateMessage getMessage();
				TransformState getValue();
			};
			interface TransformStateOutputPort extends rt::OutputPort {
				void send(TransformState msg);
			};
	

			/** Datatype
			*	name : BoundingVolume
			*	description: 
			*/
			class BoundingVolume
			{
						
				
			};
			class BoundingVolumeMessage extends rt::IMessage { BoundingVolume value; };
			interface BoundingVolumeInputPort extends rt::InputPort {
				BoundingVolumeMessage getMessage();
				BoundingVolume getValue();
			};
			interface BoundingVolumeOutputPort extends rt::OutputPort {
				void send(BoundingVolume msg);
			};
	

			/** Datatype
			*	name : AABB
			*	description: 
			*/
			class AABB extends simulation::BoundingVolume
			{
				/** Attribute : min
				*	description : 
				*/
				base::Vector3 min;		
				/** Attribute : max
				*	description : 
				*/
				base::Vector3 max;		
						
				
			};
			class AABBMessage extends rt::IMessage { AABB value; };
			interface AABBInputPort extends rt::InputPort {
				AABBMessage getMessage();
				AABB getValue();
			};
			interface AABBOutputPort extends rt::OutputPort {
				void send(AABB msg);
			};
	

			/** Datatype
			*	name : BoundingSphere
			*	description: 
			*/
			class BoundingSphere extends simulation::BoundingVolume
			{
				/** Attribute : radius
				*	description : 
				*/
				double radius;		
						
				
			};
			class BoundingSphereMessage extends rt::IMessage { BoundingSphere value; };
			interface BoundingSphereInputPort extends rt::InputPort {
				BoundingSphereMessage getMessage();
				BoundingSphere getValue();
			};
			interface BoundingSphereOutputPort extends rt::OutputPort {
				void send(BoundingSphere msg);
			};
	

			/** Datatype
			*	name : Resource
			*	description: 
			*/
			class Resource
			{
				/** Attribute : numInstances
				*	description : 
				*/
				int numInstances;
				string name;
				string uid;
						
				
			};
			class ResourceMessage extends rt::IMessage { Resource value; };
			interface ResourceInputPort extends rt::InputPort {
				ResourceMessage getMessage();
				Resource getValue();
			};
			interface ResourceOutputPort extends rt::OutputPort {
				void send(Resource msg);
			};
	

			
			
			/** Component
			*	name : SimObject
			*	description: 
			*/
			/*abstract*/ interface SimObject  extends rt::Component 
			{
				/** Attribute : localTransform
				*	description : 
				*/
				simulation::TransformState getLocalTransform();
				/** Operation: getWorldTransform
				*	description: 
				*/	
				simulation::TransformState getWorldTransform() ;
				/** OutputPort: transform
				*	description: 
				*/
				simulation::TransformStateOutputPort* getTransformOutputPort();
				
				string getResourceUID();
			};

			/** Component
			*	name : SimAgent
			*	description: 
			*/
			interface SimAgent  extends simulation::SimObject
			{
				/** Attribute : behaviors
				*	description : 
				*/
				simulation::SimBehaviorSeq getBehaviors();
				bool setBehaviors(simulation::SimBehaviorSeq newValue);
				/** Operation: setLocalTransform
				*	description: 
				*/	
				void setLocalTransform(simulation::TransformState transform) ;
				/** Operation: setWorldTransform
				*	description: 
				*/	
				void setWorldTransform(simulation::TransformState transform) ;
				/** Operation: setPosition
				*	description: 
				*/	
				void setPosition(double x, double y, double z) ;
				/** Operation: setRotationEuler
				*	description: 
				*/	
				void setRotationEuler(double x, double y, double z) ;
				/** Operation: setRotationMatrix
				*	description: 
				*/	
				void setRotationMatrix(base::Matrix3x3 matrix) ;
				/** Operation: setScale
				*	description: 
				*/	
				void setScale(double x, double y, double z) ;
				/** Operation: getBoundingVolume
				*	description: 
				*/	
				simulation::BoundingVolume getBoundingVolume() ;
				/** Operation: getBoundingPolygon
				*	description: 
				*/	
				base::Polygon3 getBoundingPolygon() ;
				
				void registerBehavior(SimBehavior* behavior);
				void removeBehavior(SimBehavior* behavior);
				SimBehavior* getBehaviorByUID(string uid);
				SimBehavior* getBehaviorByName(string uid);
			};

			/** Component
			*	name : SimBehavior
			*	description: 
			*/
			/*abstract*/ interface SimBehavior  extends simulation::SimObject, scheduler2::Job
			{
				/** Operation: getAgent
				*	description: 
				*/	
				simulation::SimAgent* getAgent() ;
				void setAgent(SimAgent* agent); //Sollte nicht von aussen aufgerufen werden. Diese Methode wird getriggert, wenn das Behavior sich beim Agenten anmeldet.
				/** Operation: isDebug
				*	description: 
				*/	
				bool isDebug() ;
				/** Operation: onCreation
				*	description: 
				*/	
				void onCreation() ;
				/** Operation: onDestruction
				*	description: 
				*/	
				void onDestruction() ;
				
				void scheduleOnce(int relativeMilliseconds);
				void scheduleInterval(int relativStartMilli, int intervalMilli);
				
				bool isRepeatableScheduled();
				
				void disableSchedule();
				
			};

			/** Component
			*	name : SimSensor
			*	description: 
			*/
			/*abstract*/ interface SimSensor  extends simulation::SimBehavior, base::Sensor
			{
				/** Operation: capture
				*	description: 
				*/	
				void capture(double simTime) ;
				
			};

			/** Component
			*	name : SimCamera
			*	description: 
			*/
			interface SimCamera  extends vision::RGBImageServer, simulation::SimSensor
			{
				/** Attribute : near
				*	description : 
				*/
				double getNear();
				bool setNear(double newValue);
				/** Attribute : far
				*	description : 
				*/
				double getFar();
				bool setFar(double newValue);
				/** Attribute : fov
				*	description : 
				*/
				double getFov();
				bool setFov(double newValue);
				/** Attribute : orthographic
				*	description : 
				*/
				bool getOrthographic();
				bool setOrthographic(bool newValue);
				
				
				bool setOrthoScale(double newValue);
				double getOrthoScale();
				
				bool setFocalLength(double newValue);
				double getFocalLength();
				
				
				/** Operation: move
				*	description: 
				*	@param moveAgent: die Positionsänderung wird an den Agenten weitergegeben.
				*/	
				void move(double x, double y, double z, bool moveAgent) ;
				/** Operation: rotateEuler
				*	description: 
				*	@param rotateAgent: Die rotation wird an den Agenten weitergegeben
				*/	
				void rotateEuler(double x, double y, double z, bool rotateAgent) ;
				/** Operation: makeCurrent
				*	description: 
				*/	
				void makeCurrent() ;
			};

			/** Component
			*	name : SimLaserscanner
			*	description: 
			*/
			interface SimLaserscanner  extends laser::Laserscanner, simulation::SimSensor
			{
				/** Operation: setRange
				*	description: 
				*/	
				void setRange(double min, double max) ;
			};

			/** Component
			*	name : Scene
			*	description: 
			*/
			interface Scene  extends rt::Component 
			{
				/** Attribute : resources
				*	description : 
				*/
				simulation::ResourceSeq getResources();
				/** OutputPort: resourceCreated
				*	description: 
				*/
				simulation::ResourceOutputPort* getResourceCreatedOutputPort();
				/** OutputPort: resourceDeleted
				*	description: 
				*/
				simulation::ResourceOutputPort* getResourceDeletedOutputPort();
				
				string getFileName();
				bool isInitialized();
			};

			/** Component
			*	name : Scenario
			*	description: 
			*/
			interface Scenario  extends simulation::Scene
			{
				/** Attribute : agents
				*	description : 
				*/
				simulation::SimAgentSeq getAgents();
				/** Operation: getObjectByUID
				*	description: 
				*/	
				simulation::SimObject* getObjectByUID(string uid) ;
				/** Operation: getObjectByName
				*	description: 
				*/	
				simulation::SimObject* getObjectByName(string uid) ;
				/** OutputPort: agentCreated
				*	description: References to SimAgent->getUID()
				*/
				StringOutputPort* getAgentCreatedOutputPort();
				/** OutputPort: agentDeleted
				*	description: References to SimAgent->getUID()
				*/
				StringOutputPort* getAgentDeletedOutputPort();
				
			};

			/** Component
			*	name : SimulationControler
			*	description: 
			*/
			interface SimulationControler  extends rt::Component 
			{
				/** Attribute : scheduler
				*	description : 
				*/
				scheduler2::Scheduler* getScheduler();
				/** Attribute : scenes
				*	description : 
				*/
				simulation::SceneSeq getScenes();
				/** Attribute : activeScenario
				*	description : 
				*/
				simulation::Scenario* getActiveScenario();
				/** Operation: loadScene
				*	description: 
				*/	
				simulation::Scene* loadScene(base::File fileToLoad) ;
				/** Operation: instanceiateScene
				*	description: 
				*/	
				simulation::SimAgentSeq instanceiateScene(simulation::Scene* sceneToInit) ;
				
				simulation::SimObject* instanceiateResource(simulation::Resource res, simulation::SimAgent* parent);
				/** Operation: renderFrame
				*	description: 
				*/	
				void renderFrame(SimCamera* camera);
				
				/** Operation doEvilRenderBypass
				*	description: Macht einen Bypass, am Scheduler vorbei um
				*				 das Bild in der GUI anzeigen zu können, auch wenn
				*				 der Scheduler nicht läuft.
				*		!!	Wird diese Methode verwendet, ist die Reihenfolge
				*			in der Aktionen durchgeführt werden, nicht mehr sichergestellt. !!
				*/
				void doEvilRenderBypass(SimCamera* camera);
				
				/**
				*	deaktiviert den Obrigen ByPass, ein Aufruf dieser Methode hat in dem Fall keine Aktion zur folge
				*/
				void enableEvilRenderBypass(bool enabled);
				bool isEvilRenderBypassEnabled();
				
				void setDebugEnabled(bool enabled);
				bool isDebugEnabled();
				
				SimObject* getObjectByName(string name);
				SimObject* getObjectByUID(string uid);
				/** OutputPort: sceneLoaded
				*	description: 
				*/
				base::FileOutputPort* getSceneLoadedOutputPort();
				
				BoolOutputPort* getInitializedOutputPort();
				
				SimAgent* createNewAgentWithCamera(); 
				Resource createNewCamera(string name);
			};

			
			interface SimulationFacade
			{
				SimAgent* createNewSimAgent();
				SimCamera* createNewSimCamera();
				SimLaserscanner* createNewSimLaserscanner();
				Scene* createNewScene();
				Scenario* createNewScenario();
				SimulationControler* createNewSimulationControler();
				Object* getObjectByUID(string uid);
				void releaseObjectByUID(string uid);
			};
		};
	};
};
#endif //Simulation_ICE

