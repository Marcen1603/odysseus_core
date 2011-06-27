 //do not overwrite
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
#ifndef Scheduler2_ICE
#define Scheduler2_ICE
	

module scm
{
	module eci { //cause ice is a keyword		
		
	
		module scheduler2
		{
			
			/**
			*	Enumeration: JobStatus
			*	description: 
			*/
			enum JobStatus
			{
			
				/**	Literal
				*	UNKNOWN
				*	description: 
				*/
				UNKNOWN
			, 
				/**	Literal
				*	SCHEDULED
				*	description: 
				*/
				SCHEDULED
			, 
				/**	Literal
				*	WAITING
				*	description: 
				*/
				WAITING
			, 
				/**	Literal
				*	STARTED
				*	description: 
				*/
				STARTED
			, 
				/**	Literal
				*	INTERRUPTED
				*	description: 
				*/
				INTERRUPTED
			, 
				/**	Literal
				*	CANCELED
				*	description: 
				*/
				CANCELED
			, 
				/**	Literal
				*	FINISHED
				*	description: 
				*/
				FINISHED
			, 
				/**	Literal
				*	CHANGED
				*	description: 
				*/
				CHANGED
			, 
				/**	Literal
				*	RESCHEDULED
				*	description: 
				*/
				RESCHEDULED
			
			};

			//Module Scheduler2 forward declarations
			
			interface Job;
			sequence<Job*> JobSeq;

			interface ProcessMonitor;
			sequence<ProcessMonitor*> ProcessMonitorSeq;

			class JobDescription;
			sequence<JobDescription> JobDescriptionSeq;

			class ExecutionContext;
			sequence<ExecutionContext> ExecutionContextSeq;

			interface Strategie;
			sequence<Strategie*> StrategieSeq;

			interface JobStatusListener;
			sequence<JobStatusListener*> JobStatusListenerSeq;

			interface JobProcessor;
			sequence<JobProcessor*> JobProcessorSeq;

			interface Scheduler;
			sequence<Scheduler*> SchedulerSeq;

			//end of forward declarations
			 
			
			/** Datatype
			*	name : JobDescription
			*	description: 
			*/
			class JobDescription
			{
				/** Attribute : id
				*	description : 
				*/
				int id;		
				/** Attribute : owns
				*	description : 
				*/
				bool owns;		
				/** Attribute : remainingRepeats
				*	description : 
				*/
				int remainingRepeats;		
				/** Attribute : updateInterval
				*	description : 
				*/
				int updateInterval;		
				/** Attribute : lastActivationTime
				*	description : 
				*/
				int lastActivationTime;		
				/** Attribute : nextActivationTime
				*	description : 
				*/
				int nextActivationTime;		
				/** Attribute : monitor
				*	description : 
				*/
				scheduler2::ProcessMonitor* monitor;		
				/** Attribute : activeJob
				*	description : 
				*/
				scheduler2::Job* activeJob;		
						
				
			};
			

			/** Datatype
			*	name : ExecutionContext
			*	description: 
			*/
			class ExecutionContext
			{
				/** Attribute : jobDesc
				*	description : 
				*/
				scheduler2::JobDescription jobDesc;		
				/** Attribute : activeScheduler
				*	description : 
				*/
				scheduler2::Scheduler* activeScheduler;		
				/** Attribute : timestamp
				*	description : 
				*/
				int timestamp;		
						
				
			};
					
			
			/** Component
			*	name : Job
			*	description: 
			*/
			/*abstract*/ interface Job 
			{
				/** Attribute : activeProcessor
				*	description : 
				*/
				scheduler2::JobProcessor* getActiveProcessor();
				bool setActiveProcessor(scheduler2::JobProcessor* newValue);
				/** Operation: canExecute
				*	description: 
				*/	
				bool canExecute() ;
				bool execute(ExecutionContext ctx);
				
				string getJobName();
				string getJobDescription();
			};

			/** Component
			*	name : ProcessMonitor
			*	description: 
			*/
			/*abstract*/ interface ProcessMonitor 
			{
				/** Operation: setStatus
				*	description: 
				*/	
				void setStatus(scheduler2::JobStatus status, string msg) ;
				/** Operation: setProgress
				*	description: 
				*/	
				void setProgress(int progress, string msg) ;
			};

