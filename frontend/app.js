const { app, BrowserWindow, ipcMain } = require('electron');
app.commandLine.appendSwitch('trace-warnings'); // Visa felmeddelanden.

let mainWindow;

function createMainWindow() {
    const mainWindow = new BrowserWindow({
        width: 600,
        height: 800,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false
        },
        show: false
    });

    mainWindow.loadFile('./src/view/main.html')

    mainWindow.maximize();
    mainWindow.show();
}


function createSpotifyWindow() {
    spotifyWindow = new BrowserWindow({
        width: 1000,
        height: 700,
        modal: true,
        show: false,
        parent: mainWindow, // Make sure to add parent window here

        // Make sure to add webPreferences with below configuration
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
            enableRemoteModule: true,
        },
    });

    // Child window loads settings.html file
    spotifyWindow.loadFile('./src/view/cpspotify/login_spotify.html');

    spotifyWindow.once("ready-to-show", () => {
        spotifyWindow.show();
    });
}

ipcMain.on("openChildWindow", (event, arg) => {
    createSpotifyWindow();
});

app.whenReady().then(() => {
    createMainWindow()

    app.on('activate', () => {
        if (BrowserWindow.getAllWindows().length === 0) createMainWindow()
    })
})

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') app.quit()
})

