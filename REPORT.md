## Korte uitleg:
Honden baasjes hebben vaak het probleem dat ze geen tijd hebben om de hond uit te laten. 
Gelukkig zijn er ook mensen die geen hond hebben om bijvoorbeeld de reden dat het veel geld kost of
omdat er iemand in huis een allergie heeft.
Met deze app wil ik deze mensen aan elkaar koppelen.
Je kan een account aanmaken en daarbij aangeven of je een hondenbaasje bent of een hond wil uitlaten. Vervolgens kunnen de baasjes 
advertenties plaatsen met een foto van hun hond met een beschrijving. Als je geinteresseerd bent in een advertantie kan je vragen 
om de email van het baasje en elkaar mailen om afspraken te maken.

## Belangrijkste functies:
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
#### MainActivity:
Uitleg:
* Login page. 
* Email en wachtwoord invoeren. 
* Gaat naar AdvertActivity, ChooseActivity of RegisterActivity.

Functies:
* onClick
* logIn
* checkInput
* sentToNext
* checkIfSignedIn
* onStart

#### RegisterActivity: 
Uitleg:
* Vul email in en twee keer een wachtwoord en gebruik hierbij firebase authentication. 
* Geef aan of je een baasje of uitlater bent dmv radiobutton. Zet in database bij uitlater of baasje de volgende gegevens. 
* Geef naam op van gebruiker en zet bijpassend soort gebruiker in database. 
* Ga naar AdvertActivity of naar ChooseActivity, afhankelijk van soort gebruiker.

Functies:
* onRadioButtonClicked
* createUser
* checkInput
* addUserToDB
* goToNext
* goBack

#### ChooseActivity: 
Uitleg:
* Lijst met onclick op de honden om naar DogActivity te gaan. 
* Honden fotos met naam erbij tonen. 

Functies:
* SelectOption
* getFromDB
* OnItemClickListener

#### DogActivity: 
Uitleg:
* Dropdown/Spinner.
* Een foto met beschrijving van de hond. 
* Locatie van de hond mbv api-key. Zet deze locatie in database. 
* Button om afspraak te maken. Ga naar ContactActivity. Voeg toe aan lijst met uitgelaten honden in database. Zet advertentie bij baasje op leeg in database.

Functies:
* onMapReady
* selectOption
* getFromDB
* setFavorites
* getImage
* makeAppointment

#### ContactActivity: 
Uitleg:
* Dropdown/Spinner.
* Hierop is de naam en email te zien van het baasje van de gekozen hond. 

Functies:
* selectOption
* getFromDB

#### OverviewActivity:
Uitleg:
* Dropdown/Spinner.
* Lijst van honden foto's met namen welke al zijn uitgelaten (haal info uit database). 

Functies:
* selectOption
* getFromDB
* clearList
* OnItemClickListener
* deleteFavoritesFromDB

#### AdvertActivity: 
Uitleg:
* Mogelijkheid om advertentie te plaatsen voor baasje. 
* Foto plaatsen (niet verplicht). 
* Omschrijving invullen van hond en de tijd waarop de hond uitgelaten moet worden. 
* Naam van de hond invullen. 
* Button om advertentie te plaatsen. Ga naar BevestigActivity. 

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
