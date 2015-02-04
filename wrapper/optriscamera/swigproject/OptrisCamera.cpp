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


// This template will count down from "Cur" to 0 to find "index" and set the right callback functions.
// This avoids a big switch-case statement
template <int Cur> struct SetCameraCallbackTemplate
{
	static void Set(int index, OptrisCamera* camera)
	{
		if (index == Cur)
			IPCCallbackFunctions<Cur>::SetCamera(camera);
		else
			SetCameraCallbackTemplate<Cur-1>::Set(index, camera);
	}
};

template <> struct SetCameraCallbackTemplate<0>
{
	static void Set(int index, OptrisCamera* camera)
	{
		IPCCallbackFunctions<0>::SetCamera(camera);
	}
};

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

class IPCThread
{
	std::string errorStr;
	bool running, error;
	HANDLE threadHandle;
	OptrisCamera* camera;
	string instanceName;
	WORD instanceID;

	void ThreadInit()
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

		SetCameraCallbackTemplate<32>::Set(instanceID, camera);

		hr = RunImagerIPC(instanceID);
		if(FAILED(hr))
		{
			string msg = "RunImagerIPC failed: hr " + to_string((long long)hr);
			throw exception(msg.c_str());
		}	

		cout << "IPC initialized" << endl;
	}

	void ThreadExit()
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

	static DWORD WINAPI ThreadProc(LPVOID param)
	{
		IPCThread* ipcThread = (IPCThread*)param;

		try
		{
			ipcThread->ThreadInit();
		}
		catch (std::exception& e)
		{
			ipcThread->errorStr = e.what();
			ipcThread->error = true;
			return -1;
		}

		ipcThread->running = true;
		while (ipcThread->running)
		{
			ImagerIPCProcessMessages(ipcThread->instanceID);
		}

		ipcThread->ThreadExit();

		return 0;
	}

public:
	IPCThread(OptrisCamera* camera, string instanceName, WORD instanceID) : camera(camera), instanceName(instanceName), instanceID(instanceID)
	{
		running = false;
		error = false;
		threadHandle = CreateThread(NULL, 0, ThreadProc, this, 0, NULL);

		while (!running && !error)
		{
		}

		if (error)
			throw std::exception(errorStr.c_str());
	}

	~IPCThread()
	{
		running = false;
		WaitForSingleObject(threadHandle, 1000);
	}

	bool isRunning() const { return running; }
};

OptrisCamera::OptrisCamera(const std::string& instanceName, const std::string& ethernetAddr) throw(std::exception)
{
	this->instanceName = instanceName;
	this->ethernetAddr = ethernetAddr;

	instanceID = 0;
	currentBuffer = NULL;
}

OptrisCamera::~OptrisCamera()
{	
}

void OptrisCamera::start()
{
	initCompleted = false;
	frameInit = false;

#ifdef BUSY_WAIT
	state = IDLE;
#else
	frameReadEvent = CreateEvent(NULL, false, true, "FrameReadEvent");
	newFrameEvent = CreateEvent(NULL, false, false, "NewFrameEvent");
#endif

	// Start IPC thread
	ipcThread = new IPCThread(this, instanceName, instanceID);

	while (!initCompleted || !frameInit)
	{
	}

	if (TIPCMode(GetIPCMode(instanceID)) != ipcTemps)
		throw exception("IPC Mode must be set to \"Temperatures\"");

	if (Depth != 2)
		throw exception("Depth != 2, not implemented!");
}

void OptrisCamera::stop()
{
	delete ipcThread;
	ipcThread = NULL;	

#ifndef BUSY_WAIT
	CloseHandle(frameReadEvent);
	CloseHandle(newFrameEvent);
#endif
}

void OptrisCamera::OnServerStopped(int reason)
{
	stop();
}

void OptrisCamera::OnInitCompleted()
{
	initCompleted = true;
}

void OptrisCamera::OnFrameInit(int frameWidth, int frameHeight, int frameDepth)
{
	Width = frameWidth;
	Height = frameHeight;
	Depth = frameDepth;

	currentBuffer = NULL;

	frameInit = true;	
}

void OptrisCamera::OnNewFrame(void* pBuffer, FrameMetadata *pMetaData)
{
	if (!initCompleted || !frameInit) return;

#ifdef BUSY_WAIT
	while (state != IDLE)
	{
	}
#else
	WaitForSingleObject(frameReadEvent, INFINITE);
	ResetEvent(frameReadEvent);
#endif

//	cout << "Frame " << *((short*)pBuffer) << " in buffer " << std::hex << pBuffer << std::dec << endl;
	currentBuffer = pBuffer;

#ifdef BUSY_WAIT
	state = AVAILABLE;
	while (state != IDLE)
	{
	}
#else
	SetEvent(newFrameEvent);
	WaitForSingleObject(frameReadEvent, INFINITE);
#endif
}

bool OptrisCamera::grabImage(void *buffer, long size, unsigned int timeOutMs) throw(std::exception)
{
	if (size < getBufferSize()) throw std::exception("Buffer is too small");
	if (!initCompleted || !frameInit) return false;

#ifdef BUSY_WAIT
	while (state != AVAILABLE)
	{
	}	
#else
	if (WaitForSingleObject(newFrameEvent, timeOutMs) == WAIT_TIMEOUT) return false;
	ResetEvent(newFrameEvent);
#endif

	memcpy(buffer, currentBuffer, getBufferSize());

#ifdef BUSY_WAIT
	state = IDLE;
#else
	SetEvent(frameReadEvent);
#endif

	return true;
}