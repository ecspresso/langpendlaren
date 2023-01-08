const ipc = window.require("electron").ipcRenderer;
const ipcRenderer = require("electron").ipcRenderer;
import {millisecondsToHoursAndMinutes, clearHTMLElementByElementId} from "../../util/util.js";
import { getAvailableGenre, getUserProfile, getTokens, createPlayListByName } from "../spotify/spotify_events.js";
import {savePlayList, getRandomPlayList, displayPlayListByGenre} from "../spotify/spotify_functionality.js"
import { getGenreTemplate, displayAllGenre } from "../spotify/spotify_templates.js";
const testAccessToken = "BQBY-9zrYlgSC3WJzuRjDrxj72-av7iybZXB1XkPMP-yfNBA1yjIzsqVqcguKnD0ifBcJW4JAyNyBMDucbWP2NtfP6nsJXEigRe34xBzc154JVwCYIad423Fit7DHAL7lAPXI5KyS-4q9tiKujG7h8idBvymvW_LPYBqES6kPb1XUz6Y-sERTJGiXZUrH-g0nFhIetaMiuJgpZ19qwZJg8iIHlLQ3K94X-6pY9dxB-zxTM0yDzeryoJ5EyDUd88";
//const testRefreshToken = "BQB-r-B3VIVwuQK4-im22_qOgH5MdL4NtJzMWegRbWl46zlnQd254lnuK3vQNeVWeYJ5DOdAQilVaxPboZLwh85SLmUuwpiWQXzZ85o7I3tufps1fWbl08lWdhJ7ueVrigbjVCHF-CF3DDI9eHX01K0Rlt_oJ1yfDAbdrgTUuWhFeIC7u38VpeS0W7lA5ifb5Rld1r2evUauwZXxOfBciUvk3oO2XpU4hLQ3LnamlO0Yg9Z07vzn2bDWMWB3nwg";


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

  await getTokens(code).then(token => {
      localStorage.setItem("access_token", token.access_token.value);
      localStorage.setItem("access_token_expire", token.access_token.expires_in);
      localStorage.setItem("refresh_token", token.refresh_token.value);
  });

  const travelTime = millisecondsToHoursAndMinutes(localStorage.getItem("spotifyTime"));

  const template = getGenreTemplate(travelTime);
  
  
  // Lägg till ny innehåll
  $("#main_content").append(template);

  const accessToken = localStorage.getItem("access_token");
  
  // Filla upp med alla genre
  await getAvailableGenre(testAccessToken).then(genre => {
    displayAllGenre(genre.seeds);
  });

  // Hanterar klick event
  handleSpotifyClickEvents();
});

/**
 * Hanterar spotify Clikc events.
 */
function handleSpotifyClickEvents(){
  // Create play list
  document.getElementById("createPlayList").addEventListener("click", () => {
    const name = document.getElementById("pname");
    const desc = document.getElementById("pdescription");
    console.log(name, )
    if(name !== "" || desc !== ""){
      console.log("start creating..")
      const data = createPlayListByName(testAccessToken, name.value, desc.value);
      console.log(data)
    }
  });

  // När användaren har klickat på knappen väljas genre och hämtas nya låtar beror på genren.
  document.getElementById("send_genre_button").addEventListener("click", () => {
    var select = document.getElementById("genres");
    var genre = select.options[select.selectedIndex].text;
    displayPlayListByGenre(genre, localStorage.getItem("access_token"));
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
