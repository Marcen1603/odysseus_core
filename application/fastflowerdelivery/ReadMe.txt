Installation & Starten:

- Pakete in Eclipse importieren
- MySQL Datenbank installieren.
	- Login Credentials in de.uniol.inf.is.odysseus.fastflowerdelivery.driver/config.js eintragen
	- Login Credentials in de.uniol.inf.is.odysseus.fastflowerdelivery.store/config.js eintragen
	- Query ausführen: CREATE TABLE driver(DriverReference VARCHAR(200) NOT NULL, PRIMARY KEY(DriverReference), Location VARCHAR(30),  Ranking INT(10));
	- Query ausführen: INSERT INTO driver (DriverReference, Location, Ranking) VALUES ('Hanno', 'Wechloy', 4);
	- Query ausführen: CREATE TABLE store( MinimumRanking INT(10), StoreReference VARCHAR(200) NOT NULL, PRIMARY KEY(DriverReference));
	- Query ausführen: INSERT INTO store (MinimumRanking, StoreReference) VALUES (3, 'Helmut');
- de.uniol.inf.is.odysseus.fastflowerdelivery.driver als Java Application starten
- de.uniol.inf.is.odysseus.fastflowerdelivery.store als Java Application starten
- Odysseus starten
- Script FastFlowerDelivery.qry in Odysseus importieren
- Script ausführen
- http://localhost:8080/view und http://localhost:8081/view im Webbrowser öffnen
- Mit "Helmut" und "Hanno" einloggen (Das erste einloggen verbindet die EventReceiver mit den Senken von Odysseus)