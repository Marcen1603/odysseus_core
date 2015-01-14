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

	if (len > 0)
	{
		std::wstring wc(len, L'#');
		mbstowcs(&wc[0], str.c_str(), len);
		return wc;
	}
	else
		return L"";
}

OptrisCamera::OptrisCamera(const std::string& instanceName, const std::string& ethernetAddr) throw(std::exception)
{
	this->instanceName = instanceName;
	this->ethernetAddr = ethernetAddr;

	instanceID = 0;
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

	bool frameInit = false;
	bool initCompleted = false;
	
	while (!frameInit || !initCompleted)
	{
		WORD State = GetIPCState(instanceID, true);

		if (!initCompleted && (State & IPC_EVENT_INIT_COMPLETED))
		{
			initCompleted = true;
		}

		if (!frameInit && (State & IPC_EVENT_FRAME_INIT))
		{			
			HRESULT hr = GetFrameConfig(instanceID, &Width, &Height, &Depth);
			if(SUCCEEDED(hr))
				frameInit = true;
		}

		if (!initCompleted && !frameInit)
		{
			Sleep(200);
			// TODO: Implement timeout after which an exception is thrown
		}
		ImagerIPCProcessMessages(instanceID);
	}

	if (TIPCMode(GetIPCMode(0)) != ipcTemps)
		throw exception("IPC Mode must be set to \"Temperatures\"");

	if (Depth != 2)
		throw exception("Depth != 2, not implemented!");


	cout << "IPC initialized" << endl;

	char* buffer = new char[getBufferSize()];
	for (int i=0;i<2;i++)
	{
		bool success = grabImage(buffer, getBufferSize(), 1000);	

		if (success) 
			cout << "Test grabbing successful: " << ((short*)buffer)[0] << endl;
		else
			cout << "Test grabbing failed!" << endl;
	}
	delete buffer;
}

void OptrisCamera::stop()
{
	ReleaseImagerIPC(instanceID);

//	CloseApplication(instanceID);

/*	string imagerExe = imagerPath + imagerExeName;

	char stopParameters[256];
	sprintf(stopParameters, "%s%s /Close", parameters.c_str(), instanceName.c_str());

	STARTUPINFO si;
    PROCESS_INFORMATION pi;

    ZeroMemory( &si, sizeof(si));
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi));*/

//	CreateProcess(imagerExe.c_str(), stopParameters, NULL, NULL, false, 0, NULL, imagerPath.c_str(), &si, &pi);
}

bool OptrisCamera::grabImage(void *buffer, long size, unsigned int timeOutMs)
{
//	cout << "Current Thread: " << GetCurrentThreadId() << endl;

	if (size < getBufferSize()) return false;

	WORD State = GetIPCState(instanceID, true);
	if(State & IPC_EVENT_SERVER_STOPPED) return false;

	bool success = false;
	FrameMetadata metadata;
	if(GetFrameQueue(instanceID))
		success = SUCCEEDED(GetFrame(instanceID, (WORD)timeOutMs, buffer, getBufferSize(), &metadata));

	ImagerIPCProcessMessages(instanceID);

	return success;
}

OptrisCamera::~OptrisCamera()
{		
}