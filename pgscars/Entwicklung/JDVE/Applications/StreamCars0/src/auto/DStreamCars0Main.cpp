/* AUTOMATICALLY GENERATED FILE, DO NOT EDIT! */

/**
 * DStreamCars0Main.cpp
 */

#include "DominionFUST/include/Macros.h"

#ifdef DOMINION_MACOSX
#include <unistd.h>
#endif

#ifdef DOMINION_WIN32
#include <winsock2.h>
#endif

#include "DominionFUST/include/AThread.h"
#include "DominionTIMEKEEPER/include/DTimer.h"

#include "StreamCars0/include/auto/AStreamCars0Base.h"
#include "StreamCars0/include/DStreamCars0.h"

#ifdef DOMINION_WIN32_MOBILE
#define mainEntryPoint wmain
#else
#define mainEntryPoint main
#endif

extern AApplication *gApp;

#ifdef DOMINION_APPLICATION_ISO_C
void DStreamCars0_sendCANMessage(TStreamCars0*_cStruct, unsigned int dest, const void *data, int size)
{
    ((AStreamCars0Base *)gApp)->sendCANMessage(dest, data, size);
}
#endif

int mainEntryPoint(int argc, char *argv[])
{
    int runInterval = 1000 * 1000;
    int displayInterval = 1000 * 1000;
    AStreamCars0Base *app;
    
// when using AThread-class attach and detach for static lib is done automatically
// So we should change to AThread
	AThread::attachToWin32();

#ifdef DOMINION_APPLICATION_ISO_C
    app = new AStreamCars0Base();
#else
	app = new DStreamCars0();
#endif

    gApp = app;

	app->parseArgs(argc, argv);
    

  runInterval = 20000;
  
	ApplicationController controller(app);
	controller.setRunInterval(runInterval);
	controller.setDisplayInterval(displayInterval);
	controller.main();

//	AThread::detachFromWin32();

	return 0;
}

/* AUTOMATICALLY GENERATED FILE, DO NOT EDIT! */
