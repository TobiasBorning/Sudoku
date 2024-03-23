## TDT4100-Prosjekt Tobias Borning V23

__Sudoku-applikasjon__ bygget i Java med rekursiv løsningsalgoritme for avansert feedback.  
__Frontend__ i JavaFX, stylet med ren CSS

## Beskrivelse av appen 

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