# Guide för langpendlaren
# How to run
## Krav/Restriktioner
Spotifys API ser inte att fungera helt med gratiskonton, måste vara ett betalkonto.
Se till att ha Java 17 eller senare installerat och `java` i PATH.

## Backend
1. Ladda ner `backendv2.jar` (https://github.com/ecspresso/langpendlaren/releases/download/2/backendv2.jar).
2. Starta med `java -jar backendv2.jar`.

### För att köra i IDE
Navigera till backend från terminalen och öppna projekteten i en IDE, exekvera sedan filen Main.java
Om IDEn inte hittar/identifierar klasser eller paket, sätt isåfall upp SDK 17.
Gå till fil -> projektstruktur -> projekt -> SDK -> SDK 17
Exekvera sedan filen Main.java och kontrollera att servern körs

### Linux
Servern kör på port 8080. Starta med `java -jar backendv2.jar`.

## Frontend
### Windows
1. Ladda ner `frontend.Setup.1.0.0.exe`.
2. Exekvera `frontend.Setup.1.0.0.exe`. Installationen kommer att installera programmet `Frontend` i katalogen `C:\Users\<username>\AppData\Local\Programs\frontend`. Programmet startar automatiskt efter installationen.

### Linux
1. Ladda ner `frontendv2.AppImage` (https://github.com/ecspresso/langpendlaren/releases/download/2/frontendv2.AppImage).
2. Gör programmet körbart med `chmod +x frontendv2.AppImage`
3. Kör programmet.

### Terminalen
Följande kräver att Node.js och NPM är installerat (se how to build: one-stop-shop).
1. Clona repo.
2. Navigera till langpendlaren/frontend och kör `npm install` och `npm start`.

# How to build
## Krav
Kontrollera att följande är installerade din maskin, annars installera dessa
1. Node.js
2. NPM
3. Maven

## Backend
```bash
cd langpendlaren/backend
maven package
```

## Frontend
```bash
npm install --save-dev electron-builder
npx electron-builder build
```

# One-Stop-Shop-script
Start till slut automatiering för att bygga backend och frontend i Linux.
```bash
# Installera JDK 17, git och maven
sudo apt install openjdk-17-jdk-headless git maven -y
# Hämta all kod
git clone https://github.com/ecspresso/langpendlaren
# Flytta till backen
cd langpendlaren/backend
# Bygg jar filen
mvn package
# Flytta till frontend
cd ../frontend/
# Installera nvm
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
# Lägg till nvm utan att starta om terminalen
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"
# Installera NodeJS
nvm install node
# Installera electron-builder
npm install --save-dev electron-builder
# Bygg projektet
npx electron-builder build
```
