#pragma once
#include <exception>

namespace Pylon
{
	class CBaslerGigEInstantCamera;
	class CGrabResultPtr;
};

class BaslerCamera
{
public:
	enum OperationMode
	{
		PUSH=0, PULL
	};

private:
	typedef Pylon::CBaslerGigEInstantCamera Camera;

	Camera*	camera;

	std::string serialNumber;
	OperationMode operationMode;
//	bool supportsBGRConversion;
	int imageWidth;
	int imageHeight;
	int bufferSize;

	void onGrabbedInternal(const Pylon::CGrabResultPtr& grabResult);

public:
	BaslerCamera(std::string serialNumber) : serialNumber(serialNumber), camera(NULL) {}
	~BaslerCamera() {}

	void	start(OperationMode operationMode) throw(std::exception);
	void	stop();

	bool	trigger();
	bool	grabRGB8(void *buffer, long size, int lineLength, unsigned int timeOutMs) throw(std::exception);
	int		getImageWidth() const { return imageWidth; }
	int		getImageHeight() const { return imageHeight; }

	virtual void onGrabbed(void *buffer, long size);

public:
	static void initializeSystem() throw(std::exception);
	static void shutDownSystem();
	static bool isSystemInitialized();

private:
	static bool isInitialized;

	friend class CMyImageEventHandler;
};