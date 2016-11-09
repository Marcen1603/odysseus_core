package de.uniol.inf.is.odysseus.wrapper.xovis.communication.util;

import java.io.IOException;
import java.util.ArrayList;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessageLite;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.gpbObjects.XovisEventObj.XovisEvent;

public class XovisEventStreamListener extends AXovisStreamListener {

	public XovisEventStreamListener(CodedInputStream input,
			ArrayList<? extends GeneratedMessageLite> objList) {
		super(input, objList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void parseStream() {
		/*
		 * Receive the sensor stream continuously. Here the tcp packets are
		 * received and parsed to events.
		 */
		// while (!t.isInterrupted()) {
		try {
			// read the size of the subsequent message
			int size = inputStream.readRawVarint32();
//			if (size > 0) {
//				System.err.println("Something was received");
//			}
			// set the limit of bytes to read to the value of the
			// varint, so only the
			// bytes representing the message are read; save the old
			// limit value
			int oldLimit = inputStream.pushLimit(size);
			// parse all following bytes from the stream until the limit
			// which has
			// been set a line before is reached, generate the event object
			XovisEvent event = XovisEvent.parseFrom(inputStream);
			((ArrayList<XovisEvent>) objectList).add(event);

			// reset the limit to the old value again
			inputStream.popLimit(oldLimit);
			// process the received event (print it)

			KeyValueObject<IMetaAttribute> out = KeyValueObject.createInstance();
			out.setAttribute("timestamp", event.getTimeStampMilliseconds());
			out.setAttribute("objectID", event.getObjectId());
			out.setAttribute("eventType", event.getType());
			if (event.hasCountItemName()) {
				out.setAttribute("cItemName", event.getCountItemName());
			} else {
				out.setAttribute("cItemName", "noName");
			}
			out.setAttribute("status", event.getStatus());
			out.setAttribute("height", event.getObjectHeight());

			getkVPairs().add(out);

			// System.out.println("Object "
			// + event.getObjectId()
			// + " triggered event "
			// + event.getType()
			// + " at "
			// + event.getTimeStampMilliseconds()
			// + (event.hasCountItemName() ? " in / at count item "
			// + event.getCountItemName()
			// : "") + ", status is: "
			// + event.getStatus() + ", object height: "
			// + event.getObjectHeight() + " mm");
			// leave the while loop if stream is not intact
			// if (!event.getStatus().equals(XovisEvent.Status.OK)) {
			// break;
			// }
		} catch (IOException e) {
			// e.printStackTrace();
			// System.out.print("e");
		}
	}

	// }
}
