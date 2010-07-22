#include "stdafx.h"
//#include "iSCNX/iSCNX.h"
#include "DPUSensors.h"
#include "SensorDataSocket.h"
#include "Server.h"
#include "DPULogger.h"
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdio.h>
#include <cmath>
#include <vector>
//#include <std::string>
using namespace std; 
// link to Winsock library
#pragma comment(lib, "ws2_32.lib")

#define PI 3.14159265

DPULogger *pLog = new DPULogger("sensorresults.log");
// Die folgenden Definitionen sind zur Einbindung der SPU in das SILAB-System nötig.
SPU_Impl(Sensors, SPUSensors_API, 1.0, "sensor functionality")

SOCKET lSocket = INVALID_SOCKET;
SensorDataSocket *dataSocket;
//Server *server;
int measurementCount;

// ------------------------------------------------------------------
// sSPUSensors (Implementation)
// ------------------------------------------------------------------

sSPUSensors::sSPUSensors()
{
	pLog->Write("Inititating...");
	sSPUSensorsInit();
}

// ------------------------------------------------------------------
// cSPUSensors (Implementation)
// ------------------------------------------------------------------

cSPUSensors::~cSPUSensors()
{
	//closesocket(lSocket);
	mp_ODB->ReleaseQuery(mp_Query);
	ODBDone();
	ODBVISDone();
	mp_ODBVIS = NULL;
	Cleanup();
}

bool cSPUSensors::RegisterVars()
{
	cCFGParserIter DPUParams = GetParserIter();
	return true;
}

bool cSPUSensors::Prepare()
{
	g_LogSys << "DPUSensors Prepare!" << endl;

	dataSocket = new SensorDataSocket(lSocket, (int)m_Data.mi_Port);
	//server = new Server((int)m_Data.mi_Port, -1, false);
	g_LogSys << "Connect Client to port: " << (int)m_Data.mi_Port << endl;
	int winsockState = dataSocket->initWinsock();
	if (winsockState==1) {
		g_LogSys << "ERROR: COULD NOT INITIATE SOCKET!" << endl;
	}
	
	//server->Connect();
	//fflush(NULL);

	mp_ODB = ODBInit();
	if (!mp_ODB)
	{
		// Fehler!
		g_LogSys << "DPUSensors: Fehler beim Zugriff auf ODB." << endl;
		return false;
	}
	mp_Query = mp_ODB->CreateQuery(10);
	if (!mp_Query)
	{
		// Fehler!
		g_LogSys << "DPUSensors: Fehler beim Anfordern eines Querys." << endl;
		return false;
	}
	bool b_ok = true;
	b_ok &= mp_Query->BeginVertices(4);
	b_ok &= mp_Query->AddVertex(0.0, 0.0);
	b_ok &= mp_Query->AddVertex(50.0, -6.0);
	b_ok &= mp_Query->AddVertex(50.0, 6.0);
	b_ok &= mp_Query->AddVertex(0.0, 0.0);
	b_ok &= mp_Query->EndVertices();
	if (!b_ok)
	{
		// Fehler!
		g_LogSys << "DPUSensors: Fehler bei der Definition eines Querys." << endl;
		return false;
	}
	mp_ODBVIS = ODBVISInit();
	if (!mp_ODBVIS) { // Fehler!
		g_LogSys << "DPUmit3DObjekten: Fehler beim Zugriff auf ODBVIS." << endl;
		return false; 
	}

	measurementCount = 0;
	return true;
}

eSPUReady cSPUSensors::ReadyToStart(int i_Step)
{
	return SPU_READY;
}

void cSPUSensors::OnStart()
{
}

