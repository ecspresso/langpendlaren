const ipc = window.require("electron").ipcRenderer;
const ipcRenderer = require("electron").ipcRenderer;
import {millisecondsToHoursAndMinutes, clearHTMLElementByElementId} from "../../util/util.js";
import { getAvailableGenre, getUserProfile, getTokens } from "../spotify/spotify_events.js";
import {savePlayList, newPlaylist} from "../spotify/spotify_functionality.js"
import { getGenreTemplate } from "../spotify/spotify_templates.js";

/**
 * Körs när appen renderat färdigt.
 */
 $( document ).ready(() => {
  handleTraficClickEvents();
});

/**
 * Körs när spotify sida renderat färdigt.
 */
ipcRenderer.on("spotifyReady", async function (event, code) {
  // Rensa upp innehåll by id
  clearHTMLElementByElementId("main_content");

  const tokens = [];
  await getTokens(code).then(token => tokens.push(token.accessToken.value));
  const travelTime = millisecondsToHoursAndMinutes(localStorage.getItem("spotifyTime"));
  const template = getGenreTemplate(travelTime);

  const userProfile = getUserProfile(tokens[0]);
  
  //const genre = getAvailableGenre();
  // Lägg till ny innehåll
  $("#main_content").append(template);
  handleSpotifyClickEvents();
});

/**
 * Hanterar spotify Clikc events.
 */
 function handleSpotifyClickEvents(){
  // När användaren har klickat på knappen läggs nya låtar till i spellistan på användarens konto
  document.getElementById("savePlayList").addEventListener("click", savePlayList());
  // När användaren har klickat på knappen gör en ny API-hämtning med nya låtar (ett could krav enligt)
  document.getElementById("generatePlaylist").addEventListener("click", newPlaylist());
}

/**
* Hanterar spotify Clikc events.
*/
function handleTraficClickEvents(){
  // Rensa upp sökfältet.
  document.getElementById("clearBtn").addEventListener("click", () => {
    document.getElementById("station").value = "";
  }); 
  //Återställer tillbaka till början genom att klicka på basic_button reset
  document.getElementById("reset_button").addEventListener("click", () => {
    window.location.reload();
    ipc.send("reset");
  });
}


// Exports
export { ipc };
