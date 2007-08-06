PAGE IMDB_PersonPage

$IMDB_PersonPage:
	*<html>
	{
	  out.println("\t<person>");
	}
	*<h1><strong[ ]class="title">$Name</strong></h1>
	{
	  out.println("\t\t<name>"+$Name+"</name>");
	}
	($BirthNameBlock)?
	($BirthInfoBlock)?
	($Actor_Filmographie)?
	($Producer_Filmographie)?
	*</html>
	{
	  out.println("\t</person>");
	};

$BirthNameBlock:
	*Birth[ ]name*\n*<DD>$BirthName (\r)?\n</DD>
	{
	  out.println("\t\t<birthname>"+$BirthName+"</birthname>");
	};

$BirthInfoBlock:
	*Date[ ]of[ ]birth[ ][(]location[)]*<DD>
	<A[ ]HREF="/OnThisDay[?]day=$BIRTHDAY&month=$BIRTHMONTH">
	*<A[ ]HREF="/BornInYear[?]$BIRTHYEAR">
	{
	  out.println("\t\t<birth>");
	  out.println("\t\t\t<day>"+$BIRTHDAY+"</day>");
	  out.println("\t\t\t<month>"+$BIRTHMONTH+"</month>");
	  out.println("\t\t\t<year>"+$BIRTHYEAR+"</year>");
	}
	( *<A[ ]HREF="/BornWhere[?]*>$BIRTHPLACE</A>
	  {
	    out.println("\t\t\t<place>"+$BIRTHPLACE+"</place>");
	  }
	)?
	{
	out.println("\t\t</birth>");
	};

$Producer_Filmographie:
	*<B><A[ ]NAME="producer">*<ol>
	{
	  out.println("\t\t<filmographie type='producer'>");
	}
	( *<LI>*\n
	  {
	    out.println("\t\t\t<movie>");
	  }
	  $ProducerMovieLine *</li>
	  {
	    out.println("\t\t\t</movie>");
	    //System.out.println("ProducerMovieLine "+$ProducerMovieLine);
	  }
	)+
	</ol>
	{
	  out.println("\t\t</filmographie>");
	};

$ProducerMovieLine:
	$MovieMainPart;

$MovieMainPart:
	<A*HREF="$MovieMainLink">$MovieMainTitle[ ][(]$MovieMainYear[)]
	( [ ][(]TV[)]
	  {
	    out.println("\t\t\t\t<tv/>");
	  }
	)? *</A>
	{
	  out.println("\t\t\t\t<title>"+$MovieMainTitle+"</title>");
	  out.println("\t\t\t\t<year>"+$MovieMainYear+"</year>");
	  out.println("\t\t\t\t<url>"+$MovieMainLink+"</url>");
	  //System.out.println("MovieInfo: "+$MovieMainTitle+" "+$MovieMainYear+" "+$MovieMainLink);
	};

$Actor_Filmographie:
	*<B><A[ ]NAME=["]
	(Actor
	  {
	    //System.out.println("Schauspieler gefunden");
	    out.println("\t\t<filmographie type='actor' sex='male'>");
	  }
	|Actress
	  {
	    //System.out.println("Schauspielerin gefunden");
	    out.println("\t\t<filmographie type='actor' sex='female'>");
	  }
	)["]>*<OL>
	( *<LI>*\n
	  {
	    out.println("\t\t\t<movie>");
	  }
	  $ActorMovieLine *</li>
	  {
	    out.println("\t\t\t</movie>");
	  }
	)+
	</ol>
	{
	  out.println("\t\t</filmographie>");
	  //System.out.println("Schauspieler-Info (Filmographie) durch");
	};

$ActorMovieLine:
	$MovieMainPart[ ]
	( <SMALL>TV[ ]Series</SMALL>[ ]
	  {
	    out.println("\t\t\t\t<tvseries/>");
	  }
	)? ([(]*[)])? ([ ][(]*[)])? [ ][ ] ($ActorRolePart)? ($ActorTmpAkaPart)*
	*(?</LI>*\n|</OL>*\n|<BR><BR></LI>|[ ][ ]</LI>*\n);

END