void cSPUSensors::Trigger(double d_TimeMS, double d_TimeErrorMS)
{
	m_Data.md_Out = m_Data.md_In;

	double rotatedSensorX = cos((double)m_Data.md_EgoYaw) * (double)m_Data.md_SensorXTransform + -sin((double)m_Data.md_EgoYaw) * (double)m_Data.md_SensorYTransform;
	double rotatedSensorY = sin((double)m_Data.md_EgoYaw) * (double)m_Data.md_SensorXTransform + cos((double)m_Data.md_EgoYaw) * (double)m_Data.md_SensorYTransform;
	
	//transform sensor to the point that was specified via the text config
	if (!mp_Query->Transform((double)m_Data.md_EgoX + rotatedSensorX, (double)m_Data.md_EgoY + rotatedSensorY, (double)m_Data.md_EgoYaw)) pLog->Write("Error!"); // Fehler!
	if (!mp_Query->Update()) pLog->Write("Error!"); // Fehler!

	vector<cODBObject *> results;
	vector<cODBObject *>::iterator it = results.begin();

	unsigned long noResults = mp_Query->GetNResults();
	unsigned long i;
	unsigned long j;
	myODB *objects = 0;
	
	vector<myODB *> vec;

	srand(time(NULL));
	for (i=0; i<noResults; i++)
	{
		if ((rand() % 100) > (int) m_Data.md_disappProb)
		{	
			myODB *myObject = 0;
			myObject = new myODB();

			double angle = (2*PI) - (double)m_Data.md_EgoYaw;
			for (j=0; j<4; j++)
			{
				//rotated x:
				double rotatedX = cos(angle) * mp_Query->GetResult(i)->ma_BoundingBox[j].md_x + -sin(angle) * mp_Query->GetResult(i)->ma_BoundingBox[j].md_y;
				//rotated y:
				double rotatedY = sin(angle) * mp_Query->GetResult(i)->ma_BoundingBox[j].md_x + cos(angle) * mp_Query->GetResult(i)->ma_BoundingBox[j].md_y; 
				//translated x:
				myObject->points[j][1] = rotatedX - (
													cos(angle) * ((double)m_Data.md_EgoX + (double)m_Data.md_SensorXTransform)
													+ -sin(angle) * ((double)m_Data.md_EgoY + (double)m_Data.md_SensorYTransform)
													);
				//translated y:
				myObject->points[j][0] = rotatedY - (
													sin(angle) * ((double)m_Data.md_EgoX + (double)m_Data.md_SensorXTransform)
													+ cos(angle) * ((double)m_Data.md_EgoY + (double)m_Data.md_SensorYTransform)
													);
			}
			myObject->objType = mp_Query->GetResult(i)->mac_Name;
			
				//rotated x:
				double rotatedCenterX = cos(angle) * mp_Query->GetResult(i)->m_Position.md_x + -sin(angle) * mp_Query->GetResult(i)->m_Position.md_y;
				//rotated y:
				double rotatedCenterY = sin(angle) * mp_Query->GetResult(i)->m_Position.md_x + cos(angle) * mp_Query->GetResult(i)->m_Position.md_y; 
				//translated x:
				myObject->center[1] = rotatedCenterX - (
													cos(angle) * ((double)m_Data.md_EgoX + (double)m_Data.md_SensorXTransform)
													+ -sin(angle) * ((double)m_Data.md_EgoY + (double)m_Data.md_SensorYTransform)
													);
				//translated y:
				myObject->center[0] = rotatedCenterY - (
													sin(angle) * ((double)m_Data.md_EgoX + (double)m_Data.md_SensorXTransform)
													+ cos(angle) * ((double)m_Data.md_EgoY + (double)m_Data.md_SensorYTransform)
													);

			//rotation relative to ego car
			double tempAngle = 2*PI + mp_Query->GetResult(i)->m_Rotation.md_z - (double)m_Data.md_EgoYaw;
			int result = static_cast<int>( tempAngle / (2*PI));
			myObject->rotation = tempAngle - static_cast<double>(result) *2*PI;

			if (mp_Query->GetResult(i)->IsvValid())
			{
				myObject->speed = mp_Query->GetResult(i)->md_v;
			} else
			{
				myObject->speed = 0.0;
			}
			myObject->error = "none";
			myObject->visible = "visible";
			myObject->distance = calculateDistance(0.0, 0.0, myObject->center[0], myObject->center[1]);
			vec.push_back(myObject);
		}
	}
	
	if ((double)m_Data.md_Variance > 0.0) {addBlur(vec);}

	vector<myODB *> clusteredVec;
	if (vec.size() > 1)
	{
		quicksortByDistance(vec, 0, vec.size()-1, 0.0, 0.0);
		clusteredVec = markHiddenObjects(vec, 0, 0);
	} else {clusteredVec = vec;}
	//clusteredVec = segmentation(clusteredVec, 0, 0);
	//vector<myODB *> clusteredVec = clusterCheckTest(vec, 0, 0); // = vec;

	bool sentValues = false;
	unsigned long k;
	for (k=0;k<clusteredVec.size();k++)
	{
		sentValues = true;

		dataSocket->SendValue(measurementCount);
		dataSocket->SendValue(clusteredVec[k]->points[0][0]);
		dataSocket->SendValue(clusteredVec[k]->points[0][1]);
		dataSocket->SendValue(clusteredVec[k]->points[1][0]);
		dataSocket->SendValue(clusteredVec[k]->points[1][1]);
		dataSocket->SendValue(clusteredVec[k]->points[2][0]);
		dataSocket->SendValue(clusteredVec[k]->points[2][1]);
		dataSocket->SendValue(clusteredVec[k]->points[3][0]);
		dataSocket->SendValue(clusteredVec[k]->points[3][1]);
		dataSocket->SendString(clusteredVec[k]->objType);
		dataSocket->SendValue(clusteredVec[k]->speed);
		dataSocket->SendValue(clusteredVec[k]->rotation);
		dataSocket->SendValue(clusteredVec[k]->center[0]);
		dataSocket->SendValue(clusteredVec[k]->center[1]);
		dataSocket->SendString(clusteredVec[k]->error);
		dataSocket->SendString(clusteredVec[k]->visible);
	}

	if(sentValues){
                // send a punctuation
                dataSocket->SendValue(measurementCount);
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendString("punctuation");
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendValue(0.0);
                dataSocket->SendString("");
                dataSocket->SendString("");
        }

	measurementCount = measurementCount + 1;
}

