console.log("file")
const clearBtn = document
  .getElementById("clearBtn")
  .addEventListener("click", () => {
    resetSearch();
  });



// Basic functions
const resetSearch = () => {
  console.log("restS")
  document.getElementById("main_content").innerHTML = "";
};
const changeTheView = () => {};
const resetSetting = () => {};
