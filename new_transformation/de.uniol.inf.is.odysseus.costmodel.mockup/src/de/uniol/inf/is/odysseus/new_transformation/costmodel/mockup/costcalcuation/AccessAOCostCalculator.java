package de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.costcalcuation;

import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.sail.nativerdf.model.NativeLiteral;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.ICost;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.ICostCalculator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.MockupCost;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.streamCharacteristic.TimeDistance;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.streamCharacteristic.TimeIntervalLength;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicDatabase;

public class AccessAOCostCalculator implements ICostCalculator {
	@Override
	public ICost calculateCost(List<StreamCharacteristicCollection> incomingStreamCharacteristics) {
		return new MockupCost(0);
	}

	@Override
	public StreamCharacteristicCollection mergeStreamMetadata(
			List<StreamCharacteristicCollection> incomingStreamMetadata, ILogicalOperator ao) {

		// initialize mockup stream values
		Repository repository = StreamCharacteristicDatabase.getInstance().getRepository();
		ValueFactory factory = repository.getValueFactory();
		AccessAO accessAO = (AccessAO) ao;
		String uriString = accessAO.getSource().getURI();
		URI streamUri = factory.createURI(uriString);

		double timeDistance = 0;
		URI timeDistanceUri = factory.createURI("odysseus:StreamCharacteristic/TimeDistance");
		double timeIntervalLength = 0;
		URI timeIntervalLengthUri = factory.createURI("odysseus:StreamCharacteristic/TimeIntervalLength");
		try {
			RepositoryConnection connection = repository.getConnection();

			try {
				// query timeDistance
				RepositoryResult<Statement> statements = connection.getStatements(streamUri, timeDistanceUri, null,
						true);
				if (statements.hasNext()) {
					Statement statement = statements.next();
					NativeLiteral object = (NativeLiteral) statement.getObject();
					timeDistance = object.doubleValue();
				} else {
					connection.add(streamUri, timeDistanceUri, factory.createLiteral(10));
				}

				// query timeInterval
				statements = connection.getStatements(streamUri, timeIntervalLengthUri, null, true);
				if (statements.hasNext()) {
					Statement statement = statements.next();
					NativeLiteral object = (NativeLiteral) statement.getObject();
					timeIntervalLength = object.doubleValue();
				} else {
					connection.add(streamUri, timeIntervalLengthUri, factory.createLiteral(10));
				}
			} finally {
				connection.close();
			}

		} catch (RepositoryException e) {
			e.printStackTrace();
		}

		StreamCharacteristicCollection collection = new StreamCharacteristicCollection();
		collection.putMetadata(new TimeDistance(timeDistance));
		collection.putMetadata(new TimeIntervalLength(timeIntervalLength));
		return collection;
	}
}
