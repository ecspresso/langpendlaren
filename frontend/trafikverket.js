
var Stations = new Array();
$(document).ready(function () {
  $.support.cors = true; // Enable Cross domain requests
  try {
    $.ajaxSetup({
      url: "https://api.trafikinfo.trafikverket.se/v2/data.json",
      error: function (msg) {
        if (msg.statusText == "abort") return;
        alert("Request failed: " + msg.statusText + "\n" + msg.responseText);
      }
    });
  }
  catch (e) { alert("Ett fel uppstod vid initialisering."); }
  // Create an ajax loading indicator
  var loadingTimer;
  $("#loader").hide();
  $(document).ajaxStart(function () {
    loadingTimer = setTimeout(function () {
      $("#loader").show();
    }, 200);
  }).ajaxStop(function () {
    clearTimeout(loadingTimer);
    $("#loader").hide();
  });
  // Load stations
  PreloadTrainStations();
});

function PreloadTrainStations() {
  // Request to load all stations
  var xmlRequest = "<REQUEST>" +
          // Use your valid authenticationkey
          "<LOGIN authenticationkey='ab3f9656870c4625adfaaca4b76caeae'/>" +
          "<QUERY objecttype='TrainStation' schemaversion='1'>" +
          "<FILTER/>" +
          "<INCLUDE>Prognosticated</INCLUDE>" +
          "<INCLUDE>AdvertisedLocationName</INCLUDE>" +
          "<INCLUDE>LocationSignature</INCLUDE>" +
          "</QUERY>" +
          "</REQUEST>";
  $.ajax({
    type: "POST",
    contentType: "text/xml",
    dataType: "json",
    data: xmlRequest,
    success: function (response) {
      if (response == null) return;
      try {
        var stationlist = [];
        $(response.RESPONSE.RESULT[0].TrainStation).each(function (iterator, item)
        {
          // Save a key/value list of stations
          Stations[item.LocationSignature] = item.AdvertisedLocationName;
          // Create an array to fill the search field autocomplete.
          if (item.Prognosticated == true)
            stationlist.push({ label: item.AdvertisedLocationName, value: item.LocationSignature });
        });
        fillSearchWidget(stationlist);
      }
      catch (ex) { }
    }
  });
}

