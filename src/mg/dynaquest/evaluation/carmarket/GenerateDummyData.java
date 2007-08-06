package mg.dynaquest.evaluation.carmarket;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

public class GenerateDummyData {

	/**
	 * @uml.property  name="zufallsgen"
	 */
	Random zufallsgen = new Random();

	/**
	 * @uml.property  name="farben" multiplicity="(0 -1)" dimension="1"
	 */
	String[] farben = { "Rot", "Gr�n", "Blau", "Schwarz", "Weiss", "Lila" };

	/**
	 * @uml.property  name="strassen" multiplicity="(0 -1)" dimension="1"
	 */
	String[] strassen = { "91er-Str.", "Abraham (City/Gaststr.)", "Achterdiek",
			"Achterh�fen", "Achterm�hlen", "Achtern Wittenmoor", "Achternstr.",
			"Ackerstr.", "Acordialstr.", "Adalbert-Stifter-Str.",
			"Adelheidstr.", "Adenauerallee", "Adlerstr.", "Adolf-de-Beer-Str.",
			"Adolf-Grimme-Str.", "Adolf-Rauchheld-Str.", "Ahlersweg",
			"Ahlkenweg", "Ahornweg", "Ahrensstr.", "Akazienstr.", "Akeleiweg",
			"Albert-Schweitzer-Str.", "Alexandersfeld", "Alexanderstr.",
			"Alfred-Kubin-Str.", "Allensteinstr.", "Alma-Rogge-Str.",
			"Altburgstr.", "Alte Amalienstr.", "Alte Raad", "Alte Weide",
			"Alteneschstr.", "Alter Eichenhof", "Alter Kamp", "Alter Postweg",
			"Am Alexanderhaus", "Am Bahndamm", "Am Bienenstand", "Am Borgwall",
			"Am Born", "Am Bornhorster See", "Am Brink", "Am Dornbusch",
			"Am Drielaker Kanal", "Am Ende", "Am Evelsmoor",
			"Am Festungsgraben", "Am Hagen", "Am Hayengraben", "Am Hochstand",
			"Am Hufeisen", "Am Kreyenhof", "Am K�stenkanal", "Am Patentbusch",
			"Am Pulverturm", "Am Sande", "Am Scheibenplatz", "Am Schie�stand",
			"Am Schlo�garten", "Am Schmeel", "Am Schulgraben",
			"Am Sch�tzenplatz", "Am Stadtmuseum", "Am Stadtrand", "Am Strehl",
			"Am Struthof", "Am St�benhaus", "Am Tegelbusch", "Am Tennispark",
			"Am Tweelb�ker See", "Am Wallplacken", "Am Wasserturm",
			"Am Wendehafen", "Am Wienhof", "Am Wunderburgpark",
			"Am W�schemeer", "Am Zollbaum", "Am Zuggraben", "Amalienstr.",
			"Amannstr.", "Ammergaustr.", "Ammerl�nder Heerstr.", "Amselweg",
			"Amsterdamer Ring", "An de Bullwisch", "An den Eschen",
			"An den Vo�bergen", "An der B�ke", "An der Beverb�ke",
			"An der Braker Bahn", "An der Feldwische", "An der Gro�en Wisch",
			"An der Hausb�ke", "An der Josefskirche", "An der Kastanie",
			"An der Kolckwiese", "An der Kreuzkirche", "An der Nordb�ke",
			"An der Schmiede", "An der S�db�ke", "An der Tonkuhle",
			"Andersenstr.", "Angelweg", "Ankerstr.", "Annabergstr.",
			"A.-v.-Droste-H�lshoff-Str.", "Ansgariustiergartenweg",
			"Anton-G�nther-Str.", "Antonstr.", "Apenrader Str.", "Arnikaweg",
			"Arnsteder Weg", "Arnswalder Str.", "Arp-Schnitker-Str.",
			"Artillerieweg", "Aschhauser Weg", "Auenweg", "Auf dem Hock",
			"August-Bebel-Str.", "August-Hanken-Str.", "August-Hinrichs-Str.",
			"August-Macke-Str.", "August-Schwettmann-Str.", "Auguststr.",
			"August-Wegmann-Str.", "August-Wilh.-K�hnholz-Str.", "Babenend",
			"Bachstelzenweg", "Bachstr.", "Bahnhofsallee", "Bahnhofstr.",
			"Bahnweg", "Bakenhusweg", "B�kenkamp", "B�keplacken", "B�keweg",
			"Balthasarweg", "Balzweg", "Banater Weg", "Bardieksweg",
			"B�rlauchweg", "Barschweg", "Baumeisterstr.", "Baumschulenweg",
			"Bauordenstr.", "Beentweg", "Beethovenstr.", "Behnenkamp",
			"Behrensstr.", "Bei den Erlen", "Bei der Sch�ferei",
			"Bekassinenweg", "Beowulfsweg", "Bergdiek", "Berliner Str.",
			"Bernd-H�ttemann-Str.", "Bernhard-Friedrich-Weg", "Bernhardstr.",
			"Bernhard-Winter-Str.", "Bertha-von-Suttner-Str.",
			"Bettina-von-Arnim-Ring", "Beverb�kstr.", "Biberweg",
			"Bielitzer Str.", "Billungerweg", "Binsenstr.",
			"Birkenfelder Str.", "Birkenhof", "Birkenweg", "Birkhuhnweg ",
			"Bismarckstr.", "Bittersweg", "Blankenburger Weg", "Bleicherstr.",
			"Bloher Kamp", "Bloher Landstra�e", "Bloherfelder Str.",
			"Bl�cherstr.", "Blumenhof", "Blumenstr.", "Bodenburgallee",
			"Bogenstr.", "Bohlendamm", "Bohlkenweg", "Bokeler Str.",
			"Bollmannsweg", "Bonhoefferstr.", "Borbecker Str.", "Borchersweg",
			"Borsigstr.", "Botterkamp", "B�versweg", "Bowenkamp",
			"Brachvogelweg", "Brahmkamp", "Brahmweg", "Braker Weg",
			"Brakmannsweg", "Brandenburger Str.", "Brandsweg", "Brassenweg",
			"Breewaterweg", "Bremer Heerstr.", "Bremer Str.", "Bremersweg",
			"Breslauer Str.", "Brieger Str.", "Br�kerei", "Brokforster Weg",
			"Brokhauser Weg", "Brommystr.", "Brookweg", "Br�derstr.",
			"Brummelweg", "Brunnenkamp", "Brunsbrok", "Br�sseler Str.",
			"Buchenhof", "Bucholt", "Buchtstr.", "Buchweizenweg", "Bultenweg",
			"B�mmersteder Tredde", "Bundesbahnweg", "Burenkamp",
			"B�rgerbuschweg", "B�rgereschstr.", "B�rgerfelder Str.",
			"B�rgerstr.", "Burmesterstr.", "Buschhagenweg", "Buschstr.",
			"Bussardweg", "Butenweg", "Butjadinger Str.", "Buttelweg",
			"Byronstr.", "C�cilienplatz", "C�cilienstr.", "Carl-Eggers-Weg",
			"Carl-Friedr.-Gau�-Str.", "Carl-Friedr.-Spieske-Str.",
			"Carl-Heinr.-Slevogt-Str.", "Carl-v.-Ossietzky-Str.",
			"Carl-W�ltje-Str.", "Charlottenstr.", "Chaukenstr.",
			"Chemnitzer Str.", "Cheruskerweg", "Choletstr.",
			"Christian-Runde-Weg", "Christopherweg", "Clausewitzstr.",
			"Cloppenburger Str.", "Comeniusweg", "Dachsweg", "D�hlmannsweg",
			"Dalbenstr.", "Damm", "Dammbleiche", "Dammschanze",
			"Dannenbuschweg", "Dantestr.", "Danziger Str.", "Dedestr.",
			"Deelweg", "Deichweg", "Denkmalsweg", "Dersagauweg",
			"Devrientstr.", "Dianastr.", "Diedrich-Brinkmann-Str.",
			"Die�elweg", "Dietrich-Kohl-Weg", "Dietrichsweg", "Dillweg",
			"Dobbenstr.", "Dohlenhorst", "Dohlenweg", "Donarstr.",
			"Donnerschweer Str.", "Dorfweg", "Dornstede", "Dr.-Behring-Str.",
			"Dr.-Eden-Str.", "Dr.-Erich-Dannemann-Str.",
			"Dr.-Hans-Kl�ber-Str.", "Dr.-Sauerbruch-Str.", "Dr.-Sch��ler-Str.",
			"Dr.-Theodor-Goerlitz-Str.", "Dr.-Virchow-Str.", "Dragonerstr.",
			"Dreschkamp", "Dresdener Str.", "Drielaker Heide",
			"Drielaker Str.", "Dringenburger Str.", "Dr�gen-Hasen-Weg",
			"Drohnenweg", "Drosselweg", "D�rerstr.", "Duvenhorst", "Dwaschweg",
			"Ebereschenweg", "Eberswalder Weg", "Eddeshorn",
			"Edewechter Landstr.", "Eduard-M�ricke-Str.", "Eekenhorst",
			"Efeustr.", "Egerstr.", "Ehnernstr.", "Ehnkenweg", "Ehrenpreisweg",
			"Eibenweg", "Eichendorffstr.", "Eichenstr.", "Eichh�rnchenweg",
			"Eichkamp", "Eidechsenstr.", "Eiffelstr.", "Eike-von-Repkow-Str.",
			"Einsteinstr.", "Eisvogelring", "Ekernstr.", "Ekkardstr.",
			"Elbestr.", "Elchweg", "Elimarstr.", "Elisabeth-Frerichs-Str.",
			"Elisabethstr.", "Ellenbogen", "Ellerholtweg", "Ellernbrok",
			"Elmendorfer Str.", "Elritzenweg", "Els�sser Str.",
			"Elsflether Str.", "Elsternweg", "Emilie-Oltmanns-Str.",
			"Emil-Nolde-Str.", "Emil-Pleitner-Gang", "Emma-Ritter-Weg",
			"Emsstr.", "Erich-Heckel-Str.", "Erich-K�stner-Str.", "Erikaweg",
			"Erlenweg", "Ermlandstr.", "Ernst-Barlach-Str.",
			"Ernst-Lemmer-Str.", "Ernst-L�wenstein-Str.",
			"Ernst-Ludwig-Kirchner-Str.", "Eschenplatz", "Escherweg",
			"Eschstr.", "E�kamp", "Etzhorner Weg", "Eugen-Richter-Str.",
			"Eupener Str.", "Europaplatz", "Eustachiusweg", "Eutiner Str.",
			"Ewigkeit", "Eylersweg", "Fahrenkamp", "Falkenweg", "Falklandstr.",
			"Familie-Mechau-Str.", "Fasanenweg", "Feldahornweg", "Feldkamp",
			"Feldstr.", "Felix-Wankel-Str.", "Ferd.-Friedensburg-Str.",
			"Ferd.-Koch-Str.", "Ferd.-Lassalle-Str.", "Ferd.-von-Schill-Str.",
			"Feststr.", "Fichtenstr.", "Finkenweg", "Fliederweg",
			"Fliednerstr.", "Fliehweg", "Flogsand", "Florianstr.",
			"Fl�tenstr.", "F�hrenkamp", "Forellenweg", "Frankenstr.",
			"Frankfurter Weg", "Franz-Josef-Bu�-Str.", "Franz-Marc-Str.",
			"Franz-Poppe-Str.", "Franz-Radziwil-Str.", "Franz-Reyersbach-Str.",
			"Fraunhoferweg", "Freesenweg", "Freiherr-vom-Stein-Str.",
			"Friedhofsweg", "Friedlandstr.", "Friedr.-August-Platz",
			"Friedr.-Chr.-Dahlmann-Str.", "Friedr.-Ebert-Str.",
			"Friedr.-Naumann-Str.", "Friedr.-R�der-Str.", "Friedrichstr.",
			"Friesenstr.", "Frieslandstr.", "Fritz-Bock-Str. ",
			"Fritz-Reuter-Str.", "Fr�belstr.", "Fuchsweg", "Fuhrenweg",
			"Fuldastr.", "F�rstenwalder Str.", "Gabelsbergerweg", "Gabelweg",
			"Gabriele-M�nter-Str.", "Gagelweg", "Galileiweg", "Ganterweg",
			"Gardelerweg", "Garnholter Str.", "Gartenstr.", "Gartentorstr.",
			"G�rtnerstr.", "Gaststr.", "Gasweg", "Gau�str.", "Gebkenweg",
			"Gebr�der-Grimm-Str.", "Geestkamp", "Georg-B�lts-Str.",
			"Georg-Sello-Weg", "Georgstr.", "Gerberhof",
			"Gerhard-Muntink-Str.", "Gerhard-Schnitger-Str.",
			"Gerhard-Stalling-Str.", "Gerichtsstr.", "Gersteweg",
			"Gertrud-B�umer-Weg", "Gertrudenstr.", "Geschwister-Scholl-Str.",
			"Giesenweg", "Gildestr.", "Gimpelstr.", "Ginsterweg",
			"Glash�ttenstr.", "Glatzer Str.", "Gleisweg", "Glockenblumenweg",
			"Gloyesteenstr.", "Gneisenaustr.", "Goerdelerstr.", "Goethestr.",
			"Gografenweg", "Goldammerweg", "Goldfischweg", "Goldrautenweg",
			"Goldregenweg", "Goosweg", "Gorch-Fock-Str.", "G�rlitzer Str.",
			"Gotenstr.", "Gottfried-Keller-Str.", "Gotthelfstr.",
			"G�ttinger Str.", "Gottlieb-Becker-Str.", "Gottorpstr.",
			"Graf-Dietrich-Str.", "Graf-Spee-Str.", "Grashornweg",
			"Grasm�ckenweg", "Graudenzer Str.", "Greifswalder Str.",
			"Gremsm�hler Str.", "Grenadierweg", "Grillenweg", "Gristeder Str.",
			"Groninger Str.", "Gropiusstr.", "Gro�-Bornhorster Str.",
			"Gro�e Hamheide", "Gro�er Kuhlenweg", "Gro�marktstr.", "Grotepool",
			"Gr�nberger Str.", "Gr�ne Str.", "Gr�nenkamp", "Gr�nteweg",
			"Gulbranssonstr.", "Gustav-Lienemann-Str.", "G�strower Weg",
			"Gutenbergstr.", "G�terstr.", "Haager Str.", "Haakestr.",
			"Haareneschstr.", "Haarenfeld", "Haarenstr.", "Haarenufer",
			"Habichtsweg", "Hackenweg", "Hadersleber Str.", "Hafenpromenade",
			"Haferweg", "Hagelmannsweg", "H�herweg", "Hainbuchenweg",
			"Halsbeker Str.", "Hamelmannstr.", "H�ndelstr.",
			"Hans-B�ckler-Str.", "Hans-Fleischer-Str.", "Hans-Holbein-Str.",
			"Hans-Lodi-Str.", "Hardenbergstr.", "Harlingerstr.",
			"Harmoniestr.", "Harmsweg", "Harreweg", "Hartenkamp",
			"Hartenscher Damm", "Haseler Weg", "Haselriege", "Hasenpadd",
			"Hasenweg", "Ha�forter Str.", "Haubentaucherring", "Hauptstr.",
			"Hausb�ker Weg", "Hebbelstr.", "Hechtstr.", "Heckengang",
			"Hedwig-Heyl-Str.", "Hedwigstr.", "Heidelberger Str.", "Heideweg",
			"Heidjerweg", "Heidkamper Weg", "Heidplacken", "Heidschnuckenweg",
			"Heiligengeiststr.", "Heiligengeistwall", "Heilwigstr.", "Heimeck",
			"Heinr.-Brockmann-Str.", "Heinr.-Br�ning-Str.",
			"Heinr.-Campendonk-Str.", "Heinr.-Fr�st�ck-Str.",
			"Heinr.-Krahnst�ver-Str.", "Heinr.-Sandstede-Str.",
			"Heinr.-Sch�tte-Str.", "Heinr.-Strack-Str.", "Heinrichstr.",
			"Heinr.-von-Gagern-Str.", "Heisterweg", "Helene-Lange-Str.",
			"Helgolandstr.", "Hellmskamp", "Helmsweg", "Hemmelsb�ker Kanalweg",
			"Henkenweg", "Henri-Dunant-Str.", "Henry-B�ger-Str.",
			"Herbartstr.", "Hermann-Allmers-Weg", "Hermann-Ehlers-Str.",
			"Hermann-Kayser-Weg", "Hermann-Oncken-Weg", "Hermannst�dter Str.",
			"Hermannstr.", "Hermann-Tempel-Str.", "Hermelinweg",
			"Hero-Dietrich-Hillerns-Str.", "Herrenweg", "Hertzstr.",
			"Hesterkamp", "Heynesweg", "Hilbers Kamp", "Hindenburgstr.",
			"Hinnerksweg", "Hinrich-Wilhelm-Kopf-Str.", "Hirschberger Str.",
			"Hirschweg", "Hirseweg", "Hirtenweg", "Hochhauser Str.",
			"Hochheider Weg", "Hoffkamp", "Hogenkamp", "Hohenmoorstr.",
			"Hoher Weg", "Hoikenweg", "Holler Landstr.", "Holtmannsweg",
			"Holtzinger Str.", "Holunderweg", "Holzweg", "Hopfenweg",
			"Hoppenriekels", "H�rneweg", "Hoyersgang", "Hubertusweg",
			"Huflattichweg", "Hugo-Eckener-Str.", "Hugo-Gaudig-Str.",
			"Hugo-Zieger-Str.", "Hullmanns Kamp", "Hullmannstr.",
			"H�llstedter Str.", "H�lsenhof", "Hultschiner Str.",
			"Humboldtstr.", "Hummelweg", "Hundsm�hler Str.", "Hunsr�cker Str.",
			"Huntemannstr.", "Huntestr.", "Husarenweg", "Husbrok",
			"Husteder Weg", "Ibsenstr.", "Idar-Obersteiner-Str.", "Iltisweg",
			"Im Bahnwinkel", "Im Brook", "Im Dreieck", "Im Drielaker Moor",
			"Im Eichengrund", "Im Engelland", "Im Hankenhof", "Im Heidegrund",
			"Im Ofenerfeld", "Im Ried", "Im Schilf", "Im Wiesengrund",
			"Immenweg", "In der Allmende", "Ina-Seidel-Str.", "Industriehof",
			"Industriestr.", "Infanterieweg", "Innsbrucker Str.",
			"Insterburger Str.", "Irisweg", "Isenkamp", "Jagdweg", "J�gerstr.",
			"Jahnstr.", "Jakobistr.", "Jan-Oeltjen-Str.", "Janusz-Korczak-Weg",
			"Jawlenskystr.", "Jenaer Str.", "Johannes-Brahms-Str.",
			"Joh.-Gerdes-Str.", "Joh.-Heinr.-Brandes-Str.",
			"Joh.-Hinr.-Engelbart-Weg", "Johannisgang", "Johannisstr.",
			"Johann-Justus-Weg", "Joseph-Bernhard-Winck-Str.",
			"Julius-Leber-Str.", "Julius-Mosen-Platz", "Junkerburg",
			"Junkerstr.", "Justus-von-Liebig-Str.", "Kaiserstr.", "Kalmusweg",
			"Kampstr.", "Kanalstr.", "Kandinskystr.", "Kanonierstr.",
			"Kantstr.", "Kardinal-von-Galen-Str.", "Karl-Arnold-Str.",
			"Karl-Bunje-Str.", "Karl-Jaspers-Str.", "Karlsbadstr.", "Karlstr.",
			"Karl-Wilhelm-Flor-Str.", "K�rntner Str.", "Karpfenweg",
			"Karuschenweg", "Kasernenstr.", "Kaspersweg", "Kastanienallee",
			"Katharinenstr.", "K�the-Kollwitz-Str.", "Kattenbarg",
			"Kattowitzer Str.", "Kavallerieweg", "Kennedystr.", "Keplerstr.",
			"Kernbei�erring", "Kerschensteinerstr.", "Kiebitzweg",
			"Kiefernweg", "Kiehnpool", "Kielweg", "Kiesgrubenstr.",
			"Kirchhofstr.", "Kirchhofsweg", "Kirchweg", "Klarmannsweg",
			"Klausenburger Str.", "Klaus-Groth-Str.", "Kl�vemannstr.",
			"Kleenskamp", "Kleestr.", "Kleiberstr.", "Klein-Bornhorster Str.",
			"Kleine Hamheide", "Kleine Str.", "Kleiner Kuhlenweg",
			"Kleiststr.", "Klingenbergplatz", "Klingenbergstr.",
			"Klosterholzweg", "Klostermark", "Klosterstr.", "Klusweg",
			"Kneippstr.", "Kniestr.", "Kniphauser Str.", "Kolberger Weg",
			"Kolpingstr.", "K�nigsberger Str.", "K�nigskerzenweg",
			"Konradstr.", "Koopmannweg", "Kopenhagener Str.", "Kopernikusstr.",
			"Koppelstr.", "Kornblumenweg", "Kornstr.", "Kortjanweg",
			"Kortlangstr.", "K�sliner Weg", "K�stersweg", "K�terhof",
			"Kranbergstr.", "Kranichstr.", "Kreuzdornweg", "Kreuzstr.",
			"Kreyenstr.", "Kriegerstr.", "Kr�gerskamp", "Kronsbeerenweg",
			"Kronst�dter Str.", "Krugweg", "Krumme Str.", "Krusenweg",
			"Kuckucksweg", "Kuhlenkamp", "Kummerkamp", "K�pkersweg",
			"Kurlandallee", "Kurt-Huber-Str.", "Kurt-Schumacher-Str.",
			"Kurwickstr.", "Kurze Str.", "K�striner Str.", "Lachsweg",
			"Ladestr.", "Lafontainestr.", "Lagerstr.", "Lambertistr.",
			"Lammweg", "Landweg", "Landwehrstr.", "Lange Str.", "Langenweg",
			"L�rchenring", "Largauweg", "Lasiusstr.", "Leffersweg",
			"Lehmkuhlenstr.", "Lehmplacken", "Leibnizstr.", "Leinweg",
			"Leipziger Str.", "Lenzweg", "Leobsch�tzer Str.",
			"Leon-Bukofzer-Str.", "Lerchenspornweg", "Lerchenstr.",
			"Lerigauweg", "Lessingstr.", "Leuchtenburger Str.",
			"Leuschnerstr.", "Libellenweg", "Liegnitzer Str.",
			"Lilienthalstr.", "Lindenallee", "Lindenhofsgarten", "Lindenstr.",
			"Linsweger Str.", "Lisztstr.", "Logemannskamp", "Londoner Str.",
			"L�nsweg", "Loogenweg", "Lothringer Str.", "Louise-Schroeder-Str.",
			"L�wenzahnweg", "Loyerender Weg", "L�bbenbuschweg", "L�bskamp",
			"Ludw.-Erhard-Str.", "Ludw.-Freese-Str.", "Ludw.-Kaas-Str.",
			"Ludw.-Klingenberg-Weg", "Ludw.-Quidde-Weg", "Ludw.-Strack-Str.",
			"L�ntjenweg", "Lupinenstr.", "Lustgarten", "L�ttichstr.",
			"Luxemburger Str.", "Luzernenstr.", "Maastrichter Str.",
			"Maienweg", "Malvenweg", "Mansholter Str.", "Marburger Str.",
			"Marderweg", "Margarete-Gramberg-Str.", "Margaretenstr.",
			"Maria-Montessori-Str.", "Maria-von-Jever-Str.",
			"Marie-Juchacz-Ring", "Marienstr.", "Marschweg",
			"Mars-la-Tour-Str.", "Martin-Buber-Str.", "Martin-Luther-Str.",
			"Martin-Niem�ller-Str.", "Masurenstr.", "Matthias-Claudius-Str.",
			"Matthias-Erzberger-Str.", "Max-Beckmann-Str.",
			"Maximilian-Kolbe-Str.", "Max-Mehner-Str.", "Max-Pechstein-Str.",
			"Meerkamp", "Meerweg", "Meeschweg", "Mehlbeerenweg",
			"Meinardusstr.", "Meisenweg", "Melchiorweg", "Melkbrink",
			"Mellumstr.", "Memeler Str.", "Merzdorfstr.", "Messestr.",
			"Metzer Str.", "Migemkenweg", "Milanweg", "Milchstr.", "Mistelweg",
			"Mittagsweg", "Mittelgang", "Mittelkamp", "Mittelweg", "Mohnweg",
			"Mohrstr.", "Moli?restr.", "Moltkestr.", "Moorkamp", "Moorweg",
			"Moosweg", "Morgenweg", "Moslestr.", "Mottenstr.", "M�wenweg",
			"Mozartstr.", "M�ggenweg", "M�hlenhofsweg", "M�hlenstr.",
			"M�llersweg", "M�ller-vom-Siel-Str.", "M�mmelmannsweg",
			"M�nnichstr.", "M�nsterberger Str.", "M�nstermannstr.",
			"Muttenpottsweg", "Mutzenbecherstr.", "Myliusstr.",
			"Nachtigallenweg", "Nadorster Str.", "Nedderend", "Nedderlandsweg",
			"Neenkamp", "Neisser Str.", "Nelkenstr.", "Nerzweg",
			"Nettelbeckstr.", "Neue Donnerschweer Str.", "Neuenkruger Str.",
			"Neuer Weg", "Neus�dender Weg", "Newtonstr.", "Nibelungenstr.",
			"Niederkamp", "Niedersachsendamm", "Niendorfer Weg",
			"Nikolaikirchweg", "Nikolaiweg", "Nikolaus-Bernett-Str.",
			"Nikolausstr.", "Nimrodweg", "Noackstr.", "Nobelstr.", "Nollstr.",
			"Nordenhamer Weg", "Norderdiek", "Norderstr.",
			"Nordmoslesfehner Str.", "Nordring", "Nordstr.", "Nordtangente",
			"Nymphenweg", "Oederstr.", "Ofener Str.", "Ofenerdieker Str.",
			"Ohlenbuschweg", "Ohmsteder Esch", "Oldenrome", "Oldeweg",
			"Olmsweg", "Oppelner Str.", "Orfenweg", "Oskar-Homt-Str.",
			"Oskar-Schlemmer-Str.", "Osloer Str.", "Osterdiek", "Osteresch",
			"Osterkampsweg", "Osterstr.", "Ostlandstr.", "Ostring",
			"�stringer Str.", "Ostweg", "Otterweg", "Otto-Dix-Str.",
			"Otto-Modersohn-Str.", "Ottostr.", "Otto-Suhr-Str.",
			"Otto-Wels-Str.", "�verkamp", "Pappelallee", "Paradewall",
			"Pariser Str.", "Parkstr.", "Pasteurstr.",
			"P.-Modersohn-Becker-Str.", "Paul-Bonatz-Str.", "Paul-Klee-Str.",
			"Paul-Krey-Str.", "Paul-L�be-Str.", "Paulstr.",
			"Paul-Tantzen-Str.", "Peerdebrok", "Pestalozzistr.", "Pestrupsweg",
			"Peterstr.", "Pf�nderweg", "Pfauenstr.", "Pferdemarkt",
			"Philipp-de-Haas-Str.", "Philipp-Scheidemann-Str.",
			"Philosophenweg", "Pillauer Weg", "Pionierweg", "Pirolweg",
			"Pirschweg", "Plaggenhau", "Planckstr.", "Pophankenweg",
			"Porsenbergstr.", "Postenweg", "Posthalterweg", "Poststr.",
			"Presuhnstr.", "Prie�nitzstr.", "Prinzessinweg", "Proppingstr.",
			"Quellenweg", "Quendelstr.", "Querweg", "Rabenweg", "Radbodstr.",
			"Rahel-Varnhagen-Weg", "Raiffeisenstr.", "Rallenstr.",
			"Ramakersweg", "Ramsauerstr.", "Rankenstr.", "Rapsweg",
			"Rasteder Str.", "Rastweg", "Ratsherr-Schulze-Str.", "Rauhehorst",
			"Rebenstr.", "Rebhuhnweg", "Reekenweg", "Regenpfeiferweg",
			"Rehweg", "Reichenspergerstr.", "Reichweinstr.", "Reiherweg",
			"Reinhardweg", "Reinhold-Maier-Str.", "Reiterstr.",
			"Rembrandtstr.", "Rennplatzstr.", "Rheinstr.", "Ricarda-Huch-Str.",
			"Richard-Strauss-Str.", "Richard-tom-Dieck-Str.",
			"Richard-Wagner-Platz", "Rigaer Weg", "Ringelblumenweg",
			"Rispenweg", "Ritterstr.", "Robert-Blum-Str.",
			"Robert-Bunsen-Str.", "Robert-Koch-Str.", "Robert-von-Mayer-Str.",
			"Rodenkirchener Weg", "Roggemannstr.", "Roggenweg", "Rohdenweg",
			"Rohrdommelweg", "Rollandstr.", "R�ntgenstr.", "Roonstr.",
			"Rosenbohmsweg", "Rosenstr.", "Rosmarinweg", "Rossittenweg",
			"Rostocker Str.", "Rotdornstr.", "Rotfederweg", "Rotkehlchenweg",
			"Rotschenkelweg", "Rousseaustr.", "R�wekamp", "Rowoldstr.",
			"Rubensstr.", "R�bezahlstr.", "Rudolf-Bultmann-Str. ",
			"Rudolf-Diesel-Str.", "R�gener Ring", "R�genwalder Str.",
			"Rummelweg", "R�schenweg", "Ruselerstr.", "R�stringer Str.",
			"R�thningstr.", "Saarstr.", "Sachsenstr.", "Sackhofsweg",
			"Sagersweg", "Salbeistr.", "Sandberg", "Sanddornweg",
			"Sandfurter Weg", "Sandkamp", "Sandkruger Str.", "Sandkuhlenweg",
			"Sandstr.", "Sandweg", "Sch�ferstr.", "Schafgarbenweg",
			"Schafj�ckenweg", "Schagenweg", "Sch�persweg", "Scharnhorststr.",
			"Schaumkrautweg", "Scheibenweg", "Scheideweg", "Schellenberg",
			"Schellsteder Weg", "Schiebenkamp", "Schillerstr.", "Schimmelweg",
			"Schinkelstr.", "Schlachthofstr.", "Schlagbaumweg", "Schlehenweg",
			"Schleistr.", "Schleusenstr.", "Schlieffenstr.", "Schlo�platz",
			"Schlo�wall", "Schl�sselblumenweg", "Schl�terstr.", "Schmaler Weg",
			"Schm�lkamp", "Schmidt-Rottluff-Str.", "Schmiedeweg",
			"Schnepfenweg", "Sch�llkrautweg", "Sch�renpad", "Schramperweg",
			"Schreberweg", "Schubertstr.", "Schulenburgstr.", "Schulstr.",
			"Schulweg", "Schulzeweg", "Sch�tte-Lanz-Str.", "Sch�tzenhofstr.",
			"Sch�tzenweg", "Schwalbenstr.", "Schwanenweg", "Schwertlilienweg",
			"Sedanstr.", "Seerosenweg", "Seggenweg", "Semperstr.",
			"Shakespearestr.", "Sieben Berge", "Sieben B�sen",
			"Siebenb�rger Str.", "Siebensternweg", "Siegfriedstr.", "Sielweg",
			"Skagerrakstr.", "Sodenstich", "Sommerweg", "Sonnenkampstr.",
			"Sonnenstr.", "Sonnentauweg", "Sophienstr.", "Sophie-Sch�tte-Str.",
			"Sp�tenweg", "Spechtweg", "Spencerstr.", "Sperberweg",
			"Sperlingsweg", "Spezaweg", "Spittweg", "Sportweg", "Spreenweg",
			"Sprungweg", "Staakenweg", "Stadtfeld", "St�ndelweg", "Starenweg",
			"Stargarder Weg", "Starklofstr.", "Stationsweg", "Stau",
			"Staugraben", "Staulinie", "Staustr.", "Stedinger Str.",
			"Steenkenweg", "Steinkauzweg", "Steinweg", "Stephanusstr.",
			"Sternstr.", "Stettiner Str.", "Steubenstr.", "Stieglitzweg",
			"Stiekelkamp", "Stiftsweg", "Stiller Weg", "Stockholmer Str.",
			"Storchweg", "St�rtebekerstr.", "Strackerjanstr.",
			"Stralsunder Str.", "Stra�burger Str.", "Streekenweg",
			"Stresemannstr.", "Struthoffs Kamp", "Stubbenweg", "Stumpfer Weg",
			"S�derdiek", "Sudetenstr.", "S�dring", "S�dweg", "Suhrkamp",
			"S�ndermannsweg", "Taastruper Str.", "Tangastr.", "Tannenbergstr.",
			"Tannenkampstr.", "Tannenstr.", "Tappenbeckstr.", "Taubenstr.",
			"Teebkengang", "Tegelkamp", "Teichhuhnweg", "Teichstr.",
			"Theaterwall", "Theodor-Dirks-Weg", "Theodor-Fontane-Str.",
			"Theodor-Francksen-Str.", "Theodor-Heuss-Str.",
			"Theodor-K�rner-Weg", "Theodor-Pekol-Str.", "Theodor-Storm-Str.",
			"Theodor-Tantzen-Platz", "Theodor-Wabnitz-Str.", "Thomasburg",
			"Thomas-Dehler-Str.", "Thomas-Mann-Str.", "Thorm�hlen",
			"Thorner Str.", "Thorwaldsenstr.", "Thymianweg", "Tilsiter Str.",
			"Tirpitzstr.", "Tischbeinstr.", "Tondernstr.", "Tonweg",
			"Torsholter Str.", "Trakehnenstr.", "Triftweg", "Trommelweg",
			"Troppauer Str.", "T�binger Str.", "Tuchtweg", "Tweelb�ker Tredde",
			"Tweelb�ker Weg", "Twiskenweg", "�ber der Heide", "Uferstr.",
			"Uhlenweg", "Uhlhornsweg", "Ulmenstr.", "Ulrich-von-Hutten-Str.",
			"Unter den Eichen", "Unter den Linden", "Unterm Berg", "Unterstr.",
			"Vahlenhorst", "Vareler Str.", "Veilchenweg", "Verdistr.",
			"Vereinigungsstr.", "Viktoriastr.", "Vogelstange", "Von-Alten-Weg",
			"Von-Berger-Str.", "Von-Bodelschwingh-Str.", "Von-Borries-Str.",
			"Von-Finckh-Str.", "Von-Halem-Str.", "Von-Helmholtz-Str.",
			"Von-Ketteler-Str.", "Von-Kobbe-Str.", "Von-L�tzow-Str.",
			"Von-M�ller-Str.", "Von-Schrenck-Str.", "Von-Th�nen-Str.",
			"Von-Tresckow-Weg", "V�rdem�hlen", "Vo�str.", "Wabenweg",
			"Wacholderweg", "Wachtelweg", "Wahnb�kenweg", "Waldkauzweg",
			"Waldmannsweg", "Waldmeisterweg", "Wallstr.",
			"Walter-Meckauer-Str.", "Walther-Diekmann-Str.",
			"Walther-Rathenau-Str.", "Wardenburgstr.", "Warnsweg",
			"Waterender Weg", "Watertucht", "Wechloyer Weg", "Weddigenstr.",
			"Wehdestr.", "Weidamm", "Weidenstr.", "Weidweg", "Weimarer Str.",
			"Weiselweg", "Wei�dornweg", "Wei�enmoorstr.", "Weitzstr.",
			"Wellenweg", "Wemkenhofsweg", "Werbachstr.", "Werftweg",
			"Werrastr.", "Weserstr.", "Weskampstr.", "Westerdiek",
			"Westerender Weg", "Westeresch", "Westerholtsweg",
			"Westersteder Str.", "Westerstr.", "Westfalendamm", "Wichelnstr.",
			"Wichernstr.", "Wichmannsweg", "Wickenweg", "Widderweg",
			"Widukindstr.", "Wiefelsteder Str.", "Wiener Str.", "Wienstr.",
			"Wieselweg", "Wiesenstr.", "Wildenlohsdamm",
			"Wilhelm-Ahlhorn-Str.", "Wilhelm-Busch-Str.", "Wilhelm-Degode-Weg",
			"Wilhelm-Kempin-Str.", "Wilhelm-Kr�ger-Str.",
			"Wilhelm-Morgner-Str.", "Wilhelm-Nieberg-Str.",
			"Wilhelm-Raabe-Str.", "Wilhelmshavener Heerstr.", "Wilhelmstr.",
			"Wilhelm-Weber-Str.", "Wilhelm-Wilke-Str.", "Wilhelm-Wisser-Str.",
			"Wilkenweg", "Willa-Thorade-Str.", "Willehadweg", "Willersstr.",
			"Willi-Trinne-Str.", "Windm�hlenweg", "Windr�schenweg",
			"Windthorststr.", "Winkelmannstr.", "Winkelweg", "Wischweg",
			"Wismarer Weg", "Wittengang", "Wittingsbrok", "Wittsfeld",
			"Wolfgang-Hartung-Str.", "Wolfgang-Heimbach-Str.",
			"Wolfsbr�cker Weg", "Wulfswall", "Wullgrasweg", "Wunderburgstr.",
			"W�rzburger Str.", "Yorckstr.", "Zanderweg", "Zaunk�nigstr.",
			"Zedernweg", "Zeisigweg", "Zeppelinstr.", "Zeughausstr.",
			"Ziegelhofstr.", "Ziegelweg", "Zietenstr.", "Zillestr.",
			"Zinzendorfstr.", "Zobelweg", "Z�richer Str. ", "Zuschlag",
			"Zweigstr.", "Zypressenweg" };

