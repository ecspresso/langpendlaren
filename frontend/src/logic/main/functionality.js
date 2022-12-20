const clearBtn = document
  .getElementById("clearBtn")
  .addEventListener("click", resetSearch());

// Basic functions
const changeTheView = () => {};
const resetSearch = () => {
  document.getElementById("main_content").innerHTML = "";
};
const resetSetting = () => {};
