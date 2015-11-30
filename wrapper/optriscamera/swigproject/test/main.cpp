#include <iostream>
#include "..\OptrisCamera.h"

using namespace std;

class MyOptrisCamera : public OptrisCamera
{
public:
	MyOptrisCamera() : OptrisCamera("", "")
	{
	}

	virtual void onNewFrame(long long timeStamp, TFlagState flagState, void *buffer, long size)
	{
		short pixel = *((short*)buffer);
		cout << "New Frame: 1st pixel = " << pixel << endl;
	}
};

int main(int argc, char** argv)
{
	int i;
	cout << "Optris Camera Test" << endl;
	try
	{
		MyOptrisCamera cam;
		cam.start();

		cout << "Optris Camera started" << endl;

		cin >> i;

		cam.stop();
	
		cout << "Optris Camera stopped" << endl;		
	}
	catch (std::exception& e)
	{
		cout << "Exception: " << e.what() << endl;
	}

	cin >> i;

	return 0;
}
