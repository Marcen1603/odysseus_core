// DPUSensors
// Ein erster Sensor
// Erstellt: 17.08.2009 (SILABDPUWizard)
// Autor: Dennis W�emann

#ifndef __DPU_DPUSensors_H__
#define __DPU_DPUSensors_H__

// Die folgenden Definitionen sind zur Einbindung der DPU in das SILAB-System n�tig
// und sollten nicht ge�ndert werden.
#ifdef DPUSensors_EXPORTS
#	define SPUSensors_API __declspec(dllexport)
#else
#	define SPUSensors_API __declspec(dllimport)
#endif

#include "SYS/DEV/DEV.h"
#include "odb/odb/odb.h"

SPU_Decl(Sensors, SPUSensors_API);

// F�gen Sie hier bei Bedarf zus�tzliche Include-Files ein.
#include "odb/odbvis/odbvis.h"
// ------------------------------------------------------------------
// sSPUSensors
// ------------------------------------------------------------------

// Die Datenstruktur sSPUSensors definiert das Variablen-Interface der SPU.
// Sie sollte nur Variablen des Typs cVar enthalten.
struct SPUSensors_API sSPUSensors
{
	// Eing�nge der SPU
	cVar md_In;
	cVar md_EgoX;
	cVar md_EgoY;
	cVar md_EgoYaw;
	cVar mi_Port; // specified port for socket communication
	cVar md_ClusterDist; // minimum distance between objects for clustering to occur
	cVar md_disappProb;
	cVar md_segmentationProb;
	// sensor transformation in relation to the center of the ego car
	cVar md_SensorXTransform;
	cVar md_SensorYTransform;
	//variance for measurement blur
	cVar md_Variance;

	
	// Ausg�nge der SPU
	cVar md_Out;

	sSPUSensors ();

	void sSPUSensorsInit()
	{
		// Jede in der Interface-Struktur enthaltene Variable muss mit einem VARInitXXX- Makro
		// dem System bekannt gemacht werden. Die Markos legen den Typ, Wertebereich und
		// die Funktion (In/Out) der Variablen fest.
		// Die Methode s[SPUName]Init() muss im Konstruktor der Interface-Struktur
		// aufgerufen werden.

		// Eing�nge der SPU
		VARInitI(md_In);
		VARInitI(md_EgoX);
		VARInitI(md_EgoY);
		VARInitI(md_EgoYaw);
		VARInitI(mi_Port);
		VARInitI(md_SensorXTransform);
		VARInitI(md_SensorYTransform);
		VARInitI(md_ClusterDist);
		VARInitI(md_disappProb);
		VARInitI(md_segmentationProb);
		VARInitI(md_Variance);
		// Ausg�nge der SPU
		VARInitO(md_Out);
	}
};

struct myODB
{
	// the 4 corners of the bounding box
	double points[4][2];
	// description of the possible error which occured
	std::string error;
	// the number of the current measurement
	int measurementNo;
	// object type
	std::string objType;
	// the center of the object
	double center[2];
	// speed of the object
	double speed;
	// object visible?
	std::string visible;
	// rotation on z axxis
	double rotation;
	//distance from sensor to center
	double distance;
};

// ------------------------------------------------------------------
// cSPUSensors
// ------------------------------------------------------------------

// Die Klasse cSPUSensors ist die Hauptklasse der SPU.
// Ihre Methoden werden von SILAB w�hrend der Simulation aufgerufen.
class SPUSensors_API cSPUSensors : public cSPU
{
public:
	// Dieses Makro erzeugt einige Methoden, die bei jeder SPU �berschrieben
	// werden m�ssen. Als Argument wird der Projektname �bergeben, so da�
	// das Makro den Klassennamen entsprechend erweitern kann.
	SPU_Init(Sensors);
	~cSPUSensors();

protected:

	// Hier wird die Interface-Struktur als Schnittstelle zum System
	// instanziert.
	sSPUSensors m_Data;
	cODB *mp_ODB;
	cODBQuery *mp_Query;

