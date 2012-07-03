package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class CSVProtocolHandler<T> extends LineProtocolHandler<T> {

	private char delimiter;

	@Override
	public T getNext() throws IOException {
		List<String> ret = new LinkedList<String>();
		String line = reader.readLine();
		if (line != null) {
			StringBuffer elem = new StringBuffer();
			boolean overreadModus1 = false;
			boolean overreadModus2 = false;
			for (char c : line.toCharArray()) {
				if (c == '\"') {
					overreadModus1 = !overreadModus1;
					//elem.append(c);
				} else if (c == '\'') {
					overreadModus2 = !overreadModus2;
					//elem.append(c);
				} else {
					if (overreadModus1 || overreadModus2) {
						elem.append(c);
					} else {
						if (delimiter == c) {
							ret.add(elem.toString());
							elem = new StringBuffer();
						} else {
							elem.append(c);
						}
					}

				}
			}
			ret.add(elem.toString());
			return getDataHandler().readData(ret);
		}
		return null;
	}

	@Override
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		CSVProtocolHandler<T> instance = new CSVProtocolHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);
		instance.delimiter = options.get("delimiter").toCharArray()[0];
		return instance;
	}

	@Override
	public String getName() {
		return "CSV";
	}

}
