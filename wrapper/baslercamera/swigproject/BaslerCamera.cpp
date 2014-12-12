#include <string>
#include <Windows.h>
#include <pylon/PylonIncludes.h>
#include <pylon/gige/BaslerGigEInstantCamera.h>

#include "BaslerCamera.h"

using namespace Pylon;

bool BaslerCamera::isInitialized = false;

void BaslerCamera::initializeSystem()
{
	if (!isInitialized)
	{
		Pylon::PylonInitialize();
		isInitialized = true;
	}
}

void BaslerCamera::shutDownSystem()
{
	if (isInitialized)
	{
		Pylon::PylonTerminate();
		isInitialized = false;
	}
}

bool BaslerCamera::isSystemInitialized()
{
	return isInitialized;
}

// ******************************************************************

BaslerCamera::BaslerCamera(std::string ethernetAddress)
{
	// TODO: Create camera by ethernet address
    CDeviceInfo info;
    info.SetDeviceClass( Camera::DeviceClass());

	// Create an instant camera object with the camera device found first.
	camera = new Camera(CTlFactory::GetInstance().CreateFirstDevice(info));	

	currentImage = new CPylonImage;
	start();
	grabRGB8(1000);
	stop();
}

void BaslerCamera::start()
{
	camera->Open();
	camera->StartGrabbing();
}

void BaslerCamera::stop()
{
	camera->StopGrabbing();
	camera->Close();
}

bool BaslerCamera::grabRGB8(unsigned int timeOutMs)
{
	if (!camera->IsGrabbing()) return false;

	CBaslerGigEGrabResultPtr result;
	if (!camera->RetrieveResult(timeOutMs, result, TimeoutHandling_Return)) return false;
	if (!result->GrabSucceeded()) return false;

	CImageFormatConverter converter;

	converter.OutputPixelFormat = PixelType_RGB8packed;
	converter.OutputBitAlignment = OutputBitAlignment_MsbAligned;
	converter.Convert(*currentImage, result);

	return true;
}

int BaslerCamera::getImageSize()
{
	return (int)currentImage->GetAllocatedBufferSize();
}

int BaslerCamera::getImageWidth()
{
	return currentImage->GetWidth();
}

int BaslerCamera::getImageHeight()
{
	return currentImage->GetHeight();
}

void BaslerCamera::getImageData(int data[])
{
	struct Pixel
	{
		BYTE r, g, b;
	};

	int size = getImageSize();		
	Pixel* buffer = (Pixel*)currentImage->GetBuffer();

	while (size > 0)
	{
		Pixel* cur = buffer++;

		DWORD curDWORD = *((DWORD*)cur);
		curDWORD |= 0xFF000000;

		*(data++) = _byteswap_ulong(curDWORD);

		size -= 3;
	}
}

BaslerCamera::~BaslerCamera()
{		
	delete camera;
	delete currentImage;
}