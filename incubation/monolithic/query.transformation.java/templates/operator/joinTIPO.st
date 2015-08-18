joinTIPO(operatorVariable,input0MetaAttributeNamesCode, input1MetaAttributeNamesCode,input0MetaAttributeNamesVariable, input1MetaAttributeNamesVariable)::=<<

		$input0MetaAttributeNamesCode$
		
		$input1MetaAttributeNamesCode$

		IMetadataMergeFunction<?> $operatorVariable$metaDataMerge = MetadataRegistry.getMergeFunction($input0MetaAttributeNamesVariable$,
						$input1MetaAttributeNamesVariable$);
		
		JoinTIPO $operatorVariable$PO = new JoinTIPO($operatorVariable$metaDataMerge);

>>