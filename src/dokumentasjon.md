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
Du kan laste et tomt brett ved å skrive "empty" i "load" feltet, da kan du skrive inn en hvilken som helst sudoku selv. Så lenge den er løsbar, vil "solve" knappen løse den.  
Ønsker du å vite løsningen på en sudoku, kan du trykke på "show solution", men da må du først ha benyttet deg av "solve" knappen.  

## Del 2: Diagram
Under er et sekvensdiagram der en bruker åpner appen og benytter "solve" knappen 

![](/Sudoku%20sekvensdiagram.jpg)

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
 **Controllerens** setCell() metode er et eksempel på en metode som håndterer untakk. Her sjekkes det først at bruker har skrevet inn et tall, og ikke en streng, spesialtegn eller liknende, ved å bruke en *try - catch*. Dette fungerer fordi Integer.parseInt() funksjonen vil kaste unntaket *NumberFormatException* dersom den brukes på en streng som ikke er et heltall.  
 **Controlleren** bruker så enda en *try-catch* for å forsikre seg om at brukerens input er et tall mellom 1 og 9.  
 **FileManager** sin readBoardFromFile() metode kaster en *FileNotFoundException* dersom den ikke finner noe fil. Dette er en *checked exception*.





Teknikker som ikke brukes i prosjektet:
- **Delegering:** Delegering brukes ikke i dette prosjektet. (Kanskje FileMangager teller som delegering)




