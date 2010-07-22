//#pragma once
#include <fstream>

using namespace std;

class DPULogger
{
public:
	DPULogger(char* filename);
	~DPULogger();
	void Write(char* logline);
private:
	ofstream m_stream;
};
