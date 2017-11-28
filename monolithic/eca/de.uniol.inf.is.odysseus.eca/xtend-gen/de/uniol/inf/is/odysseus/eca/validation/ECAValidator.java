/**
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.eca.validation;

import de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION;
import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.validation.AbstractECAValidator;
import org.eclipse.xtext.validation.Check;

/**
 * This class contains custom validation rules.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
@SuppressWarnings("all")
public class ECAValidator extends AbstractECAValidator {
  public final static String INVALID_START = "State invalid for startQuery";
  
  public final static String INVALID_STOP = "State invalid for stopQuery";
  
  @Check
  public void checkState(final COMMANDACTION act) {
    String actName = null;
    String actState = null;
    String _subActname = act.getSubActname();
    boolean _tripleNotEquals = (_subActname != null);
    if (_tripleNotEquals) {
      actName = act.getSubActname();
    }
    String _stateName = act.getFunctAction().getStateName();
    boolean _tripleNotEquals_1 = (_stateName != null);
    if (_tripleNotEquals_1) {
      actState = act.getFunctAction().getStateName();
    }
    if ((((act.getFunctAction().getStateName() != null) && (actName != null)) && (actState != null))) {
      if ((actName.equals("startQuery") && (!actState.equals("INACTIVE")))) {
        String _stateName_1 = act.getFunctAction().getStateName();
        String _plus = ("Cannot start Query if state = " + _stateName_1);
        String _plus_1 = (_plus + "!");
        this.error(_plus_1, 
          ECAPackage.Literals.COMMANDACTION__FUNCT_ACTION, 
          ECAValidator.INVALID_START);
      } else {
        if ((actName.equals("stopQuery") && (!(actState.equals("RUNNING") || actState.equals("PARTIAL"))))) {
          String _stateName_2 = act.getFunctAction().getStateName();
          String _plus_2 = ("Cannot stop Query if state = " + _stateName_2);
          String _plus_3 = (_plus_2 + "!");
          this.error(_plus_3, 
            ECAPackage.Literals.COMMANDACTION__FUNCT_ACTION, 
            ECAValidator.INVALID_STOP);
        } else {
          if ((actName.equals("suspendQuery") && 
            (!(actState.equals("RUNNING") || actState.equals("PARTIAL"))))) {
            String _stateName_3 = act.getFunctAction().getStateName();
            String _plus_4 = ("Cannot suspend Query if state = " + _stateName_3);
            String _plus_5 = (_plus_4 + "!");
            this.error(_plus_5, act, 
              ECAPackage.Literals.COMMANDACTION__FUNCT_ACTION);
          } else {
            if ((actName.equals("resumeQuery") && 
              (!(actState.equals("SUSPENDED") || actState.equals("PARTIAL_SUSPENDED"))))) {
              String _stateName_4 = act.getFunctAction().getStateName();
              String _plus_6 = ("Cannot resume Query if state = " + _stateName_4);
              String _plus_7 = (_plus_6 + "!");
              this.error(_plus_7, act, 
                ECAPackage.Literals.COMMANDACTION__FUNCT_ACTION);
            } else {
              if ((actName.equals("fullQuery") && 
                (!(actState.equals("PARTIAL") || actState.equals("PARTIAL_SUSPENDED"))))) {
                String _stateName_5 = act.getFunctAction().getStateName();
                String _plus_8 = ("Cannot set Query full if state = " + _stateName_5);
                String _plus_9 = (_plus_8 + "!");
                this.error(_plus_9, act, 
                  ECAPackage.Literals.COMMANDACTION__FUNCT_ACTION);
              } else {
                if ((actName.equals("partialQuery") && 
                  (!((actState.equals("INACTIVE") || actState.equals("RUNNING")) || actState.equals("SUSPENDED"))))) {
                  String _stateName_6 = act.getFunctAction().getStateName();
                  String _plus_10 = ("Cannot set Query partial if state = " + _stateName_6);
                  String _plus_11 = (_plus_10 + "!");
                  this.error(_plus_11, act, 
                    ECAPackage.Literals.COMMANDACTION__FUNCT_ACTION);
                }
              }
            }
          }
        }
      }
    }
  }
}
