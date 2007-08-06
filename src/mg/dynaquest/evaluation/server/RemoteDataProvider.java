package mg.dynaquest.evaluation.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;

public interface RemoteDataProvider extends Remote {
	public boolean executeQuery(String query, Object caller,
			SDFConstantList inputValues) throws RemoteException;

	public RelationalTuple nextObject(Object caller) throws RemoteException;
}