vector<myODB *> cSPUSensors::clusterCheckTest(vector<myODB *> &obj, int start1, int start2)
{
	vector<myODB *> newVec;
	unsigned long i;
	unsigned long j;
	for (i=start1;i<obj.size();i++)
	{
		for (j=start2;j<obj.size();j++)
		{
			if(j!=i)
			{
				myODB *a = obj[i];
				myODB *b = obj[j];
				unsigned long k;
				unsigned long l;
				
				bool case1 = false;
				bool case2 = false;
				bool case3 = false;
				bool case4 = false;
				for (k=0;k<=3;k++)
				{
					for (l=0;l<=3;l++)
					{
						if (calculateDistance(a->points[k][0], a->points[k][1], b->points[l][0], b->points[l][1]) < (double)m_Data.md_ClusterDist)
						{
							if ((k == 1 && l == 0))
							{
								case1 = true;
							} 
							else if ((k == 2 && l == 3))
							{
								case2 = true;
							} 
							else if ((k == 0 && l == 1))
							{
								case3 = true;
							} 
							else if ((k == 3 && l == 1))
							{
								case4 = true;
							}
						}
					}
				}
				if (case1 && case2) {
					myODB *myObject = 0;
					myObject = new myODB();
					myObject->points[0][0] = a->points[0][0];
					myObject->points[0][1] = a->points[0][1];
					myObject->points[1][0] = b->points[1][0];
					myObject->points[1][1] = b->points[1][1];
					myObject->points[2][0] = b->points[2][0];
					myObject->points[2][1] = b->points[2][1];
					myObject->points[3][0] = a->points[3][0];
					myObject->points[3][1] = a->points[3][1];
					myObject->error = "clustered";
					myObject->objType = "clustered";
					newVec.push_back(myObject);
				} else if (case3 && case4) {
					myODB *myObject = 0;
					myObject = new myODB();
					myObject->points[0][0] = b->points[0][0];
					myObject->points[0][1] = b->points[0][1];
					myObject->points[1][0] = a->points[1][0];
					myObject->points[1][1] = a->points[1][1];
					myObject->points[2][0] = a->points[2][0];
					myObject->points[2][1] = a->points[2][1];
					myObject->points[3][0] = b->points[3][0];
					myObject->points[3][1] = b->points[3][1];
					myObject->error = "clustered";
					myObject->objType = "clustered";
					newVec.push_back(myObject);
				} else {
					myODB *myObject1 = 0;
					myObject1 = new myODB();
					myObject1->points[0][0] = a->points[0][0];
					myObject1->points[0][1] = a->points[0][1];
					myObject1->points[1][0] = a->points[1][0];
					myObject1->points[1][1] = a->points[1][1];
					myObject1->points[2][0] = a->points[2][0];
					myObject1->points[2][1] = a->points[2][1];
					myObject1->points[3][0] = a->points[3][0];
					myObject1->points[3][1] = a->points[3][1];
					myObject1->error = "none";
					myObject1->objType = a->objType;
					myODB *myObject2 = 0;
					myObject2 = new myODB();
					myObject2->points[0][0] = b->points[0][0];
					myObject2->points[0][1] = b->points[0][1];
					myObject2->points[1][0] = b->points[1][0];
					myObject2->points[1][1] = b->points[1][1];
					myObject2->points[2][0] = b->points[2][0];
					myObject2->points[2][1] = b->points[2][1];
					myObject2->points[3][0] = b->points[3][0];
					myObject2->points[3][1] = b->points[3][1];
					myObject2->error = "none";
					myObject1->objType = b->objType;
					newVec.push_back(myObject1);
					newVec.push_back(myObject2);
				}
				//if (i==newVec.size()-1 && j==newVec.size()-2) {
				//	return newVec;
				//}
				//return clusterCheckTest(newVec, i, j);
			}
		}
	}
	return newVec;
}

