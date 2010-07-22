
/* AUTOMATICALLY GENERATED FILE, DO NOT EDIT! */

/**
 * @file AStreamCars0Base.cpp
 *
 * File generated at TODO.
 *
 * Copyright (C) 2009 DLR TS. All rights reserved.
 */

#include <stdlib.h>
#include <string.h>

#include "DominionFUST/include/Types.h"

#include "StreamCars0/include/auto/AStreamCars0Base.h"

#define DOMINION_ERROR_RANGE_MAX 0x0001
#define DOMINION_ERROR_RANGE_MIN 0x0002

AStreamCars0Base::AStreamCars0Base()
{
#ifndef DOMINION_SFUNCTION

    /* CAN communication. */
#ifdef DOMINION_APPLICATION_CAN
    _can = new DTransceiverCAN(this);
    _can->setRecvDataCallback(&_canCallback);
#endif
	/* UDP/IP communication. */
#ifdef DOMINION_APPLICATION_UDP
	_udp = new DTransceiverUDP(this);
	_udp->setRecvDataCallback(&_udpCallback);
#endif
	/* Serial communication. */
#ifdef DOMINION_APPLICATION_SERIAL
	_serial = new DTransceiverUDP(this);
	_serial->setRecvDataCallback(&_serialCallback);
#endif

    /* Temporary solution for the new data core interface. */
    //_dio->setVersion(2);
    
    /* Random information */
    setName("StreamCars0");
    setAuthorName("Project Group StreamCars, Uni Oldenburg");
    setAuthorEmail("pg-streamcars@polaris-neu.offis.uni-oldenburg.de");
    setDomainName("HMI");
    setDomainID(0); // TODO
    setBuiltStamp("TODO");
#endif
}

AStreamCars0Base::~AStreamCars0Base()
{
#ifndef DOMINION_SFUNCTION

    /* CAN communication. */
#ifdef DOMINION_APPLICATION_CAN
    delete _can;
#endif
    /* UDP/IP communication. */
#ifdef DOMINION_APPLICATION_UDP
    delete _udp;
#endif
    /* Serial communication. */
#ifdef DOMINION_APPLICATION_SERIAL
    delete _udp;
#endif

#endif
}

/**
 * Make the mapping from the Dominion HADES space into user
 * view specific address space.
 */
void AStreamCars0Base::doMapping(void)
{
	printf("AStreamCars0Base: Mapping ...\n");

	doMappingParameter();
	doMappingOutput();
	doMappingInput();

	printf("AStreamCars0Base: Mapping finished.\n");
}

/**
 * Start the external communication, CAN, UDP and Serial at this point.
 */
void AStreamCars0Base::startExternalCommunication(void)
{
    /* CAN communication. */
#ifdef DOMINION_APPLICATION_CAN
    _can->openReceivingChannel();
    _can->openSendingChannel();
#endif

    /* UDP/IP communication. */
#ifdef DOMINION_APPLICATION_UDP
    _udp->openReceivingChannel();
    _udp->openSendingChannel();
#endif

	/* Serial communication. */
#ifdef DOMINION_APPLICATION_SERIAL
    _serial->openReceivingChannel();
    _serial->openSendingChannel();
#endif

}

/**
 * Stop the external communication.
 */
void AStreamCars0Base::stopExternalCommunication(void)
{
    /* Serial communication. */
#ifdef DOMINION_APPLICATION_SERIAL
    _serial->closeReceivingChannel();
    _serial->closeSendingChannel();
#endif

    /* UDP/IP communication. */
#ifdef DOMINION_APPLICATION_UDP
    _udp->closeReceivingChannel();
    _udp->closeSendingChannel();
#endif    

    /* CAN communication. */
#ifdef DOMINION_APPLICATION_CAN
    _can->closeReceivingChannel();
    _can->closeSendingChannel();
#endif
}

/**
 * Make the mapping from the Dominion HADES space into user
 * data input view specific address space.
 */
