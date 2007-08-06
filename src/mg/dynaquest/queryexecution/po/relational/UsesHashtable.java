package mg.dynaquest.queryexecution.po.relational;

import java.util.ArrayList;
import java.util.HashMap;

import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;

public interface UsesHashtable {
	HashMap<RelationalTuple, ArrayList<RelationalTuple>> getHashtable(int input);

	boolean setHashtable(int input, HashMap<RelationalTuple, ArrayList<RelationalTuple>> newTable);
}