/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class CSVProtocolHandler<T extends ByteBuffer> extends LineProtocolHandler<T> {

    private char    textDelimiter;
    private char    delimiter;
    private boolean readFirstLine    = true;
    private boolean firstLineSkipped = false;

    public CSVProtocolHandler() {
        super();
    }

    public CSVProtocolHandler(ITransportDirection direction, IAccessPattern access) {
        super(direction, access);
    }

    @Override
    public T getNext() throws IOException {
        delay();
        if (!firstLineSkipped && !readFirstLine) {
            reader.readLine();
            firstLineSkipped = true;
        }
        String line = reader.readLine();
        if (line != null) {
            return read(line);
        }
        return null;
    }

    @Override
    public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access,
            Map<String, String> options, IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
        CSVProtocolHandler<T> instance = new CSVProtocolHandler<T>(direction, access);
        instance.setDataHandler(dataHandler);
        instance.setTransfer(transfer);
        instance.delimiter = options.containsKey("delimiter") ? options.get("delimiter").toCharArray()[0] : ","
                .toCharArray()[0];
        instance.textDelimiter = options.containsKey("textdelimiter") ? options.get("textdelimiter").toCharArray()[0]
                : "'".toCharArray()[0];

        if (options.get("readfirstline") != null) {
            instance.readFirstLine = Boolean.parseBoolean(options.get("readfirstline"));
        }
        else {
            readFirstLine = true;
        }
        if (options.get("delay") != null) {
            instance.setDelay(Long.parseLong(options.get("delay")));
        }
        return instance;
    }

    @Override
    public String getName() {
        return "CSV";
    }

    @Override
    public void onConnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisonnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void process(ByteBuffer message) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(message.array())));
        if (!firstLineSkipped && !readFirstLine) {
            try {
                reader.readLine();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            firstLineSkipped = true;
        }
        String line;
        try {
            line = reader.readLine();
            if (line != null) {
                T retValue = read(line);
                System.out.println(retValue);
                getTransfer().transfer(retValue);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private T read(String line) {
        List<String> ret = new LinkedList<String>();
        StringBuffer elem = new StringBuffer();
        boolean overreadModus1 = false;
        boolean overreadModus2 = false;

        for (char c : line.toCharArray()) {

            if (c == textDelimiter) {
                overreadModus1 = !overreadModus1;
                // elem.append(c);
            }
            else {
                if (overreadModus1 || overreadModus2) {
                    elem.append(c);
                }
                else {
                    if (delimiter == c) {
                        ret.add(elem.toString());
                        elem = new StringBuffer();
                    }
                    else {
                        elem.append(c);
                    }
                }

            }
        }
        ret.add(elem.toString());
        T retValue = getDataHandler().readData(ret);
        return retValue;
    }
}
