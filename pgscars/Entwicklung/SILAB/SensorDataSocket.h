#include <WinSock2.h>
#include <string>

class SensorDataSocket
{
public:
	SensorDataSocket(SOCKET, int);
	int SendValue(double);
	int SendString(std::string pStr);
	int initWinsock();
};