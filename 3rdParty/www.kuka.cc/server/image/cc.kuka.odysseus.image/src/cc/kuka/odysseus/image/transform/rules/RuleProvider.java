/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.image.transform.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;

/**
 * Rule provider for image operators.
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RuleProvider implements IRuleProvider {
    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<IRule<?, ?>> getRules() {
        final List<IRule<?, ?>> rules = new ArrayList<>();
        rules.add(new TEyeTrackerAORule());
        //rules.add(new TFingerDetectionAORule());
        return rules;
    }
}
