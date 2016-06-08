/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import cc.kuka.odysseus.ontology.common.SensorOntologyService;
import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import cc.kuka.odysseus.ontology.rcp.dialogs.ConditionDialog;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class CreateConditionAction extends Action {
    private Shell shell;
    private MeasurementCapability measurementCapability;

    /**
     * Class constructor.
     *
     */
    public CreateConditionAction(Shell shell, MeasurementCapability measurementCapability) {
        super();
        this.shell = shell;
        this.measurementCapability = measurementCapability;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        final ConditionDialog conditionDialog = new ConditionDialog(this.shell);
        conditionDialog.open();

        if (conditionDialog.getReturnCode() == Window.OK) {
            final SensorOntologyService ontology = SensorRegistryPlugIn.getSensorOntologyService();
            Condition condition = conditionDialog.getCondition();
            this.measurementCapability.add(condition);
            ontology.create(this.measurementCapability, condition);
        }
    }
}
