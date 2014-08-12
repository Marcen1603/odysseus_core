package de.uniol.inf.is.odysseus.core.planmanagement.query;

import java.util.HashMap;
import java.util.Map;

public enum QueryState {
	INACTIVE, PARTIAL, RUNNING, PARTIAL_SUSPENDED, SUSPENDED;

	static Map<QueryState, QueryState[]> reachable = new HashMap<>();
	static {
		reachable.put(INACTIVE, new QueryState[] { PARTIAL, RUNNING });
		reachable
				.put(RUNNING, new QueryState[] { PARTIAL, INACTIVE, SUSPENDED });
		reachable.put(PARTIAL, new QueryState[] { INACTIVE, RUNNING,
				PARTIAL_SUSPENDED });
		reachable.put(PARTIAL_SUSPENDED,
				new QueryState[] { SUSPENDED, PARTIAL });
		reachable
				.put(SUSPENDED, new QueryState[] { RUNNING, PARTIAL_SUSPENDED });
	}

	static public QueryState[] reachable(QueryState current) {
		return reachable.get(current);
	}

	static public QueryState next(QueryState current, QueryFunction function) {
		switch (current) {
		case INACTIVE:
			if (function == QueryFunction.START) {
				return RUNNING;
			}
			if (function == QueryFunction.PARTIAL) {
				return PARTIAL;
			}
			break;
		case RUNNING:
			if (function == QueryFunction.STOP) {
				return INACTIVE;
			}
			if (function == QueryFunction.PARTIAL) {
				return PARTIAL;
			}
			if (function == QueryFunction.SUSPEND) {
				return SUSPENDED;
			}
			break;
		case PARTIAL:
			if (function == QueryFunction.STOP) {
				return INACTIVE;
			}
			if (function == QueryFunction.FULL) {
				return PARTIAL;
			}
			if (function == QueryFunction.SUSPEND) {
				return PARTIAL_SUSPENDED;
			}
			break;
		case PARTIAL_SUSPENDED:
			if (function == QueryFunction.RESUME) {
				return PARTIAL;
			}
			if (function == QueryFunction.FULL) {
				return SUSPENDED;
			}
			break;
		case SUSPENDED:
			if (function == QueryFunction.RESUME) {
				return RUNNING;
			}
			if (function == QueryFunction.PARTIAL) {
				return PARTIAL_SUSPENDED;
			}
			break;
		}
		throw new IllegalStateException("Cannot call " + function.name()
				+ " in " + current.name());
	}

	public static void main(String[] args) {
		for (QueryState s : QueryState.values()) {
			System.out.print("Reachable from " + s.name() + ": ");
			print(reachable(s));
		}

		for (QueryState s : QueryState.values()) {
			for (QueryFunction f : QueryFunction.values()) {
				try {
					System.out.print("Move from " + s.name() + " with " + f.name()
							+ " to " + next(s, f));
				} catch (IllegalStateException e) {
					System.out.print("INVALID! " + e.getMessage());
				}
				System.out.println();
			}
		}

	}

	private static void print(QueryState[] reachable) {
		for (QueryState s : reachable) {
			System.out.print(s.name() + " ");
		}
		System.out.println();
	}

}
