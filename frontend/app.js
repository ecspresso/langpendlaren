const { app, BrowserWindow, ipcMain } = require("electron");
const fetch = require("electron-fetch").default;
let mainWindow;
let authWindow;




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
    //mainWindow.maximize();
    mainWindow.show();
  });
}

// Hantera inloggning mot Spotify.
// Skickar inloggningsfråga till vår server som skickar oss vidare till Spotify, och sedan tillbaka igen.
ipcMain.on("spotifyLogin", () => {
  fetch("http://localhost/spotify/login")
  .then((res) => res.json())
  .then((authUrl) => {
    //https://stackoverflow.com/questions/37546656/handling-oauth2-redirect-from-electron-or-other-desktop-platforms
    authWindow = new BrowserWindow({
      width: 800,
      height: 600,
      show: false,
      'node-integration': false,
      'web-security': false
    });

    authWindow.webContents.on('will-navigate', function (event, newUrl) {
      authWindow.loadURL(newUrl).then(() => {
        // Kontrollera att den URL vi ska till inte är Spotifys domän.
        if(!new URL(newUrl).host.match("spotify\.com$")) {
          let code = new URL(newUrl).searchParams.get("code");
          mainWindow.webContents.send("spotifyReady", code);
          authWindow.close();
        }
      });
    });

    authWindow.on('closed', function() { authWindow = null; });

    authWindow.loadURL(authUrl);
    authWindow.show();
  });
});

ipcMain.on("traficStops", (event) => {
  mainWindow.loadFile("./src/view/cptrafic/stops.html");
});

ipcMain.on("reset", () => {
    if(authWindow != null) {
        authWindow.close()
    }
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