vector<myODB *> cSPUSensors::markHiddenObjects(vector<myODB *> &obj, int start1, int start2)
{
	unsigned long i;
	unsigned long j;
	bool next = false;

	for (i=start1;i<obj.size();i++)
	{
		//only check cars that are nearer to the ego car
		for (j=start2;j<i;j++)
		{
			//if(j!=i)
			//{
				unsigned long l;
				myODB *a = obj[i];
				myODB *b = obj[j];
			
				//definition of a line from the sensor to the center of the checked object
				double x1g1 = 0.0; //(double)m_Data.md_SensorXTransform;
				double x2g1 = a->center[0];
				double y1g1 = 0.0; //(double)m_Data.md_SensorYTransform;
				double y2g1 = a->center[1];
				double qg1 = y1g1 - ((y1g1 - y2g1)/(x1g1 - x2g1)) * x1g1;
				double mg1 = (y2g1 - y1g1) / (x2g1 - x1g1);

				for (l=0;l<=3;l++)
				{
					double y1g2 = b->points[l][1];
					double y2g2 = b->points[(l+1)%4][1];
					double x1g2 = b->points[l][0];
					double x2g2 = b->points[(l+1)%4][0];
					double qg2 = y1g2 - ((y1g2 - y2g2)/(x1g2 - x2g2)) * x1g2;
					double mg2 = (y2g2 - y1g2) / (x2g2 - x1g2);

					double x = 0.0;
					double y = 0.0;

					if (x1g1 == x2g1)
					{
						x = x1g1;
					} 
					else if (x1g2 == x2g2)
					{
						x = x1g2;
					}
					else
					{
						x = (qg2 - qg1) / (mg1 - mg2);
					}

					if (y1g1 == y2g1)
					{
						y = y1g1;
					} 
					else if (y1g2 == y2g2)
					{
						y = y1g2;
					}
					else
					{
						y = (mg2 * x) + qg2;
					}

					if ((x < x1g2 && x < x2g2) || (x > x1g2 && x > x2g2) || (y < y1g2 && y < y2g2) || (y > y1g2 && y > y2g2))
					{
						a->visible = "visible";
					}
					else 
					{
						a->visible = "hidden";
						next = true;
						break;
					}
				//}
				if (next == true) { next = false; break; }
			}
		}
	}
	return obj;
}

