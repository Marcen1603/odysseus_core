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

void BaslerCamera::start()
{
	if (camera != NULL) stop();

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

	if (!CImageFormatConverter::IsSupportedOutputFormat(PixelType_BGR8packed))
		throw std::exception("Conversion to BGR not implemented!");

/*	supportsBGRConversion = CImageFormatConverter::IsSupportedOutputFormat(PixelType_BGR8packed);
	std::cout << "supportsBGRConversion = " << supportsBGRConversion << std::endl;*/

	camera->Open();
	camera->StartGrabbing();

	grabRGB8(NULL, 0, 0, 1000);
}

void BaslerCamera::stop()
{
	if (camera != NULL)
	{
		camera->StopGrabbing();
		camera->Close();

		delete camera;
		camera = NULL;
	}
}

bool BaslerCamera::grabRGB8(void *buffer, long size, int lineLength, unsigned int timeOutMs)
{
	if (!camera->IsGrabbing()) return false;

	CBaslerGigEGrabResultPtr result;
	if (!camera->RetrieveResult(timeOutMs, result, TimeoutHandling_Return)) return false;
	if (!result->GrabSucceeded()) return false;
	
	imageWidth = result->GetWidth();
	imageHeight = result->GetHeight();
	bufferSize = imageWidth*imageHeight*getImageChannels();

	if (size < bufferSize) return false;
	if (buffer == NULL) return false;

	CImageFormatConverter converter;
//	if (supportsBGRConversion)
	{
		converter.OutputPixelFormat = PixelType_BGR8packed;
		converter.OutputBitAlignment = OutputBitAlignment_LsbAligned; // OutputBitAlignment_MsbAligned;
		converter.OutputPaddingX = lineLength - imageWidth * getImageChannels();
		converter.Convert(buffer, size, result);
	}
/*	else
	{
		// TODO: Code not tested
		CPylonImage image;
		converter.OutputPixelFormat = PixelType_RGB8packed;
		converter.OutputBitAlignment = OutputBitAlignment_LsbAligned; //OutputBitAlignment_MsbAligned;
		converter.OutputPaddingX = 0;
		converter.Convert(image, result);

		int bytesLeft = image.GetImageSize();

		struct PixelRGB
		{
			BYTE r, g, b;
		};

		struct PixelBGR
		{
			BYTE b, g, r;
		};

		PixelRGB* inBuffer = (PixelRGB*)image.GetBuffer();
		PixelBGR* outBuffer = (PixelBGR*)buffer;

		while (bytesLeft > 0)
		{
			PixelRGB* cur = inBuffer++;

			outBuffer->r = cur->r;
			outBuffer->g = cur->g;
			outBuffer->b = cur->b;

			outBuffer++;

			bytesLeft -= 3;
		}
	}*/

	return true;
}
