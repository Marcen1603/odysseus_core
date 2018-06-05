package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;

/**
 * Specialized DatabaseKeyVault for storing EncSymKeys.
 * 
 * @author MarkMilster
 *
 */
public class DbEncSymKeyVault extends AbstractDbKeyVault implements IDbEncSymKeyVault {

	/**
	 * Default constructor, which uses the default Configuration file.
	 */
	public DbEncSymKeyVault() {
		super(AbstractDbKeyVault.Configuration.encSymKeys);
	}

	@Override
	public boolean insertEncSymKey(EncKeyWrapper keyWrapper) {
		PreparedStatement stmt = null;
		boolean success = false;
		Timestamp created = null;
		if (keyWrapper.getCreated() != null) {
			created = Timestamp.valueOf(keyWrapper.getCreated());
		}
		Timestamp valid = null;
		if (keyWrapper.getValid() != null) {
			valid = Timestamp.valueOf(keyWrapper.getValid());
		}

		int dbId = this.getId(keyWrapper);
		if (dbId != -1) {
			// this keyWrapper is already there -> update the key
			try {
				stmt = conn.prepareStatement("update " + this.keyTableName
						+ " set enckey = ?, algorithm = ?, receiverID = ?, streamID = ?, created = ?, valid = ?, comment = ? where ID = ?");
				stmt.setBytes(1, keyWrapper.getKey());
				stmt.setString(2, keyWrapper.getAlgoritm());
				stmt.setInt(3, keyWrapper.getReceiverId());
				stmt.setInt(4, keyWrapper.getStreamId());
				stmt.setTimestamp(5, created);
				stmt.setTimestamp(6, valid);
				stmt.setString(7, keyWrapper.getComment());
				stmt.setInt(8, dbId);
				if (stmt.executeUpdate() > 0) {
					success = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			// the keyWrapper is not there -> create it
			try {
				stmt = conn.prepareStatement("insert into " + this.keyTableName + " values (0, ?, ?, ?, ?, ?, ?, ?)");
				stmt.setBytes(1, keyWrapper.getKey());
				stmt.setString(2, keyWrapper.getAlgoritm());
				stmt.setInt(3, keyWrapper.getReceiverId());
				stmt.setInt(4, keyWrapper.getStreamId());
				stmt.setTimestamp(5, created);
				stmt.setTimestamp(6, valid);
				stmt.setString(7, keyWrapper.getComment());
				if (stmt.executeUpdate() > 0) {
					success = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			stmt.close();
		} catch (SQLException sqlEx) { // ignore
			stmt = null;
		}
		return success;
	}

	@Override
	public EncKeyWrapper getEncSymKey(int receiverID, int streamID) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EncKeyWrapper keyWrapper = new EncKeyWrapper();
		try {
			stmt = conn
					.prepareStatement("select * from " + this.keyTableName + " where receiverID = ? and streamID = ?");
			stmt.setInt(1, receiverID);
			stmt.setInt(2, streamID);
			if (stmt.execute()) {
				rs = stmt.getResultSet();
				while (rs.next()) {
					keyWrapper.setId(rs.getInt(1));
					keyWrapper.setKey(rs.getBytes(2));
					keyWrapper.setAlgoritm(rs.getString(3));
					keyWrapper.setReceiverId(rs.getInt(4));
					keyWrapper.setStreamId(rs.getInt(5));
					Timestamp tsCreated = rs.getTimestamp(6);
					if (tsCreated != null) {
						keyWrapper.setCreated(tsCreated.toLocalDateTime());
					}
					Timestamp tsValid = rs.getTimestamp(7);
					if (tsValid != null) {
						keyWrapper.setValid(tsValid.toLocalDateTime());
					}
					keyWrapper.setComment(rs.getString(8));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
		} catch (SQLException sqlEx) { // ignore
			stmt = null;
		}
		return keyWrapper;
	}

	/**
	 * Returns the id or -1 if the encKeyWrapper doesnt exist
	 * 
	 * @param encKeyWrapper
	 *            the encKeyWrapper, where you want to get the id. This object
	 *            wont be modified
	 * @return the id of the given encKEyWrapper or -1 if the encKeyWrapper
	 *         doesnt exist
	 */
	private int getId(EncKeyWrapper encKeyWrapper) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int id = -1;
		try {
			stmt = conn
					.prepareStatement("select ID from " + this.keyTableName + " where receiverID = ? and streamID = ?");
			stmt.setInt(1, encKeyWrapper.getReceiverId());
			stmt.setInt(2, encKeyWrapper.getStreamId());
			if (stmt.execute()) {
				rs = stmt.getResultSet();
				if (rs.next()) {
					id = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
		} catch (SQLException sqlEx) { // ignore
			stmt = null;
		}
		return id;
	}

}
