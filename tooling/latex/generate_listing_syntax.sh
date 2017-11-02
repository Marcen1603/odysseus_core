#!/bin/bash

# Find all logical operator
pqloperator=$(find ../../ -name \*.java -type f |xargs grep "@LogicalOperator" | sed -rn 's/.*name\s*\=\s*\"([a-zA-Z_]*)\".*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/, \1/; ta ; P ; D' )

# Find all logical operator parameter
pqlparameter=$(find ../../ -name \*.java -type f |xargs grep "@Parameter" | sed -rn 's/.*name\s*\=\s*\"([a-zA-Z_]*)\".*/\U\1/p'  | sort -fu | sed -r ':a ;$! N; s/\n(.*)/, \1/; ta ; P ; D' )

# Find all keywords
cqlparser='../../server/cql/parser.cql/src/de/uniol/inf/is/odysseus/parser/cql/parser/NewSQLParser.jjt'
cqlkeywords=$(cat $cqlparser | tr '\n\r' '\n' | grep -E '^\| < K_[A-Z_]+ : \"' | sed -rn 's/.*\| < K_[A-Z_]+ : \"([a-zA-Z_]*)\" >.*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/, \1/; ta ; P ; D')

# Find all datatypes
datatypes=$(find ../../ -name \*.java -type f |xargs grep -E "public .*static .*SDF[a-zA-Z_]*Datatype" | sed -rn 's/.*new SDF[a-zA-Z_]*Datatype\(\"([a-zA-Z_]*)\"\);.*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/, \1/; ta ; P ; D')

# Sase keywords
sasekeywords=$(echo "CREATE STREAM VIEW PATTERN WHERE WITHIN ENDS AT RETURN SEQ ALT AND AS LEN WEEK WEEKS DAY DAYS HOUR HOURS MINUTE MINUTES SECOND SECONDS MILLISECOND MILLISECONDS SKIP_TILL_NEXT_MATCH SKIP_TILL_ANY_MATCH STRICT_CONTIGUITY AVG MIN MAX SUM COUNT TRUE FALS" | tr ' ' '\n' | sort | sed -r ':a ;$! N; s/\n(.*)/, \1/; ta ; P ; D')

cat > listing.tex <<- EOF

\lstdefinelanguage{PQL}%
   {morekeywords={% Operators
$pqloperator%
      },% Parameters
   morekeywords=[2]{%
$pqlparameter%
      },%
   morendkeywords={$datatypes},%
   sensitive=false,
   morecomment=[l]///,%
   morestring=[d]",%
   morestring=[d]',%
   moredelim=*[directive]\#,%
   moredirectives={ADDQUERY, BUFFERPLACEMENT, CONFIG, DEFINE, DOQUERYSHARING, DOREWRITE, DROPALLQUERIES, DROPALLSINKS, DROPALLSOURCES, ENDIF, IF, IFDEF, IFNDEF, IFSRCDEF, IFSRCNDEF, INPUT, INCLUDE, LOGIN, LOGOUT, LOOP, METADATA, ODYSSEUS_PARAM, PRINT, PARSER, QName, QUERY, RELOADFROMLOG, RUNQUERY, SCHEDULER, SLEEP, STARTQUERIES, STARTSCHEDULER, STOPSCHEDULER, TRANSCFG, UNDEF}%
}[keywords,comments,strings,directives]

\lstdefinelanguage{CQL}%
   {morekeywords={% Reserved keywords
$cqlkeywords%
      },%
   morendkeywords={$datatypes},%
   sensitive=false,
   morecomment=[l]///,%
   morestring=[d]",%
   morestring=[d]',%
   moredelim=*[directive]\#,%
   moredirectives={ADDQUERY, BUFFERPLACEMENT, CONFIG, DEFINE, DOQUERYSHARING, DOREWRITE, DROPALLQUERIES, DROPALLSINKS, DROPALLSOURCES, ENDIF, IF, IFDEF, IFNDEF, IFSRCDEF, IFSRCNDEF, INPUT, INCLUDE, LOGIN, LOGOUT, LOOP, METADATA, ODYSSEUS_PARAM, PRINT, PARSER, QName, QUERY, RELOADFROMLOG, RUNQUERY, SCHEDULER, SLEEP, STARTQUERIES, STARTSCHEDULER, STOPSCHEDULER, TRANSCFG, UNDEF}%
}[keywords,comments,strings,directives]

\lstdefinelanguage{SASE}
   {morekeywords={% Reserved keywords
$sasekeywords%
      },%
   morendkeywords={$datatypes},%
   sensitive=false,
   morecomment=[l]///,%
   morestring=[d]",%
   morestring=[d]',%
   moredelim=*[directive]\#,%
   moredirectives={ADDQUERY, BUFFERPLACEMENT, CONFIG, DEFINE, DOQUERYSHARING, DOREWRITE, DROPALLQUERIES, DROPALLSINKS, DROPALLSOURCES, ENDIF, IF, IFDEF, IFNDEF, IFSRCDEF, IFSRCNDEF, INPUT, INCLUDE, LOGIN, LOGOUT, LOOP, METADATA, ODYSSEUS_PARAM, PRINT, PARSER, QName, QUERY, RELOADFROMLOG, RUNQUERY, SCHEDULER, SLEEP, STARTQUERIES, STARTSCHEDULER, STOPSCHEDULER, TRANSCFG, UNDEF}%
}[keywords,comments,strings,directives]

EOF
