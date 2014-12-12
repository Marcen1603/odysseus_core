#include <string>
#include <Windows.h>
#include <iostream>

#include "ImagerIPC2.h"
#include "OptrisCamera.h"

using namespace std;

static string imagerExeName = "Imager.exe";
static string imagerPath = "C:/Program Files (x86)/Optris GmbH/PI Connect/";
static string parameters = "/Invisible /IPC=Colors /Name=";

// This template generates sets of callback functions which are linked to an instance of OptrisCamera.
// The Optris callback functions require a different set of callback functions (different call address) for each camera, since the camera index is not given as a parameter
template <int IDX> 
class IPCCallbackFunctions
{
public:
	static const unsigned short Index = IDX;
	static OptrisCamera* Camera;

	static HRESULT WINAPI OnServerStopped(int reason)
	{
		Camera->OnServerStopped(reason);
		return 0;
	}

	static HRESULT WINAPI OnInitCompleted()
	{
		Camera->OnInitCompleted();
		return S_OK;
	}

	static HRESULT WINAPI OnFrameInit(int frameWidth, int frameHeight, int frameDepth)
	{
		Camera->OnFrameInit(frameWidth, frameHeight, frameDepth);
		return 0;
	}

	static HRESULT WINAPI OnNewFrame(void * pBuffer, FrameMetadata *pMetaData)
	{
		Camera->OnNewFrame(pBuffer, pMetaData);
		return 0;
	}

	static void SetCamera(OptrisCamera* cam)
	{
		Camera = cam;
		SetCallback_OnServerStopped(Index, OnServerStopped);
		SetCallback_OnFrameInit(Index, OnFrameInit);
		SetCallback_OnNewFrameEx(Index, OnNewFrame);
		SetCallback_OnInitCompleted(Index, OnInitCompleted);
	}
};

template <int IDX> OptrisCamera* IPCCallbackFunctions<IDX>::Camera = NULL;

wstring string2wstring(const string& str)
{
	size_t len = str.length();
	std::wstring wc(len, L'#');
	mbstowcs(&wc[0], str.c_str(), len);

	return wc;
}

void OptrisCamera::start()
{
	// Start application
	string imagerExe = imagerPath + imagerExeName;

	char startParameters[256];
	sprintf(startParameters, "%s%s", parameters.c_str(), instanceName.c_str());

	STARTUPINFO si;
    PROCESS_INFORMATION pi;

    ZeroMemory( &si, sizeof(si));
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi));

//	CreateProcess(imagerExe.c_str(), startParameters, NULL, NULL, FALSE, 0, NULL, imagerPath.c_str(), &si, &pi);
	
	// Connect IPC	#
	wstring wc = string2wstring(instanceName);

	int tries = 20;

	cout << "Initialize IPC..." << endl;
	HRESULT hr;
	do
	{
		Sleep(500);
		hr = InitNamedImagerIPC(instanceID, (wchar_t*)wc.c_str());
		tries--;
		cout << tries << " tries left..." << endl;
	}
	while (FAILED(hr) && (tries > 0));

	if(FAILED(hr))
	{
		string msg = "InitImagerIPC failed: hr " + to_string((long long)hr);
		throw exception(msg.c_str());
	}

	// Add additional cameras here if necessary
/*	switch (instanceID)
	{
	case 0: IPCCallbackFunctions<0>::SetCamera(this); break;
	case 1: IPCCallbackFunctions<0>::SetCamera(this); break;
	case 2: IPCCallbackFunctions<0>::SetCamera(this); break;
	case 3: IPCCallbackFunctions<0>::SetCamera(this); break;
	default: throw exception("instanceID > 3");
	}*/

	hr = RunImagerIPC(instanceID);
	if(FAILED(hr))
	{
		string msg = "RunImagerIPC failed: hr " + to_string((long long)hr);
		throw exception(msg.c_str());
	}	

	// Call GetMessage several times, otherwise PI Connect hangs
	MSG msg;
	for (int i=0;i<5;i++)
	GetMessage(&msg, NULL, 0, 0);

	bool frameInit = false;
	bool initCompleted = false;
	
	while (!frameInit || !initCompleted)
	{
		WORD State = GetIPCState(0, true);

		if (!initCompleted && (State & IPC_EVENT_INIT_COMPLETED))
		{
			initCompleted = true;
		}

		if (!frameInit && (State & IPC_EVENT_FRAME_INIT))
		{			
			HRESULT hr = GetFrameConfig(0, &Width, &Height, &Depth);
			if(SUCCEEDED(hr))
				frameInit = true;
		}

		// TODO: Implement sleep, when both conditions aren't met, and a timeout after which an exception is thrown

	}
	cout << "IPC initialized" << endl;
}

void OptrisCamera::stop()
{
	ReleaseImagerIPC(instanceID);

	string imagerExe = imagerPath + imagerExeName;

	char stopParameters[256];
	sprintf(stopParameters, "%s%s /Close", parameters.c_str(), instanceName.c_str());

	STARTUPINFO si;
    PROCESS_INFORMATION pi;

    ZeroMemory( &si, sizeof(si));
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi));

//	CreateProcess(imagerExe.c_str(), stopParameters, NULL, NULL, false, 0, NULL, imagerPath.c_str(), &si, &pi);
}

void OptrisCamera::OnServerStopped(int reason)
{
	cout << "OnServerStopped reason = " << reason << endl;
}

void OptrisCamera::OnInitCompleted()
{
	cout << "OnInitCompleted" << endl;
}

void OptrisCamera::OnFrameInit(int frameWidth, int frameHeight, int frameDepth)
{
	cout << "OnFrameInit w = " << frameWidth << ", h = " << frameHeight << ", d = " << frameDepth << endl;
}

void OptrisCamera::OnNewFrame(void* pBuffer, FrameMetadata* pMetaData)
{
	cout << "OnNewFrame" << endl;
}

/*bool OptrisCamera::grabRGB8(unsigned int timeOutMs)
{
	if (!camera->IsGrabbing()) return false;

	WORD State = GetIPCState(0, true);
	if(State & IPC_EVENT_SERVER_STOPPED)
		OnServerStopped(0);


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
}*/

int OptrisCamera::getImageWidth()
{
	return 0; //currentImage->GetWidth();
}

int OptrisCamera::getImageHeight()
{
	return 0; //currentImage->GetHeight();
}

/*void OptrisCamera::getImageData(int data[])
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
		curDWORD &= 0x00FFFFFF;

		*(data++) = curDWORD;

//		*(data++) = *((DWORD*)(buffer++)) >> 8;
		size -= 3;
	}

//	memcpy(data, currentImage->GetBuffer(), getImageSize());
} */

OptrisCamera::~OptrisCamera()
{		
}