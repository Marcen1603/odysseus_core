systemTimeTimestamp(timestampAO,targetOperatorVariable)::=<<

SystemTimeIntervalFactory<ITimeInterval, IStreamObject<ITimeInterval\>> $targetOperatorVariable$mUpdater = new SystemTimeIntervalFactory<ITimeInterval, IStreamObject<ITimeInterval\>>();		
$targetOperatorVariable$mUpdater.clearEnd($timestampAO.clearEnd$);

((IMetadataInitializer) $targetOperatorVariable$PO).addMetadataUpdater($targetOperatorVariable$mUpdater);

>>