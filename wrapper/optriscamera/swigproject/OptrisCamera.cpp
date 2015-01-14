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
	bool running;
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

		ipcThread->ThreadInit();

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
		threadHandle = CreateThread(NULL, 0, ThreadProc, this, 0, NULL);
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
	frameCallback = NULL;
	frameBuffer = NULL;
}

OptrisCamera::~OptrisCamera()
{	
	delFrameCallback();
}

void OptrisCamera::start()
{
	// TODO: Can start take the frameCallback as a parameter?
	// Find out how GC works with java Directors
	if (!frameCallback) throw exception("setFrameCallback() must be called prior to start()");

	initCompleted = false;
	frameInit = false;

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
}

void OptrisCamera::delFrameCallback() 
{ 
	FrameCallback* cb = frameCallback;
	frameCallback = NULL;
	delete cb; 
}

void OptrisCamera::setFrameCallback(FrameCallback* frameCallback) 
{ 
	delFrameCallback(); 
	this->frameCallback = frameCallback; 
}

void OptrisCamera::OnServerStopped(int reason)
{
	stop();
}

void OptrisCamera::OnInitCompleted()
{
	initCompleted = true;
}

void OptrisCamera::setFrameBuffer(void *buffer, long size)
{
	if (size < getBufferSize()) throw std::exception("Buffer is too small");

	frameBuffer = buffer;
}

void OptrisCamera::OnFrameInit(int frameWidth, int frameHeight, int frameDepth)
{
	Width = frameWidth;
	Height = frameHeight;
	Depth = frameDepth;

	frameInit = true;

	if (frameCallback)
		frameCallback->onFrameInit(frameWidth, frameHeight, getBufferSize());
}

void OptrisCamera::OnNewFrame(void * pBuffer, FrameMetadata *pMetaData)
{
	if (!initCompleted || !frameInit) return;

	cout << "Frame " << *((short*)pBuffer) << endl;

	if (frameBuffer)
		memcpy(frameBuffer, pBuffer, getBufferSize());

	if (frameCallback)
		frameCallback->onNewFrame();
}