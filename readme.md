<h1> Guide för langpendlaren </h1>
<h3>Krav för att starta upp applikationen</h3>
<p>Kontrollera att Node.js och NPM är installerat på din maskin, annars installera dessa</p>

<h3>Backend</h3>
<p>Navigera till backend från terminalen och öppna projekteten i en IDE, exekvera sedan filen Main.java</p>
<p>Om IDEn inte hittar/identifierar klasser eller paket, sätt isåfall upp SDK 17.</p>
<p>Gå till fil -> projektstruktur -> projekt -> SDK -> SDK 17</p>
<p>Exekvera sedan filen Main.java och kontrollera att servern körs</p>

<h3>Frontend</h3>
<p>From terminal navigate to frontend and run commond: <h4> - npm install</h4> <h4> - npm start</h4></p>
<p>Navigera till frontend från terminalen och kör följande kommando: <h4> - npm install </h4> <h4> - npm start </h4></p>

<h1>Om spotify inte fungerar</h1>
<p>Om spotify kan inte hitta spelistan beror på genre, forsök köra med refresh token. </p>
<p> 1. Starta server från intellij på localhost genom att Main.java körs </p>
<p> Öppna länken för att få en refresh token:</p>
<h4>http://localhost/spotify/login/refresh?refresh_token=AQCYnCqni9-EB5f6QvOmtmlOZx2_tyhMaehDUae0H-lpJxEZQ1H84jOxgFl_75BO4X7IUGATizacjZoZ2lxxL2hn88eaZQMzQ6wafR5SjwEqEAcAGYXaXGxoLKDLsIWRCGY<h4>
<p>Läggs in i rad 28 under functionality.js </p>
