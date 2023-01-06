import { getHoursMinutsFromTime } from "../../util/util.js";
import { getStationNames, getStationByName } from "./trafic_preload.js";
import { getAllStopsByTrainId } from "./trafic_events.js";
import { getStopsTemplate } from "./trafic_templates.js";
import { removeContent } from "../main/functionality.js";
import "./trafic_preload.js";
import {ipc} from "../main/functionality.js";

//Skapar variabler för tiderna
let depTimeMilli;
let arrTimeMilli;
let spotifyTime;

//Hämtar vald avgångstid och ankomstid och kontrollerar att tidskillnaden är ok och konverterar sedan skillnaden till millisekunder
function spotifyInit(time) {
    const { hours, minutes } = getHoursMinutsFromTime(time);
    arrTimeMilli = (hours * 60 * 60 * 1000 + minutes * 60 * 1000);
    spotifyTime = Number((arrTimeMilli - depTimeMilli));

    if (spotifyTime < 0) {
      alert("Var god välj en avgång som är senare än din avgångstid!");
    } else {
      localStorage.setItem("spotifyTime", spotifyTime)
      ipc.send("spotifyLogin");
    }
}

/**
 * Define some properties...
 */
function preDefination() {
  $.support.cors = true; // Enable Cross domain requests
  try {
    $.ajaxSetup({
      //url: "http://localhost",
      error: function (msg) {
        if (msg.statusText === "abort") return;
        alert("Request failed: " + msg.statusText + "\n" + msg.responseText);
        console.log(msg);
      },
    });
  } catch (e) {
    alert("Ett fel uppstod vid initialisering.");
  }
  // Create an ajax loading indicator
  let loadingTimer;
  $("#loader").hide();
  $(document)
    .ajaxStart(function () {
      loadingTimer = setTimeout(function () {
        $("#loader").show();
      }, 200);
    })
    .ajaxStop(function () {
      clearTimeout(loadingTimer);
      $("#loader").hide();
    });
}




/**
 * Search the train announcement
 */

//Söka efter station genom departures
function search() {
  let sign = $("#station").data("sign");
  // Clear html table
  $("#timeTableDeparture tr:not(:first)").remove();

  // Request to load announcements for a station by its signature
  $.ajax({
    type: "GET",
    dataType: "json",
    url: "http://localhost/trafikverket/departures/" + sign,
    success: function (response) {
      if (response == null) return;
      if (response.RESPONSE.RESULT[0].TrainAnnouncement == null)
        jQuery("#timeTableDeparture tr:last").after(
          '<tr class="list"><td colspan="4">Inga avgångar hittades</td></tr>'
        );
      try {
        displayTrainAnnouncement(response.RESPONSE.RESULT[0].TrainAnnouncement);
      } catch (ex) {}
    },
  });
}

/**
 * Display train announcement.
 */
//Den visar alla tåg som avgång från den stationen användaren valt genom att lägger till en tabell med informationen
function displayTrainAnnouncement(announcement) {
  announcement.forEach((item) => {
    const { hours, minutes } = getHoursMinutsFromTime( item.AdvertisedTimeAtLocation );
    let depTime = hours + ":" + minutes;
    let toList = getStationNames(item.ToLocation);

    let owner = "";
    if (item.InformationOwner != null) owner = item.InformationOwner;

    jQuery("#timeTableDeparture tr:last").after(`<tr>
                <td>${depTime}</td>
                <td>${toList.join(", ")}</td>
                <td>${owner}</td>
                <td style='text-align: center'>${item.TrackAtLocation}</td>
                <td>${item.AdvertisedTrainIdent}</td>
                <td><button 
                class="basic_button" 
                data-owner=${owner} 
                data-dep-hour=${hours} 
                data-dep-minute=${minutes} 
                data-train-id=${item.AdvertisedTrainIdent} 
                type='button'
                onclick="displayStopStationsByTrainId(${item.AdvertisedTrainIdent})">Välj resa</button></td>
            </tr>"
        `);
  });
}

/**
 * Display stop stations by choosen train.
 * @param {string} trainIdent
 */

//Funktion som visar stopp som ett tåg stannar vid som användaren sedan väljer mellan
function displayStopStationsByTrainId(trainIdent) {
  let train = document.querySelector(`[data-train-id="${trainIdent}"]`);
  let hours = train.dataset["depHour"];
  let minutes = train.dataset["depMinute"];
  let trainId = train.dataset["trainId"];
  let owner = train.dataset["owner"];
  depTimeMilli = hours * 60 * 60 * 1000 + minutes * 60 * 1000;

  removeContent("main_content");

  getAllStopsByTrainId(trainIdent)
    .then((result) => {
      result.forEach((item) => {
        const { hours, minutes } = getHoursMinutsFromTime( item.AdvertisedTimeAtLocation );

        let arrTime = hours + ":" + minutes;
        const stationName = getStationByName(item.LocationSignature);

        jQuery("#timeTableDeparture tr:last").after(`<tr>
                    <td id='arrivalTime'>${arrTime}</td>
                    <td> <button class='basic_button' type='button' onclick="spotifyInit('${item.AdvertisedTimeAtLocation}')">${stationName}</button></td>
                </tr>"
            `);
      });

    })
    .catch((e) => console.assert(e));
  $("#main_content").append(getStopsTemplate());
}



// Call the functions at the end
preDefination();


// Export globaly.
window.displayStopStationsByTrainId = displayStopStationsByTrainId;
window.search = search;
window.removeContent = removeContent;
window.spotifyInit = spotifyInit;