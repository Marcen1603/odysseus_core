package de.uniol.inf.is.odysseus.wrapper.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;


/**
 * @author Marc Preuschaft
 *
 */
public class FacebookProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {

	protected BufferedReader reader;
	private Long lastTimeStamp = null;
	private boolean firstQuery = true;


	@SuppressWarnings("rawtypes")
	List<KeyValueObject> facebookMessages = new ArrayList<KeyValueObject>();

	public FacebookProtocolHandler() {
	}

	public FacebookProtocolHandler(
			ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler,OptionMap optionsMap) {
		super(direction, access,dataHandler,optionsMap);
	}

	@Override
	public boolean hasNext() throws IOException {
		if (facebookMessages.size() > 0) {
			return true;
		}
		intern_open();
		return false;
	}

	private void intern_open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		reader = new BufferedReader(new InputStreamReader(getTransportHandler()
				.getInputStream()));

		try {
			String jsonText = readAll(reader);
			JSONObject json = new JSONObject(jsonText);
			JSONArray requestarray = (JSONArray) json.get("data");

			for (int i = requestarray.length() - 1; i > 0; i--) {
				JSONObject singlePost = requestarray.getJSONObject(i);
				if (singlePost.has("message")) {

					String message = singlePost.getString("message");
					String id = singlePost.getString("id");
					String created_time = singlePost.getString("created_time");

					if (firstQuery|| getTimeStamp(created_time).getTime() > lastTimeStamp && !(id.equals("empty"))) {
						KeyValueObject<IMetaAttribute> out = KeyValueObject.createInstance();
						out.setAttribute("message", message);
						out.setAttribute("id", id);
						out.setAttribute("created_time", created_time);

						lastTimeStamp = getTimeStamp(created_time).getTime();

						facebookMessages.add(out);
						firstQuery = false;
					}

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		intern_close();

	}

	private void intern_close() throws IOException {
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			getTransportHandler().close();
		}
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		intern_open();
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {

			@SuppressWarnings("unchecked")
			KeyValueObject<IMetaAttribute> out = facebookMessages.get(0);
			facebookMessages.remove(0);
			return out;
	}

	@Override
	public void close() throws IOException {
		intern_close();
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			OptionMap options,
			IStreamObjectDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {

		FacebookProtocolHandler instance = new FacebookProtocolHandler(direction, access, dataHandler,options);
		return instance;
	}

	@Override
	public String getName() {
		return "Facebook";
	}

	/**
	 * read the JSONObject and return it as a string
	 * @param rd
	 * @return
	 * @throws IOException
	 */
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}



	/**
	 * convert the Facebook-TimeStamp to java timestamp
	 * @param createdTime
	 * @return
	 */
	private Timestamp getTimeStamp(String createdTime) {
		try {
			DateFormat formatterS;
			formatterS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSS");
			Date datet = formatterS.parse(createdTime);
			java.sql.Timestamp timeStampDate = new Timestamp(datet.getTime());

			return timeStampDate;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if(!(o instanceof FacebookProtocolHandler)) {
			return false;
		}
		// the datahandler was already checked in the isSemanticallyEqual-Method of AbstracProtocolHandler
		return true;
	}
}
