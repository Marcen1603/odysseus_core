/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;

public class QSGraph {
    private List<Vertice> vertices = new ArrayList<Vertice>();
    private List<GraphConnection> connections = new ArrayList<GraphConnection>();

    protected QSGraph() {
    }

    protected QSGraph(List<Vertice> vertices, List<GraphConnection> connections) {
        this.vertices = vertices;
        this.connections = connections;
    }

    @SuppressWarnings("rawtypes")
    protected void addVertice(IPhysicalOperator po) {
        // TODO: Ordnungsgem��ge Beschriftung des Operators ermitteln
        // Irgendwas der Form "Bezeichner <-- expression + input"
        String label = po.getName();
        if (po instanceof SelectPO) {
            IPredicate<?> pred = ((SelectPO<?>) po).getPredicate();
            @SuppressWarnings("unchecked")
            List<SDFAttribute> attlist = ((IPredicate) pred).getAttributes();
            System.out.println(((IPredicate) pred).toString());
            for (SDFAttribute a : attlist) {
                System.out.println(a.toString());
            }
        }
        vertices.add(new Vertice(po, label));
    }

    protected boolean removeVertice(Vertice v) {
        if (hasConnections(v)) {
            return false;
        }
        vertices.remove(v);
        return true;
    }

    protected boolean addConnection(Vertice source, Vertice target) {
        if (!vertices.contains(source) || !vertices.contains(target) || existsConnection(source, target)) {
            return false;
        }
        connections.add(new GraphConnection(source, target));
        return true;
    }

    protected boolean addConnection(IPhysicalOperator source, IPhysicalOperator target) {
        Vertice sourcev = getVertice(source);
        Vertice targetv = getVertice(target);
        return addConnection(sourcev, targetv);
    }

    protected boolean removeConnection(Vertice source, Vertice target) {
        for (GraphConnection gc : connections) {
            if (source.equals(gc.getSource()) && target.equals(gc.getTarget())) {
                connections.remove(gc);
                return true;
            }
        }
        return false;
    }

    protected void replaceInputConnection(Vertice v, Vertice oldSource, Vertice newSource) {
        removeConnection(oldSource, v);
        addConnection(newSource, v);
        // TODO: Beschriftung des Knotens in Abh�ngigkeit von der neuen Quelle
        // anpassen
        String newLabel = "";
        v.setLabel(newLabel);
    }

    protected boolean hasConnections(Vertice v) {
        for (GraphConnection gc : connections) {
            if (gc.getSource().equals(v) || gc.getTarget().equals(v)) {
                return true;
            }
        }
        return false;
    }

    protected boolean existsConnection(Vertice source, Vertice target) {
        for (GraphConnection gc : connections) {
            if (gc.getSource().equals(source) && gc.getTarget().equals(target)) {
                return true;
            }
        }
        return false;
    }

    public List<Vertice> getVertices() {
        return vertices;
    }

    public Vertice getVertice(IPhysicalOperator ipo) {
        for (Vertice v : vertices) {
            if (v.getPo().equals(ipo)) {
                return v;
            }
        }
        return null;
    }

    public List<GraphConnection> getConnections() {
        return connections;
    }

    public Vertice getInput(Vertice v) {
        if (!vertices.contains(v))
            return null;
        for (GraphConnection gc : connections) {
            if (gc.getTarget().equals(v)) {
                return gc.getSource();
            }
        }
        return null;
    }

    public List<Vertice> getOutput(Vertice v) {
        if (!vertices.contains(v))
            return null;
        ArrayList<Vertice> res = new ArrayList<Vertice>();
        for (GraphConnection gc : connections) {
            if (gc.getSource().equals(v)) {
                res.add(gc.getTarget());
            }
        }
        return res;
    }

}
