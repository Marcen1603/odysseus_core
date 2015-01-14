#ifndef OPTRIS_CAMERA_H
#define OPTRIS_CAMERA_H

#include <exception>
#include <string>

struct FrameMetadata;

class OptrisCamera
{
private:
	unsigned short	instanceID;
	std::string		instanceName;
	std::string		ethernetAddr;

	int Width;
	int Height;
	int Depth;

public:
	OptrisCamera(const std::string& instanceName, const std::string& ethernetAddr) throw(std::exception);
	~OptrisCamera();

	void start() throw(std::exception);
	void stop();

	bool grabImage(void *buffer, long size, unsigned int timeOutMs) throw(std::exception);

	int		getBufferSize() const { return Width * Height * Depth; }
	int		getImageWidth() const { return Width; }
	int		getImageHeight() const { return Height; }

//	template <int IDX> friend class IPCCallbackFunctions;
};

#endif