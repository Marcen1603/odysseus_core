module TelemetriePublishSubscribe
{
	struct Pose
	{
		int X;
		int Y;
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