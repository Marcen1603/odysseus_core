/* AUTOMATICALLY GENERATED FILE. */

/**
 * @file DStreamCars0.cpp
 *
 * Copyright (C) 2009 DLR TS. All rights reserved.
 */

#include "StreamCars0/include/DStreamCars0.h"
#include <iostream>
#include "windows.h"

using namespace std;

/**
 * The initialization routine for this module.
 */
void DStreamCars0::init(void)
{
    /* You have access to the member variables input, output and parameter. */
	_controller->triggerModeIsPossible(true);
	_udp->setRecvPort(5001);
}

/**
 * The deinitialization routine for this module.
 */
void DStreamCars0::exit(void)
{
    /* You have access to the member variables input, output and parameter. */
	_udp->closeSocketSend();
}

/**
 * The actual runnable function of this very module.
 */
void DStreamCars0::run(void)
{
	
    /* You have access to the member variables input, output and parameter. */
	//_input.Environment.Traffic.Vehicles[0].
	//_input.Environment.SensorData.DetectedVehicles[0].
	//_output.HMI.Cockpit.Pedals.ThrottleAct = 100;
	//std::Hallo << "cou!" << std::endl;
    /* You have access to the member variables _input, _output and _parameter. */
    
    /* You may call a corresponding plain C function here, like
    DISiPADAS_PADASC_run(&_input, &_output, &_parameter);
    */
	
	//First Car
	/*
	CarData carData;
	carData.carType = 1;
	carData.carTrafficId = 2;
	carData.laneId = 3;
	carData.positionUTM[0] = 4.0;
	carData.positionUTM[1] = 5.0;
	carData.positionUTM[2] = 6.0;
	carData.positionUTM[3] = 7.0;
	carData.positionUTM[4] = 8.0;
	carData.positionUTM[5] = 9.0;
	carData.velocity = 10;
	carData.length = 11;
	carData.width = 12;*/

	//Second Car
	 /*
	CarData secondCar;
	secondCar.carType = 13;
	secondCar.carTrafficId = 14;
	secondCar.laneId = 15;
	secondCar.positionUTM[0] = 16.0;
	secondCar.positionUTM[1] = 17.0;
	secondCar.positionUTM[2] = 18.0;
	secondCar.positionUTM[3] = 19.0;
	secondCar.positionUTM[4] = 20.0;
	secondCar.positionUTM[5] = 21.0;
	secondCar.velocity = 22;
	secondCar.length = 23;
	secondCar.width = ;*/

	WholeScan scan;
	//scan.cars[0] = carData;
	//scan.cars[1] = secondCar;

	for (int i = 0; i < 50; ++i)
	{
		//if( _input.Environment.SensorData.DetectedVehicles[i].CarTrafficID == -1 ) continue;

		CarData carData;

		// Timestamp zusammenbauen:
		time_t msec = time(NULL) * 1000;
		carData.timestamp = msec;

		carData.carTrafficId = _input.Environment.SensorData.DetectedVehicles[i].CarTrafficID;
		carData.carType = _input.Environment.SensorData.DetectedVehicles[i].Type;
		carData.laneId = _input.Environment.SensorData.DetectedVehicles[i].LaneID;
		carData.length = _input.Environment.SensorData.DetectedVehicles[i].Length;

		// PositionUTM zusammenbauen:
		for (int j = 0; j < 6; ++j)
		{
			carData.positionUTM[j] = _input.Environment.SensorData.DetectedVehicles[i].PositionUTM[j];
		}

		carData.velocity = _input.Environment.SensorData.DetectedVehicles[i].Velocity;
		carData.width = _input.Environment.SensorData.DetectedVehicles[i].Width;

		scan.cars[i] = carData;
		
		
		/*cout << "Year: " << now.wYear << endl;
		cout << "Month: " << now.wMonth << endl;
		cout << "Day: " << now.wDay << endl;
		cout << "Hour: " << now.wHour << endl;
		cout << "Minute: " << now.wMinute << endl;
		cout << "Second: " << now.wSecond << endl;
		cout << "MilliSecond: " << now.wMilliseconds << endl;*/

		/*if (carData.carTrafficId != -1)
		{
			time_t currentTime = time(NULL);
			tm *now = localtime(&currentTime);
			
			cout << "Detektion um " << now->tm_hour << ":" << now->tm_min << ":" << now->tm_sec << " Uhr" << endl;
			cout << "Type: " << carData.carType << endl;
			cout << "CarTrafficID: " << carData.carTrafficId << endl;
			cout << "LaneID: " << carData.laneId << endl;
			cout << "PositionUTM: " << endl;
			cout << "  0: " << carData.positionUTM[0] << endl;
			cout << "  1: " << carData.positionUTM[1] << endl;
			cout << "  2: " << carData.positionUTM[2] << endl;
			cout << "  3: " << carData.positionUTM[3] << endl;
			cout << "  4: " << carData.positionUTM[4] << endl;
			cout << "  5: " << carData.positionUTM[5] << endl;
			cout << "Velocity: " << carData.velocity << endl;
			cout << "Length: " << carData.length << endl;
			cout << "Width: " << carData.width << endl;
			cout << endl;
		}*/
	}

	//cout << "Hello" << " " << "World!" << endl;

	sendUDPMessage("192.168.0.1", 5001, &scan, sizeof(scan));
}

/**
* The method executed before going into pause state of this module.
*/
void DStreamCars0::pause(void)
{

}

/**
* The method executed before resuming operation.
*/
void DStreamCars0::resume(void)
{

}

/**
 * Method that is triggered for periodic display.
 */
void DStreamCars0::display(void)
{
	
}

/* Optional: CAN callback. */
#ifdef DOMINION_APPLICATION_CAN
/**
 * Callback handler for the processing of an incoming CAN message.
 */
void DStreamCars0::receiveCANMessage(unsigned int id, const void *data, int size)
{

}
#endif

/* Optional: UDP callback. */
#ifdef DOMINION_APPLICATION_UDP
/**
 * Callback handler for the processing of an incoming UDP message.
  */
void DStreamCars0::receiveUDPMessage(unsigned int port, const void *data, int size)
{

}
#endif

/* AUTOMATICALLY GENERATED FILE. */
