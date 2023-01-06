//Läser in alla stationer som användaren kan välja mellan genom att lägga dessa i en lista
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
        $(response.stations).each(function (
          iterator,
          item
        ) {
          // Save a key/value list of stations
          Stations[item.short_name] = item.name;
          // Create an array to fill the search field autocomplete.
          if (item.prognosticated === true)
            stationlist.push({
              label: item.name,
              value: item.short_name,
            });
        });
        fillSearchWidget(stationlist);
      } catch (ex) {}
    },
  });
}
preLoadTrainStations();

//Sökfunktion för att söka på stationer
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
