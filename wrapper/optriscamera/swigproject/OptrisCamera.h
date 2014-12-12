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

private:
	void OnServerStopped(int reason);
	void OnInitCompleted();
	void OnFrameInit(int frameWidth, int frameHeight, int frameDepth);
	void OnNewFrame(void* pBuffer, FrameMetadata* pMetaData);

	int Width;
	int Height;
	int Depth;

public:
	OptrisCamera(const std::string& instanceName, const std::string& ethernetAddr) throw(std::exception)
	{
		this->instanceName = instanceName;
		this->ethernetAddr = ethernetAddr;

		instanceID = 0;
	}

	~OptrisCamera();

	void	start();
	void	stop();

//	bool	grabRGB8(unsigned int timeOutMs) throw(std::exception);
//	int		getImageSize();
	int		getImageWidth();
	int		getImageHeight();
//	void	getImageData(int data[]);

	template <int IDX> friend class IPCCallbackFunctions;
};

#endif