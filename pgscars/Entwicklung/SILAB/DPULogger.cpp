#include "DPULogger.h"

DPULogger::DPULogger(char* filename)
{
	m_stream.open(filename);
}

void DPULogger::Write(char* logline){
  m_stream << logline << endl;
}

DPULogger::~DPULogger()
{
	m_stream.close();
}