PAGE IMDB_ReturnedMovies

$IMDB_ReturnedMovies:
	*<p>A[ ]search[ ]for[ ]<b>*</b>[ ]found[ ]the[ ]following[ ]results:</p><ol>
	{
	  out.println("<result>");
	}
	($Movie)+
	*</ol>
	{
	  out.println("</result>");
	};

$Movie:
	<li><a[ ]href="$MovieLink">$MovieTitle</a></li>
	{
	  out.println("t<movie>");
	  out.println("\t\t<title>"+$MovieTitle+"</title>");
	  out.println("\t\t<url>"+$MovieLink+"</url>");
	  out.println("\t</movie>");
	};

END