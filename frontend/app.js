const { app, BrowserWindow, ipcMain } = require("electron");
const fetch = require("electron-fetch").default;
let mainWindow;

app.commandLine.appendSwitch("trace-warnings"); // Visa felmeddelanden.
function createMainWindow() {
  mainWindow = new BrowserWindow({
    width: 1000,
    height: 1000,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false,
    },
    show: false,
  });

  mainWindow.loadFile("./src/view/main.html").then(() => {
    mainWindow.show();
  });
  //mainWindow.maximize();
}

function afterSpotifyLogin(newUrl) {
  fetch(newUrl)
    .then(() => {
      alert("ANROPA http://localhost/spotify/authenticated?code=...");
      mainWindow.loadFile("./src/view/cpspotify/playlist.html");
    })
    .catch(mainWindow.loadFile("./src/view/error.html"));
}

// Hantera inloggning mot Spotify.
// Skickar inloggningsfråga till vår server som skickar oss vidare till Spotify, och sedan tillbaka igen.
ipcMain.on("spotifyLogin", (event, arg) => {
  fetch("http://localhost/spotify/login")
    .then((res) => res.json())
    .then((json) => {
      mainWindow.loadURL(json);
    });
});

ipcMain.on("traficStops", (event) => {
  mainWindow.loadFile("./src/view/cptrafic/stops.html");
});

app.whenReady().then(() => {
  createMainWindow();

  app.on("activate", () => {
    if (BrowserWindow.getAllWindows().length === 0) createMainWindow();
  });
});

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") app.quit();
});
