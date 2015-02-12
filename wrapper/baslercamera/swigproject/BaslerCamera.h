#pragma once
#include <exception>

namespace Pylon
{
	class CBaslerGigEInstantCamera;
};

class BaslerCamera
{
	typedef Pylon::CBaslerGigEInstantCamera Camera;

	Camera*	camera;

	bool supportsRGBAConversion;
	int imageWidth;
	int imageHeight;
	int bufferSize;

public:
	BaslerCamera(std::string serialNumber) throw(std::exception);
	~BaslerCamera();

	void	start();
	void	stop();

	bool	grabRGB8(void *buffer, long size, unsigned int timeOutMs) throw(std::exception);
	int		getBufferSize() const { return bufferSize; }
	int		getImageWidth() const { return imageWidth; }
	int		getImageHeight() const { return imageHeight; }
	int		getImageChannels() const { return 4; }

public:
	static void initializeSystem() throw(std::exception);
	static void shutDownSystem();
	static bool isSystemInitialized();

private:
	static bool isInitialized;
};