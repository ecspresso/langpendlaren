const ipc = window.require("electron").ipcRenderer;
const ipcRenderer = require("electron").ipcRenderer;

export { ipc };

// Basic functions, förbereder electron och olika knapparna
const setUpView = () => {
  const clearBtn = document.getElementById("clearBtn");
  const searchInput = document.getElementById("station");
  const timeTable = document.getElementById("timeTableDeparture");

  searchInput.onmouseout = () => {
    const value = searchInput.value;
    if (value === "") {
      clearBtn.setAttribute("disabled", "");
    }
    clearBtn.disabled = false;
  };

  clearBtn.addEventListener("click", () => {
    clearSearchInput(searchInput);
  });
};

//Återställer tillbaka till början genom att klicka på basic_button reset
const resetButton = document.getElementById("reset_button");
resetButton.addEventListener('click', () => {
  window.location.reload();
  ipc.send("reset");
});

const clearSearchInput = (searchInput) => {
  searchInput.value = "";
  clearBtn.disabled;
  timeTable.innerH;
};
const changeTheView = () => {};
const resetSetting = () => {};

setUpView();

//Hanterar ruta för spotify
function spotifyLogin() {
  ipc.send("spotifyLogin");
}

//Tar bort content som inte behövs längre när användaren går vidare
function removeContent(elementId) {
  document.getElementById(elementId).innerHTML = "";
}

//Genererar sidan där användaren väljer genre
ipcRenderer.on('spotifyReady', function(event, code) {
  removeContent("main_content");


  //Funktion för att göra tiden läsbar för användaren. Skrivs sedan ut i HTML koden
  function convertMillisecondsToHoursAndMinutes(milliseconds) {
    let seconds = milliseconds / 1000;
    let minutes = seconds / 60;
    let hours = Math.floor(minutes / 60);
    minutes = Math.floor(minutes % 60);
    if (hours < 10) {
      hours = "0" + hours;
    }
    if (minutes < 10) {
      minutes = "0" + minutes;
    }
    return hours + ":" + minutes;
  }
  
  let spotifyTime = localStorage.getItem("spotifyTime");
  let hoursAndMinutes = convertMillisecondsToHoursAndMinutes(spotifyTime);
  
  //Skriver ut HTML kod för att välja vilken genre man önskar

  let content =
      `<div class='center_me'>
      <h1>Generera spellista</h1>
      <h3>Din reselängd - ${hoursAndMinutes}H</h3>
        <label for="genre" id='genre'>Välj Genre</label>
        <select name="genres" id="getGenre">
          <option value="" disabled selected>Välj</option>
          <option value="pop">Pop</option>
          <option value="rock">Rock</option>
          <option value="julmusik">Julmusik</option>
          <option value="hiphop">Hip hop</option>
          <option value="jazz">Jazz</option>
        </select>

        <input type="submit" class="basic_button" id="send_genre_button" value="Skicka"/>
      </div>
      
      <h3 id='playlistHeader'>Förslag på spellista - visas upp efter användaren skickat</h3>

      <div id="song_suggestions">
        <table id="timeTableDeparture">
          <tr>
            <th>Låt</th>
            <th>Artist</th>
          </tr>

          <tr>
            <td>Sommar och sol</td>
            <td>Markoolio</td>
          </tr>

          <tr>
            <td>Sommar och sol</td>
            <td>Markoolio</td>
          </tr>

          <tr>
            <td>Sommar och sol</td>
            <td>Markoolio</td>
          </tr>
        </table>
      </div>

      <div id="flex_buttons">
        <button type="button" id="green">Spara</button>
        <button type="button" id="red">Generera ny lista</button>
      </div>
      `

  $("#main_content").append(content);

  // Event listeners to functions
  document.getElementById("send_genre_button").addEventListener("click", getPlaylistFromGenre)
  document.getElementById("green").addEventListener("click", savePlaylist)
  document.getElementById("red").addEventListener("click", newPlaylist)


  // När användaren har klickat på knappen hämta från API:et låtar som är lika långa som resan från den valda genren
  function getPlaylistFromGenre() {
    console.log("getPlaylistFromGenre fungerar!")
    var selectedGenre = document.getElementById("getGenre");
    if (selectedGenre != undefined && selectedGenre.value != ""){
      console.log(selectedGenre.value)
    } else {
      console.log("Vänligen välj en genre")
  }}
  

  // När användaren har klickat på knappen läggs nya låtar till i spellistan på användarens konto
  function savePlaylist() {
    console.log("SavePlaylist fungerar!") 
  }


  // När användaren har klickat på knappen gör en ny API-hämtning med nya låtar (ett could krav enligt)
  function newPlaylist() {
    console.log("newPlaylist fungerar!")     
  }
});

export { removeContent }