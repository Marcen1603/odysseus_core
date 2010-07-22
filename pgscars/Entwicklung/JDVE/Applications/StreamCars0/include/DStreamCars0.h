/* AUTOMATICALLY GENERATED FILE. */

/**
 * @file DStreamCars0.h
 *
 * @author Project Group StreamCars, Uni Oldenburg, <pg-streamcars@polaris-neu.offis.uni-oldenburg.de>
 *
 * Copyright (C) 2007 DLR FS. All rights reserved.
 */

#ifndef __StreamCars0_H__
#define __StreamCars0_H__

#include "StreamCars0/include/auto/DStreamCars0Views.h"
#include "StreamCars0/include/auto/AStreamCars0Base.h"

struct CarData{
	SInt32 timestamp;
	SInt32 carType;
	SInt32 carTrafficId;
	SInt32 laneId;
	Float64 positionUTM[6];
	Float32 velocity;
	Float32 length;
	Float32 width;
};

struct WholeScan {
	CarData cars[50];
};

/**
 * @class DStreamCars0
 *
 * @author Project Group StreamCars, Uni Oldenburg, <pg-streamcars@polaris-neu.offis.uni-oldenburg.de>
 *
 * Copyright (C) 2009 DLR TS. All rights reserved.
 */
class DStreamCars0 : public AStreamCars0Base
{
public:
    /**
     * Constructor for the DStreamCars0 class.
     */
    DStreamCars0() {  }
    /**
     * Destructor for the DStreamCars0 class.
     */
    ~DStreamCars0() {  }
    /**
    * The actual runnable function of this very module.
    */
    void run(void);
    /**
    * The initialization function of this module.
    */
    void init(void);
    /**
    * The deinitialization function of this module.
    */
    void exit(void);
    /**
    * The method executed before going into pause state of this module.
    */
    void pause(void);
    /**
    * The method executed before resuming operation.
    */
    void resume(void);
    /**
     * Display function, triggered periodically.
     */
    void display(void);
    /* Optional: CAN callback. */
    #ifdef DOMINION_APPLICATION_CAN
    /**
    * Callback handler for the processing of an incoming CAN message.
    */
    void receiveCANMessage(unsigned int id, const void *data, int size);
    #endif
    /* Optional: UDP callback. */
    #ifdef DOMINION_APPLICATION_UDP
    /**
    * Callback handler for the processing of an incoming UDP message.
    */
    void receiveUDPMessage(unsigned int port, const void *data, int size);
    #endif
};

#endif /* __StreamCars0_H__ */

/* AUTOMATICALLY GENERATED FILE, DO NOT EDIT! */
