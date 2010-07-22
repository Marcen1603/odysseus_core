
/* AUTOMATICALLY GENERATED FILE, DO NOT EDIT! */

/**
 * @file AStreamCars0Base.h
 *
 * @author Project Group StreamCars, Uni Oldenburg, <pg-streamcars@polaris-neu.offis.uni-oldenburg.de>
 *
 * File generated at TODO.
 * This is the abstract base class for the application StreamCars0,
 * which includes all necessary mappings for taking part in
 * the Dominion.
 *
 * Template made by Jan Gacnik, <jan.gacnik@dlr.de>.
 * Modified for Dominion 2 by Andreas Richter <andreas.richter@dlr.de>.
 *
 * Copyright (C) 2009 DLR TS. All rights reserved.
 */

#ifndef __AStreamCars0BASE_H__
#define __AStreamCars0BASE_H__

#include "DStreamCars0Views.h"

#ifdef DOMINION_APPLICATION_ISO_C
#include "StreamCars0/include/DStreamCars0.h"
#endif

#define AStreamCars0BASE_ADRESSES_MAX_SIZE 0x20

#ifdef DOMINION_SFUNCTION
class DStreamCars0SFunction;
#else
#include "DominionMINION/include/AApplication.h"
#endif

/* Handle CAN communication configuration. */
#ifdef DOMINION_APPLICATION_CAN
#if defined(DOMINION_APPLICATION_CAN_VECTOR)
#include "DominionGATE/include/win32/DTransceiverCANVector.h"
#define DTransceiverCAN DTransceiverCANVector
#elif defined(DOMINION_APPLICATION_CAN_PEAK)
#include "DominionGATE/include/win32/DTransceiverCANPeak.h"
#define DTransceiverCAN DTransceiverCANPeak
#else
#error "No CAN transceiver type specified. Please define either DOMINION_APPLICATION_CAN_VECTOR or DOMINION_APPLICATION_CAN_PEAK."
#endif
#endif

/* Install UDP handler. */
#ifdef DOMINION_APPLICATION_UDP
#include "DominionGATE/include/DTransceiverUDP.h"
#endif

#ifdef DOMINION_APPLICATION_CAN
class DStreamCars0CANCallback : public ATransceiver::ARecvDataCallbackFunctor
{
public:
	  void call(void *recvData, int numBytes, unsigned int messageId,
		    const struct timeval *TimeStamp, ATransceiver* t);
};
#endif

#ifdef DOMINION_APPLICATION_UDP
class DStreamCars0UDPCallback : public ATransceiver::ARecvDataCallbackFunctor
{
public:
	  void call(void *recvData, int numBytes, unsigned int port,
		    const struct timeval *TimeStamp, ATransceiver* t);
};
#endif


/**
 * @class AStreamCars0Base
 *
 * Copyright (C) 2009 DLR TS. All rights reserved.
 */
#ifdef DOMINION_SFUNCTION
class AStreamCars0Base
{
public:
#else
class AStreamCars0Base : public AApplication
{
protected:
#endif

#ifdef DOMINION_APPLICATION_ISO_C
    /**
     * State for the ISO C implementation.
     */
    TStreamCars0 _cState;
		#endif

		/**
		* Input data.
		*/
		TInput _input;
		/**
		* Output data.
		*/
		TOutput _output;
		/**
		* Parameter data.
		*/
		TParameter _parameter;

		/**
		* Input meta data.
		*/
		TInputMeta _inputMeta;
		/**
		* Output meta data.
		*/
		TOutputMeta _outputMeta;
		/**
		* Parameter meta data.
		*/
		TParameterMeta _parameterMeta;

		/**
		* Timer state (dispatchRun).
		*/
		TTimerState _timerState;

public:
		/**
		* Constructor.
		*/
		AStreamCars0Base();
    /**
     * Destructor.
     */
    virtual ~AStreamCars0Base();
    
private:
    /**
     * Make the mapping from the Dominion HADES space into user
     * view specific address space.
     */
    void doMapping(void);
    /**
     * Make the mapping from the Dominion HADES space into user
     * input data view specific address space.
     */
    void doMappingInput(void);
    /**
     * Make the mapping from the Dominion HADES space into user
     * output data view specific address space.
     */
    void doMappingOutput(void);
    /**
     * Make the mapping from the Dominion HADES space into user
     * parameter data view specific address space.
     */
    void doMappingParameter(void);
    /**
     * Start external communication.
     */
    void startExternalCommunication(void);
    /**
     * Stop external communication.
     */
    void stopExternalCommunication(void);
    
public:
    /**
     * Print the current state of the input data to stdout.
     */
    void printInput(void);
    /**
     * Print the current state of the output data to stdout.
     */
    void printOutput(void);
    /**
     * Print the current state of the parameter data to stdout.
     */
    void printParameter(void);
    /**
     * The dispatch routine, called by the event trigger.
     */
    void dispatchRun(const TTimerState *timerState);
    /**
     * Another dispatch routine, used for command line debugging.
     */
    void dispatchDisplay(const TTimerState *timerState);
    
#ifdef DOMINION_APPLICATION_ISO_C

