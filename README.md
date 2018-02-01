## Walk My dog
Honden baasjes hebben vaak het probleem dat ze geen tijd hebben om de hond uit te laten. Gelukkig zijn er ook mensen die geen hond hebben om bijvoorbeeld de reden dat het veel geld kost of omdat er iemand in huis een allergie heeft. Met deze app wil ik deze mensen aan elkaar koppelen. Zo kan iemand zonder hond toch even een loopje maken en hoeft het baasje zijn schema niet aan te passen om de hond uit te laten.

Je kan een account aanmaken en daarbij aangeven of je een hondenbaasje bent of een hond wil uitlaten. Vervolgens kunnen de baasjes advertenties plaatsen met een foto van hun hond met een beschrijving. Als uitlater zie je een overzicht van alle advertenties. Door op een advertentie te klikken zie je meer informatie. Als je geinteresseerd bent in een advertantie kan je vragen om de email van het baasje en elkaar mailen om afspraken te maken. Als uitlater wordt er ook een lijst met honden begehouden die je al uitgelaten heb. Zo kan je makkelijk de vorrige afspraken terug vinden.

Deze app is alleen beschikbaar in portrait modus. De landscape modus is niet meegenomen omdat dit geen toegevoegde waarde heeft bij deze app.

### Screenshots:
<img src="doc/screenshot_login.jpeg" alt="screenshot MainActivity" width="200" height="300"/> <img src="doc/screenshot_register.jpeg" alt="screenshot RegisterActivity" width="200" height="300"/> <img src="doc/screenshot_advert.jpeg" alt="screenshot AdvertActivity" width="200" height="300"/> <img src="doc/screenshot_confirm.jpeg" alt="screenshot ConfirmActivity" width="200" height="300"/> <img src="doc/screenshot_choose.jpeg" alt="screenshot ChooseActivity" width="200" height="300"/> <img src="doc/screenshot_dog.jpeg" alt="screenshot DogActivity" width="200" height="300"/> <img src="doc/screenshot_contact_spinner.jpeg" alt="screenshot ContactActivity" width="200" height="300"/> <img src="doc/screenshot_overview.jpeg" alt="screenshot OverviewActivity" width="200" height="300"/>

### Een aantal interessante punten:
In deze app wordt gps gebruikt om de locatie van een baasje te verkrijgen.
Vervolgens wordt de API voor google maps gebruikt om de locatie te laten zien aan de uitlater.
De app maakt ook gebruik van de camera van de gebruiker (baasje).
Verder wordt er gebruik gemaakt van firebase als database om alle gegevens van de gebruikers op te slaan.

### Extern vergregen code:
* Gebruik van base 64:
  https://stackoverflow.com/questions/4830711/how-to-convert-a-image-into-base64-string
  https://gist.github.com/WrathChaos/80cd7e613cd7577dae9326e3cd75e4be
* Neerzetten van de map met google api:
  https://github.com/googlemaps/android-samples/blob/master/AndroidWearMap/Wearable/src/main/java/com/example/androidwearmap/MainActivity.java
* Gebruiken van de camera:
  https://developer.android.com/training/camera/photobasics.html
* Permissie vragen voor locatie/camera gebruik:
  https://developer.android.com/training/permissions/requesting.html