vector<myODB *> cSPUSensors::segmentation(vector<myODB *> &obj, int start1, int start2)
{
	vector<myODB *> newVec;
	unsigned long i;

	srand(time(NULL));

	for (i=start1;i<obj.size();i++)
	{
		if ((rand() % 100) > (int) m_Data.md_segmentationProb)
		{
			unsigned long l;
			myODB *a = obj[i];
			
			double y1g1 = (double)m_Data.md_EgoY + (double)m_Data.md_SensorYTransform;
			double y2g1 = a->center[1];
			double x1g1 = (double)m_Data.md_EgoX + (double)m_Data.md_SensorXTransform;
			double x2g1 = a->center[0];
			double qg1 = y1g1 - ((y1g1 - y2g1)/(x1g1 - x2g1)) * x1g1;
			double mg1 = (y2g1 - y1g1) / (x2g1 - x1g1);

			for (l=0;l<=3;l++)
			{
				double y1g2 = a->points[l][1];
				double y2g2 = a->points[(l+1)%4][1];
				double x1g2 = a->points[l][0];
				double x2g2 = a->points[(l+1)%4][0];
				double qg2 = y1g2 - ((y1g2 - y2g2)/(x1g2 - x2g2)) * x1g2;
				double mg2 = (y2g2 - y1g2) / (x2g2 - x1g2);

				double x = 0.0;
				double y = 0.0;

				if (x1g1 == x2g1)
				{
					x = x1g1;
				} 
				else if (x1g2 == x2g2)
				{
					x = x1g2;
				}
				else
				{
					x = (qg2 - qg1) / (mg1 - mg2);
				}

				if (y1g1 == y2g1)
				{
					y = y1g1;
				} 
				else if (y1g2 == y2g2)
				{
					y = y1g2;
				}
				else
				{
					y = (mg2 * x) + qg2;
				}

				if (!((x < x1g2 && x < x2g2) || (x > x1g2 && x > x2g2) || (y < y1g2 && y < y2g2) || (y > y1g2 && y > y2g2)))
				{
					if (l == 0 || l == 2)
					{
						myODB *myObject1 = 0;
						myObject1 = new myODB();
						//old p0
						myObject1->points[0][0] = a->points[0][0];
						myObject1->points[0][1] = a->points[0][1];
						//new p1
						myObject1->points[1][0] = ((a->points[1][0] - a->points[0][0]) / 2) + a->points[0][0];
						myObject1->points[1][1] = myObject1->points[1][0] * mg2;
						//new p2
						myObject1->points[2][0] = ((a->points[3][0] - a->points[2][0]) / 2) + a->points[2][0];
						myObject1->points[2][1] = myObject1->points[2][0] * mg2;
						//old p3
						myObject1->points[3][0] = a->points[3][0];
						myObject1->points[3][1] = a->points[3][1];
						myObject1->error = "segmented";
						myObject1->objType = a->objType;
						myObject1->visible = a->visible;
						
						myODB *myObject2 = 0;
						myObject2 = new myODB();
						//new p0
						myObject2->points[0][0] = myObject1->points[1][0];
						myObject2->points[0][1] = myObject1->points[1][1];
						//old p1
						myObject2->points[1][0] = a->points[1][0];
						myObject2->points[1][1] = a->points[1][1];
						//old p2
						myObject2->points[2][0] = a->points[2][0];
						myObject2->points[2][1] = a->points[2][1];
						//new p3
						myObject2->points[3][0] = myObject1->points[2][0];
						myObject2->points[3][1] = myObject1->points[2][1];
						myObject2->error = "segmented";
						myObject2->objType = a->objType;
						myObject2->visible = a->visible;
						newVec.push_back(myObject1);
						newVec.push_back(myObject2);
						break;
					}
					else if (l == 1 || l == 3)
					{
						// TODO!
					}
				}
				else
				{
					newVec.push_back(a);
				}
			}
		}
	}
	return newVec;
}

