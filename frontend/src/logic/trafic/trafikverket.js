import { getHoursMinutsFromTime } from "../../util/util.js";
import { getStationNames, getStationByName } from "./trafic_preload.js";
import { getAllStopsByTrainId } from "./trafic_events.js";
import { getStopsTemplate, removeContent } from "./trafic_templates.js";
import "./trafic_preload.js";

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
function displayTrainAnnouncement(announcement) {
  announcement.forEach((item) => {
    const { hours, minutes } = getHoursMinutsFromTime(
      item.AdvertisedTimeAtLocation
    );
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
                data-dep-time=${depTime} 
                data-train-id=${item.AdvertisedTrainIdent} 
                type='button' 
                onclick="displayStopStationsByTrainId(${
                  item.AdvertisedTrainIdent
                })">Välj resa</button></td>
            </tr>"
        `);
  });
}

/**
 * Display stop stations by choosen train.
 * @param {string} trainIdent
 */
function displayStopStationsByTrainId(trainIdent) {
  let train = document.querySelector(`[data-train-id="${trainIdent}"]`);
  let depTime = train.dataset["depTime"];
  let trainId = train.dataset["trainId"];
  let owner = train.dataset["owner"];

  // Sätt kakor som hämtas av nästa sida.
  // window.localStorage.setItem("depTime", depTime);
  // window.localStorage.setItem("trainId", trainId);
  // window.localStorage.setItem("owner", owner);

  // ipc.send("traficStops");

  removeContent("main_content");

  getAllStopsByTrainId(trainIdent)
    .then((result) => {
      result.forEach((item) => {
        const { hours, minutes } = getHoursMinutsFromTime(
          item.AdvertisedTimeAtLocation
        );
        let depTime = hours + ":" + minutes;
        const stationName = getStationByName(item.LocationSignature);

        jQuery("#timeTableDeparture tr:last").after(`<tr>
                    <td>${depTime}</td>
                    <td><button 
                    class="selectTrain"
                    type='button' 
                    onclick="")">${stationName}</button></td>
                </tr>"
            `);
      });
    })
    .catch((e) => console.assert(e));
  $("#main_content").append(getStopsTemplate());
}

function reset() {
  removeContent();
  let content = `
    <div class="flex-container">
      <div class="trains">
      <h3>Sök avgång</h3>
        <div id="searchbar">
          <div id="station-wraper">
            <input id="station" type="text" placeholder="Ange stad här..." />
            <i id="clearBtn" class="fa-solid fa-xmark"></i>
          </div>
          <input id="showBtn" type="button" value="Visa"/>
          <span id="loader" style="margin-left: 10px">Laddar data ...</span>
        </div>

        <div id="result">
          <h3>Avgående tåg</h3>
          <table id="timeTableDeparture">
            <tr>
              <th scope="col" style="width:40px;">Tid</th>
              <th scope="col" style="width:200px;">Till</th>
              <th scope="col" style="width:80px;"></th>
              <th scope="col"  style="width:80px;">Spår</th>
              <!--Train ID-->
              <th scope="col"  style="width:80px;">ID</th>
              <th scope="col"  style="width:80px;">Välj tåg</th>
            </tr>
          </table>
        </div>
      </div>
    </div>
  `;

  $("#main_content").append(content);
}

// Call the functions at the end
preDefination();

// Export globaly.
window.displayStopStationsByTrainId = displayStopStationsByTrainId;
window.search = search;
