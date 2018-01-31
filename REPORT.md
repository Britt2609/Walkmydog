## Korte uitleg:
Honden baasjes hebben vaak het probleem dat ze geen tijd hebben om de hond uit te laten. 
Gelukkig zijn er ook mensen die geen hond hebben om bijvoorbeeld de reden dat het veel geld kost of
omdat er iemand in huis een allergie heeft.
Met deze app wil ik deze mensen aan elkaar koppelen.
Je kan een account aanmaken en daarbij aangeven of je een hondenbaasje bent of een hond wil uitlaten. Vervolgens kunnen de baasjes 
advertenties plaatsen met een foto van hun hond met een beschrijving. Als je geinteresseerd bent in een advertantie kan je vragen 
om de email van het baasje en elkaar mailen om afspraken te maken.

## Belangrijkste functies van de app:
Deze functies waren van tevoren het belangrijkste om te maken:
* Account aanmaken/inloggen als baasje of uitlater.
* Als baasje een advertentie kunnen plaatsen.
* Als uitlater advertenties zien van anderen inclusief locatie.
* Alleen advertenties in de buurt zien.
* Contactgegevens zien van gekozen advertentie.

Hiervan heb ik alleen het zien van advertenties die in de buurt zijn niet gedaan.
Wel heb ik nog toegevoegd dat uitlaters een lijst met favorieten (uitgelaten honden) krijgen.
Ook heb ik toegevoegd dat de gebruiker een foto kan maken in de app.

## Activities met functies:
In de meeste activities (alleen MainActivity en RegisterActivity niet) is ook bovenin een spinner geplaatst, zodat de gebruiker makkelijk kan navigeren tussen verschillende activities.gg

#### MainActivity:
De MainActivity is de login pagina. 
Hierin kan je email en wachtwoord invullen en wordt de validatie gecheckt met firebase.
Vanaf hier kan je naar RegisterActivity door de register knop.
Met de log in knop kan je naar AdvertActivity of ChooseActivity gestuurd worden, afhankelijk van het soort gebruiker.
Hierbij gebruik ik de variabele "user type" uit de class User in de database.

#### RegisterActivity: 
RegisterActivity is de registreer pagina.
Hierin vul je een email in en twee keer een wachtwoord.
De opgegeven gegevens worden in firebase authentication gezet.
Bij registratie geef je ook aan of je een baasje of uitlater bent en geef je een naam op.
Geef naam op van gebruiker en zet gegevens in class User.
Zet deze class in de databse onder het kopje "users" met als naam de user id.
Met de aanmeld knop kan je naar AdvertActivity of ChooseActivity gestuurd worden, afhankelijk van het soort gebruiker.

#### ChooseActivity: 
In deze activity zie je een lijst alle uitstaande advertenties van honden (foto met naam).
Deze data haal ik uit de database uit het kopje "dogs".
Op deze honden kan je klikken om naar DogActivity te gaan om meer informatie te zien over een hond.
Hierbij wordt het id van het baasje van de hond meegegeven.

#### DogActivity:
Hierin is een foto met beschrijving van de hond en de naam van de hond te zien in een scrollview.
Ook de locatie van het baasje van de hond wordt weergeven in een map (indien beschikbaar).
De locatie haal ik als een latitude en longitude uit de database.
Hierbij gebruik ik de api-key van google om de map te maken.
Als de gebruiker een afspraak wil maken, gaat de gebruiker via een knop naar ContactActivity.
Bij het make van de afspraak wordt ook de hond toegevoegd aan lijst met uitgelaten honden in database.

#### ContactActivity: 
Deze activity geeft simpelweg de gegevens van het baasje van de hond, zodat een afspraak gemaakt kan worden.
Hierop is de naam en email te zien van het baasje van de gekozen hond.
Deze informatie is uit de database gehaald door onder het kopje "users" bij het id van het baasje de class User op te vragen die deze informatie bevat.

#### OverviewActivity:
De OverviewActivity bestaat uit een lijst van honden foto's met namen welke al zijn uitgelaten.
Deze komen uit de favorites list van de user onder het kopje "users" in de database.

#### AdvertActivity: 
In deze activity is er de mogelijkheid om advertentie te plaatsen voor baasje.
Bij het openen van deze activity wordt (als dit nog niet eerder gedaan is of deze toestemming al gegeven is) gevraagd om toestemming voor locatie en het aanzetten van de gps. Deze wordt dan ook meteen opgevraagd.
Er is ook de mogelijkheid om een foto te maken. Ook hiervoor wordt toestemming gevraagd (als deze niet al gegeven is).
Bij de keuze om geen foto te maken wordt het logo van de app gebruikt. Bij de keuze om wel een foto te maken zal deze foto alleen in firebase opgeslagen worden door middel van base 64 (het omzetten van de foto naar een string).
Vervolgens wordt er gevraagd de naam en een omschrijving in te vullen van de hond, waarin ook enkele details aangegeven kunnen worden, zoals de uitlaattijden. 
Deze data wordt opgeslagen in een class Dog, die vervolgens geplaatst wordt in de database onder het kopje "dogs" bij het id van het baasje.
Bij het maken van de advertentiea naar BevestigActivity. 

Functies:
* setSpinner
* getLocationPermission
* SelectOption
* getDogFromDB
* makePicture
* onRequestPermissionsResult
* checkLocation
* checkCamera
* dispatchTakePictureIntent
* onActivityResult
* encodeBitmap
* getLocation
* showSettingAlert
* makeAdvert

#### ConfirmActivity: 
Uitleg:
* Dropdown/Spinner.
* Bevestigend bericht dat advertentie is geplaatst. 

Functie:
* selectOption
