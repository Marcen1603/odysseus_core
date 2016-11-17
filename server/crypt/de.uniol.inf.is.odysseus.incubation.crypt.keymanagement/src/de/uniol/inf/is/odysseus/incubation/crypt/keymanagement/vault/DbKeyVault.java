package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util.Converter;

/**
 * This is a KeyVault, stored in a Database.<br>
 * This class manages the storing of keys in the database.
 * 
 * @author MarkMilster
 *
 */
public class DbKeyVault extends AbstractDbKeyVault implements IDbKeyVault {

	/**
	 * Constructor, which uses credentials, stored in a Configuration file, to
	 * connect to a database.
	 * 
	 * @param configuration
	 *            Enum, which specifies the configuration of the vault to
	 *            connect to the database
	 */
	public DbKeyVault(AbstractDbKeyVault.Configuration configuration) {
		super(configuration);
	}

	public boolean insertKey(KeyWrapper<?> key) {
		PreparedStatement stmt = null;
		boolean success = false;
		Timestamp created = null;
		if (key.getCreated() != null) {
			created = Timestamp.valueOf(key.getCreated());
		}
		Timestamp valid = null;
		if (key.getValid() != null) {
			valid = Timestamp.valueOf(key.getValid());
		}

		try {
			stmt = conn.prepareStatement("insert into " + this.keyTableName + " values (0, ?, ?, ?, ?)");
			stmt.setObject(1, key.getKey());
			stmt.setTimestamp(2, created);
			stmt.setTimestamp(3, valid);
			stmt.setString(4, key.getComment());
			if (stmt.executeUpdate() > 0) {
				success = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
		} catch (SQLException sqlEx) { // ignore
			stmt = null;
		}
		return success;
	}

	public KeyWrapper<Key> getKey(int id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Key key = null;
		LocalDateTime created = null;
		LocalDateTime valid = null;
		String comment = null;
		try {
			stmt = conn.prepareStatement("select * from " + this.keyTableName + " where id = ?");
			stmt.setInt(1, id);
			if (stmt.execute()) {
				rs = stmt.getResultSet();
				while (rs.next()) {
					byte[] bytes = (byte[]) rs.getObject(2);
					key = Converter.bytesToKey(bytes);
					Timestamp tsCreated = rs.getTimestamp(3);
					if (tsCreated != null) {
						created = tsCreated.toLocalDateTime();
					}
					Timestamp tsValid = rs.getTimestamp(4);
					if (tsValid != null) {
						valid = tsValid.toLocalDateTime();
					}
					comment = rs.getString(5);
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
		KeyWrapper<Key> keyWrapper = new KeyWrapper<>();
		keyWrapper.setId(id);
		keyWrapper.setKey(key);
		keyWrapper.setCreated(created);
		keyWrapper.setValid(valid);
		keyWrapper.setComment(comment);
		return keyWrapper;
	}

	@Override
	public int getNextKeyId() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int nextId = -1;
		try {
			stmt = conn.prepareStatement("select max(id) from " + this.keyTableName);
			if (stmt.execute()) {
				rs = stmt.getResultSet();
				if (rs.next()) {
					nextId = rs.getInt(1);
					nextId++;
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
		return nextId;
	}

}
