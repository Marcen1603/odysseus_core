#include <string>
#include <Windows.h>
#include <pylon/PylonIncludes.h>
#include <pylon/gige/BaslerGigEInstantCamera.h>

#include "BaslerCamera.h"

using namespace Pylon;
using std::cout;
using std::endl;

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

	GenApi::INodeMap& nodemap = camera->GetNodeMap();

	camera->Open();

	imageWidth  = GenApi::CIntegerPtr(nodemap.GetNode("Width"))->GetValue();
	imageHeight = GenApi::CIntegerPtr(nodemap.GetNode("Height"))->GetValue();
	ticksPerSecond = GenApi::CIntegerPtr(nodemap.GetNode("GevTimestampTickFrequency"))->GetValue();
	GenApi::CCommandPtr(nodemap.GetNode("GevTimestampControlReset"))->Execute();	
	
	if (operationMode == PUSH)
	{
		camera->Close();

        camera->RegisterConfiguration(new CSoftwareTriggerConfiguration, RegistrationMode_ReplaceAll, Cleanup_Delete);
		camera->RegisterImageEventHandler(new CMyImageEventHandler(this), RegistrationMode_Append, Cleanup_Delete);
		camera->StartGrabbing(GrabStrategy_OneByOne, GrabLoop_ProvidedByInstantCamera);
		trigger();
	}
	else
	{
		camera->StartGrabbing();

/*		CBaslerGigEGrabResultPtr result;
		if (!camera->RetrieveResult(1000, result, TimeoutHandling_Return) || !result->GrabSucceeded()) 
			throw std::exception("Could open camera, but grabbing failed!");

		cout << "Grab on startup ok! w = %d, h = %d" << result->GetWidth() << result->GetHeight() << endl;*/
	}
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

	onGrabbed((double)grabResult->GetTimeStamp() / ticksPerSecond, image.GetBuffer(), image.GetImageSize());
}

void BaslerCamera::onGrabbed(double timeStamp, void* buffer, long size)
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

	lastTimeStamp = (double)result->GetTimeStamp() / ticksPerSecond;

	if (buffer == NULL) return true;

	imageWidth = result->GetWidth();
	imageHeight = result->GetHeight();		

	CImageFormatConverter converter;
	converter.OutputPixelFormat = PixelType_BGR8packed;
	converter.OutputBitAlignment = OutputBitAlignment_LsbAligned; // OutputBitAlignment_MsbAligned;
	converter.OutputPaddingX = lineLength - imageWidth * 3; // NumChannels = 3
	converter.Convert(buffer, size, result);

	return true;
}
