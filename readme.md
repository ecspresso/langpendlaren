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
# Installera electron och electron packager??
# Paktera koden
chmod +x install.sh
./install.sh # <-- fungerar inte: ./install.sh: line 1: electron-packager: command not found
```


<h1> Setup guid for langpendlaren </h1>
<h2>Basic requirment for starting up the project</h2>
<p>Install node js and npm on your machine, before everything else </p>

<h3>Frontend</h3>
<p>From terminal navigate to frontend directory and run commond (npm i)</p>
<h3>Backend</h3>
<p>Open the project in intellij or any other code editor.</p>