void cSPUSensors::quicksortByDistance(vector<myODB *> &obj, int left, int right, double egoX, double egoY) {

	int i = left, j = right;
	myODB *tmp;
	double pivot = obj[(left + right) / 2]->distance;
	while (i <= j) {
		while (obj[i]->distance<pivot){i++;}   
		while (obj[j]->distance>pivot){j--;}

		if (i <= j) {
			tmp = obj[i];
			obj[i] = obj[j];
			obj[j] = tmp;
			i++;
			j--;
		}
	};
	if (left < j){quicksortByDistance(obj, left, j, egoX, egoY);}
	if (i < right){quicksortByDistance(obj, i, right, egoX, egoY);}
}

double cSPUSensors::getDistanceToEgo(myODB* result, double egoX, double egoY)
{
	double x = result->center[0];
	double y = result->center[1];

	double distance = sqrt((egoX - x)*(egoX - x) + (egoY - y)*(egoY -y));
	return distance;
}

void cSPUSensors::addBlur(vector<myODB *> &obj)
{
	unsigned long i;
	srand(time(NULL));

	for (i=0;i<obj.size();i++)
	{
		unsigned long l;
		for (l=0;l<=3;l++)
		{
			unsigned long k;
			for (k=0;k<=1;k++)
			{
				srand((unsigned)time(0));
				double rnd1 = ((float)rand())/(RAND_MAX + 1);
				double rnd2 = ((float)rand())/(RAND_MAX + 1);
				//varianz
				double s = (double)m_Data.md_Variance;
				//mittelwert
				double m = obj[i]->points[l][k];
				obj[i]->points[l][k] = s * sqrt(-2 * log(rnd1)) * sin(2 * PI * rnd2) + m;
			}
		}
	}
}

double cSPUSensors::calculateDistance(double x1, double y1, double x2, double y2)
{
	double distance = sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
	return distance;
}

int* cSPUSensors::checkBoundingBoxDistance(myODB* a, myODB* b, int* corners)
{
	unsigned long i;
	unsigned long j;
	for (i=0;i<=3;i++)
	{
		for (j=0;j<=3;j++)
		{
			if (calculateDistance(a->points[i][0], a->points[i][1], b->points[j][0], b->points[j][1]) < (double)m_Data.md_ClusterDist)
			{
				corners[0] = i;
				corners[1] = j;
				return corners;
			}
		}
	}
	corners[0] = -1;
	corners[1] = -1;
	return corners;
}

eSPUReady cSPUSensors::ReadyToStop(int i_Step)
{
	return SPU_READY;
}

void cSPUSensors::OnStop()
{
}

void cSPUSensors::ReadyToPause()
{
}

void cSPUSensors::OnPause()
{
}

void cSPUSensors::ReadyToResume()
{
}

void cSPUSensors::OnResume()
{
}

void cSPUSensors::OnIdle(eSPUIdleReason ReasonForIdle)
{
}

void cSPUSensors::Cleanup()
{
}


// -------------------------------------------------------------------
// DLL- Hauptfunktion. Diese muss üblicherweise nicht geändert werden.
// -------------------------------------------------------------------

BOOL APIENTRY DllMain( HANDLE hModule,
					  DWORD  ul_reason_for_call,
					  LPVOID lpReserved
					  )
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
	case DLL_THREAD_ATTACH:
	case DLL_THREAD_DETACH:
	case DLL_PROCESS_DETACH:
		break;
	}
	return TRUE;
}