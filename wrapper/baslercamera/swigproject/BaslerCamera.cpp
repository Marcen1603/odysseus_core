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

	supportsRGBAConversion = CImageFormatConverter::IsSupportedOutputFormat(PixelType_RGBA8packed);

	start();
	grabRGB8(NULL, 0, 1000);
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

bool BaslerCamera::grabRGB8(void *buffer, long size, unsigned int timeOutMs)
{
	if (!camera->IsGrabbing()) return false;

	CBaslerGigEGrabResultPtr result;
	if (!camera->RetrieveResult(timeOutMs, result, TimeoutHandling_Return)) return false;
	if (!result->GrabSucceeded()) return false;

	bufferSize = (int)result->GetImageSize();
	imageWidth = result->GetWidth();
	imageHeight = result->GetHeight();

	if (buffer != NULL)
	{
		if (size < bufferSize) return false;

		CImageFormatConverter converter;
		if (supportsRGBAConversion)
		{
			converter.OutputPixelFormat = PixelType_RGBA8packed;
			converter.OutputBitAlignment = OutputBitAlignment_LsbAligned; // OutputBitAlignment_MsbAligned;
			converter.OutputPaddingX = 0;
			converter.Convert(buffer, size, result);
		}
		else
		{
			CPylonImage image;
			converter.OutputPixelFormat = PixelType_RGB8packed;
			converter.OutputBitAlignment = OutputBitAlignment_LsbAligned; // OutputBitAlignment_MsbAligned;
			converter.OutputPaddingX = 0;
			converter.Convert(image, result);

			struct Pixel
			{
				BYTE r, g, b;
			};

			int bytesLeft = bufferSize;	

			Pixel* inBuffer = (Pixel*)image.GetBuffer();
			DWORD* outBuffer = (DWORD*)buffer;

			while (bytesLeft > 0)
			{
				Pixel* cur = inBuffer++;

				DWORD curDWORD = *((DWORD*)cur);
				curDWORD |= 0xFF000000;

				*(outBuffer++) = curDWORD; //_byteswap_ulong(curDWORD);

				bytesLeft -= 3;
			}
		}
	}

	return true;
}

BaslerCamera::~BaslerCamera()
{		
	delete camera;
}