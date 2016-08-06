package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferUtil;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class AddQueryPartMessage implements IMessage {
	
	private static final Logger logger = LoggerFactory.getLogger(AddQueryPartMessage.class);

	private String pqlStatement;
	private UUID sharedQueryID;
	private String transCfgName;
	private int queryPartID;
	private Collection<String> metadataTypes;
	private Collection<IQueryBuildSetting<?>> settings = new ArrayList<>();

	public AddQueryPartMessage() {

	}

	public AddQueryPartMessage(UUID sharedQueryID, String pqlStatement, String transCfgName, int queryPartID, Collection<String> metadataTypes, Collection<IQueryBuildSetting<?>> settings) {
		this.pqlStatement = pqlStatement;
		this.sharedQueryID = sharedQueryID;
		this.transCfgName = transCfgName;
		this.queryPartID = queryPartID;
		this.metadataTypes = metadataTypes;
		for(IQueryBuildSetting<?> setting : settings) {
			if(setting instanceof Serializable) {
				this.settings.add(setting);
			} else {
				logger.warn("Ignoring not serializabl query build setting", setting);
			}
		}
		this.settings = settings;
	}

	@Override
	public byte[] toBytes() {
		byte[] pqlStatementBytes = pqlStatement.getBytes();
		byte[] sharedQueryIDBytes = sharedQueryID.toString().getBytes();
		byte[] transCfgNameBytes = transCfgName.getBytes();

		int bbSize = 4 + 4 + pqlStatementBytes.length + 4 + sharedQueryIDBytes.length + 4 + transCfgNameBytes.length;
		bbSize += (4 + (metadataTypes.size() * 4) + calcByteSizeOfStringCollection(metadataTypes));
		bbSize += (4 + (settings.size() * 4) + calcByteSizeOfQueryBuildSettingCollection(settings));
		ByteBuffer bb = ByteBuffer.allocate(bbSize);

		bb.putInt(queryPartID);
		bb.putInt(pqlStatementBytes.length);
		bb.putInt(sharedQueryIDBytes.length);
		bb.putInt(transCfgNameBytes.length);

		bb.put(pqlStatementBytes);
		bb.put(sharedQueryIDBytes);
		bb.put(transCfgNameBytes);

		bb.putInt(metadataTypes.size());
		for (String metadataType : metadataTypes) {
			byte[] metadataTypeBytes = metadataType.getBytes();
			bb.putInt(metadataTypeBytes.length);
			bb.put(metadataTypeBytes);
		}
		
		bb.putInt(settings.size());
		for (IQueryBuildSetting<?> setting : settings) {
			try {
				byte[] settingBytes = ByteBufferUtil.toByteArray((Serializable) setting);
				bb.putInt(settingBytes.length);
				bb.put(settingBytes);
			} catch(IOException e) {
				logger.error("Can not serialize query build setting {}!", setting, e);
				continue;
			}
		}

		bb.flip();

		return bb.array();
	}

	private static int calcByteSizeOfStringCollection(Collection<String> list) {
		int size = 0;
		for (String txt : list) {
			size += (txt.getBytes().length);
		}
		return size;
	}
	
	private static int calcByteSizeOfQueryBuildSettingCollection(Collection<IQueryBuildSetting<?>> list) {
		int size = 0;
		for (IQueryBuildSetting<?> setting : list) {
			try {
				size += ByteBufferUtil.toByteArray((Serializable) setting).length;
			} catch (IOException e) {
				logger.error("Can not serialize query build setting {}!", setting, e);
				continue;
			}
		}
		return size;
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);

		queryPartID = bb.getInt();
		int pqlStatementLength = bb.getInt();
		int sharedQueryIDLength = bb.getInt();
		int transCfgNameLength = bb.getInt();

		byte[] pqlStatementBytes = new byte[pqlStatementLength];
		byte[] sharedQueryIDBytes = new byte[sharedQueryIDLength];
		byte[] transCfgNameBytes = new byte[transCfgNameLength];

		bb.get(pqlStatementBytes);
		bb.get(sharedQueryIDBytes);
		bb.get(transCfgNameBytes);

		pqlStatement = new String(pqlStatementBytes);
		sharedQueryID = UUID.fromString(new String(sharedQueryIDBytes));
		transCfgName = new String(transCfgNameBytes);
		
		int metadataTypeCount = bb.getInt();
		metadataTypes = Lists.newArrayList();
		for( int i = 0; i < metadataTypeCount; i++ ) {
			int size = bb.getInt();
			byte[] typeBytes = new byte[size];
			bb.get(typeBytes);
			metadataTypes.add( new String(typeBytes));
		}
		
		int settingsCount = bb.getInt();
		for( int i = 0; i < settingsCount; i++ ) {
			try {
				int size = bb.getInt();
				byte[] settingBytes = new byte[size];
				bb.get(settingBytes);
				settings.add((IQueryBuildSetting<?>) ByteBufferUtil.fromByteArray(settingBytes));
			} catch (IOException | ClassNotFoundException e) {
				logger.error("Can not deserialize query build setting!", e);
				continue;
			}
		}
	}

	public String getPqlStatement() {
		return pqlStatement;
	}

	public UUID getSharedQueryID() {
		return sharedQueryID;
	}

	public String getTransCfgName() {
		return transCfgName;
	}

	public int getQueryPartID() {
		return queryPartID;
	}
	
	public Collection<String> getMetadataTypes() {
		return metadataTypes;
	}
	
	public Collection<IQueryBuildSetting<?>> getQueryBuildSettings() {
		return settings;
	}
}
