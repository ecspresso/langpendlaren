const ipc = window.require("electron").ipcRenderer;
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
const resetButton = document.querySelector('.basic_button');
resetButton.addEventListener('click', () => {
  window.location.reload();
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
