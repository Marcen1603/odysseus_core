#pragma once

#include <exception>
#include <string>
#include <iostream>
#include <Windows.h>

#include "ImagerIPC2.h"

#pragma warning(disable:4290)

class IPCThread;
struct FrameMetadata;

class OptrisCamera
{
private:
	IPCThread* ipcThread;

	unsigned short	instanceID;
	std::string		instanceName;
	std::string		ethernetAddr;

	volatile bool initCompleted;
	volatile bool frameInit;

	int Width;
	int Height;
	int Depth;

	void OnServerStopped(int reason);
	void OnInitCompleted();
	void OnFrameInit(int frameWidth, int frameHeight, int frameDepth);
	void OnNewFrameCallback(void * pBuffer, FrameMetadata *pMetaData);
	
public:
	OptrisCamera(const std::string& instanceName, const std::string& ethernetAddr) throw(std::exception);
	virtual ~OptrisCamera();

	void start() throw(std::exception);
	void stop();

	virtual void onNewFrame(long long timeStamp, TFlagState flagState, void *buffer, long size);

	int		getImageChannels() const { return 1; }
	int		getBufferSize() const { return Width * Height * Depth; }
	int		getImageWidth() const { return Width; }
	int		getImageHeight() const { return Height; }

	template <int IDX> friend class IPCCallbackFunctions;
};