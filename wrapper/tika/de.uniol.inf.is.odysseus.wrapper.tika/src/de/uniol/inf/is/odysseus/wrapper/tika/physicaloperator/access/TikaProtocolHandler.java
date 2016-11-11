/*******************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.wrapper.tika.physicaloperator.access;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.LineProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class TikaProtocolHandler extends LineProtocolHandler<KeyValueObject<IMetaAttribute>> {
    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TikaProtocolHandler.class);
    /** The name of the protocol handler. */
    public static final String NAME = "Tika";
    /** The parser. */
    private final AutoDetectParser parser;

    /**
     *
     */
    public TikaProtocolHandler() {
        super();
        this.parser = new AutoDetectParser();
    }

    /**
     *
     * @param direction
     * @param access
     * @param dataHandler
     */
    public TikaProtocolHandler(final ITransportDirection direction, final IAccessPattern access, final IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler, OptionMap optionsMap ) {
        super(direction, access, dataHandler, optionsMap);
        this.parser = new AutoDetectParser();
        init_internal();
    }

    private void init_internal() {
        Map<MediaType, Parser> parsers = this.parser.getParsers();
        LOG.debug("Available parsers int Tika:");
        for (Entry<MediaType, Parser> p : parsers.entrySet()) {
            LOG.debug(p.getKey() + ": " + p.getValue());
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return TikaProtocolHandler.NAME;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IProtocolHandler<KeyValueObject<IMetaAttribute>> createInstance(final ITransportDirection direction, final IAccessPattern access, final OptionMap options,
            final IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler) {
        final TikaProtocolHandler instance = new TikaProtocolHandler(direction, access, dataHandler, options);
        return instance;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public KeyValueObject<IMetaAttribute> getNext() throws IOException {
        final String line = super.getNextLine(reader);

        KeyValueObject<IMetaAttribute> object = null;
        final Map<String, Object> event = new HashMap<>();
        final Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        StringWriter stringWriter = new StringWriter();

        // Wrap message into an InputStream
        ByteArrayInputStream stream = new ByteArrayInputStream(line.getBytes());
        try {
            this.parser.parse(stream, new BodyContentHandler(stringWriter), metadata, context);
            String content = stringWriter.toString();
            if (metadata.get(HttpHeaders.CONTENT_LANGUAGE) == null) {
                LanguageIdentifier identifier = new LanguageIdentifier(content);
                String language = identifier.getLanguage();
                metadata.add(HttpHeaders.CONTENT_LANGUAGE, language);
            }
            for (final String key : metadata.names()) {
                event.put(key.toLowerCase(Locale.US), metadata.get(key));
            }
            event.put("content", content);

            object = KeyValueObject.createInstance(event);
        }
        catch (IOException | SAXException | TikaException e) {
            LOG.error(e.getMessage(), e);
        }

        return object;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void process(long callerId, final ByteBuffer message) {
		// TODO: check if callerId is relevant
        KeyValueObject<IMetaAttribute> object = null;
        final Map<String, Object> event = new HashMap<>();
        final Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        StringWriter stringWriter = new StringWriter();
        // Wrap message into an InputStream
        ByteArrayInputStream stream = new ByteArrayInputStream(message.array());
        try {
            this.parser.parse(stream, new BodyContentHandler(stringWriter), metadata, context);
            String content = stringWriter.toString();
            if (metadata.get(HttpHeaders.CONTENT_LANGUAGE) == null) {
                LanguageIdentifier identifier = new LanguageIdentifier(content);
                String language = identifier.getLanguage();
                metadata.add(HttpHeaders.CONTENT_LANGUAGE, language);
            }
            for (final String key : metadata.names()) {
                event.put(key.toLowerCase(Locale.US), metadata.get(key));
            }
            event.put("content", content);

            object = KeyValueObject.createInstance(event);
            this.getTransfer().transfer(object);
        }
        catch (IOException | SAXException | TikaException e) {
            LOG.error(e.getMessage(), e);
        }

    }


    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void write(final KeyValueObject<IMetaAttribute> object) throws IOException {
        throw new IllegalArgumentException("write not supported");
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;
    }


}