function fillSearchWidget(data) {
  $("#station").val("");
  $("#station").autocomplete({
    // Make the autocomplete fill with matches that "starts with" only
    source: function (request, response) {
      var matches = $.map(data, function (tag) {
        if (tag.label.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {
          return {
            label: tag.label,
            value: tag.value
          }
        }
      });
      response(matches);
    },
    select: function (event, ui) {
      var selectedObj = ui.item;
      $("#station").val(selectedObj.label);
      // Save selected stations signature
      $("#station").data("sign", selectedObj.value);
      return false;
    },
    focus: function (event, ui) {
      var selectedObj = ui.item;
      // Show station name in search field
      $("#station").val(selectedObj.label);
      return false;
    }
  });
}

function Search() {
  var sign = $("#station").data("sign");
  // Clear html table
  $('#timeTableDeparture tr:not(:first)').remove();

  // Request to load announcements for a station by its signature
  var xmlRequest = "<REQUEST>" +
          "<LOGIN authenticationkey='ab3f9656870c4625adfaaca4b76caeae' />" +
          "<QUERY objecttype='TrainAnnouncement' " +
          "orderby='AdvertisedTimeAtLocation' schemaversion='1'>" +
          "<FILTER>" +
          "<AND>" +
          "<OR>" +
          "<AND>" +
          "<GT name='AdvertisedTimeAtLocation' " +
          "value='$dateadd(-00:15:00)' />" +
          "<LT name='AdvertisedTimeAtLocation' " +
          "value='$dateadd(14:00:00)' />" +
          "</AND>" +
          "<GT name='EstimatedTimeAtLocation' value='$now' />" +
          "</OR>" +
          "<EQ name='LocationSignature' value='" + sign + "' />" +
          "<EQ name='ActivityType' value='Avgang' />" +
          "</AND>" +
          "</FILTER>" +
          // Just include wanted fields to reduce response size.
          "<INCLUDE>InformationOwner</INCLUDE>" +
          "<INCLUDE>AdvertisedTimeAtLocation</INCLUDE>" +
          "<INCLUDE>TrackAtLocation</INCLUDE>" +
          "<INCLUDE>FromLocation</INCLUDE>" +
          "<INCLUDE>ToLocation</INCLUDE>" +
          // Train ID
          "<INCLUDE>AdvertisedTrainIdent</INCLUDE>" +
          "</QUERY>" +
          "</REQUEST>";
  $.ajax({
    type: "POST",
    contentType: "text/xml",
    dataType: "json",
    data: xmlRequest,
    success: function (response) {
      if (response == null) return;
      if (response.RESPONSE.RESULT[0].TrainAnnouncement == null)
        jQuery("#timeTableDeparture tr:last").
        after("<tr><td colspan='4'>Inga avgångar hittades</td></tr>");
      try {
        renderTrainAnnouncement(response.RESPONSE.RESULT[0].TrainAnnouncement);
      }
      catch (ex) { }
    }
  });
}

function renderTrainAnnouncement(announcement) {
  $(announcement).each(function (iterator, item) {
    var advertisedtime = new Date(item.AdvertisedTimeAtLocation);
    var hours = advertisedtime.getHours()
    var minutes = advertisedtime.getMinutes()
    if (minutes < 10) minutes = "0" + minutes
    if (hours < 10) hours = "0" + hours
    // Departure time
    var depTime = (hours + ":" + minutes)
    var toList = new Array();
    $(item.ToLocation).each(function (iterator, toItem) {
      toList.push(Stations[toItem]);
    });
    var owner = "";
    if (item.InformationOwner != null) owner = item.InformationOwner;
    jQuery("#timeTableDeparture tr:last").
    after("<tr><td>" + hours + ":" + minutes + "</td><td>" + toList.join(', ') +
            "</td><td>" + owner + "</td><td style='text-align: center'>" + item.TrackAtLocation + "</td><td>" +
            // Train ID
            item.AdvertisedTrainIdent + "</td><td> <button id='selectTrain' type='button' onclick='clearBox('main_content')'>Välj resa</button> </td></tr>");

            var selectTrain = document.getElementById("selectTrain");
            selectTrain.addEventListener("click", function(e) {
            console.log("Train ID: " + item.AdvertisedTrainIdent + " | " + "Departure time: " + depTime);
});
  });
}

function clearBox(elementID) {
  document.getElementById(elementID).innerHTML= "";
}

// http://jsfiddle.net/je01otqn/36/
var departureTime = document.getElementById("departureTime").value;
var arrivalTime = document.getElementById("arrivalTime").value;
function calcTimeDiffrence(departureTime, arrivalTime) {
    departureTime = departureTime.split(":");
    arrivalTime = arrivalTime.split(":");
    var startDate = new Date(0, 0, 0, departureTime[0], departureTime[1], 0);
    var endDate = new Date(0, 0, 0, arrivalTime[0], arrivalTime[1], 0);
    var diff = endDate.getTime() - startDate.getTime();
    var hours = Math.floor(diff / 1000 / 60 / 60);
    diff -= hours * 1000 * 60 * 60;
    var minutes = Math.floor(diff / 1000 / 60);
    return (hours < 9 ? "0" : "") + hours + ":" + (minutes < 9 ? "0" : "") + minutes;
}
document.getElementById("timeDiffrence").value = calcTimeDiffrence(departureTime, arrivalTime);

/* HTML
<input id="departureTime" value="11:00">
<input id="arrivalTime" value="22:40">
<input id="timeDiffrence">
*/ 