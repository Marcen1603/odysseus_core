package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Time
import de.uniol.inf.is.odysseus.parser.cql2.cQL.TimebasedWindow
import de.uniol.inf.is.odysseus.parser.cql2.cQL.TuplebasedWindow
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IWindowParser
import java.util.Map
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException

class WindowParser implements IWindowParser {
	
	private AbstractPQLOperatorBuilder builder
	
	@Inject
	new (AbstractPQLOperatorBuilder builder) {
		this.builder = builder;
	}	
	
	override parse(SimpleSource source) throws PQLOperatorBuilderException {
	
	if(source.window === null) return source.name
		var Map<String, String> args = newHashMap
		var window = source.window
		if (window instanceof TimebasedWindow) {
			var var1 = if(window.advance_size !== 0) window.advance_size.toString else '1'
			var var3 = if(window.unit !== Time.NULL) window.unit.getName else Time.NANOSECONDS.getName
			var var2 = if(window.advance_unit !== Time.NULL) window.advance_unit.getName else var3
			var2 += if(!var2.charAt(var2.length - 1).toString.equalsIgnoreCase("S")) "S" else ""
			args.put('size', window.size.toString + ",'" + var3 + "'")
			args.put('advance', var1 + ",'" + var2 + "'")
			args.put('input', source.name)
			return builder.build(typeof(TimeWindowAO), args)
		} else if (window instanceof TuplebasedWindow) {
			args.put('size', window.size.toString)
			args.put('advance', (if(window.advance_size != 0) window.advance_size else 1).toString)
			args.put('partition', if(window.partition_attribute !== null) window.partition_attribute.name else null)
			args.put('input', source.name)
			return builder.build(typeof(ElementWindowAO), args)
		} else {
			return source.name
		}
	
	}

}