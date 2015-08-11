relationalTimestampAttributeTimeIntervalMFactory(operatorVariable,pos,posEnd,clearEnd,timestampAO)::=<<


RelationalTimestampAttributeTimeIntervalMFactory $operatorVariable$MetaUpdater = new RelationalTimestampAttributeTimeIntervalMFactory(
			$pos$, 
			$posEnd$,
			$clearEnd$,
			$timestampAO.dateFormat; null="null"$,
			$timestampAO.timezone; null="null"$,
			$timestampAO.locale; null="null"$,
			$timestampAO.factor; null="null"$,
			$timestampAO.offset; null="null"$);
			
((IMetadataInitializer) $operatorVariable$PO).addMetadataUpdater($operatorVariable$MetaUpdater);
			
>>