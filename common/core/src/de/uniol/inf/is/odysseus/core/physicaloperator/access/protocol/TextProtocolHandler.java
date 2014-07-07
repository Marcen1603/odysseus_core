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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Scanner;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class TextProtocolHandler<T> extends AbstractProtocolHandler<T> {
    private String charset;
    private String objectDelimiter;
    private Scanner scanner;
    private boolean keepDelimiter;
    private BufferedWriter writer;
    
    private Map<String,String> optionsMap;
    
    public TextProtocolHandler() {
        super();
    }

    public TextProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<T> dataHandler) {
        super(direction, access, dataHandler);
    }

    @Override
    public void open() throws UnknownHostException, IOException {
        getTransportHandler().open();
        if (getDirection().equals(ITransportDirection.IN)) {
            if ((this.getAccessPattern().equals(IAccessPattern.PULL)) || (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL))) {
                this.scanner = new Scanner(getTransportHandler().getInputStream(), charset);
                scanner.useDelimiter(objectDelimiter);
            }
        } else {
            if ((this.getAccessPattern().equals(IAccessPattern.PULL)) || (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL))) {
                writer = new BufferedWriter(new OutputStreamWriter(getTransportHandler().getOutputStream()));
            }
        }

    }

    @Override
    public void close() throws IOException {
        if (getDirection().equals(ITransportDirection.IN)) {
            if (scanner != null) {
                scanner.close();
            }
        } else {
            if (writer != null) {
                writer.close();
            }
        }
        getTransportHandler().close();
    }

    @Override
    public boolean hasNext() throws IOException {
        return scanner.hasNext();
    }

    @Override
    public T getNext() throws IOException {
        if (keepDelimiter) {
            return getDataHandler().readData(scanner.next() + objectDelimiter);
        } else {
            return getDataHandler().readData(scanner.next());
        }
    }

    @Override
    public void write(T object) throws IOException {
        writer.write(object.toString());
        writer.write(objectDelimiter);
    }

    @Override
    public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, Map<String, String> options, IDataHandler<T> dataHandler) {

        TextProtocolHandler<T> instance = new TextProtocolHandler<T>(direction, access, dataHandler);
        instance.setOptionsMap(options);

		if (options.containsKey("charset")) {
			instance.charset = options.get("charset");
		} else {
			instance.charset = "UTF-8";
		}
		if (options.containsKey("delimiter")) {
			instance.objectDelimiter = options.get("delimiter");
		} else {
			instance.objectDelimiter = ",";
		}
		if (options.containsKey("keepdelimiter")) {
			instance.keepDelimiter = Boolean.parseBoolean(options
					.get("keepdelimiter"));
		} else {
			instance.keepDelimiter = false;
		}
        return instance;
    }

    // @Override
    // public IProtocolHandler<T> createInstance(Map<String, String> options,
    // ITransportHandler transportHandler,
    // IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
    // TextProtocolHandler<T> instance = new TextProtocolHandler<T>();
    // instance.setDataHandler(dataHandler);
    // instance.setTransportHandler(transportHandler);
    // instance.setTransfer(transfer);
    //
    // instance.charset = options.get("charset");
    // instance.objectDelimiter = options.get("delimiter");
    // instance.keepDelimiter =
    // Boolean.parseBoolean(options.get("keepdelimiter"));
    // return instance;
    // }

    @Override
    public String getName() {
        return "Text";
    }

    @Override
    public ITransportExchangePattern getExchangePattern() {
        if (this.getDirection().equals(ITransportDirection.IN)) {
            return ITransportExchangePattern.InOnly;
        } else {
            return ITransportExchangePattern.OutOnly;
        }
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
		Scanner scanner = new Scanner(
				new ByteArrayInputStream(message.array()), charset);
		scanner.useDelimiter(objectDelimiter);
		while (scanner.hasNext()) {
			if (keepDelimiter) {
				getTransfer().transfer(
						getDataHandler().readData(
								scanner.next() + objectDelimiter));
			} else {
				getTransfer().transfer(
						getDataHandler().readData(scanner.next()));
			}
		}
		scanner.close();
    }
	
	@Override
	public void setOptionsMap(Map<String, String> optionsMap) {
		this.optionsMap = optionsMap;
	}
	
	@Override
	public Map<String, String> getOptionsMap() {
		return this.optionsMap;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if(!(o instanceof TextProtocolHandler)) {
			return false;
		}
		TextProtocolHandler<?> other = (TextProtocolHandler<?>)o;
		if(!this.charset.equals(other.getCharset()) ||
				!this.objectDelimiter.equals(other.getObjectDelimiter()) ||
				this.keepDelimiter != other.isKeepDelimiter()) {
			return false;
		}
		return true;
	}

	public String getCharset() {
		return charset;
	}

	public String getObjectDelimiter() {
		return objectDelimiter;
	}

	public boolean isKeepDelimiter() {
		return keepDelimiter;
	}

}
