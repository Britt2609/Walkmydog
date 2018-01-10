## Database
Voor het aanmaken van een account ga ik firebase gebruiken met inlognaam en wachtwoord. 

Database authentication:

Gebruikers
  * email
  * tijd van registratie
  * wachtwoord
  * gebruikers id

Ik maak een database met 2 delen:
* Een deel voor de baasjes met een foto, omschrijving, naam van het baasje, email van het baasje, naam van de hond, locatie van de hond en of er een advertentie uitstaat. 
* Een deel voor de uitlaters met een lijst van gemaakte afspraken en de naam van de uitlater. (evt later nog de ranking van de uitlater)


Database
  * baasjes
    * naam
    * email
    * locatie
    * hond
      * omschrijving
      * foto
      * hondennaam
    * advertentiestatus
  * uitlaters
    * naam uitlater
    * lijst van afspraken
    


## Activities
#### MainActivity:
Uitleg:
* Login page. 
* Email en wachtwoord invoeren. 
* Button om in te loggen en gegevens checken met firebase, gaat naar AdvertActivity of naar ChooseActivity, afhankelijk van soort gebruiker. 
* Button om aan te melden, gaat naar RegisterActivity.

Functies:
* login()
* goToRegister() (onClick)

#### RegisterActivity: 
Uitleg:
* Vul email in en twee keer een wachtwoord en gebruik hierbij firebase. 
* Geef aan of je een baasje of uitlater bent dmv radiobutton. Zet in database bij uitlater of baasje de volgende gegevens. 
* Geef naam op van gebruiker en zet bij bijpassend soort gebruiker in database. 
* Button om aan te melden. Ga naar AdvertActivity of naar ChooseActivity, afhankelijk van soort gebruiker.

Functies:
* addToDatabase()
* register() (onClick, intent)

#### ChooseActivity: 
Uitleg:
* Lijst met onclick op de honden om naar DogActivity te gaan. 
* Honden fotos met naam erbij tonen. 

Functies:
* logOut() (onClick met intent)
* goToOverview() (onClick met intent)
* goToDog() (onClick met intent)

#### DogInfoActivity: 
Uitleg:
* Dropdown/Spinner.
* Een foto met beschrijving van de hond. 
* Locatie van de hond mbv api-key. Zet deze locatie in database. 
* Button om afspraak te maken. Ga naar ContactActivity. Voeg toe aan lijst met uitgelaten honden in database. Zet advertentie bij baasje op leeg in database.

Functies:
* makeContact() (onClick, intent)
* logOut() (onClick, intent)
* goToOverview() (onClick, intent)
* goToAdverts() (onClick, intent)

#### ContactActivity: 
Uitleg:
* Dropdown/Spinner.
* Hierop is de naam en email te zien van het baasje van de gekozen hond. 

Functie:
* logOut() (onClick, intent)
* goToOverview() (onClick, intent)
* goToAdverts() (onClick, intent)

#### OverviewActivity:
Uitleg:
* Dropdown/Spinner.
* Lijst van honden foto's met namen welke al zijn uitgelaten (haal info uit database). 

Functie:
* logOut() (onClick, intent)
* goToAdverts() (onClick, intent)

#### AdvertActivity: 
Uitleg:
* Mogelijkheid om advertentie te plaatsen voor baasje. 
* Foto plaatsen (niet verplicht). 
* Omschrijving invullen van hond en de tijd waarop de hond uitgelaten moet worden. 
* Naam van de hond invullen. 
* Button om advertentie te plaatsen. Ga naar BevestigActivity. 

Functie:
* addAdvertToDatabase()
* goToConfirm() (onClick, intent)

#### ConfirmActivity: 
Uitleg:
* Dropdown/Spinner.
* Bevestigend bericht dat advertentie is geplaatst. 

Functie:
* goBackToAdvert (onClick, intent)
* logOut() (onClick, intent)
