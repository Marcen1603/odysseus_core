/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.LinkedList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryoptimization.trafo.FullProcessPlanTransform;

/**
 * @author  Olaf Twesten
 */
public class ProcessPlanRestruct {

    private static LinkedList<CARule> rules = new LinkedList<CARule>();
    
    /**
	 * @uml.property  name="maxRuns"
	 */
    private int maxRuns;

    public ProcessPlanRestruct() {
        maxRuns = 20;
        rules.add(new RestructSelectionGroup());
        rules.add(new RestructProjectionGroup());
        rules.add(new SwitchSelectionProjection());
        rules.add(new SwitchSelectionUnionDifference());
        rules.add(new SwitchSelectionJoin());
        rules.add(new SwitchProjectionUnionDifference());
        rules.add(new SwitchProjectionJoin());
        rules.add(new SwitchJoin());
        rules.add(new SwitchJoinChilds());
    }

    public AlgebraPO restruct(AlgebraPO logicalplan) {

        if (logicalplan == null)
            return logicalplan;

        boolean restruct = true;

        while (restruct && maxRuns > 0) {
            int negativeRuns = 0;
            for (int i = 0; i < rules.size(); i++) {
                if (!rules.get(i).test(logicalplan))
                    negativeRuns++;
                while (rules.get(i).test(logicalplan)) {
                    rules.get(i).process(logicalplan);
                }
            }
            if (negativeRuns == rules.size())
                restruct = false;
            maxRuns--;
        }
        return logicalplan;
    }

    public void addRule(CARule rule) {
        rules.add(rule);
    }

    public void addRule(int pos, CARule rule) {
        rules.add(pos, rule);
    }

    /**
	 * @return  the rules
	 * @uml.property  name="rules"
	 */
    public LinkedList<CARule> getRules() {
        return rules;
    }

    public CARule getRule(int pos) {
        return rules.get(pos);
    }

    public void removeRule(int pos) {
        rules.remove(pos);
    }

    /**
	 * @param rules  the rules to set
	 * @uml.property  name="rules"
	 */
    public void setRules(LinkedList<CARule> rules) {
        ProcessPlanRestruct.rules = rules;
    }

    /**
	 * @return  the maxRuns
	 * @uml.property  name="maxRuns"
	 */
    public int getMaxRuns() {
        return maxRuns;
    }

    /**
	 * @param maxRuns  the maxRuns to set
	 * @uml.property  name="maxRuns"
	 */
    public void setMaxRuns(int maxRuns) {
        this.maxRuns = maxRuns;
    }

    public static void main(String[] args) {
        RestructTest test = new RestructTest();
        ProcessPlanRestruct restructPlan = new ProcessPlanRestruct();
        FullProcessPlanTransform.dumpPlan(test.getTestPlan(), " ");

        System.out.println("Nach der Restrukturierung:");
        FullProcessPlanTransform.dumpPlan(restructPlan.restruct(test
                .getTestPlan()), " ");
    }
}
