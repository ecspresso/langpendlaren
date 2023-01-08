const ipc = window.require("electron").ipcRenderer;
const ipcRenderer = require("electron").ipcRenderer;
import {millisecondsToHoursAndMinutes, clearHTMLElementByElementId} from "../../util/util.js";
import { getAvailableGenre, getUserProfile, getTokens } from "../spotify/spotify_events.js";
import {savePlayList, getRandomPlayList, getPlayListByGenre} from "../spotify/spotify_functionality.js"
import { getGenreTemplate, displayAllGenre } from "../spotify/spotify_templates.js";
let listOfGenre;
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
  const testAccessToken = "BQAREYMX4nsUj-tx6YfrKq-jY7YZhjgyDxe8zx40v87wTtQO6TtY1rEXTmTJ2h8AFI-ZS8xBlBnKLgjpK0KUDJU4MhP2teh4XlrFo7znmm63F_C3L4IZembVKD6v3AKyjkEvIPDl1VDZKmwFFnlP5ReKWKJBGxVKr_u0hYchar8AsL-m-JGDrcp0Z71l1wEOvYb8e52w3Kbo9W6dwcmGPrsfgARyeX5JW0_MrOYlfTgTP-acpSXx3JdD6ehF1XQ";
  //const testRefreshToken = "BQB-r-B3VIVwuQK4-im22_qOgH5MdL4NtJzMWegRbWl46zlnQd254lnuK3vQNeVWeYJ5DOdAQilVaxPboZLwh85SLmUuwpiWQXzZ85o7I3tufps1fWbl08lWdhJ7ueVrigbjVCHF-CF3DDI9eHX01K0Rlt_oJ1yfDAbdrgTUuWhFeIC7u38VpeS0W7lA5ifb5Rld1r2evUauwZXxOfBciUvk3oO2XpU4hLQ3LnamlO0Yg9Z07vzn2bDWMWB3nwg";


  //const userProfile = getUserProfile(testAccessToken).then(res => res.json());
  const options = [];
  
  const template = getGenreTemplate(travelTime);
  
  
  // Lägg till ny innehåll
  $("#main_content").append(template);
  // Filla upp med alla genre
  await getAvailableGenre(testAccessToken).then(genre => {
    displayAllGenre(genre.seeds);
    listOfGenre = genre.seeds;
  });
  // Hanterar klick event
  handleSpotifyClickEvents();
});

/**
 * Hanterar spotify Clikc events.
 */
 function handleSpotifyClickEvents(){
  // När användaren har klickat på knappen väljas genre och hämtas nya låtar beror på genren.
  document.getElementById("send_genre_button").addEventListener("click", () => {
    var select = document.getElementById("genres");
    var genre = select.options[select.selectedIndex].text;
    getPlayListByGenre(genre);
    
  });

  // När användaren har klickat på knappen läggs nya låtar till i spellistan på användarens konto
  document.getElementById("savePlayList").addEventListener("click", () => savePlayList());
  // När användaren har klickat på knappen gör en ny API-hämtning med nya låtar (ett could krav enligt)
  document.getElementById("generatePlaylist").addEventListener("click", () => getRandomPlayList(listOfGenre));
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
