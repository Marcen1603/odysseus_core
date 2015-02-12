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

BaslerCamera::BaslerCamera(std::string serialNumber)
{
	CTlFactory& tlFactory = CTlFactory::GetInstance();

	if (serialNumber == "")
	{
		// Create an instant camera object with the camera device found first.
		CDeviceInfo info;
		info.SetDeviceClass(Camera::DeviceClass());
		camera = new Camera(tlFactory.CreateFirstDevice(info));	
	}
	else
	{
        DeviceInfoList_t devices;
        if (tlFactory.EnumerateDevices(devices) == 0)
			throw std::exception("No basler cameras present!");

		camera = NULL;
		for (int i=0; i<devices.size(); i++)
		{
			if (devices[i].GetSerialNumber().c_str() == serialNumber)
			{
				camera = new Camera(CTlFactory::GetInstance().CreateDevice(devices[i]));	
				break;
			}
		}

		if (camera == NULL)
			throw std::exception(("Camera with serial number " + serialNumber + " not present!").c_str());
	}

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
	
	imageWidth = result->GetWidth();
	imageHeight = result->GetHeight();
	bufferSize = imageWidth*imageHeight*4;

	if (size < bufferSize) return false;
	if (buffer == NULL) return false;

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
		converter.OutputBitAlignment = OutputBitAlignment_MsbAligned;
		converter.OutputPaddingX = 0;
		converter.Convert(image, result);

		int bytesLeft = image.GetImageSize();//bufferSize;				

		struct Pixel
		{
			BYTE r, g, b;
		};

		Pixel* inBuffer = (Pixel*)image.GetBuffer();
		DWORD* outBuffer = (DWORD*)buffer;

		while (bytesLeft > 0)
		{
			Pixel* cur = inBuffer++;

			DWORD curDWORD = *((DWORD*)cur);
			curDWORD |= 0xFF000000;

			*(outBuffer++) = _byteswap_ulong(curDWORD);

			bytesLeft -= 3;
		}
	}

	return true;
}

BaslerCamera::~BaslerCamera()
{		
	delete camera;
}