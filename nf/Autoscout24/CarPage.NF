PAGE CarPage

$CarPage:
	*<table[ ]width="100%"[ ]border="0"[ ]cellspacing="0"[ ]cellpadding="2">*\n
	<tr*</tr>*\n
	{
	  out.println("<result>");
	}
	($CarEntry)+
	{
	  out.println("</result>");
	};

$CarDetail:
	*(?["]);

$CarEntry:
	<TR><TD[ ]BGCOLOR="#FFFFFF"[ ]ALIGN="CENTER"[ ]VALIGN="MIDDLE">
	(<table*><tr><td*>)?
	<A[ ]HREF="$CarDetail">
	(<IMG[ ]SRC="$CarThumbnail"[ ]WIDTH="*"[ ]border="0">
	|<img[ ]src="$CarThumbnail"*>)
	(</A>|</a>)
	(</td></tr></table>)?
	</TD>*\n
	\t<td*>*\n\t\t<a[ ]href="$CarLink">$CarName</a><br>*</td>*\n
	\t<td*>*\n
	$CarBuild [&]nbsp[;]<br>$CarColor [&]nbsp[;]*\n\t</td>*\n
	\t<td*>$Baujahr<br>*</td>*\n
	\t<td*>$KM [&]nb*</td>*\n
	\t<td*>*\n\tEUR[&]nbsp[;]$Price(\r)?\n\t</td>*\n
	</tr>*\n<tr>*</tr>*\n
	{
	  out.println("<car>");
	  out.println("\t<detail>"+$CarDetail+"</detail>");
	  out.println("\t<thumbnail>"+$CarThumbnail+"</thumbnail>");
	  out.println("\t<link>"+$CarLink+"</link>");
	  out.println("\t<name>"+$CarName+"</name>");
	  out.println("\t<build>"+$CarBuild+"</build>");
	  out.println("\t<color>"+$CarColor+"</color>");
	  out.println("\t<baujahr>"+$Baujahr+"</baujahr>");
	  out.println("\t<km>"+$KM+"</km>");
	  out.println("\t<price>"+$Price+"</price>");
	  out.println("</car>");
	};

END