package de.uniol.inf.is.odysseus.rcp.viewer.view.position.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.position.INodePositioner;

public class ForcesPositioner<T> implements INodePositioner<T> {

	@Override
	public void positionize(IGraphView<T> graph, int w, int h) {

		List<INodeView<T>> nodes = new LinkedList<INodeView<T>>();
		List<INodeView<T>> completedNodes = new LinkedList<INodeView<T>>();
		HashMap<Double, Integer> widths = new HashMap<Double, Integer>();

		nodes.addAll(graph.getViewedNodes());

		// Place sinks
		int i = 0;
		Collection<INodeView<T>> sinks = getSinks(nodes);
		for (INodeView<T> node : sinks) {
			node.setPosition(new Vector(i * 150, 0));
			i++;

			nodes.remove(node);
			completedNodes.add(node);
		}

		// Place other nodes
		while (!nodes.isEmpty()) {

			IConnectionView<T> connection = getNextConnection(nodes, completedNodes);
			INodeView<T> node = connection.getViewedStartNode();
			Vector position = connection.getViewedEndNode().getPosition();

			if (!widths.containsKey(position.getY()))
				widths.put(position.getY(), 0);
			else {
				int width = widths.get(position.getY());
				widths.put(position.getY(), width + 150);
			}

			Vector newPosition = new Vector(widths.get(position.getY()), position.getY() + 150);
			node.setPosition(newPosition);

			nodes.remove(node);
			completedNodes.add(node);
		}

		int iteration = 0;
		Vector[] movements = new Vector[completedNodes.size()];

		// Calc neighbours
		HashMap<INodeView<T>, Collection<INodeView<T>>> neighbours = new HashMap<INodeView<T>, Collection<INodeView<T>>>();
		for (INodeView<T> node : completedNodes) {
			List<INodeView<T>> list = new LinkedList<INodeView<T>>();
			for (IConnectionView<T> con : node.getConnectionsAsStart()) {
				list.add(con.getViewedEndNode());
			}
			for (IConnectionView<T> con : node.getConnectionsAsEnd()) {
				list.add(con.getViewedStartNode());
			}
			neighbours.put(node, list);
		}

		while (iteration < 100) {

			// Calc Movements
			for (int k = 0; k < completedNodes.size(); k++) {
				INodeView<T> node = completedNodes.get(k);
				Collection<INodeView<T>> n = neighbours.get(node);

				// Neighbours
				Vector movementForNeighbours = new Vector(0, 0);
				for (INodeView<T> neighbour : n) {
					Vector direction = neighbour.getPosition().sub(node.getPosition());

					if (direction.getLength() > 150) {
						Vector move = direction;
						movementForNeighbours = movementForNeighbours.add(move);
					}

				}

				// All
				Vector movementForNonNeighbours = new Vector(0, 0);
				for (INodeView<T> otherNode : completedNodes) {
					if (otherNode == node)
						continue;
					Vector direction = otherNode.getPosition().sub(node.getPosition());
					if (direction.getLength() < 200) {
						Vector move = direction.mul(-1);
						movementForNonNeighbours = movementForNonNeighbours.add(move);
					}
				}

				Vector m1 = movementForNeighbours;
				Vector m2 = movementForNonNeighbours.mul(0.5f);
				movements[k] = m1.add(m2).mul(0.0005f);
			}

			// Do Movements
			for (int k = 0; k < completedNodes.size(); k++) {
				INodeView<T> node = completedNodes.get(k);

				Vector newPosition = node.getPosition().add(movements[k]);
				node.setPosition(newPosition);
			}

			iteration++;
		}
	}

	private IConnectionView<T> getNextConnection(Collection<INodeView<T>> nodesToSearch, Collection<INodeView<T>> completedNodes) {
		for (INodeView<T> node : nodesToSearch) {
			Collection<IConnectionView<T>> startConns = node.getConnectionsAsStart();
			for (IConnectionView<T> conn : startConns) {
				if (completedNodes.contains(conn.getViewedEndNode())) {
					return conn;
				}
			}
		}
		return null;
	}

	private Collection<INodeView<T>> getSinks(Collection<INodeView<T>> nodes) {
		ArrayList<INodeView<T>> list = new ArrayList<INodeView<T>>();

		for (INodeView<T> node : nodes) {
			Collection<IConnectionView<T>> connections = node.getConnectionsAsStart();
			if (connections.size() == 0) {
				list.add(node);
			} else {
				boolean found = false;
				for (IConnectionView<T> connection : connections) {
					if (nodes.contains(connection.getViewedEndNode())) {
						found = true;
						break;
					}
				}
				if (!found) {
					list.add(node);
				}

			}
		}

		if (nodes.size() > 0 && list.size() == 0) {
			list.addAll(nodes);
		}

		return list;
	}
}