	// Pointer to objects and visualization
	cODBVIS *mp_ODBVIS;
	cODBObject *mp_OBJ;

	// *** SPU-Ablaufsteuerung *** //
	// Die folgenden Methoden der SPU-Klasse werden von SILAB jeweils zu bestimmten
	// Zeitpunkten der Simulation aufgerufen. Eine ausf�hrliche Beschreibung findet sich in
	// der Klasse cSPU. Die Methoden sind hier in der Reihenfolge aufgef�hrt, in der sie �blicherweise
	// aufgerufen werden.

	// Allgemeine Initialisierung der SPU. Wird beim Laden der SPU ausgef�hrt.
	void cSPUSensorsInit()
	{
		mp_ODBVIS = NULL;
		mp_OBJ = NULL;
	}
	// Wird einmal beim Laden der SPU aufgerufen.
	// Aufgabe: Zus�tzliche Variablen, die dynamisch erzeugt werden m�ssen, registrieren.
	// Achtung: Zu diesem Zeitpunkt ist die Konfiguration noch nicht vollst�ndig eingelesen.
	//          DPU-Parameter m�ssen (nur) in dieser Methode �ber den CFG-Parser (GetParserIter())
	//          ausgelesen werden.
	// R�ckgabe: true  -> Variablen-Registrierung war erfolgreich.
	// 			 false -> Ein Fehler ist aufgetreten.
	virtual bool RegisterVars();
	// Wird einmal beim Laden einer Konfiguration in SILAB aufgerufen.
	// Aufgabe: Zeitintensive Vorbereitungen durchf�hren.
	// R�ckgabe: true  -> Vorbereitungen waren erfolgreich.
	// 			 false -> Bei den Vorbereitungen ist ein Fehler aufgetreten.
	virtual bool Prepare();
	// Wird periodisch aufgerufen, sobald der Benutzer 'Launch' w�hlt.
	// Aufgabe: Zeitintensive, in kleine Schritte (ca. 1 ms) aufgeteilte Vorbereitungen, die bei jedem Start
	//          einer Simulation gemacht werden m�ssen. Diese Methode wird solange aufgerufen,
	//			wie die SPU 'SPU_NOTREADY' zur�ck gibt.
	// Parameter: i_Step       -> Aufrufz�hler (0 beim ersten Aufruf, 1 beim zweiten, usw.)
	// R�ckgabe:  SPU_READY    -> SPU ist fertig.
	//            SPU_NOTREADY -> Es m�ssen noch weitere Vorbereitungsschritte durchgef�hrt werden.
	//                            Die Methode wird dann erneut aufgerufen.
	//			  SPU_ABORT	   -> Es ist ein Fehler aufgetreten. Die Simulation wird beendet.
	virtual eSPUReady ReadyToStart(int i_Step);
	// Wird einmal aufgerufen, wenn alle SPUs die Vorbereitungen in ReadyToStart abgeschlossen haben.
	// Aufgabe: Nicht zeitintensive Vorbereitungen, die bei jedem Start der Simulation gemacht werden m�ssen.
	//			Die Vorg�nge in dieser Methode sollten nicht l�nger als ca. 1 ms dauern.
	virtual void OnStart();
	// Wird periodisch aufgerufen, w�hrend die Simulation l�uft.
	// Aufgabe: Funktionalit�t der SPU w�hrend der Simulation. Die Vorg�nge in dieser Methode sollten
	//          nicht l�nger als ca. 1 ms dauern.
	virtual void Trigger(double d_TimeMS, double d_TimeErrorMS);
	// Wird periodisch aufgerufen, sobald der Benutzer 'Stop' w�hlt, oder falls
	// die Vorbereitungen einer SPU in ReadyToStart fehlgeschlagen sind.
	// Aufgabe: Zeitintensive, in kleine Schritte (ca. 1 ms) aufgeteilte Vorbereitungen zum Stop der Simulation.
	//			Diese Methode wird solange aufgerufen, wie die SPU 'SPU_NOTREADY' zur�ck gibt.
	// Parameter: i_Step      -> Aufrufz�hler (0 beim ersten Aufruf, 1 beim zweiten, usw.)
	// R�ckgabe: SPU_READY    -> SPU ist fertig.
	//           SPU_NOTREADY -> Es m�ssen noch weitere Schritte durchgef�hrt werden.
	//                           Die Methode wird dann erneut aufgerufen.
	//			 SPU_ABORT	  -> Es ist ein Fehler aufgetreten. Die Simulation wird beendet.
	virtual eSPUReady ReadyToStop(int i_Step);
	// Wird einmal aufgerufen, wenn alle SPUs die Vorbereitungen in ReadyToStop abgeschlossen haben.
	// Aufgabe: Nicht zeitintensive Vorg�nge beim Stop der Simulation. Die Vorg�nge in dieser Methode sollten
	//          nicht l�nger als ca. 1 ms dauern.
	virtual void OnStop();
	// Wird einmal aufgerufen, sobald der Benutzer 'Pause' w�hlt.
	// Aufgabe: Vorbereitungen f�r Pause der Simulation.
	virtual void ReadyToPause();
	// Wird einmal aufgerufen, wenn alle SPUs die Vorbereitungen in ReadyToPause abgeschlossen haben.
	// Aufgabe: Nicht zeitintensive Vorg�nge zur Pause der Simulation. Die Vorg�nge in dieser Methode sollten
	//          nicht l�nger als ca. 1 ms dauern.
	virtual void OnPause();
	// Wird einmal aufgerufen, sobald der Benutzer 'Resume' w�hlt.
	// Aufgabe: Vorbereitungen zum Resume der Simulation.
	//			Diese Methode wird solange aufgerufen, wie die SPU 'SPU_NOTREADY' zur�ck gibt.
	virtual void ReadyToResume();
	// Wird einmal aufgerufen, wenn alle SPUs die Vorbereitungen in ReadyToResume abgeschlossen haben.
	// Aufgabe: Nicht zeitintensive Vorbereitungen zum Resume der Simulation.
	//			Die Vorg�nge in dieser Methode sollten nicht l�nger als ca. 1 ms dauern.
	virtual void OnResume();
	// Wird aufgerufen, wenn freie Rechenzeit verf�gbar ist.
	// Aufgabe:   Zus�tzliche Operationen bei freier Rechenzeit.
	//			  Die Vorg�nge in dieser Methode sollten nicht l�nger als ca. 1 ms dauern.
	// Parameter: ReasonForIdle: Grund, weshalb derzeit Rechenzeit �brig ist. (siehe SPU.h)
	virtual void OnIdle(eSPUIdleReason ReasonForIdle);
	// Allgemeines Aufr�umen der SPU.
	// Wird beim Herunterfahren der Simulation (ordnungsgem�� oder �ber ein Not-Aus (Shutdown))
	// und beim Entladen der SPU (im Destruktor der SPU) aufgerufen.
	// Achtung: Cleanup() wird m�glicherweise mehrfach hintereinander aufgerufen.
	//          Cleanup() kann auf jeden Zustand folgend aufgerufen werden, da ein Shutdown
	//          der Simulation stets m�glich ist.
	// Aufgabe: Freigabe aller von der SPU belegten Resourcen.
	virtual void Cleanup();

	void quicksortByDistance(vector<myODB *> &obj, int left, int right, double egoX, double egoY);
	double getDistanceToEgo(myODB *result, double egoX, double egoY);
	double calculateDistance(cODBObject* a, cODBObject* b);
	double calculateDistance(double x1, double y1, double x2, double y2);
	vector<myODB *> clusterCheckTest(vector<myODB *> &obj, int start1, int start2);
	int* checkBoundingBoxDistance(myODB* a, myODB* b, int* corners);
	vector<myODB *> markHiddenObjects(vector<myODB *> &obj, int start1, int start2);
	vector<myODB *> segmentation(vector<myODB *> &obj, int start1, int start2);
	void cSPUSensors::addBlur(vector<myODB *> &obj);


};

#endif