    /**
     * For ISO C implementation: Call the C init function.
     */
    void init(void)
    {
        /* Hand over the state to the application. */
        DStreamCars0_setState(&_cState);
        /* Call the C version of the init function. */
        DStreamCars0_init();
    }
    /**
     * For ISO C implementation: Call the C exit function.
     */
    void exit(void)
    {
        /* Call the C version of the exit function. */
        DStreamCars0_exit();
    }
    /**
     * For ISO C implementation: Call the C runnable function.
     */
    void run(void)
    {
        /* Update! Input, parameter later. */
        _cState.input = _input;
        /* Call the C version of the runnable function. */
        DStreamCars0_run();
        /* Update! Output! */
        _output = _cState.output;
    }
    /**
     * For ISO C implementation: Call the C pause function.
     */
    void pause(void)
    {
        /* Call the C version of the pause function. */
        DStreamCars0_pause();
    }
    /**
     * For ISO C implementation: Call the C resumt function.
     */
    void resume(void)
    {
        /* Call the C version of the resume function. */
        DStreamCars0_resume();
    }
    /**
     * For ISO C implementation: Call the C display function.
     */
    void display(void)
    {
        /* Call the C version of the display function. */
        DStreamCars0_display();
    }
    
#else

    /**
     * Method that shall be used for periodic command line
     * debugging.
     */
    virtual void display(void) = 0x0;
    
#endif /* DOMINION_APPLICATION_ISO_C */

#ifdef DOMINION_APPLICATION_CAN
protected:
    /**
     * Transceiver for CAN communication.
     */
    DTransceiverCAN *_can;
private:
    /**
     * Callback functor for the CAN Transceiver.
     */
    DStreamCars0CANCallback _canCallback;
public:
#ifdef DOMINION_APPLICATION_ISO_C
    /**
     * For ISO C implementation: Call the callback.
     */
    inline void receiveCANMessage(unsigned int id, const void *data, int size)
    {
        DStreamCars0_receiveCANMessage(id, data, size);
    }
#else
    /**
     * Callback routine for the CAN Transceiver that is to be implemented.
     */
    virtual void receiveCANMessage(unsigned int id, const void *data, int size) = 0x0;
#endif
    /**
     * Routine that is used to deliver CAN messages.
     */
    inline void sendCANMessage(unsigned int id, const void *data, int size)
    {
        _can->sendDataCAN(id, data, size);
    }
#endif /* DOMIONION_APPLICATION_CAN */

#ifdef DOMINION_APPLICATION_UDP
protected:
    /**
     * Transceiver for UDP communication.
     */
    DTransceiverUDP *_udp;
private:
    /**
     * Callback functor for the UDP Transceiver.
     */
    DStreamCars0UDPCallback _udpCallback;
public:
    /**
     * Callback routine for the UDP Transceiver that is to be implemented.
     */
    virtual void receiveUDPMessage(unsigned int port, const void *data, int size) = 0x0;
    /**
     * Routine that is used to deliver a UDP Message.
     */
    inline void sendUDPMessage(unsigned int IP, unsigned int port, const void *data, int size)
    {
        _udp->sendDataUDP(IP, port, data, size);
    }
    /**
     * Routine that is used to deliver a UDP Message.
     */
    inline void sendUDPMessage(const char *IP, unsigned int port, const void *data, int size)
    {
        _udp->sendDataUDP((char *)IP, port, data, size);
    }
#endif /* DOMINION_APPLICATION_UDP */

#ifdef DOMINION_APPLICATION_KEYBOARD
#ifdef DOMINION_APPLICATION_ISO_C
    void handleKeyboardInput(int key)
    {
        DStreamCars0_handleKeyboardInput(key);
    }
#else
public:
    virtual void handleKeyboardInput(int key) = 0x0;
#endif /* DOMINION_APPLICATION_ISO_C */
#else
public:
    void handleKeyboardInput(int key) { };
#endif /* DOMINION_APPLICATION_KEYBOARD */

#ifdef DOMINION_APPLICATION_AUTOCHECK
public:
    void checkInputData(void);
    void checkOutputData(void);
#endif /* DOMINION_APPLICATION_AUTOCHECK */

};

/**
 * @class DStreamCars0KeyHandler
 * Keyboard handler for this class.
 */
class DStreamCars0KeyHandler : public AApplicationKeyHandler
{
public:
	void userHandle(EKey *k)
	{
		AStreamCars0Base *a = (AStreamCars0Base *)getApplication();
    a->handleKeyboardInput(k->getKey());
	}
};


#endif /* DOMINION_SFUNCTION */

/* AUTOMATICALLY GENERATED FILE, DO NOT EDIT! */
