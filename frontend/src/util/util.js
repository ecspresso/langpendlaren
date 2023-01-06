import "../logic/trafic/trafic_preload.js";

/**
 * Funktion för att formattera tiden.
 * @param {string} AdvertisedTimeAtLocation 
 * @returns {string}
 */
function getHoursMinutsFromTime(AdvertisedTimeAtLocation) {
  let advertisedTime = new Date(AdvertisedTimeAtLocation);
  let hours = advertisedTime.getHours();
  let minutes = advertisedTime.getMinutes();
  if (minutes < 10) minutes = "0" + minutes;
  if (hours < 10) hours = "0" + hours;

  return { hours, minutes };
}

/**
 * Funktion för att göra tiden läsbar för användaren. Skrivs sedan ut i HTML koden
 * @param {string|null} milliseconds 
 * @returns {string}
 */
function millisecondsToHoursAndMinutes(milliseconds){
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


/**
 * Tar bort content som inte behövs längre när användaren går vidare
 * @param {string} elementId 
 */
function clearHTMLElementByElementId(elementId){
  document.getElementById(elementId).innerHTML = "";
}

export { getHoursMinutsFromTime, millisecondsToHoursAndMinutes, clearHTMLElementByElementId };
