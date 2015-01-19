
#include <iostream> // C++ Standard Input / Output Streams Library
#include <sstream> // C++ Standard string Stream Library
#include <vector> // STL dynamic array
#include <string> // STL string class

#include "API_AComms.h" // Actisense API
#include "BEMProtocolEnums.h"
#include "ARLErrorCodes.h"      // Actisense error codes
#include "ActiWrap.h"

enum deviceType_t {NGT, NGW, Unknown};
#define cMAX_INPUT_SIZE 80
#define cMAX_OUTPUT_SIZE 2048


using namespace std;


s32 Handle = NULL;
s32 ApiError = 0;
s32 portNumber = 0;


sPortEnum PortEnum;
s32 Baudrate;
s32 defaultOptionIdx = 0;
s32 defaultPortChoice = 0;
s32 defaultBaudChoice = 0;

ActisenseWrapper::ActisenseWrapper(std::string comPort, int baudRate) : JavaCallback(NULL)
{
	this->comPort = comPort;
			
	
	     int brcheck = 0;
	     const unsigned int acceptableBR[6] = {4800, 9600, 19200, 38400, 57600, 115200};
		 
		 for (int i = 0; i < 6; i++) //check if baudrate is accaptable
		 {
			 if (acceptableBR[i] == baudRate)
			 {
				 brcheck = 1;
			 }
		 }
		 if (0 == brcheck) //not accepted use standard baudrate
		 {
			this->baudRate = 115200; 
		 }
		 else // accpeted use the user BR
		 {
			this->baudRate = baudRate; 
		 }

		 ACommsEnumerateSerialPorts (&PortEnum);


		 portNumber = -1;
	for ( u32 i = 0; i < PortEnum.Size; i++ )
	{
		// add friendly name for COM port to choice list
		stringstream portNum;
		portNum<<PortEnum.Ports[i];
		if ((PortEnum.PortStatus[i] == ACPES_Open) & (this->comPort.compare("COM" + portNum.str ()) == 0 )) // Port is open already
			{

				throw exception(("COM" + portNum.str () + " " + ACommsEnumerateSerialPortsGetName (PortEnum.Ports[i]) + "- open already").c_str());
		}
		else if(this->comPort.compare("COM" + portNum.str ()) == 0 ) //Port is good Take it 
		{
			portNumber = PortEnum.Ports[i];
			
		}
	}
	if(portNumber == -1){throw exception("COM Port is invalid");}
		
}

void ActisenseWrapper::start()
{
	if(Handle != NULL) // Check if the handle exists and a port is defined
		{
			ACommsClose (Handle);
		}
	
	
		ACommsCreate((int*)&Handle);
		const char* portName = ACommsEnumerateSerialPortsGetName (portNumber);
		if (strstr (portName, "NGT"))
		{
			ACommsCommand_SetStream (Handle, COMMANDSTREAM_BST); //needed to talk to a local NGT
			ACommsCommand_SetOperatingMode (Handle, cOM_NGTransferRxAllMode);
			
			
		}
		else {	
			Handle = NULL;
			throw exception("Device is not Actisense NGT");
		}

		ACommsN2K_SetRxCallback (Handle, RxCallback, (void*) this);

		ACommsOpen (Handle, portNumber, baudRate);
		ACommsN2K_FlushRx (Handle);
	
		//ACommsN2K_SetRxCallback (BusInput::Handle, BusInput::RxCallback, NULL);
		ACommsN2K_SetRxCallback (Handle, RxCallback, (void*) this);
		
}


void __stdcall ActisenseWrapper::RxCallback (void* p)
{
	
	static_cast<ActisenseWrapper*>(p)->onReceive();
	
}
#pragma pack(push) 
#pragma pack(1)
struct packedN2kMsg {
	u32 Timestamp;
	u32 PGN;
	u8	Priority;
	u8	SrcAddr;
	u8	DestAddr;
	u32	Length;
	u8	Data[N2K_MAXLEN_TP];

} ;
#pragma pack(pop)

void ActisenseWrapper::onReceive()
{
	if(Handle != NULL && portNumber > 0) //Check if handle and port is set
	{
		// reads a single message
	sN2KMsg Msg;
//	cout<<"Call N2K ACOMMS"<<endl;
	int MsgRxStatus = ACommsN2K_Read (Handle, &Msg);
	
	if(N2K_MAXLEN_TP<Msg.Length) cout<<"Length longer then max"<<endl;
	if(0==Msg.Length) cout<<"Length is NULL"<<endl;
//	cout<<Msg.PGN<<endl;
//	cout<<Msg.Length<<endl;

	if ( MsgRxStatus == 0 ) //0 is no error
	{
		if(JavaCallback)
		{
		packedN2kMsg MsgX;
		MsgX.Timestamp = Msg.Timestamp;
		MsgX.PGN = Msg.PGN;
		MsgX.Priority = Msg.Priority;
		MsgX.SrcAddr = Msg.SrcAddr;
		MsgX.DestAddr = Msg.DestAddr;
		MsgX.Length = Msg.Length;
		memcpy(MsgX.Data, Msg.Data,Msg.Length);
		
		int len = sizeof(MsgX)-N2K_MAXLEN_TP+MsgX.Length;
	//	cout<<"Callback start"<<endl;
		JavaCallback->run(n2kMessage((signed char*)&MsgX,len));
		}
	
	}
//	cout<<"Call back succesfull"<<endl;
	
	}

}

void ActisenseWrapper::stop()
{
	ACommsClose (Handle);
	Handle = NULL;
}