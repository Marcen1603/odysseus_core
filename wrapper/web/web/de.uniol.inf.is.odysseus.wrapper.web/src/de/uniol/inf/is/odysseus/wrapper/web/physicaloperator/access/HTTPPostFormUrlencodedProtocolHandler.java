/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.web.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Protocol handler for HTTP posts with MIME type
 * application/x-www-form-urlencoded
 * 
 * @author Cornelius Ludmann
 *
 * @param <T>
 */
public class HTTPPostFormUrlencodedProtocolHandler<T extends Tuple<?>> extends AbstractProtocolHandler<T> {

	private static final Logger LOG = LoggerFactory.getLogger(HTTPPostFormUrlencodedProtocolHandler.class);

	public HTTPPostFormUrlencodedProtocolHandler() {
		super();
	}

	private HTTPPostFormUrlencodedProtocolHandler(final ITransportDirection direction, final IAccessPattern access,
			final IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * IProtocolHandler#createInstance(de.uniol.inf.is.odysseus.core.
	 * physicaloperator.access.transport.ITransportDirection,
	 * de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.
	 * IAccessPattern, de.uniol.inf.is.odysseus.core.collection.OptionMap,
	 * de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler)
	 */
	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		final HTTPPostFormUrlencodedProtocolHandler<T> instance = new HTTPPostFormUrlencodedProtocolHandler<>(direction,
				access, dataHandler, options);

		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * IProtocolHandler#getName()
	 */
	@Override
	public String getName() {
		return "PostFormUrlencoded";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * AbstractProtocolHandler#isSemanticallyEqualImpl(de.uniol.inf.is.odysseus.
	 * core.physicaloperator.access.protocol.IProtocolHandler)
	 */
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * AbstractProtocolHandler#process(long, java.nio.ByteBuffer)
	 */
	@Override
	public void process(long callerId, ByteBuffer message) {
		String str = getCharset().decode(message).toString();
		getTransfer().transfer(getDataHandler().readData(parseAttributes(str).iterator()));
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler#process(java.lang.String[])
	 */
	@Override
	public void process(String[] message) {
		getTransfer().transfer(getDataHandler().readData(parseAttributes(message[0]).iterator()));
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler#process(java.io.InputStream)
	 */
	@Override
	public void process(InputStream message) {
		try {
			getTransfer().transfer(getDataHandler().readData(parseAttributes(IOUtils.toString(message)).iterator()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public List<String> parseAttributes(String message) {
		String[] attr = message.split("&");
		for (int i = 0; i < attr.length; ++i) {
			try {
				String[] keyValue = attr[i].trim().split("=");
				if (keyValue.length != 2) {
					LOG.warn("Could not split " + i + "th paramter '" + attr[i].trim() + "' into key and value.");
					attr[i] = null;
				} else {
					attr[i] = URLDecoder.decode(keyValue[1], "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				LOG.warn("Unsupported encoding: " + e.getMessage());
				attr[i] = null;
			}
		}
		return Arrays.asList(attr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * AbstractProtocolHandler#write(de.uniol.inf.is.odysseus.core.metadata.
	 * IStreamObject)
	 */
	@Override
	public void write(T object) throws IOException {
		final SDFSchema schema = this.getDataHandler().getSchema();
		int noOfAttr = schema.size();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < schema.size(); i++) {
			final String attr = schema.get(i).getAttributeName();
			sb.append(URLEncoder.encode(attr, "UTF-8")).append("=");
			sb.append(URLEncoder.encode(attr, object.getAttribute(i).toString()));
			if (i != noOfAttr - 1) {
				sb.append("&");
			}
		}

		super.write(object);
	}

}