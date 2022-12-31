let Stations = [];
function preLoadTrainStations() {
  // Request to load all stations
  $.ajax({
    type: "GET",
    dataType: "json",
    url: "http://localhost/trafikverket/stations",
    success: function (response) {
      if (response == null) return;
      try {
        let stationlist = [];
        $(response.RESPONSE.RESULT[0].TrainStation).each(function (
          iterator,
          item
        ) {
          // Save a key/value list of stations
          Stations[item.LocationSignature] = item.AdvertisedLocationName;
          // Create an array to fill the search field autocomplete.
          if (item.Prognosticated === true)
            stationlist.push({
              label: item.AdvertisedLocationName,
              value: item.LocationSignature,
            });
        });
        fillSearchWidget(stationlist);
      } catch (ex) {}
    },
  });
}
preLoadTrainStations();

function fillSearchWidget(data) {
  $("#station").val("");
  $("#station").autocomplete({
    // Make the autocomplete fill with matches that "starts with" only
    source: function (request, response) {
      let matches = $.map(data, function (tag) {
        if (tag.label.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {
          return {
            label: tag.label,
            value: tag.value,
          };
        }
      });
      response(matches);
    },
    select: function (event, ui) {
      let selectedObj = ui.item;
      $("#station").val(selectedObj.label);
      // Save selected stations signature
      $("#station").data("sign", selectedObj.value);
      return false;
    },
    focus: function (event, ui) {
      let selectedObj = ui.item;
      // Show station name in search field
      $("#station").val(selectedObj.label);
      return false;
    },
  });
}

function getStationNames(stations) {
  return stations.map((station) => Stations[station]);
}

function getStationByName(station) {
  return Stations[station];
}

export { getStationNames, getStationByName };
