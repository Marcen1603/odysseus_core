PAGE IMDB_MoviePage

$IMDB_MoviePage:
	* $MovieTitleYearBlock
	($DirectorsBlock)?
	($WritersBlock)?
	($GenreBlock)?
	($TaglineBlock)?
	($PlotBlock)?
	($RatingBlock)?
	($ActorsBlock)?
	*</html>
	{
	  out.println("\t</movie>");
	};

$MovieTitleYearBlock:
	*<html>
	{
	  out.println("\t<movie>");
	}
	*<table[ ]border="0"[ ]cellspacing="0"[ ]cellpadding="0"[ ]width="100%">*\n
	*<tr>*<td[ ]valign="top">*<br>*\n
	*<h1><strong[ ]class="title">$MovieTitle<small>[(]<a[ ]href="*">$MovieYear</a>[)]
	{
	  out.println("\t\t<title>"+$MovieTitle+"</title>");
	  out.println("\t\t<year>"+$MovieYear+"</year>");
	}
	</small>;

$DirectorsBlock:
	*<b[ ]class="blackcatheader">Directed[ ]by</b><br>*\n
	($Director)+
	*<br>*\n;

$Director:
	<a[ ]href="/Name$DirectorLink">$DirectorName</a><br>
	{
	  out.println("\t\t<director>");
	  out.println("\t\t\t<name>"+$DirectorName+"</name>");
	  out.println("\t\t\t<url>/Name"+$DirectorLink+"</url>");
	  out.println("\t\t</director>");
	};

$WritersBlock:
	*<b[ ]class="blackcatheader">Writing[ ]credits</b>
	*<br>*\n
	($Writer)+
	*<br>;

$Writer:
	<a[ ]href="$WriterLink">$WriterName</a>*<br>
	{
	  out.println("\t\t<writer>");
	  out.println("\t\t\t<name>"+$WriterName+"</name>");
	  out.println("\t\t\t<url>"+$WriterLink+"</url>");
	  out.println("\t\t</writer>");
	};

$GenreBlock:
	*<b[ ]class="ch">Genre:</b>*\n
	($GenreItem)+
	*<br>;

$GenreItem:
	*<a[ ]href="/Sections/Genres/*">$Genre</a>
	{
	  out.println("\t\t<genre>"+$Genre+"</genre>");
	};

$TaglineBlock:
	*<b[ ]class="ch">Tagline:</b>$Tagline<a*>
	{
	  out.println("\t\t<tagline>"+$Tagline+"</tagline>");
	};

$PlotBlock:
	*<b[ ]class="ch">Plot[ ]Outline:</b>$PlotOutline<a*>
	{
	  out.println("\t\t<plotOutline>"+$PlotOutline+"</plotOutline>");
	};

$RatingBlock:
	*<b[ ]class="ch">User[ ]Rating:</b>
	*<a[ ]href="*">*</a>
	*<b>$Rating</b>[ ][(]$NoOfVotes[ ]votes[)]
	{
	  out.println("\t\t<rating>");
	  out.println("\t\t\t<value>"+$Rating+"</value>");
	  out.println("\t\t\t<votes>"+$NoOfVotes+"</votes>");
	  out.println("\t\t</rating>");
	};

$ActorsBlock:
	*<b[ ]class="blackcatheader">Cast[ ]overview*</b></td></tr>
	($Actor)+
	*</table>;

$Actor:
	*<tr><td[ ]valign="top"><a[ ]href="$ActorLink">$ActorName</a></td>
	<td*</td><td[ ]valign="top">$ActorRole</td></tr>
	{
	  out.println("\t\t<actor>");
	  out.println("\t\t\t<name>"+$ActorName+"</name>");
	  out.println("\t\t\t<url>"+$ActorLink+"</url>");
	  out.println("\t\t\t<role>"+$ActorRole+"</role>");
	  out.println("\t\t</actor>");
	};

END