void AStreamCars0Base::doMappingInput(void)
{
#ifndef DOMINION_SFUNCTION

	DList<void*> addresses;
	DList<void*> addressesMeta;
	DList<unsigned int> multiplicities;
	DList<TEntryType> types;
	DList<DString> namesEntries;
	DList<DString> namesAllNodes;
	DByteArray xml;
	TEntryType type = ENTRY_TYPE_INPUT;

	/* Pass the XML. */
	
  xml.push_back("<Domain name=\"root\" id=\"-1\"> "
" <Domain name=\"Environment\" id=\"-1\"> "
" <Data> "
" <Structure name=\"SensorData\"> \
	<MetaInformation> \
		<Multiplicity>1</Multiplicity> \
		<Visibility>public</Visibility> \
	</MetaInformation> "
" <Structure name=\"DetectedVehicles\"> \
	<MetaInformation> \
		<Multiplicity>50</Multiplicity> \
		<Visibility>public</Visibility> \
	</MetaInformation> "
" <Entry name=\"Type\"> \
	<MetaInformation> \
		<Multiplicity>1</Multiplicity> \
		<Visibility>public</Visibility> \
	</MetaInformation> \
	<Range> \
		<Min>0</Min> \
		<Max>12</Max> \
		<Granularity>1</Granularity> \
	</Range> \
	<Default>0</Default> \
	<PhysUnit>-</PhysUnit> \
	<Type>SInt32</Type> \
</Entry> "
" <Entry name=\"CarTrafficID\"> \
	<MetaInformation> \
		<Multiplicity>1</Multiplicity> \
		<Visibility>public</Visibility> \
	</MetaInformation> \
	<Range> \
		<Min>0</Min> \
		<Max>60</Max> \
		<Granularity>1</Granularity> \
	</Range> \
	<Default>0</Default> \
	<PhysUnit>-</PhysUnit> \
	<Type>SInt32</Type> \
</Entry> "
" <Entry name=\"LaneID\"> \
	<MetaInformation> \
		<Multiplicity>1</Multiplicity> \
		<Visibility>public</Visibility> \
	</MetaInformation> \
	<Range> \
		<Min>-1</Min> \
		<Max>9999</Max> \
		<Granularity>1</Granularity> \
	</Range> \
	<Default>0</Default> \
	<PhysUnit>-</PhysUnit> \
	<Type>SInt32</Type> \
</Entry> "
" <Entry name=\"PositionUTM\"> \
	<MetaInformation> \
		<Multiplicity>6</Multiplicity> \
		<Visibility>public</Visibility> \
	</MetaInformation> \
	<Range> \
		<Min>0</Min> \
		<Max>10e6</Max> \
		<Granularity>0.01</Granularity> \
	</Range> \
	<Default>0.0</Default> \
	<PhysUnit>m</PhysUnit> \
	<Type>Float64</Type> \
</Entry> "
" <Entry name=\"Velocity\"> \
	<MetaInformation> \
		<Multiplicity>1</Multiplicity> \
		<Visibility>public</Visibility> \
	</MetaInformation> \
	<Range> \
		<Min>-50.0</Min> \
		<Max>250.0</Max> \
		<Granularity>0.01</Granularity> \
	</Range> \
	<Default>0.0</Default> \
	<PhysUnit>m/s</PhysUnit> \
	<Type>Float32</Type> \
</Entry> "
" <Entry name=\"Length\"> \
	<MetaInformation> \
		<Multiplicity>1</Multiplicity> \
		<Visibility>public</Visibility> \
	</MetaInformation> \
	<Range> \
		<Min>-1.0</Min> \
		<Max>50.0</Max> \
		<Granularity>0.01</Granularity> \
	</Range> \
	<Default>-1.0</Default> \
	<PhysUnit>m</PhysUnit> \
	<Type>Float32</Type> \
</Entry> "
" <Entry name=\"Width\"> \
	<MetaInformation> \
		<Multiplicity>1</Multiplicity> \
		<Visibility>public</Visibility> \
	</MetaInformation> \
	<Range> \
		<Min>-1.0</Min> \
		<Max>50.0</Max> \
		<Granularity>0.01</Granularity> \
	</Range> \
	<Default>-1.0</Default> \
	<PhysUnit>m/s</PhysUnit> \
	<Type>Float32</Type> \
</Entry> "
" </Structure> "
" </Structure> "
" </Data> "
" </Domain> "
" </Domain> ");
	
	/* Fill the entry list. */
	
	/* SuperDomain */
	char path[0x800];
	void *data;
	void *meta;
	
	/* Path is empty for SuperDomain. */
	path[0] = '\0';
	/* Data and meta points to _input/_output/_parameter. */
	data = &_input;
	meta = &_inputMeta;
		
	/* Domain 'Environment' */
	{
		data = &_input;
		meta = &_inputMeta;
		void *oldData = data;
		void *oldMeta = meta;

		/* Assemble the full qualified name. */
		sprintf(path, "%s", "Environment");
		namesAllNodes.push_back(path);
		sprintf(path, "%s@", path);

		/* Build the addresses. */
		data = &( ((TInput *)data)->Environment );
		meta = &( ((TInputMeta *)meta)->Environment._multiplicity );

		/* Fill the maps. */
		addressesMeta.push_back(meta);
		multiplicities.push_back(1);

		/* Get back the old items. */
		meta = &( ((TInputMeta *)oldMeta)->Environment);
		
		/* Structure 'Environment@SensorData' */
		{
			void *oldData = data;
			void *oldMeta = meta;
			char oldPath[0x800];
			strcpy(oldPath, path);
				
			/* Assemble the full qualified name. */
			sprintf(path, "%s%s", path, "SensorData");
			namesAllNodes.push_back(path);
			sprintf(path, "%s.", path);

			/* Build the addresses. */
			data = &( ((TInputEnvironment *)data)->SensorData );
			meta = &( ((TInputMetaEnvironment *)meta)->SensorData._multiplicity );

			/* Fill the maps. */
			addressesMeta.push_back(meta);
			multiplicities.push_back(1);

			/* Get back the old items */
			meta = &( ((TInputMetaEnvironment *)oldMeta)->SensorData);
						
		/* Structure 'Environment@SensorData.DetectedVehicles' */
		{
			void *oldData = data;
			void *oldMeta = meta;
			char oldPath[0x800];
			strcpy(oldPath, path);
				
			for(int i = 0; i < 50; i++)
			{	/* Assemble the full qualified name. */
				sprintf(path, "%s%s[%i]", path, "DetectedVehicles", i);
				namesAllNodes.push_back(path);
				sprintf(path, "%s.", path);

				/* Build the addresses. */
				data = &( ((TInputEnvironmentSensorData *)data)->DetectedVehicles[i] );
				meta = &( ((TInputMetaEnvironmentSensorData *)meta)->DetectedVehicles._multiplicity );

				/* Fill the maps. */
				addressesMeta.push_back(meta);
				multiplicities.push_back(50);

				/* Get back the old items */
				meta = &( ((TInputMetaEnvironmentSensorData *)oldMeta)->DetectedVehicles);
				
			/* Entry 'Environment@SensorData.DetectedVehicles.Type' */
			{
				void *oldData = data;
				void *oldMeta = meta;
				char oldPath[0x800];
				strcpy(oldPath, path);

				/* Assemble the full qualified name. */
				sprintf(path, "%s%s", path, "Type");
				namesEntries.push_back(path);
				namesAllNodes.push_back(path);

				/* Build the addresses. */
				data = &( ((TInputEnvironmentSensorDataDetectedVehicles *)data)->Type );
				meta = &( ((TInputMetaEnvironmentSensorDataDetectedVehicles *)meta)->Type._multiplicity );

				/* Fill the maps. */
				addresses.push_back(data);
				addressesMeta.push_back(meta);
				multiplicities.push_back(1);
				types.push_back(type);

				/* Get back the old items. */
				strcpy(path, oldPath);
				data = oldData;
				meta = oldMeta;
			}
			
			/* Entry 'Environment@SensorData.DetectedVehicles.CarTrafficID' */
			{
				void *oldData = data;
				void *oldMeta = meta;
				char oldPath[0x800];
				strcpy(oldPath, path);

				/* Assemble the full qualified name. */
				sprintf(path, "%s%s", path, "CarTrafficID");
				namesEntries.push_back(path);
				namesAllNodes.push_back(path);

				/* Build the addresses. */
				data = &( ((TInputEnvironmentSensorDataDetectedVehicles *)data)->CarTrafficID );
				meta = &( ((TInputMetaEnvironmentSensorDataDetectedVehicles *)meta)->CarTrafficID._multiplicity );

				/* Fill the maps. */
				addresses.push_back(data);
				addressesMeta.push_back(meta);
				multiplicities.push_back(1);
				types.push_back(type);

				/* Get back the old items. */
				strcpy(path, oldPath);
				data = oldData;
				meta = oldMeta;
			}
			
			/* Entry 'Environment@SensorData.DetectedVehicles.LaneID' */
			{
				void *oldData = data;
				void *oldMeta = meta;
				char oldPath[0x800];
				strcpy(oldPath, path);

				/* Assemble the full qualified name. */
				sprintf(path, "%s%s", path, "LaneID");
				namesEntries.push_back(path);
				namesAllNodes.push_back(path);

				/* Build the addresses. */
				data = &( ((TInputEnvironmentSensorDataDetectedVehicles *)data)->LaneID );
				meta = &( ((TInputMetaEnvironmentSensorDataDetectedVehicles *)meta)->LaneID._multiplicity );

				/* Fill the maps. */
				addresses.push_back(data);
				addressesMeta.push_back(meta);
				multiplicities.push_back(1);
				types.push_back(type);

				/* Get back the old items. */
				strcpy(path, oldPath);
				data = oldData;
				meta = oldMeta;
			}
			
			/* Entry 'Environment@SensorData.DetectedVehicles.PositionUTM' */
			{
				void *oldData = data;
				void *oldMeta = meta;
				char oldPath[0x800];
				strcpy(oldPath, path);

				/* Assemble the full qualified name. */
				sprintf(path, "%s%s", path, "PositionUTM");
				namesEntries.push_back(path);
				namesAllNodes.push_back(path);

				/* Build the addresses. */
				data = &( ((TInputEnvironmentSensorDataDetectedVehicles *)data)->PositionUTM );
				meta = &( ((TInputMetaEnvironmentSensorDataDetectedVehicles *)meta)->PositionUTM._multiplicity );

				/* Fill the maps. */
				addresses.push_back(data);
				addressesMeta.push_back(meta);
				multiplicities.push_back(6);
				types.push_back(type);

				/* Get back the old items. */
				strcpy(path, oldPath);
				data = oldData;
				meta = oldMeta;
			}
			
			/* Entry 'Environment@SensorData.DetectedVehicles.Velocity' */
			{
				void *oldData = data;
				void *oldMeta = meta;
				char oldPath[0x800];
				strcpy(oldPath, path);

				/* Assemble the full qualified name. */
				sprintf(path, "%s%s", path, "Velocity");
				namesEntries.push_back(path);
				namesAllNodes.push_back(path);

				/* Build the addresses. */
				data = &( ((TInputEnvironmentSensorDataDetectedVehicles *)data)->Velocity );
				meta = &( ((TInputMetaEnvironmentSensorDataDetectedVehicles *)meta)->Velocity._multiplicity );

				/* Fill the maps. */
				addresses.push_back(data);
				addressesMeta.push_back(meta);
				multiplicities.push_back(1);
				types.push_back(type);

				/* Get back the old items. */
				strcpy(path, oldPath);
				data = oldData;
				meta = oldMeta;
			}
			
			/* Entry 'Environment@SensorData.DetectedVehicles.Length' */
			{
				void *oldData = data;
				void *oldMeta = meta;
				char oldPath[0x800];
				strcpy(oldPath, path);

				/* Assemble the full qualified name. */
				sprintf(path, "%s%s", path, "Length");
				namesEntries.push_back(path);
				namesAllNodes.push_back(path);

				/* Build the addresses. */
				data = &( ((TInputEnvironmentSensorDataDetectedVehicles *)data)->Length );
				meta = &( ((TInputMetaEnvironmentSensorDataDetectedVehicles *)meta)->Length._multiplicity );

				/* Fill the maps. */
				addresses.push_back(data);
				addressesMeta.push_back(meta);
				multiplicities.push_back(1);
				types.push_back(type);

				/* Get back the old items. */
				strcpy(path, oldPath);
				data = oldData;
				meta = oldMeta;
			}
			
			/* Entry 'Environment@SensorData.DetectedVehicles.Width' */
			{
				void *oldData = data;
				void *oldMeta = meta;
				char oldPath[0x800];
				strcpy(oldPath, path);

				/* Assemble the full qualified name. */
				sprintf(path, "%s%s", path, "Width");
				namesEntries.push_back(path);
				namesAllNodes.push_back(path);

				/* Build the addresses. */
				data = &( ((TInputEnvironmentSensorDataDetectedVehicles *)data)->Width );
				meta = &( ((TInputMetaEnvironmentSensorDataDetectedVehicles *)meta)->Width._multiplicity );

				/* Fill the maps. */
				addresses.push_back(data);
				addressesMeta.push_back(meta);
				multiplicities.push_back(1);
				types.push_back(type);

				/* Get back the old items. */
				strcpy(path, oldPath);
				data = oldData;
				meta = oldMeta;
			}
			
				/* Get back the old items */
				strcpy(path, oldPath);
				data = oldData;
				meta = oldMeta;
			}
					
			/* Get back the old items */
			strcpy(path, oldPath);
			data = oldData;
			meta = oldMeta;
		}
		
			/* Get back the old items */
			strcpy(path, oldPath);
			data = oldData;
			meta = oldMeta;
		}
		
	}
	
	/* Process the entry list. */
	_dio->initialize(xml,&namesEntries,&types,&addresses);
	/* Process the meta list. */
	_dio->initializeMeta(xml,&namesAllNodes,&multiplicities,&addressesMeta);

#endif
}

