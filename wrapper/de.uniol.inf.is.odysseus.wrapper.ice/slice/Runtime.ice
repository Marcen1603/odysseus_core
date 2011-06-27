 
#ifndef Runtime_ICE
#define Runtime_ICE

#include "Scheduler2.ice"

module scm
{
	module eci
	{
		#ifndef STRING_SEQ_RT
		#define STRING_SEQ_RT
			sequence<string> StrSeq;
		#endif //STRING_SEQ_RT
		
		module rt
		{
			
			//Module rt forward declarations
					
			interface SCMElement;
			interface SCMRunnable;
			interface Component;
			interface Port;
			interface InputPort;
			sequence<InputPort*> InputPortSeq;
			interface OutputPort;
			sequence<OutputPort*> OutputPortSeq;
			interface PortListener;
			sequence<PortListener*> PortListenerSeq;
			interface Property;
			sequence<Property*> PropertySeq;
			interface PropertyListener;
			sequence<PropertyListener*> PropertyListenerSeq;
		
			//end of forward declarations
					
					
			/** Component
			*	name : SCMElement
			*	description: 
			*/
			interface SCMElement
			{
				/** Attribute : name
				*	description : 
				*/
				string getName();
						
				/** Attribute : desc
				*	description : 
				*/
				string getDescription();
						
				/** Attribute : uid
				*	description : 
				*/
				string getUid();
				
			};
		
			/** Component
			*	name : SCMRunnable
			*	description: 
			*/
			interface SCMRunnable extends SCMElement
			{
				/** Operation: run
				*	description: 
				*/	
				void run() ;	
			};
		
			/** Component
			*	name : Component
			*	description: 
			*/
			interface Component extends SCMRunnable, scheduler2::Job
			{
				/** Attribute : inputPorts
				*	description : 
				*/
				InputPortSeq getInputPorts();
				InputPort* getInputPort(string name);
				InputPort* getInputPortByUID(string uid);
						
				/** Attribute : outputPorts
				*	description : 
				*/
				OutputPortSeq getOutputPorts();
				OutputPort* getOutputPort(string name);
				OutputPort* getOutputPortByUID(string uid);
						
				/** Attribute : properties
				*	description : 
				*/
				PropertySeq getProperties();
				Property* getProperty(string name);
				Property* getPropertyByUID(string uid);
				
				//execute moved to scheduler::Job::execute(ExecutionContext)
			};
		
			/** Component
			*	name : Port
			*	description: 
			*/
			interface Port extends SCMElement
			{
			};
		
			interface OutputPort;
			class IMessage 
			{
				/**
				*	References OutputPort::getUID()
				*/
				string senderPort;
				
				/**
				*	Timestamp of Message sending (not creation of the containing data)
				*	Milliseconds since 1.1.1970
				*/
				double timeStamp; 
			};
			/** Component
			*	name : InputPort
			*	description: 
			*/
			interface InputPort extends Port
			{
				/** Attribute : noSync
				*	description : 
				*/
				bool getNoSync();
					
				/** Attribute : mayNull
				*	description : 
				*/
				bool getMayNull();
						
				/** Attribute : cycle
				*	description : 
				*/
				bool getCycle();
								
				bool hasSource();
				OutputPort* getSource();
				void setSource(OutputPort* source);
				void resetSource();//setzt source auf NULL
											
				/** Attribute : neighbors
				*	description : References to other inputports in the same component
				*/
				InputPortSeq getNeighbors();
				
				bool setNeighbors(InputPortSeq newValue);
				
				void receiveMessage(IMessage msg);
			};
		
			/** Component
			*	name : PortListener
			*	description: 
			*/
			interface PortListener
			{
				void update(IMessage message);
			};
		
			/** Component
			*	name : OutputPort
			*	description: 
			*/
			interface OutputPort extends Port
			{
				/** Attribute : sync
				*	description : 
				*/
				bool getSync();
				
				bool setSync(bool newValue);
						
				/** Attribute : allowObserver
				*	description : 
				*/
				bool getAllowObserver();
				
				bool setAllowObserver(bool newValue);
						
						
				bool connect(InputPort* target);
				bool disconnect(InputPort* target);
				/** Attribute : targets
				*	description : 
				*/
				InputPortSeq getTargets();
				
						
				/** Attribute : listener
				*	description : 
				*/
				PortListenerSeq getListener();
				bool registerListener(PortListener* listener);
				bool removeListener(PortListener* listener);
				bool setListener(PortListenerSeq newValue);
				
				void sendMessage(IMessage msg);
			
			};
		
			/** Component
			*	name : PropertyListener
			*	description: 
			*/
			interface PropertyListener
			{
				void propertyChanged(Property* changedProperty);
			};
		
			/** Component
			*	name : Property
			*	description: 
			*/
			interface Property extends SCMElement
			{
				/** Attribute : listener
				*	description : 
				*/
				PropertyListenerSeq getListener();
				
				bool registerListener(PropertyListener* listener);
				bool removeListener(PropertyListener* listener);
				bool setListener(PropertyListenerSeq newValue);
			};

		};
		
		#ifndef STRING_SEQ_
		#define STRING_SEQ_
			sequence<string> stringSeq;
			class StringMessage extends rt::IMessage { string value; };
			interface StringInputPort extends rt::InputPort {
				StringMessage getMessage();
				string getValue();
			};
			interface StringOutputPort extends rt::OutputPort {
				void send(string msg);
			};
			
			
		#endif //STRING_SEQ_
		#ifndef INT_SEQ_
		#define INT_SEQ_
			sequence<int> intSeq;
			class IntegerMessage extends rt::IMessage { int value; };
			interface IntegerInputPort extends rt::InputPort {
				IntegerMessage getMessage();
				int getValue();
			};
			interface IntegerOutputPort extends rt::OutputPort {
				void send(int msg);
			};
		#endif // INT_SEQ_
		#ifndef BYTE_SEQ_
		#define BYTE_SEQ_
			sequence<byte> byteSeq;
			class ByteMessage extends rt::IMessage { byte value; };
			interface ByteInputPort extends rt::InputPort {
				ByteMessage getMessage();
				byte getValue();
			};
			interface ByteOutputPort extends rt::OutputPort {
				void send(byte msg);
			};
		#endif //BYTE_SEQ_
		#ifndef DOUBLE_SEQ_
		#define DOUBLE_SEQ_
			sequence<double> doubleSeq;
			class DoubleMessage extends rt::IMessage { double value; };
			interface DoubleInputPort extends rt::InputPort {
				DoubleMessage getMessage();
				double getValue();
			};
			interface DoubleOutputPort extends rt::OutputPort {
				void send(double msg);
			};
		#endif //DOUBLE_SEQ_
		#ifndef BOOL_SEQ_
		#define BOOL_SEQ_
			sequence<bool>	boolSeq;
			class BoolMessage extends rt::IMessage { bool value; };
			interface BoolInputPort extends rt::InputPort {
				BoolMessage getMessage();
				bool getValue();
			};
			interface BoolOutputPort extends rt::OutputPort {
				void send(bool msg);
			};
		#endif//BOOL_SEQ_
	};
};
#endif //Runtime_ICE

