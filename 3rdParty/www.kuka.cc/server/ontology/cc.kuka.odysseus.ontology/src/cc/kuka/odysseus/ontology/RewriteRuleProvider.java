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
package cc.kuka.odysseus.ontology;

import java.util.ArrayList;
import java.util.List;

import cc.kuka.odysseus.ontology.rewrite.rules.RInsertSensingDevicesRule;
import de.uniol.inf.is.odysseus.rewrite.flow.IRewriteRuleProvider;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RewriteRuleProvider implements IRewriteRuleProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IRule<?, ?>> getRules() {
        final ArrayList<IRule<?, ?>> rules = new ArrayList<>();
        rules.add(new RInsertSensingDevicesRule());

        return rules;
    }

}
