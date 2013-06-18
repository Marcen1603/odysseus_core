package de.uniol.inf.is.odysseus.wsenrich.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

/* Note: Based on a copy of TransportHandlerRegistry */
public class RequestBuilderRegistry {

		static Logger logger = LoggerFactory
				.getLogger(RequestBuilderRegistry.class);

		static private Map<String, IRequestBuilder> builders = new HashMap<String, IRequestBuilder>();

		static public void register(IRequestBuilder builder) {
			logger.debug("Register new Handler " + builder.getName());
			if (!builders.containsKey(builder.getName().toLowerCase())) {
				builders.put(builder.getName().toLowerCase(), builder);
			} else {
				logger.error("Handler with name " + builder.getName()
						+ " already registered");
			}
		}
		
		static public void remove(IRequestBuilder builder){
			logger.debug("Remove handler "+builder.getName());
			builders.remove(builder.getName().toLowerCase());
		}
		
		static public IRequestBuilder getInstance(String name){
			IRequestBuilder ret = builders.get(name.toLowerCase());
			if (ret != null){
				return ret.createInstance();
			}
			logger.error("No handler with name "+name+" found!");
			return null;
		}
		
		public static ImmutableList<String> getHandlerNames() {
			return ImmutableList.copyOf(builders.keySet());
		}

	}
