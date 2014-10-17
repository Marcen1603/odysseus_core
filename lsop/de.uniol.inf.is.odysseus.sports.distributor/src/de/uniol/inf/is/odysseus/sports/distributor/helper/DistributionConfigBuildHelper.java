package de.uniol.inf.is.odysseus.sports.distributor.helper;

public class DistributionConfigBuildHelper {
	
	
	//TODO enum über OSGI 
	public static enum PARTITION
	{
		OPERATORCLOUD, OPERATORSETCLOUD, QUERYCLOUD, USER
	}
	
	public static enum ALLOCATE
	{
		ROUNDROBIN, LOADBALANCING, DIRECT, ROUNDROBINWITHLOCAL, ROUNDROBINWITHUSER, SURVEY, USER
	}
	
	
	public static enum PARSER
	{
		SportsQL
	}
		
	
	public static String createDistribute(){
		return "#CONFIG DISTRIBUTE true\n";
	}
	
	public static String createAddQuery(){
		return "#ADDQUERY\n";
	}
	
	public static String createParser(PARSER parser){
		return "#PARSER "+parser.toString()+"\n";
	}
	
	public  static String createPeerPartition(PARTITION partition){
		return "#PEER_PARTITION "+partition.toString()+"\n";
	}
	
	public static String createPeerAllocate(ALLOCATE allocate){
		return "#PEER_ALLOCATE "+allocate.toString()+"\n";
	}
}