	/**
	 * @uml.property  name="markenUndModelle" multiplicity="(0 -1)" dimension="2"
	 */
	String[][] markenUndModelle = {
			{ "Alfa Romeo", "147", "156", "166", "GT", "GTV", "Spider" },
			{ "Aston Martin", "DB7", "DB9", "Vanquish" },
			{ "Audi", "A2", "A3", "A4", "A6", "A8", "allroad quattro", "TT" },
			{ "Bentley", "Arnage", "Continental" },
			{ "BMW", "3er-Reihe", "5er-Reihe", "6er-Reihe", "7er-Reihe", "X3",
					"X5", "Z4" },
			{ "BMW Alpina", "B10/D10", "B3", "B7", "Roadster S" },
			{ "Cadillac", "CTS", "Seville" },
			{ "Chevrolet", "Corvette", "S10 Pickup", "Tahoe", "Trailblazer",
					"Trans Sport" },
			{ "Chrysler", "300M", "Crossfire", "Grand Voyager", "PT Cruiser",
					"Sebring", "Viper", "Voyager" },
			{ "Citroen", "Berlingo", "C2", "C3", "C3 Pluriel", "C5", "C8",
					"Jumper", "Jumpy", "Xsara", "Xsara Picasso" },
			{ "Daewoo", "Evanda", "Kalos", "Lacetti", "Lanos", "Matiz",
					"Nubira", "Rezzo" },
			{ "Daihatsu", "Copen", "Cuore", "Sirion", "Terios", "YRV" },
			{ "Ferrari", "360", "456M", "575M", "Challenge Stradale" },
			{ "Fiat", "Barchetta", "Doblo", "Ducato", "Idea", "Multipla",
					"Panda", "Punto", "Scudo", "Seicento", "Stilo", "Strada",
					"Ulysse" },
			{ "Ford", "Fiesta", "Focus", "Fusion", "Galaxy", "Ka", "Maverick",
					"Mondeo", "Ranger", "Streetka", "Tourneo Connect",
					"Transit", "Transit Connect" },
			{ "Honda", "Accord", "Civic", "CR-V", "HR-V", "Jazz", "Legend",
					"NSX", "S2000", "Stream" },
			{ "Hyundai", "Accent", "Atos", "Coupe", "Elantra", "Getz",
					"Matrix", "Santa Fe", "Sonata", "Terracan", "Trajet", "XG" },
			{ "Jaguar", "Daimler", "S-Type", "XJ", "XK", "X-Type" },
			{ "Jeep", "Cherokee", "Grand Cherokee", "Wrangler" },
			{ "KIA", "Carens", "Carnival", "Cerato", "Magentis", "Opirus",
					"Picanto", "Pregio", "Rio", "Shuma II", "Sorento" },
			{ "Lada", "110", "111", "112", "Niva", "Samara" },
			{ "Lamborghini", "Gallardo", "Murcielago" },
			{ "Lancia", "Lybra", "Phedra", "Thesis", "Ypsilon" },
			{ "Land Rover", "Defender", "Discovery", "Freelander",
					"Range Rover" },
			{ "Lexus", "GS", "IS", "LS", "RX", "SC" },
			{ "Lotus", "Elise", "Esprit", "Exige" },
			{ "Maserati", "Coupe", "Quattroporte", "Spyder" },
			{ "Maybach", "Maybach" },
			{ "Mazda", "2", "3", "6", "B-Serie", "MPV", "MX-5", "Premacy",
					"RX-8", "Tribute" },
			{ "Mercedes", "A-Klasse", "C-Klasse", "CLK-Klasse", "CL-Klasse",
					"E-Klasse", "G-Klasse", "M-Klasse", "S-Klasse",
					"SLK-Klasse", "SL-Klasse", "SLR McLaren", "Sprinter",
					"Vaneo", "Viano", "Vito" },
			{ "MG", "TF", "ZR", "ZS", "ZT", "ZT-T" },
			{ "Mini", "Mini" },
			{ "Mitsubishi", "Carisma", "Colt", "Galant", "Grandis", "L200",
					"Lancer", "Outlander", "Pajero", "Pajero Classic",
					"Pajero Pinin", "Pajero Sport", "Space Star", "Space Wagon" },
			{ "Morgan", "4/4", "Aero", "Plus 8" },
			{ "Nissan", "350Z", "Almera", "Almera Tino", "Interstar",
					"Kubistar", "Maxima QX", "Micra", "Pathfinder",
					"Patrol GR", "Pick Up", "Primastar", "Primera", "Terrano",
					"X-Trail" },
			{ "Opel", "Agila", "Astra G", "Astra H", "Combo", "Combo Tour",
					"Corsa C", "Meriva", "Movano", "Signum", "Speedster",
					"Vectra C", "Vivaro", "Zafira" },
			{ "Peugeot", "206", "307", "406", "607", "807", "Boxer", "Expert",
					"Partner" },
			{ "Porsche", "911", "Boxster", "Carrera GT", "Cayenne" },
			{ "Renault", "Clio III", "Espace", "Grand Espace", "Grand Scenic",
					"Kangoo", "Laguna", "Master", "Megane", "Scenic", "Trafic",
					"Twingo", "Vel Satis" },
			{ "Rolls-Royce", "Phantom", "Silver Seraph" },
			{ "Rover", "25", "45", "75", "Streetwise" },
			{ "Saab", "9-3", "9-5" },
			{ "Seat", "Alhambra", "Arosa", "Cordoba", "Ibiza", "Leon", "Toledo" },
			{ "Skoda", "Fabia", "Octavia", "Superb" },
			{ "smart", "smart" },
			{ "SsangYong", "Korando", "Musso", "Rexton" },
			{ "Subaru", "Forester", "G3X Justy", "Impreza", "Legacy", "Outback" },
			{ "Suzuki", "Alto", "Grand Vitara", "Ignis", "Jimny", "Liana",
					"Wagon R+" },
			{ "Toyota", "Avensis", "Avensis Verso", "Camry", "Celica",
					"Corolla", "Corolla Verso", "Hiace", "Hilux",
					"Land Cruiser", "Land Cruiser 100", "MR2", "Previa",
					"Prius", "RAV4", "Yaris", "Yaris Verso" },
			{ "Volga", "GAZ 31105" },
			{ "Volvo", "C70", "S40", "S60", "S80", "V50", "V70", "XC70", "XC90" },
			{ "VW", "Bora", "Caddy", "Golf IV", "Golf V", "LT 28", "LT 35",
					"Lupo", "New Beetle", "Passat", "Phaeton", "Polo",
					"Sharan", "T5", "Touareg", "Touran" } };

