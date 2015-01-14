#pragma once

#include <exception>
#include <string>
#include <iostream>

class IPCThread;
struct FrameMetadata;

class FrameCallback 
{
public:
	virtual ~FrameCallback(){}
	virtual void onFrameInit(int width, int height, int bufferSize) {}
	virtual void onNewFrame() {}
};

class OptrisCamera
{
private:
	IPCThread* ipcThread;

	unsigned short	instanceID;
	std::string		instanceName;
	std::string		ethernetAddr;

	void* frameBuffer;
	FrameCallback* frameCallback;

	bool initCompleted;
	bool frameInit;

	int Width;
	int Height;
	int Depth;

	void OnServerStopped(int reason);
	void OnInitCompleted();
	void OnFrameInit(int frameWidth, int frameHeight, int frameDepth);
	void OnNewFrame(void * pBuffer, FrameMetadata *pMetaData);
	
public:
	OptrisCamera(const std::string& instanceName, const std::string& ethernetAddr) throw(std::exception);
	~OptrisCamera();

	void start() throw(std::exception);
	void stop();
	void setFrameBuffer(void *buffer, long size);

	void delFrameCallback();
	void setFrameCallback(FrameCallback* frameCallback);

	int		getBufferSize() const { return Width * Height * Depth; }
	int		getImageWidth() const { return Width; }
	int		getImageHeight() const { return Height; }

	template <int IDX> friend class IPCCallbackFunctions;
};