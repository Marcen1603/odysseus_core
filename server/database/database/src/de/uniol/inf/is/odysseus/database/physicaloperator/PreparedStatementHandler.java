package de.uniol.inf.is.odysseus.database.physicaloperator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is used to handle prepared statements 
 * currently, especially to handle batch processing for
 * sources that do not support this
 * 
 * @author Marco Grawunder
 *
 */
public class PreparedStatementHandler {
	
	final private boolean batchSupported;

	private PreparedStatement preparedStatement;

	private int out = 0;

	public PreparedStatementHandler(boolean batchSupported) {
		this.batchSupported = batchSupported;
	}

	public void setPreparedStatement(PreparedStatement prepareStatement) {
		this.preparedStatement = prepareStatement;
	}

	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	public void setNull(int i, int null1) throws SQLException {
		this.preparedStatement.setNull(i, null1);	
	}

	public void addBatch() throws SQLException {
		if (batchSupported){
			preparedStatement.addBatch();
		}else{
			if(preparedStatement.execute()){
				out++;
			}
		}
	}

	public int executeBatch() throws SQLException {
		if (batchSupported){
			int[] ret = preparedStatement.executeBatch();
			return ret.length;
		}
		int b = out;
		out = 0;
		return b;
	}

	public void clearBatch() throws SQLException {
		if (batchSupported){
			preparedStatement.clearBatch();
		}else{
			out = 0;
		}
		
	}

	public void executeUpdate() throws SQLException {
		if (batchSupported){
			preparedStatement.executeUpdate();
		}else{
			throw new UnsupportedOperationException("Cannot call execute Update on the sink");
		}
	}

	
}
