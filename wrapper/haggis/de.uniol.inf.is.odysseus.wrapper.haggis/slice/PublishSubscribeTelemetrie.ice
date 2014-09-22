module TelemetriePublishSubscribe
{
	struct Pose
	{
		long timestamp;
		int X;
		int Y;
		int Z;
		float orientation;
	};

	interface TelemetrieSubscriber
	{
		void notify( Pose p );
	};

	interface TelemetriePublisher
	{
		void subscribe( TelemetrieSubscriber * sub );
	};
};