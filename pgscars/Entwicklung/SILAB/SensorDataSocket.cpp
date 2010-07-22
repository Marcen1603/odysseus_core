#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdio.h>
#include "SensorDataSocket.h"
#include "DPULogger.h"
#include <sstream>
#include <string>

// link to Winsock library
#pragma comment(lib, "ws2_32.lib")

// Winsock
WSADATA wsaData;
SOCKET ListenSocket = INVALID_SOCKET;
SOCKET ClientSocket = INVALID_SOCKET;
int port;
DPULogger *log = new DPULogger("socket.log");

int SensorDataSocket::initWinsock()
{
	int iResult;

	// Initialize Winsock
	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		return 1;
	}
	struct addrinfo *result = NULL, *ptr = NULL, hints;

	ZeroMemory(&hints, sizeof (hints));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_protocol = IPPROTO_TCP;
	hints.ai_flags = AI_PASSIVE;

	std::ostringstream sout;
	sout << port;

	std::string converted_port(sout.str());
	const char* connPort = converted_port.c_str();

	// Resolve the local address and port to be used by the server
	iResult = getaddrinfo(NULL, connPort, &hints, &result);
	if (iResult != 0) {
		printf("getaddrinfo failed: %d\n", iResult);
		WSACleanup();
		return 1;
	}
	// Create a SOCKET for the server to listen for client connections
	ListenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);

	if (ListenSocket == INVALID_SOCKET) {
		printf("Error at socket(): %ld\n", WSAGetLastError());
		freeaddrinfo(result);
		WSACleanup();
		return 1;
	}
	// Setup the TCP listening socket
	iResult = bind( ListenSocket, 
    result->ai_addr, (int)result->ai_addrlen);
	if (iResult == SOCKET_ERROR) {
		printf("bind failed: %d\n", WSAGetLastError());
		freeaddrinfo(result);
		closesocket(ListenSocket);
		WSACleanup();
		return 1;
	}

	if ( listen( ListenSocket, SOMAXCONN ) == SOCKET_ERROR ) {
		printf( "Error at bind(): %ld\n", WSAGetLastError() );
		closesocket(ListenSocket);
		WSACleanup();
		return 1;
	}

	// Accept a client socket
	ClientSocket = accept(ListenSocket, NULL, NULL);
	if (ClientSocket == INVALID_SOCKET) {
		printf("accept failed: %d\n", WSAGetLastError());
		closesocket(ListenSocket);
		WSACleanup();
		return 1;
	}
	return 0;
}

int SensorDataSocket::SendValue(double x) {
	
	#define DEFAULT_BUFLEN 512

	int iSendResult;

	std::string s;
	// convert double x to string s
	std::ostringstream ss;
	ss << x;
	s = ss.str();
	std::string s2 = s + '\n';

	// Echo the buffer to the sender
	iSendResult = send(ClientSocket, s2.c_str(), strlen(s2.c_str())+1, 0);

	if (iSendResult == SOCKET_ERROR) {
		printf("send failed: %d\n", WSAGetLastError());
		closesocket(ClientSocket);
		WSACleanup();
		return 1;
    }
	return 0;
}

int SensorDataSocket::SendString(std::string pStr) {
	
	#define DEFAULT_BUFLEN 512

	int iSendResult;
	pStr += "\n";

	// Echo the buffer to the sender
	iSendResult = send(ClientSocket, (char *)pStr.c_str(), strlen(pStr.c_str())+1, 0);

	//log->Write((char *)sprintf(strg,"%d",iSendResult));
	if (iSendResult == SOCKET_ERROR) {
		printf("send failed: %d\n", WSAGetLastError());
		closesocket(ClientSocket);
		WSACleanup();
		return 1;
    }
	return 0;
}

SensorDataSocket::SensorDataSocket(SOCKET lSocket, int Port) {
	ListenSocket = lSocket;
	port = Port;
}