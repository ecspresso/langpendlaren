import { getHoursMinutsFromTime } from "../../util/util.js";
import { getStationNames, getStationByName } from "./preload.js";
import { getAllStopsByTrainId } from "./events.js";
import "./preload.js";


function preDefind() {
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
preDefind();


// Load stations

function Search() {
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
        renderTrainAnnouncement(response.RESPONSE.RESULT[0].TrainAnnouncement);
      } catch (ex) {}
    },
  });
}

function renderTrainAnnouncement(announcement) {
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
                class="selectTrain" 
                data-owner=${owner} 
                data-dep-time=${depTime} 
                data-train-id=${item.AdvertisedTrainIdent} 
                type='button' 
                onclick="getTrainById(${
                  item.AdvertisedTrainIdent
                })">Välj resa</button></td>
            </tr>"
        `);
  });
}

function getTrainById(trainIdent) {
  let train = document.querySelector(`[data-train-id="${trainIdent}"]`);
  let depTime = train.dataset["depTime"];
  let trainId = train.dataset["data-train-id"];
  let owner = train.dataset["owner"];
  clearBox("main_content");

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
  let stopsHTML = `
        <main>
            <div id="searchbar">
                <h3 class="pagetitle">Sök slut station</h3>
                <div id="search_station">
                    <input id="station" type="text" placeholder="Ange station..." onkeyup="search()"/>
                </div>
            
                <h4>Eller välj en station från listan under</h4>
                <div id="result" class="limit">
                    <table id="timeTableDeparture">
                        <tr>
                            <th scope="col" style="width:200px;">Tid</th>
                            <th scope="col"  style="width:200px;">Välj slut station</th>
                        </tr>
                    </table>
                </div>
            </div> 
        </main>
    `;

  $("#main_content").append(stopsHTML);
}

function clearBox(elementID) {
  document.getElementById(elementID).innerHTML = "";
}

// http://jsfiddle.net/je01otqn/36/
//let departureTime = document.getElementById("departureTime").value;
//let arrivalTime = document.getElementById("arrivalTime").value;

function calcTimeDiffrence(departureTime, arrivalTime) {
  departureTime = departureTime.split(":");
  arrivalTime = arrivalTime.split(":");
  let startDate = new Date(0, 0, 0, departureTime[0], departureTime[1], 0);
  let endDate = new Date(0, 0, 0, arrivalTime[0], arrivalTime[1], 0);
  let diff = endDate.getTime() - startDate.getTime();
  let hours = Math.floor(diff / 1000 / 60 / 60);
  diff -= hours * 1000 * 60 * 60;
  let minutes = Math.floor(diff / 1000 / 60);
  let timeDiff =
    (hours < 9 ? "0" : "") + hours + ":" + (minutes < 9 ? "0" : "") + minutes;
  console.log(timeDiff);
}
//document.getElementById("timeDiffrence").value = calcTimeDiffrence(departureTime, arrivalTime);

/* HTML
<input id="departureTime" value="11:00">
<input id="arrivalTime" value="22:40">
<input id="timeDiffrence">
*/

// Export globaly from modul scope.
window.getTrainById = getTrainById;
window.Search = Search();