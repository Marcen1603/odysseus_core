module GridPublishSubscribe {

	sequence<byte> GridData;

	struct GridStruct {
		long timestamp;
		double x;
		double y;
		int width;
		int height;
		double cellsize;
		GridData data;
	};

	interface GridSubscriber
	{
		void notify(GridStruct grid);
	};

	interface GridPublisher
	{
		void subscribe(GridSubscriber * sub);
	};

};