	/**
	 * @uml.property  name="orte" multiplicity="(0 -1)" dimension="1"
	 */
	String[] orte = { "ADorf", "BDorf", "CDorf", "DDorf", "EDorf", "FDorf",
			"GDorf", "HDorf", "IDorf", "JDorf", "KDorf", "LDorf", "MDorf",
			"NDorf", "ODorf", "PDorf", "QDorf", "RDorf", "SDorf", "TDorf",
			"UDorf", "VDorf", "WDorf", "XDorf", "YDorf", "ZDorf", "AStadt",
			"BStadt", "CStadt", "DStadt", "EStadt", "FStadt", "GStadt",
			"HStadt", "IStadt", "JStadt", "KStadt", "LStadt", "MStadt",
			"NStadt", "OStadt", "PStadt", "QStadt", "RStadt", "SStadt",
			"TStadt", "UStadt", "VStadt", "WStadt", "XStadt", "YStadt",
			"ZStadt" };

	/**
	 * @uml.property  name="vornamen" multiplicity="(0 -1)" dimension="1"
	 */
	String[] vornamen = { "Adalbero", "Adalbert", "Adelheid", "Afra", "Agathe",
			"Agnes", "Alban", "Alexis", "Alfons", "Alois", "Anastasia",
			"Andreas", "Angela", "Anna", "Anton", "Barbara", "Beatrix",
			"Benedikt", "Benno", "Berta", "Berthold", "Birgitta", "Blanka",
			"Bruno", "Burkhard", "Christine", "Christoph", "Clemens",
			"Cornelius", "Corona", "Daniel", "Daria", "Dominikus", "Dorothea",
			"Eberhard", "Eduard", "Elisabeth", "Euphemia", "Fabian", "Fabiola",
			"Felix", "Felizitas", "Ferdinand", "Florian", "Franz", "Franziska",
			"Fridolin", "Gabriel", "Georg", "Gerhard", "Gero", "Gottfried",
			"Gregor", "G�nter", "Hedwig", "Heinrich", "Helene", "Herbert",
			"Hildegard", "Hubert", "Hugo", "Irene", "Irmgard", "Ivo", "Jakob",
			"Joachim", "Johanna", "Johannes", "Joseph", "Juliana", "Julius",
			"Karl", "Kasimir", "Katharina", "Klara", "Konrad", "Korbinian",
			"Kunibert", "Kunigunde", "Leo", "Lucia", "Ludwig", "Lukas",
			"Magdalena", "Marcella", "Margarete", "Markus", "Martin",
			"Martina", "Maximilian", "Michael", "Monika", "Moritz", "Norbert",
			"Olaf", "Otto", "Pascal", "Patrick", "Paul", "Paula", "Peter",
			"Philipp", "Prisca", "Raimund", "Rainer", "Raphael", "Rita",
			"Robert", "Sabine", "Sebastian", "Severin", "Siegfried", "Silvia",
			"Simon", "Sophia", "Stephan", "Susanne", "Theodor", "Therese",
			"Thomas", "Ulrich", "Ursula", "Verena", "Veronika", "Viktor",
			"Werner", "Wilhelm", "Wolfgang", "Wolfram", "Xaver", "Zacharias" };

