#ifndef BASLER_CAMERA_H
#define BASLER_CAMERA_H

#include <exception>

namespace Pylon
{
	class CBaslerGigEInstantCamera;
	class CPylonImage;
};

class BaslerCamera
{
	typedef Pylon::CBaslerGigEInstantCamera Camera;

	Camera*					camera;
	Pylon::CPylonImage*		currentImage;

public:
	BaslerCamera(std::string ethernetAddress) throw(std::exception);
	~BaslerCamera();

	void	start();
	void	stop();

	bool	grabRGB8(unsigned int timeOutMs) throw(std::exception);
	int		getImageSize();
	int		getImageWidth();
	int		getImageHeight();
//	void	getImageData(char *BYTE);
	void	getImageData(int data[]);





public:
	static void initializeSystem() throw(std::exception);
	static void shutDownSystem();
	static bool isSystemInitialized();

private:
	static bool isInitialized;
};

#endif