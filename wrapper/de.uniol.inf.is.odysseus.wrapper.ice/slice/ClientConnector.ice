#ifndef ReACT_ICE_
#define ReACT_ICE_ 

module ReACT {
	sequence<byte> ByteSeq;
	struct JPGImage{
		ByteSeq data;	
	};
	struct ScannerData{
		int frontLeft;
		int frontRight;
		int left;
		int right;
		int backLeft;
		int backRight;
	};
	
	struct ScooterPos{
		int x;
		int y;
		double heading;
	};
	
	interface ClientCommunicator {
		int moveScooter(double steerAngle, double speed,int id);
		bool emergency(int id);
		bool exit(int id);
		bool setWaypoint(int x,int y,int id);
		bool setMaxSpeed(int id, float maxSpeed);
		bool stopNavigation(int id, bool activated);
		bool enableLaserscanner(bool enablescanner,int id);
		ScannerData getLaserScannerData(int id);
		int connect(string user, string passwd, bool steering, bool capture, bool scanner, bool navigation);
		JPGImage getMap(int id);
		JPGImage getCapture(int width,int height,int id);
		int getMapHeight(int id);
		int getMapWidth(int id);
		ScooterPos getScooterPos(int id);
		bool enableLineFolow(bool enabled, int id);
	};
	
	interface ImageListener
	{
   	 void receive(JPGImage img, long sendTime); 
	};

	interface ImageServer
	{
    	void registerListener(ImageListener* proxy, int width, int height, int quality);
    	void removeListener(ImageListener* proxy);
	};
};

#endif //ReACT_ICE_