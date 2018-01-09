## Database
Voor het aanmaken van een account ga ik firebase gebruiken met inlognaam en wachtwoord. 

Ik maak een database met 2 delen:
* Een deel voor de baasjes met een foto, omschrijving, naam van het baasje, email van het baasje, naam van de hond, locatie van de hond en of er een advertentie uitstaat. 
* Een deel voor de uitlaters met een lijst van gemaakte afspraken en de naam van de uitlater. (evt later nog de ranking van de uitlater)

Database
  * baasjes
    * naam
    * email
    * omschrijving
    * foto
    * locatie
    * hondennaam
    * advertentiestatus
  * uitlaters
    * naam uitlater
    * lijst van afspraken

## Activities
MainActivity: 
* Login page. 
* Email en wachtwoord invoeren. 
* Button om in te loggen en gegevens checken met firebase, gaat naar AdvertentieActivity of naar KeuzeActivity, afhankelijk van soort gebruiker. 
* Button om aan te melden, gaat naar AanmeldActivity. 

AanmeldActivity: 
* Vul email in en twee keer een wachtwoord en gebruik hierbij firebase. 
* Geef aan of je een baasje of uitlater bent dmv radiobutton. Zet in database bij uitlater of baasje de volgende gegevens. 
* Geef naam op van gebruiker en zet bij bijpassend soort gebruiker in database. 
* Button om aan te melden. Ga naar AdvertentieActivity of naar KeuzeActivity, afhankelijk van soort gebruiker. 

Keuzeactivity: 
* Lijst met onclick op de honden om naar HondActivity te gaan. 
* Honden fotos met naam erbij tonen. 

HondActivity: 
* Dropdown. 
* Een foto met beschrijving van de hond. 
* Locatie van de hond mbv api-key. Zet deze locatie in database. 
* Button om afspraak te maken. Ga naar AfspraakActivity. Voeg toe aan lijst met uitgelaten honden in database. Zet advertentie bij baasje op leeg in database. 

AfspraakActivity: 
* Dropdown. 
* Hierop is de naam en email te zien van het baasje van de gekozen hond. 

OverzichtActivity 
* Dropdown. 
* Lijst van honden foto's met namen welke al zijn uitgelaten (haal info uit database). 

AdvertentieActivity: 
* Mogelijkheid om advertentie te plaatsen voor baasje. 
* Foto plaatsen (niet verplicht). 
* Omschrijving invullen van hond en de tijd waarop de hond uitgelaten moet worden. 
* Naam van de hond invullen. 
* Button om advertentie te plaatsen. Ga naar BevestigActivity. 

BevestigActivity: 
* Bevestigend bericht dat advertentie is geplaatst. 
 

Dropdown opties: 
* Uitloggen. 
* Naar de KeuzeActivity. 
* Naar overzicht van gemaakte afspraken. 