/**
 * Make the mapping from the Dominion HADES space into user
 * data output view specific address space.
 */
void AStreamCars0Base::doMappingOutput(void)
{
#ifndef DOMINION_SFUNCTION

#endif
}

/**
* Make the mapping from the Dominion HADES space into user
* data parameter view specific address space.
*/
void AStreamCars0Base::doMappingParameter(void)
{
#ifndef DOMINION_SFUNCTION

#endif
}

#ifdef DOMINION_APPLICATION_CAN
/**
 * CAN Callback Functor calls the application's routine.
 */
void DStreamCars0CANCallback::call(void *recvData, int numBytes, unsigned int Port,
	const struct timeval *TimeStamp, ATransceiver *t)
{
    AStreamCars0Base *owner = t->getOwner<AStreamCars0Base>();
    owner->receiveCANMessage(Port, recvData, numBytes);
}
#endif /* DOMINION_APPLICATION_CAN */

#ifdef DOMINION_APPLICATION_UDP
/**
 * UDP Callback Functor calls the application's routine.
 */
void DStreamCars0UDPCallback::call(void *recvData, int numBytes, unsigned int Port,
	const struct timeval *TimeStamp, ATransceiver *t)
{
    AStreamCars0Base *owner = t->getOwner<AStreamCars0Base>();
    owner->receiveUDPMessage(Port, recvData, numBytes);
}
#endif /* DOMINION_APPLICATION_CAN */