			/** Component
			*	name : Strategie
			*	description: 
			*/
			interface Strategie 
			{
				string getName();
				string getDescription();
				/** Operation: insert
				*	description: 
				*/	
				bool insert(scheduler2::JobDescription jobDesc) ;
				/** Operation: remove
				*	description: 
				*/	
				bool remove(scheduler2::Job* jobDesc) ;
				/** Operation: hasNextStep
				*	description: 
				*/	
				bool hasNextStep() ;
				/** Operation: nextStep
				*	description: @return: Timestamp des n�chsten Zeitschrittes
				*/	
				int nextStep() ;
				/** Operation: hasNextJob
				*	description: 
				*/	
				bool hasNextJob() ;
				/** Operation: nextJob
				*	description: 
				*/	
				scheduler2::JobDescription nextJob() ;
			};

			/** Component
			*	name : JobStatusListener
			*	description: 
			*/
			/*abstract*/ interface JobStatusListener 
			{
				/** Operation: jobFinished
				*	description: 
				*/	
				void jobFinished(scheduler2::ExecutionContext ctx) ;
			};

			/** Component
			*	name : JobProcessor
			*	description: 
			*/
			/*abstract*/ interface JobProcessor 
			{
				/** Operation: executeJob
				*	description: 
				*/	
				bool executeJob(scheduler2::ExecutionContext execCont, scheduler2::JobStatusListener* listener) ;
			};

			interface SchedulerListener {
				void onJobInsert(JobDescription ctx);
				/** OutputPort: onJobRemoved
				*	description: 
				*/
				void onJobRemoved(Job* ctx);
				/** OutputPort: onPreExecute
				*	description: 
				*/ 
				void onPreExecute(ExecutionContext ctx);
				/** OutputPort: onPostExecute
				*	description: 
				*/
				void onPostExecute(ExecutionContext ctx);
				/** OutputPort: onNextStep
				*	description: 
				*/
				void onNextStep(int timestamp);
			};
			/** Component
			*	name : Scheduler
			*	description: 
			*/
			interface Scheduler 
			{
				string getName();
				string getDescription();
				
				/** Attribute : strategie
				*	description : Speichert die Jobs und bestimmt die Reihenfolge, mit der sie ausgefuehrt werden
				*/
				scheduler2::Strategie* getStrategie();
				bool setStrategie(scheduler2::Strategie* newValue);
				/** Attribute : timestamp
				*	description : aktueller zeitschritt
				*/
				int getTimestamp();
				/** Attribute : defaultProcessor
				*	description : Prozessor (Thread) in dem die Jobs ausgef�hrt werden, wenn der Job keinen eigenen JobProcessor definiert
				*/
				scheduler2::JobProcessor* getDefaultProcessor();
				/** Operation: insert
				*	description: F�gt einen neuen Job hinzu
				*/	
				bool insert(scheduler2::JobDescription jobDesc) ;
				/** Operation: remove
				*	description: 
				*/	
				bool remove(scheduler2::Job* jobDesc) ;
				/** Operation: hasNextStep
				*	description: Gibt es einen weiteren Zeitschritt 
				*/	
				bool hasNextStep() ;
				/** Operation: nextStep
				*	description: Springt in den n�chsten Zeitschritt\n @return: Timestamp des n�chsten Zeitschrittes
				*/	
				int nextStep() ;
				/** Operation: hasNextJob
				*	description: Gibt es im aktuellen Zeitschritt noch geplante Aufgaben
				*/	
				bool hasNextJob() ;
				/** Operation: executeNextJob
				*	description: F�hrt den n�chsten Job im aktuellen Zeitschritt aus
				*/	
				void executeNextJob() ;
				
				void registerListener(SchedulerListener* listener);
				void removeListener(SchedulerListener* listener);
			};

			
			interface Scheduler2Facade
			{
				Strategie* createNewStrategie();
				Scheduler* createNewScheduler();
				Object* getObjectByUID(string uid);
				void releaseObjectByUID(string uid);
			};
		};
	};
};
#endif //Scheduler2_ICE

