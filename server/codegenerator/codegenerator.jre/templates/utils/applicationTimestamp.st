applicationTimestamp(operatorVariable,year, month, day, hour, minute, second, millisecond,factor, clearEnd, timezone)::=<<



RelationalTimestampAttributeTimeIntervalMFactory $operatorVariable$MetaUpdater = new RelationalTimestampAttributeTimeIntervalMFactory(
						$year$, $month$, $day$, $hour$, $minute$, $second$, $millisecond$,$factor$, $clearEnd$, $timezone$);
			
((IMetadataInitializer) $operatorVariable$PO).addMetadataUpdater($operatorVariable$MetaUpdater);

>>