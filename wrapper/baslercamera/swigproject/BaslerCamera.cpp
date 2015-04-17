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

class CMyImageEventHandler : public CImageEventHandler
{
	BaslerCamera* camera;

public:
	CMyImageEventHandler(BaslerCamera* camera) : camera(camera) {}

    virtual void OnImageGrabbed(CInstantCamera& camera, const CGrabResultPtr& grabResult) override
    {
		this->camera->onGrabbedInternal(grabResult);
    }
};

void BaslerCamera::start(OperationMode operationMode)
{
	if (camera != NULL) stop();

	this->operationMode = operationMode;

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

	GenApi::INodeMap& nodemap = camera->GetNodeMap();

	camera->Open();

	imageWidth  = GenApi::CIntegerPtr(nodemap.GetNode("Width"))->GetValue();
	imageHeight = GenApi::CIntegerPtr(nodemap.GetNode("Height"))->GetValue();
	
	if (operationMode == PUSH)
	{
		camera->Close();

        camera->RegisterConfiguration(new CSoftwareTriggerConfiguration, RegistrationMode_ReplaceAll, Cleanup_Delete);
		camera->RegisterImageEventHandler(new CMyImageEventHandler(this), RegistrationMode_Append, Cleanup_Delete);
		camera->StartGrabbing(GrabStrategy_OneByOne, GrabLoop_ProvidedByInstantCamera);
		trigger();
	}
	else
		camera->StartGrabbing();
}

void BaslerCamera::stop()
{
	if (camera != NULL)
	{
		camera->StopGrabbing();
		if (operationMode == PULL)
			camera->Close();

		delete camera;
		camera = NULL;
	}
}

void BaslerCamera::onGrabbedInternal(const CGrabResultPtr& grabResult)
{
	CPylonImage image;
	CImageFormatConverter converter;
	converter.OutputPixelFormat = PixelType_BGR8packed;
	converter.OutputBitAlignment = OutputBitAlignment_LsbAligned; // OutputBitAlignment_MsbAligned;
	converter.OutputPaddingX = lineLength - imageWidth * 3; // NumChannels = 3
	converter.Convert(image, grabResult);

	onGrabbed(image.GetBuffer(), image.GetImageSize());
}

void BaslerCamera::onGrabbed(void* buffer, long size)
{
}

bool BaslerCamera::trigger()
{
	if (camera->WaitForFrameTriggerReady( 100, TimeoutHandling_ThrowException))
	{
		camera->ExecuteSoftwareTrigger();
		return true;
	}
	else return false;
}

bool BaslerCamera::grabRGB8(void *buffer, long size, unsigned int timeOutMs)
{
	if (!camera->IsGrabbing()) return false;

	CBaslerGigEGrabResultPtr result;
	if (!camera->RetrieveResult(timeOutMs, result, TimeoutHandling_Return)) return false;
	if (!result->GrabSucceeded()) return false;
	
	imageWidth = result->GetWidth();
	imageHeight = result->GetHeight();
//	bufferSize = imageWidth*imageHeight*getImageChannels();

	if (size < bufferSize) return false;
	if (buffer == NULL) return false;

	CImageFormatConverter converter;
//	if (supportsBGRConversion)
	{
		converter.OutputPixelFormat = PixelType_BGR8packed;
		converter.OutputBitAlignment = OutputBitAlignment_LsbAligned; // OutputBitAlignment_MsbAligned;
		converter.OutputPaddingX = lineLength - imageWidth * 3; // NumChannels = 3
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
