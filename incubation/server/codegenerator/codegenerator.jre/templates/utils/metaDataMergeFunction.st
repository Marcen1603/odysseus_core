metaDataMergeFunction(operatorVariable,metaAttributeNames)::=<<
	List<String> $operatorVariable$MetadataSet = new ArrayList<String>();
	
	
	$metaAttributeNames : {metaAttribute |
		$operatorVariable$MetadataSet.add("$metaAttribute$");
}$
	

	// Attention: Time meta data is set in aggregation
	$operatorVariable$MetadataSet.remove(ITimeInterval.class.getName());
	@SuppressWarnings("rawtypes")
	IMetadataMergeFunction $operatorVariable$Mf = metadataregistry.getMergeFunction($operatorVariable$MetadataSet);

>>