	/**
	 * @uml.property  name="nachnamen" multiplicity="(0 -1)" dimension="1"
	 */
	String[] nachnamen = { "M�ller", "Schmidt", "Schneider", "Meyer",
			"Benaglio", "Bordon", "Soldo", "Kuranyi", "Heinen", "Gerber",
			"Vranjes", "Barreto", "Hildebrand", "Meira", "Tiffert", "Szabics",
			"Hinkel", "Centurion", "Streller", "Marques", "Mei�ner", "Lahm",
			"Hleb", "Dangelmayr", "Mutzel", "Husterer", "Heldt", "Branco",
			"Stojanov", "Zivkovic", "Yakin", };

	public void dumpAll() {
		for (int i = 0; i < markenUndModelle.length; i++) {
			String[] row = markenUndModelle[i];
			System.out.print("Die Marke :" + row[0]
					+ " hat die folgenden Modelle");

			for (int j = 1; j < row.length; j++) {
				System.out.print(row[j] + " ");
			}
			System.out.println();
		}
	}

	public String[] getRandomMarkenzeile() {
		int zufall = zufallsgen.nextInt(markenUndModelle.length);
		return markenUndModelle[zufall];
	}

	public String getRandomString(String[] zeile) {
		int zufall = zufallsgen.nextInt(zeile.length);
		return zeile[zufall];
	}

