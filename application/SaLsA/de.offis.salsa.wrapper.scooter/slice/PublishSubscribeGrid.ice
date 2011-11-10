module GridPublishSubscribe {

	sequence<byte> GridData;

	struct GridStruct {
		int width;
		int height;
		double cellsize;
		GridData data;
	};

	interface GridSubscriber
	{
		void notify(long timestamp, double x, double y, GridStruct grid);
	};

	interface GridPublisher
	{
		void subscribe(GridSubscriber * sub);
	};

};
