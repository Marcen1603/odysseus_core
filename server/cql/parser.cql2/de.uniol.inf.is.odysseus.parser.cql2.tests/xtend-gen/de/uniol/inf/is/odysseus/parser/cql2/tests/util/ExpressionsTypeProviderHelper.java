package de.uniol.inf.is.odysseus.parser.cql2.tests.util;

import org.eclipse.xtext.xbase.lib.Extension;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.parser.cql2.typing.ExpressionsTypeProvider;

@SuppressWarnings("all")
public class ExpressionsTypeProviderHelper extends ExpressionsTypeProvider {
  @Inject
  @Extension
  private /* CQLDictionaryHelper */Object _cQLDictionaryHelper;
}
