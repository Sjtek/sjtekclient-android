# Sjtek client Android

## Inleiding
Deze app dient als client voor Sjtek Control. Een zelf gemaakt systeem voor enkele dingen in het huis te automatiseren. De belangrijkste dingen hiervan zijn de lichten, een LED strip, het koffiezet apparaat, de TV, een thermometer in de woonkamer en Mopidy. Mopidy is een headless muziek speler die Spotify, Youtube etc. ondersteund. Dit alles draait op een kleine computer in de woonkamer.

## De API
De communicatie met de API loopt vooral via HTTP GET request.
Voor alle modules zijn endpoints beschikbaar om de onderdelen aan te sturen. Deze endpoints zijn te vinden in de module `shared` in de `enum` `.api.Action`. 
Voor sommige endpoints zijn ook URL argumenten beschikbaar. Dit is bijvoorbeeld een gebruikersnaam voor persionalistatie. Deze argumenten worden gemaakt d.m.v. de `.api.Arguments` klasse in de `app` module.

Bij ieder API call wordt de status van alle modules in een grote JSON terug gestuurd. Een voorbeeld van deze JSON is via [deze link](https://sjtek.nl/api/info) te zien.
Vorige versies van de Sjtek app bleven bijgewerkt door iedere seconde te pollen. Hiervoor zijn websockets in de plaats gekomen om realtime updates te krijgen. Als de status van een van de modules wordt veranderd, wordt de status JSON verstuurd over de websocket. De websockets zijn 1 richtingsverkeer.

De rede dat deze 1 richtingsverkeer zijn is dat de websocket samen met de voorbeeld URL open zijn voor buiten het huis. Alle andere API calls vereisen authenticatie in de vorm van Basic Authentication. Hierdoor kan de app ook werken als er niemand is ingelogd. Daadwerkelijke interactie met de API kan dan wel alleen vanuit het huis worden gedaan. Handig voor gasten.

Naast de status JSON is er nog een "data" JSON. Deze bevat statische informatie waaronder de afspeellijsten van de gebruikers, aanspreek methodes (voor text to speech). Deze JSON is alleen met authenticatie te benaderen.

De status JSON en de data JSON worden in de app gebruikt via de `ResponseCollection` en `DataCollection` klasses. Deze klasses zijn afkomstig van een library van de API. De JSON wordt m.b.v. Gson naar de klassen omgezet.

### Interne werking
Communicatie van de API gebeurt m.b.v. Volley. De klasse `.api.API` zorgt voor abstractie tussen de rest van de app en Volley.
Normaal gesproken maakt de Activity die de request maakt ook gelijk callbacks aan voor de response. Omdat er veel onderdelen zijn die op een status verandering van de API moeten reageren heb ik er voor gekozen op de library EventBus te gebruiken. De API klasse vangt alle antwoorden van Volley op en stuurt deze over de EventBus. Hierdoor krijgt iedereen die het nodig heeft een status update, ongeacht wie de aanvraag heeft gestuurd.

Voor de websocket connectie wordt gemaakt in een service. Deze service wordt gestart en gestopt door de Activties die realtime updates nodig hebben. De ontvangen status updates worden ook weer via de EventBus verzonden.

Om de UI snel te tonen bij het opstarten is er een `.storage.StateManager` klasse die de status en data JSON opslaan als statische variabelen. Verschillende onderdelen kunnen hiervan gebruik maken voor het initialiseren. Als de main activity wordt afgesloten worden deze data geserialiseerd opgeslagen op het interne geheugen zodat deze direct beschikbaar zijn bij een volgende opstart van de app.

## De UI

### Het dashboard
Het begin scherm van de app is het dashboard. Dit dashboard bestaat uit verschillende kaarten die de modules van de API representeren. Ieder kaart is een custom view die op zichzelf staat. De view registreert zichzelf voor updates via de EventBus en gebruikt de API klasse om interacties met de API uit te voeren.

### Bottom sheet
Onderin de main activity is een bottom sheet voor het bedienen van de muziek. Deze kan omhoog worden geswiped voor meer informatie. Hier is een afbeelding van het huidige album te zien. De API vraagt de url hiervoor op van Last.FM en cached deze. De app laad deze url in via Picasso. De Last.FM API is redelijk langzaam, daardoor kan het lang duren voordat er een afbeelding verschijnt.

Zodra er op de playlist knop wordt gedrukt wordt een dialog getoond om een afspeellijst te starten. De afspeellijsten worden uit de data JSON gehaald voor de ingelogde gebruiker.

De transitie tussen de bottom sheet en de dialog verloopt nog niet helemaal optimaal. Om het zo ver te krijgen diende ik een doorzichtige activity met een CardView te gebruiken i.p.v. een activity met dialog theme. Daardoor zal de dialog zich niet als een echte dialog gedragen. Daarnaast springt de tekst nog door een onbekende heen en weer tijdens de transitie.

### Navigation drawer
De main activity bevat ook een navigation drawer voor navigatie naar andere onderdelen van de app. De main activity toont naast het dashboard ook verschillende fragments met WebViews. Hierin zijn de web interfaces te vinden van Mopidy, Transmission en Sonarr.

### Notificatie en widget
De app bevat ook een widget voor in de launcher. Deze bevat enkele knoppen voor de lichten, muziek etc. Daarnaast kan de muziek activity worden geopend om snel een afspeellijst te starten.
Deze widget wordt ook als een notificatie getoond als de telefoon verbonden is met ons thuis WiFi netwerk.

### Instellingen
Via de settings is het mogelijk om de widgets en notificaties aan te passen van de app. Daarnaast is er een optie om in te loggen bij de API.

## Accounts
De app kan zonder een account worden gebruikt. Maar om interactie uit te voeren met de API is een account vereist. Inloggen kan via de instellingen van de app of via de account instellingen van Android.
De data JSON API call wordt gebruikt om de account gegevens te testen. Deze account gegevens worden met Base64 versleuteld en als een Basic Authentication header meegestuurd met de API call. Als dit lukt wordt deze token opgeslagen in de SharedPreferences. Daarnaast wordt deze opgeslagen in de AccountManager van Android.

### AccountManager
De bedoeling was op de authenticatie van de app af te handelen via de AccountManger van Android. Na een gedeeltelijke implementatie ervan bleek dat dit te uitgebreid is voor de authenticatie van deze app. Daarnaast leek de beveiliging van de opslag van de Basic Authenticatie token niet veiliger dan de SharedPreferences. Daarom wordt er verder geen gebruikt van gemaakt.
Als de API later wordt geupdgrade naar OAuth zal dit wel weer van pas komen.

## Android Wear
Als laatste heb ik voor deze opdracht een Android Wear app gemaakt. Deze app toont een simpele interface met de belangrijkste bedieningsknoppen voor de app.
Op het start scherm zijn knoppen voor de lichten en de muziek.
Door verder te scrollen zijn er meer knoppen te vinden voor de muziek en het huidige nummer. Door vervolgens van rechts naar links te swipen kan er een afspeellijst worden gestart.
Door verder te scrollen na de muziek is de binnen en buiten temperatuur te vinden.

### Communicatie Wear app
De communicatie tussen de Wear app en de API bleek lastiger dan gedacht. Het is namelijk niet mogelijk om in Android Wear direct HTTP communicatie uit te voeren. Dit kan pas in de nog niet officieel uitgebrachte versie 2.0 van Android Wear.
Daarom moet de Wear app alle communicatie via de telefoon app uitvoeren. Hiervoor zijn de Google Play Services vereist wat de nodige complexiteit met zich meebrengt.

Er zijn twee opties voor de communicatie. Een ervan is d.m.v. berichten tussen de Wear app en de telefoon app. Dit lukte me niet om op te zetten. Daarom gebruik ik de Data Layer API. Hiermee is het mogelijk om een data 'map' de koppelen aan een URI.

Acties voor de API worden in `/action` opgeslagen. Als er een knop op de Wear app wordt ingedrukt wordt de bijbehorende API actie opgeslagen op dit pad. Een service op de telefoon app kijkt naar wijzigingen voor dit pad en voert de actie uit zodra deze veranderd.
Andersom is er ook communicatie mogelijk. De temperatuur wordt opgeslagen op het `/weather` pad zodra de `.storage.StateManager` de data opslaat op het interne geheugen. Dit doe ik niet vaak omdat de communicatie met het horloge veel batterij zou vereisen. Aangezien er zeer frequent data wordt bijgewerkt in de app tijdens het gebruik zou dit veel batterij kosten.
Omdat ik hiermee basis communicatie aantoon en zelf geen horloge heb, ben ik hier niet mee verder gegaan.

## Unit testen
Tenslotte heb ik ook nog enkele unit testen geschreven voor de app. Omdat ik vooral problemen met de `.storage.Preferences` klasse in het verleden heb gehad besloot ik deze te testen. De klasse maakt gebruik van de SharedPreferences klasse. Omdat deze niet op een pc beschikbaar is heb ik Robolectric gebruikt om deze testen toch op een pc en Jenkins te draaien. De resultaten zijn op de download pagina te zien die beneden is vermeld.

## Downloads en screenshots
Diverse screenshots zijn [hier](../screenshots) te vinden.
Downloads voor de telefoon en Android Wear app zijn [hier](https://jenkins.habets.io/job/Sjtek/job/sjtekclient-android/job/master/) te vinden.