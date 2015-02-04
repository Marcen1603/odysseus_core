#pragma once

#include <exception>
#include <string>
#include <iostream>
#include <Windows.h>

class IPCThread;
struct FrameMetadata;

class OptrisCamera
{
private:
	IPCThread* ipcThread;

	unsigned short	instanceID;
	std::string		instanceName;
	std::string		ethernetAddr;

	bool initCompleted;
	bool frameInit;

	int Width;
	int Height;
	int Depth;

	CRITICAL_SECTION bufferMutex;
	void* currentBuffer;

	void OnServerStopped(int reason);
	void OnInitCompleted();
	void OnFrameInit(int frameWidth, int frameHeight, int frameDepth);
	void OnNewFrame(void * pBuffer, FrameMetadata *pMetaData);
	
public:
	OptrisCamera(const std::string& instanceName, const std::string& ethernetAddr) throw(std::exception);
	~OptrisCamera();

	void start() throw(std::exception);
	void stop();

	bool	grabImage(void *buffer, long size, unsigned int timeOutMs) throw(std::exception);
	int		getImageChannels() const { return 1; }
	int		getBufferSize() const { return Width * Height * Depth; }
	int		getImageWidth() const { return Width; }
	int		getImageHeight() const { return Height; }

	template <int IDX> friend class IPCCallbackFunctions;
};