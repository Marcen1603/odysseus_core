package de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.Game;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SportsQL {
	 public Game[] games();
	 public StatisticType[] types();
	 public String name();
}
