package de.uniol.inf.is.odysseus.sports.distributor.helper;

import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Allocate;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Modification;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Parser;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Partition;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Postprocessor;


/**
 * Class with static methods which help you to create a DistributionConfig for a
 * Distributor
 * 
 * @author Marc Preuschaft
 * @since 22.10.2014
 *
 */

public class DistributionConfigBuildHelper {
	
		
	public static String createDistribute(){
		return "#CONFIG DISTRIBUTE true\n";
	}
	
	public static String createAddQuery(){
		return "#ADDQUERY\n";
	}
	
	public static String createRunQuery(){
		return "#RUNQUERY\n";
	}
	
	/**
	 * 
	 * @param parser
	 * @return
	 */
	public static String createParser(Parser parser){
		return "#PARSER "+parser.toString()+"\n";
	}
	
	/**
	 * 
	 * @param partition
	 * @return
	 */
	public  static String createPeerPartition(Partition partition){
		return "#PEER_PARTITION "+partition.toString()+"\n";
	}
	
	/**
	 * 
	 * @param allocate
	 * @return
	 */
	public static String createPeerAllocate(Allocate allocate){
		return "#PEER_ALLOCATE "+allocate.toString()+"\n";
	}
	
	/**
	 * 
	 * @param postprocessor
	 * @return
	 */
	public static String createPeerPostProcessor(Postprocessor postprocessor){
	 return "#PEER_POSTPROCESSOR "+postprocessor.toString()+"\n";
	}
	
	public static String createPeerModification(Modification modification, String parameter){
		return "#PEER_MODIFICATION "+modification.toString()+" "+parameter +"\n";
	}
	
	public static String createPeerModification(Modification modification){
		return "#PEER_MODIFICATION "+modification.toString() +"\n";
	}
		
	
}
