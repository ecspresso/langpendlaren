const ipc = window.require("electron").ipcRenderer;
const ipcRenderer = require("electron").ipcRenderer;
import {millisecondsToHoursAndMinutes, clearHTMLElementByElementId} from "../../util/util.js";
import { getAvailableGenre, getUserProfile, getTokens, createPlayListByName } from "../spotify/spotify_events.js";
import {savePlayList, getRandomPlayList, displayTracks} from "../spotify/spotify_functionality.js"
import { getGenreTemplate, displayAllGenre } from "../spotify/spotify_templates.js";
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
  localStorage.removeItem("timeCounter");
  localStorage.removeItem("offset");
  // Rensa upp innehåll by id
  clearHTMLElementByElementId("main_content");

  await getTokens(code).then(token => {
      localStorage.setItem("access_token", token.access_token.value);
      //localStorage.setItem("access_token", "BQCKxHlYyDMimP8ViZ4f8K9l4d1b7pBPR_aBv_rHljjc0Lk23Wmy4Hoq5OYmR02_RbPm7g3HhnsREKWys_s-oElNdIiIHUTbBjCVnIzXEpZhJ73EP5zNCRSTHtbH9IN6ZmvQmgvjAoEfjO8IPQSgXuqgOb0AriFQJHXFytcqqWEKvzqY-How3uD6sZOm1nYbQ2HzoaU_KquHsJdtVQOScoNTRWhKZ2O9_iBaUVneu_1haTxuYeyVDKRxUiGc42E");
      localStorage.setItem("access_token_expire", token.access_token.expires_in);
      localStorage.setItem("refresh_token", token.refresh_token.value);
  });

  const travelTime = millisecondsToHoursAndMinutes(localStorage.getItem("spotifyTime"));

  const template = getGenreTemplate(travelTime);
  
  
  // Lägg till ny innehåll
  $("#main_content").append(template);
  
  // Filla upp med alla genre
  await getAvailableGenre(localStorage.getItem("access_token")).then(genre => {
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
      createPlayListByName(localStorage.getItem("access_token"), name.value, desc.value).then(result => {
        localStorage.setItem("p_id", result.id);
        document.getElementById("playListCreatorWraper").innerHTML = `<div>
          <h4 style="color: green;">Successful created!</h4>
          <p>PlayListName: ${result.name} </p>
          <p>PlayListName: ${desc.value} </p>
        </div>`
      });
    }
  });

  // När användaren har klickat på knappen väljas genre och hämtas nya låtar beror på genren.
  document.getElementById("send_genre_button").addEventListener("click", () => {
    var select = document.getElementById("genres");
    var genre = select.options[select.selectedIndex].text;
    displayTracks(genre, localStorage.getItem("spotifyTime"), localStorage.getItem("access_token"), localStorage.getItem("p_id"));
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


function getTravelTimeInFormat(){
  return millisecondsToHoursAndMinutes(localStorage.getItem("spotifyTime"));
}

// Exports
export { ipc };
