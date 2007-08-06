package mg.dynaquest.evaluation.weasimulation;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.access.OOStreamAccessPO;
import mg.dynaquest.queryexecution.po.algebra.OOAccessPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.base.SimplePlanOperator;
import mg.dynaquest.queryexecution.po.streaming.algebra.WindowPO;
import mg.dynaquest.queryexecution.po.streaming.algebra.WindowType;
import mg.dynaquest.queryexecution.po.streaming.base.AbstractWindowPO;
import mg.dynaquest.queryexecution.po.streaming.base.SlidingWindowPO;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.queryexecution.po.streaming.oo.OOStreamingBasePO;
import mg.dynaquest.queryexecution.po.streaming.oo.OOStreamingProjectPO;
import mg.dynaquest.queryexecution.po.streaming.oo.OOStreamingSelectPO;
import mg.dynaquest.sourcedescription.sdf.description.SDFSource;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFGreaterOrEqualThanOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFNumberPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFExpression;
import weasim.InputStreamCreator;
import weasim.cdc.AnalogueValue;

public class DynaQuest {

	/**
	 * @param args
	 * @throws TimeoutException
	 * @throws POException
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws POException,
			TimeoutException, UnknownHostException, IOException {
		
		new DynaQuest().run();
	}

	private void run() throws POException, TimeoutException,
			UnknownHostException, IOException {
		OOAccessPO apo = new OOAccessPO(new SDFSource("http://todo"), "a");
		WindowPO winPO = new WindowPO(WindowType.Sliding, 100l);
		
		SDFSimplePredicate pred = new SDFNumberPredicate("blub", new SDFAttribute("a.inst.i"), new SDFGreaterOrEqualThanOperator(), new SDFExpression("asdf", "4"));
		SelectPO selectPo = new SelectPO(pred);
		
		SDFAttributeList attributes = new SDFAttributeList();
		attributes.add(new SDFAttribute("a.instMag"));
		ProjectPO projectPO = new ProjectPO(attributes);
		
		OOStreamAccessPO access = new OOStreamAccessPO(apo);
		access.setInputStream(InputStreamCreator.getInstance().createStream("SELECT w.wtow.BrkPres FROM WEA w WHERE w.id = 2"));

		AbstractWindowPO win = new SlidingWindowPO(winPO);
		win.setInputPO(access);
		
		OOStreamingBasePO sel = new OOStreamingSelectPO(selectPo);
		sel.setInputPO(win);
		
		OOStreamingBasePO pro = new OOStreamingProjectPO(projectPO);
		pro.setInputPO(sel);
		
		access.start();
		win.start();
		sel.start();
		pro.start();
		
		SimplePlanOperator op = new SimplePlanOperator() {

			public boolean close(SimplePlanOperator caller) throws POException {
				// TODO Auto-generated method stub
				return true;
			}

			public String getPOName() {
				// TODO Auto-generated method stub
				return "DynaQuest";
			}

			public <T> T next(SimplePlanOperator caller, long timeout)
					throws POException, TimeoutException {
				// TODO Auto-generated method stub
				return null;
			}

			public void open(SimplePlanOperator caller) throws POException {
				// TODO Auto-generated method stub

			}
		};
		pro.open(op);
		while (true) {
			StreamExchangeElement<Map<String, Object>> element = pro.next(op, -1);
			System.out.println(element.getValidity() + " : "
					+ ((AnalogueValue)element.getCargo().get("a.instMag")).getI());
		}
	}

}
