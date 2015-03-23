/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.optimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * Implementation of Quine–McCluskey algorithm to minimize boolean functions
 * http://en.wikipedia.org/wiki/Quine%E2%80%93McCluskey_algorithm
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: QuineMcCluskeyRule.java | QuineMcCluskeyRule.java |
 *          QuineMcCluskeyRule.java $
 *
 */
public class QuineMcCluskeyRule extends AbstractExpressionOptimizerRule<OrOperator> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IExpression<?> execute(OrOperator expression) {
        // Create disjunctive split of the expression
        Set<IExpression<?>> split = getDisjunctiveSplit(expression);
        Truthtable truthtable = new Truthtable(expression.getVariables());
        // Add terms to truthtable
        for (IExpression<?> s : split) {
            truthtable.add(s);
        }
        // Reduce terms
        truthtable.reduce();
        truthtable.select();
        // Return simplified expression
        return truthtable.toExpression();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IExpression<?> expression) {
        return expression instanceof OrOperator;
    }

    private class Truthtable {
        /** List of available variables used as index. */
        List<Variable> variables;
        /** Disjunctive terms. */
        List<Entry> entries;
        /** Number of original terms in truthtable. */
        int terms = 0;

        /**
         * 
         * Class constructor.
         *
         * @param variables
         *            List of used {@link Variable}.
         */
        public Truthtable(Set<Variable> variables) {
            this.entries = new ArrayList<>();
            this.variables = new ArrayList<>(variables.size());
            this.variables.addAll(variables);
        }

        /**
         * Add a term to the truthtable.
         * 
         * @param term
         *            The term
         */
        public void add(IExpression<?> term) {
            this.terms++;
            this.entries.add(new Entry(term, new int[] { this.entries.size() }));
            // Sort entries according to the number of '1' in the bitString
            // (existing variables)
            Collections.sort(this.entries, new Comparator<Entry>() {
                /**
                 * 
                 * {@inheritDoc}
                 */
                @Override
                public int compare(Entry o1, Entry o2) {
                    return new Integer(o2.count).compareTo(new Integer(o1.count));
                }

            });
        }

        /**
         * Reduce all corresponding minterms with a hamming distance of 1
         */
        public void reduce() {
            if (this.entries.size() > 1) {
                List<Entry> reducedEntries = new ArrayList<>();
                for (int i = 0; i < this.entries.size(); i++) {
                    for (int j = i + 1; j < this.entries.size(); j++) {
                        int hammingDistance = this.entries.get(i).bitString.length;
                        for (int b = 0; b < this.entries.get(i).bitString.length; b++) {
                            if (this.entries.get(i).bitString[b] == this.entries.get(j).bitString[b]) {
                                hammingDistance--;
                            }
                        }
                        if (hammingDistance == 1) {
                            List<IExpression<?>> newMinTerm = new ArrayList<>();
                            Set<IExpression<?>> split = getConjunctiveSplit(this.entries.get(i).minterm);
                            for (IExpression<?> s : split) {
                                int index = -1;
                                if (s instanceof NotOperator) {
                                    index = this.variables.indexOf(s.toFunction().getArgument(0).toVariable());
                                }
                                else {
                                    index = this.variables.indexOf(s.toVariable());
                                }
                                if (this.entries.get(i).bitString[index] == this.entries.get(j).bitString[index]) {
                                    newMinTerm.add(s);
                                }
                            }
                            int[] base = new int[this.entries.get(i).base.length + this.entries.get(j).base.length];
                            System.arraycopy(this.entries.get(i).base, 0, base, 0, this.entries.get(i).base.length);
                            System.arraycopy(this.entries.get(j).base, 0, base, this.entries.get(i).base.length, this.entries.get(j).base.length);
                            reducedEntries.add(new Entry(conjunction(newMinTerm), base));
                        }
                    }
                }
                this.entries.addAll(reducedEntries);
            }
        }

        /**
         * Select the terms to simplify the expression.
         */
        public void select() {
            List<Entry> selection = new ArrayList<>();
            // Sort entries according to their base size. The fewer minterms are
            // at the top.
            Collections.sort(this.entries, new Comparator<Entry>() {
                /**
                 * 
                 * {@inheritDoc}
                 */
                @Override
                public int compare(Entry o1, Entry o2) {
                    return new Integer(o2.base.length).compareTo(new Integer(o1.base.length));
                }

            });

            boolean[] selected = new boolean[this.terms];
            for (Entry entry : this.entries) {
                boolean use = false;
                for (int i : entry.base) {
                    if (!selected[i]) {
                        selected[i] = true;
                        use = true;
                    }
                }
                if (use) {
                    selection.add(entry);
                }
            }
            this.entries.clear();
            this.entries.addAll(selection);
        }

        public IExpression<?> toExpression() {
            List<IExpression<?>> expressions = new ArrayList<>();
            for (Entry entry : this.entries) {
                expressions.add(entry.minterm);
            }
            return disjunction(expressions);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Entry entry : this.entries) {
                sb.append(entry.toString()).append("\n");
            }
            return sb.toString();
        }

        private class Entry {
            IExpression<?> minterm;
            char[] bitString;
            int count;
            int[] base;

            /**
             * Class constructor.
             * 
             * @param base
             *
             */
            public Entry(IExpression<?> term, int[] base) {
                this.minterm = term;
                this.base = base;
                this.bitString = new char[Truthtable.this.variables.size()];
                for (int i = 0; i < this.bitString.length; i++) {
                    this.bitString[i] = '-';
                }
                Set<IExpression<?>> split = getConjunctiveSplit(term);
                for (IExpression<?> s : split) {
                    if (s instanceof NotOperator) {
                        this.bitString[Truthtable.this.variables.indexOf(s.toFunction().getArgument(0).toVariable())] = '0';
                    }
                    else {
                        this.bitString[Truthtable.this.variables.indexOf(s.toVariable())] = '1';
                    }
                }
                for (int i = 0; i < this.bitString.length; i++) {
                    if (this.bitString[i] == '1') {
                        this.count++;
                    }
                }
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String toString() {
                return this.minterm.toString() + " [" + new String(this.bitString) + "]" + " [" + this.count + "] " + Arrays.toString(this.base);
            }
        }
    }
}