	public int getRandomKM() {
		return getRandomZahl(200, 1000);
	}

	public int getRandomPreis() {
		return getRandomZahl(60, 1000);
	}

	public int getRandomZahl(int bereich, int mult) {
		int zufall = zufallsgen.nextInt(bereich);
		return zufall * mult;
	}

	public int getRandomZahlAusBereich(int min, int max) {
		int zufall = zufallsgen.nextInt(max - min) + min;
		return zufall;
	}

	public void dumpAutos(int count, String filename) throws Exception {
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));

		//out.write("FgstNr,Marke,Modell,Farbe,km,Preis"+"\n");
		for (int i = 0; i < count; i++) {
			String[] markenzeile = getRandomMarkenzeile();
			String marke = markenzeile[0];
			String modell = getRandomString(markenzeile);
			String farbe = getRandomString(farben);
			out.write((i + 1) + ";" + marke + ";" + modell + ";" + farbe + ";"
					+ this.getRandomKM() + ";" + this.getRandomPreis() + "\n");
		}
		out.close();
	}

	public void dumpHersteller(String filename) throws Exception {
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		//out.write("Marke,Strasse,Hausnummer,Plz,Ort"+"\n");
		for (int i = 0; i < this.markenUndModelle.length; i++) {
			out.write(markenUndModelle[i][0] + ";"
					+ this.getRandomString(this.strassen) + ";"
					+ this.getRandomZahlAusBereich(1, 999) + ";"
					+ this.getRandomZahlAusBereich(10000, 99999) + ";"
					+ this.getRandomString(this.orte) + "\n");
		}
		out.close();
	}

	public void dumpVerkaeufer(int count, String filename) throws Exception {
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		//out.write("VKNr,Vorname,Nachname,Strasse,Hausnummer,Plz,Ort"+"\n");
		for (int i = 0; i < count; i++) {
			out.write((i + 1) + ";" + this.getRandomString(vornamen) + ";"
					+ getRandomString(nachnamen) + ";"
					+ this.getRandomString(this.strassen) + ";"
					+ this.getRandomZahlAusBereich(1, 999) + ";"
					+ this.getRandomZahlAusBereich(10000, 99999) + ";"
					+ this.getRandomString(this.orte) + "\n");
		}
		out.close();
	}

	public void dumpVerkauf(int anzahlVerkaeufer, int anzahlAutos,
			String filename) throws Exception {
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		//out.write("FgstNr,VKNr"+"\n");
		for (int i = 0; i < anzahlAutos; i++) {
			out.write((i + 1) + ";"
					+ this.getRandomZahlAusBereich(1, anzahlVerkaeufer) + "\n");
		}
		out.close();
	}

	public GenerateDummyData() {

	}

	public void generateCarDummyData(int AnzahlVerkaeufer, int AnzahlAutos,
			String basedir) {
		try {
			dumpAutos(AnzahlAutos, basedir + "autos.txt");
			dumpHersteller(basedir + "hersteller.txt");
			dumpVerkaeufer(AnzahlVerkaeufer, basedir + "verkaeufer.txt");
			dumpVerkauf(AnzahlVerkaeufer, AnzahlAutos, basedir
					+ "verkaeufe.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateDummyAccessData(int anzahlQuellen, Calendar from,
			Calendar to, float minRate, float maxRate, String filename) {
		// F�r alle Quellen Daten ermitteln
		// Stunden-Intervalle
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));

			long noOfEntriesPerSource = ((((to.getTimeInMillis() - from
					.getTimeInMillis()) / 1000) / 60) / 60);
			for (int q = 0; q < anzahlQuellen; q++) {
				Calendar aktPunkt = (Calendar) from.clone();
				for (int d = 0; d < noOfEntriesPerSource; d++) {
					aktPunkt.add(Calendar.HOUR, 1);
					out.write("Quelle_"
							+ q
							+ ";"
							+ aktPunkt.get(Calendar.DAY_OF_MONTH)
							+ ";"
							+ (aktPunkt.get(Calendar.MONTH) + 1)
							+ ";"
							+ aktPunkt.get(Calendar.DAY_OF_WEEK)
							+ ";"
							+ aktPunkt.get(Calendar.HOUR_OF_DAY)
							+ ";"
							+ aktPunkt.get(Calendar.MINUTE)
							+ ";"
							+ aktPunkt.get(Calendar.WEEK_OF_YEAR)
							+ ";"
							+ ((float) (getRandomZahlAusBereich(Math
									.round(minRate * 100), Math
									.round(maxRate * 100))) / 100) + ";"
							+ getRandomZahlAusBereich(1, 4) + "\n");
				}
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		GenerateDummyData generateDummyData = new GenerateDummyData();
		generateDummyData.generateCarDummyData(30000,100000,"E:\\dissertation\\db\\AutoDB\\gen\\");
		Calendar from = Calendar.getInstance();
		from.set(2004, Calendar.JANUARY, 1, 0, 0, 0);
		Calendar to = Calendar.getInstance();
		to.set(2006, Calendar.DECEMBER, 31, 23, 59, 0);
		//generateDummyData.generateDummyAccessData(10, from, to, 0.5f, 10.0f,
		//		"E:\\dissertation\\db\\gen\\dummyAccess.txt");
	}
}