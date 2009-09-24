/*
 * Created on 19.01.2005
 *
 */
package de.uniol.inf.is.odysseus.base.wrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

/**
 * @author Marco Grawunder
 * 
 */
public class WrapperPlanFactory {
	
	private static final Logger logger = LoggerFactory.getLogger( WrapperPlanFactory.class );
	
	//private static final String STREAM_DEFINITION_FILE = "input_stream_definition";
	@SuppressWarnings("unchecked")
	private static Map<String, ISource> sources = new HashMap<String, ISource>();
//	private static boolean initFunctions = true;

	public synchronized static void init() throws Exception {
//		if (initFunctions) {
//			SDFExpression.addFunction(new DolToEur());
//			SDFExpression.addFunction(new Now());
//		}
//		InputStream input = WrapperPlanFactory.class
//				.getResourceAsStream(STREAM_DEFINITION_FILE);
//		if (input == null) {
//			throw new Exception("no stream definition file");
//		}
//		InputStreamReader reader = new InputStreamReader(input);
//		CQLParser.parse(reader);
//		System.out.println("WPF: Sources initialized from "
//				+ STREAM_DEFINITION_FILE);
		// add linearroadstuff for testing purposes
		logger.debug("init wrapper");
		DataDictionary.getInstance();
//		LinearRoadSource lrSource = new LinearRoadSource();
//		sources.put("linearroad", lrSource);
	}

	@SuppressWarnings("unchecked")
	public synchronized static ISource getAccessPlan(String uri) {
		ISource po = sources.get(uri);
		return po;
	}

	public synchronized static ISource getAccessPlan(AccessAO ao) throws IOException {
		String name = ao.getSource().toString();
		ISource ret = getAccessPlan(name);
//		if (ret == null){
//			
//			if (ao instanceof FixedSetAccessAO){
//				ret = new FixedSetPO(((FixedSetAccessAO)ao).getTuples());
//			}else{
//			
//				SourceType st = ao.getSource().getSourceType();			 
//				switch (st){
//				case RelationalInputStreamAccessPO:
//					ret = new InputStreamAccessPO(ao.getHost(),ao.getPort(),new IdentityTransformation());
//					break;
//				case RelationalByteBufferAccessPO:
//					ret = new ByteBufferReceiverPO(new RelationalTupleObjectHandler(ao.getOutputSchema()), ao.getHost(), ao.getPort());
//					break;
//				case RelationalAtomicDataInputStreamAccessPO:
//					ret = new AtomicDataInputStreamAccessPO(ao.getHost(), ao.getPort(),	ao.getOutputSchema());
//					break;
//				case RelationalAtomicDataInputStreamAccessMVPO:
//					ret = new AtomicDataInputStreamAccessMVPO(ao.getHost(), ao.getPort(), ao.getOutputSchema());
//					break;
//				case OSGI:
//					ret = new WrapperArchitectureAccessPO(((WrapperArchitectureAO)ao).getFilter(), ao.getOutputSchema());
//					break;
//				case Relational:
//				case RelationalStreaming: 
//					ret = new AtomicDataInputStreamAccessPO(ao.getHost(), ao.getPort(),	ao.getOutputSchema());
//					break;
//				case RDFStreaming:
//					// Ansonsten kann es eigentlich nur ein View sein 
//					// und dann sollte diese Methode nicht aufgerufen werden, wenn der Plan
//					// vorher nicht �bersetzt worden ist !!
//					throw new IllegalArgumentException("Not Supported Access Type");
//				}
//			}
//			putAccessPlan(name, ret);
//		}
		return ret;
	}

	public synchronized static void putAccessPlan(String uri, ISource s) {
		sources.put(uri, s);
	}
	
	//Fuer P2P
	public static Map<String, ISource> getSources() {
		return sources;
	}

}
