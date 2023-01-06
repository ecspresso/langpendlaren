const ipc = window.require("electron").ipcRenderer;
const ipcRenderer = require("electron").ipcRenderer;

export { ipc };

// Basic functions
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

function spotifyLogin() {
  ipc.send("spotifyLogin");
}

function removeContent(elementId) {
  document.getElementById(elementId).innerHTML = "";
}

ipcRenderer.on('spotifyReady', function(event, code) {
  alert("Denna kod ska till egen fil?");
  console.log("Spotify redo! " + code);
  removeContent("main_content");

  let travelTime = localStorage.getItem("travelTime") // access travel time from localStorage
  let spotifyTime = localStorage.getItem("spotifyTime")

  let content =
      `<h2>Generera spellista</h2>
      <h2>Din reselängd - ${travelTime} || ${spotifyTime}</h2>
      <form action="do_something">
        <label for="genre">Välj genre:</label>
        <select name="genres" id="genres">
          <option value="pop">Pop</option>
          <option value="rock">Rock</option>
          <option value="julmusik">Julmusik</option>
          <option value="hiphop">Hip hop</option>
          <option value="jazz">Jazz</option>
        </select>

        <input type="submit" value="Skicka" />
      </form>`

  $("#main_content").append(content);
});

export { removeContent }