void AStreamCars0Base::dispatchRun(const TTimerState *timerState)
{
#ifndef DOMINION_SFUNCTION
	_dio->read();
#ifdef DOMINION_APPLICATION_AUTOCHECK
	checkInputData();
#endif /* DOMINION_APPLICATION_AUTOCHECK */
	run();
#ifdef DOMINION_APPLICATION_AUTOCHECK
	checkOutputData();
#endif /* DOMINION_APPLICATION_AUTOCHECK */
	_dio->write();	
#endif

    if(timerState != NULL)
    {
        memcpy(&_timerState, timerState, sizeof(TTimerState));
    }
}

void AStreamCars0Base::dispatchDisplay(const TTimerState *timerState)
{
    display();
}

#ifdef DOMINION_APPLICATION_AUTOCHECK
	void AStreamCars0Base::checkInputData(void)
  {
  	
  }
  
  void AStreamCars0Base::checkOutputData(void)
  {
  	
  }
#endif /* DOMINION_APPLICATION_AUTOCHECK */

#ifndef DOMINION_SFUNCTION

void AStreamCars0Base::printInput(void)
{
/*
    
*/
}

void AStreamCars0Base::printOutput(void)
{
/*
    
*/
}

void AStreamCars0Base::printParameter(void)
{
/*
    
*/
}

#endif

/* AUTOMATICALLY GENERATED FILE, DO NOT EDIT! */
