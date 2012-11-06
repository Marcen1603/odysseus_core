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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class FileHandler extends AbstractTransportHandler {

    String           filename;
    FileInputStream  in;
    FileOutputStream out;

    public FileHandler() {
        super();
    }

    public FileHandler(IProtocolHandler<?> protocolHandler) {
        super(protocolHandler);
    }

    @Override
    public void processInOpen() throws  IOException {
        final File file = new File(filename);
        try {
            in = new FileInputStream(file);
            fireOnConnect();
        }
        catch (Exception e) {
            fireOnDisconnect();
            throw e;
        }
    }

    @Override
    public void processOutOpen() throws  IOException {
        final File file = new File(filename);
        try {
            out = new FileOutputStream(file, true);
            fireOnConnect();
        }
        catch (Exception e) {
            fireOnDisconnect();
            throw e;
        }
    }

    @Override
    public void processInClose() throws IOException {
        fireOnDisconnect();
        in.close();
    }

    @Override
    public void processOutClose() throws IOException {
        fireOnDisconnect();
        out.flush();
        out.close();
    }

    @Override
    public void send(byte[] message) throws IOException {
        out.write(message);
    }

    @Override
    public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        FileHandler fh = new FileHandler(protocolHandler);
        fh.filename = options.get("filename");
        return fh;
    }

    @Override
    public InputStream getInputStream() {
        return in;
    }

    @Override
    public OutputStream getOutputStream() {
        return out;
    }

    @Override
    public String getName() {
        return "File";
    }

}
