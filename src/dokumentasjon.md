# Dokumentasjon - Sudoku 

## Del 1: Beskrivelse av appen

Appen er et sudoku program med innebygd sudoku-løser.
Appen vil alltid starte med et tilfeldig valgt sudoku fra en samling med 50 sudokuer.  
Brukeren av appen kan skrive inn tall, i rutene som ikke er fastsatt av sudokuen.  
Når du spiller, kan du velge mellom to **tilbakemeldingstyper:**
- *Standard*: Du vil ikke få lov til å plassere et tall i en rute dersom tallet allerede er i samme rad, kollone, eller 3x3 rute.
- *Avansert*: En algoritme løser sudoken for deg og kan gi deg tilbakemelding på feil som ikke er åpenbare.

Ønsker du å lagre sudokuen og spille videre en annen gang, kan du lagre sudokuen med et ønsket navn.  
Du kan laste inn denne og andre lagrede sudokuer igjen, da vil fortsatt de opprinnelig låste rutene, forbli låste, tallene du selv har skrevet inn vil fortsatt være mulige og endre på.  
De lagrede sudokuen finner du ved å trykke på browse saves, her finnes også tomt brett om du ønsker det. Brukeren kan også laste inn et tilfeldig brett (slik som når appen startes) ved å trykke på load random. 
Ønsker du å vite løsningen på en sudoku, kan du trykke på "show solution", men da må du først ha benyttet deg av "solve" knappen.  

## Del 2: Diagram
Under er et sekvensdiagram der en bruker åpner appen og benytter "solve" knappen 

![](/assets/Sudoku%20sekvensdiagram.jpg)

Merk her at kallene med firkantet boks rundt er forenklinger av et stort sett med kall.

## Del 3 Spørsmål

Teknikker som brukes i prosjektet:
- ***Observatør - Observert: & Grensesnitt*** Brukes mellom **SudokuGrid** klassen og klasser som implementerer **SudokuObserver** grensesnittet.  
Klassene **MistakeChecker** og **RecursiveSolver** observerer **SudokuGrid** for å selv lage en kopi av brettet som brukes til å sjekke for feil og lovlige verdier.  
Hver gang et **SudokuGrid** opprettes, vil gridInitialized() kalles hos alle klasser som implementerer **SudokuObserver**.  
Endringer i **SudokuGrid** vil kalle gridChanged() hos observatørene.
**SudokuObserver** grensesnittet vil også kunne brukes dersom man ønsker å lage klasser som utvider funksjonaliteten i programmet. Dette kan f.eks være en move-tracker som samler alle endringer som er blitt gjort på brettet. 

- ***Arv:*** **MistakeChecker** klassen har mange metoder som er nyttige å bruke når jeg skulle lage en algorimte for å løse sudokuene. Alle forsøkene mine på disse algoritmene, som til slutt ble **RecusiveSolver** klassen, arvet fra **MistakeChecker**. CheckMistake() metoden i **MistakeChecker** er essensiell for å kunne lage en algoritme som løser sudokuer.

- ***Streams og Funksjonelle grensesnitt*** : Brukes for eksempel i completedGrid() metoden i **MistakeChecker**, her benyttes naturligvis også et **Predicate** for å filtrere ønskede ellementer. Jeg har også laget en egen **Comparator** for å sortere lister i en liste fra minste til største listestørrelse.

- ***Unntakshåndering og validering:*** Brukes for å passe på at bruker ikke skriver inn ulovlig input i sudokuen, samt passe på at f.eks en ikke legger til samme observatør to ganger i et **SudokuGrid**.  
 **Controllerens** setCell() metode er et eksempel på en metode som håndterer untakk. Her sjekkes det først at bruker har skrevet inn et tall, og ikke en streng, spesialtegn eller liknende, ved å bruke en *try - catch*. Dette fungerer fordi Integer.parseInt() funksjonen vil kaste unntaket *NumberFormatException* dersom den brukes på en streng som ikke er et heltall. **Controlleren** bruker så enda en *try-catch* for å forsikre seg om at brukerens input er et tall mellom 1 og 9.  
 **FileManager** sin readBoardFromFile() metode kaster en *FileNotFoundException* dersom den ikke finner noe fil. Dette er en *checked exception*.

- **Delegering:** Når **SudokuGrid(String filename)** konstruktøren kalles, delegeres hentingen av Listene som inneholder informasjonen om brettet og det opprinnelige brettet fra fil til **FileManager** objektet. 

- **instanceOf og casting** benyttes i oppretting av fxml elementer. I tillegg brukes det til å konvertere fra Integer til int.
- **ArrayList og HashMap** benyttes i stor grad til oppsett og løsingsalgoritmen.
- **Rekursjon** bentyttes for å lage **RecusiveSolver** klassen å løse en sudoku uten rekusjon vil nok være veldig vanskelig.


Teknikker som ikke benyttes:
- **Abstrakte klasser** dette var det ikke særlig behov for, interface gjorde jobben.
- **static** modifikatorer benyttet jeg heller ikke
- en del **funksjonelle grensesnitt** benyttes heller ikke, typ BinaryOperator og Function.

Model-View-Controller prinsippet:
- I prosjektet har jeg prøvd å holde så mye logikk som mulig unna kontrolleren. Men kontrolleren inneholder fortsatt litt logikk, på steder der den tar inn informasjon fra brukeren. SetCell() -metoden i kontrolleren burde optimalt sett implementert litt mindre logikk.
- Programmet er forsøkt laget så responsivt som mulig, med feilmeldinger, og oppdateringer til brukeren. På innlastingssiden, burde lagrede sudokuer fjernes umiddelbart fra lista når man trykker slett, dette fikk jeg ikke til, så jeg gir heller skriftlig tilbakemelding til bruker om at filen er slettet.
Den vil såklart være borte neste gang bruker åpner innlastingssiden.

Testing:
- Testene som er laget tester at de mest essensielle funksjonene fungerer som de skal. Testene tester ikke kontrolleren, kun logikken som sudoku programmet er bygget på. Her testes **SudokuGrid** klassen grundigst, ettersom det er den mest sentale klassen i programmet. **FileManager**, **MistakeChecker** og **RecursiveSolver** testes også. Testene er designet slik at dersom noe er grundig feil med programmet, vil det ende i en rød test, enten det er ruteplassering, lagring, lesing, løsing eller oppretting av sudokuer.  



