
import "../logic/trafic/trafic_preload.js";
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
          return stationlist;
        }
        catch (ex) { }
      }
    });
  }

function getHoursMinutsFromTime(AdvertisedTimeAtLocation){
  let advertisedTime = new Date(AdvertisedTimeAtLocation);
  let hours = advertisedTime.getHours()
  let minutes = advertisedTime.getMinutes()
  if (minutes < 10) minutes = "0" + minutes
  if (hours < 10) hours = "0" + hours

  return {hours, minutes}
}

export {PreloadTrainStations, getHoursMinutsFromTime}