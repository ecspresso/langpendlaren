let Stations = [];

$.support.cors = true; // Enable Cross domain requests
try {
    $.ajaxSetup({
        //url: "http://localhost",
        error: function(msg) {
            if (msg.statusText === "abort") return;
            alert("Request failed: " + msg.statusText + "\n" + msg.responseText);
            console.log(msg);
        }
    });
} catch (e) {
    alert("Ett fel uppstod vid initialisering.");
}
// Create an ajax loading indicator
let loadingTimer;
$("#loader").hide();
$(document).ajaxStart(function() {
    loadingTimer = setTimeout(function() {
        $("#loader").show();
    }, 200);
}).ajaxStop(function() {
    clearTimeout(loadingTimer);
    $("#loader").hide();
});
// Load stations
PreloadTrainStations();

function PreloadTrainStations() {
    // Request to load all stations
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost/trafikverket/stations",
        success: function(response) {
            if (response == null) return;
            try {
                let stationlist = [];
                $(response.RESPONSE.RESULT[0].TrainStation).each(function(iterator, item) {
                    // Save a key/value list of stations
                    Stations[item.LocationSignature] = item.AdvertisedLocationName;
                    // Create an array to fill the search field autocomplete.
                    if (item.Prognosticated === true)
                        stationlist.push({
                            label: item.AdvertisedLocationName,
                            value: item.LocationSignature
                        });
                });
                fillSearchWidget(stationlist);
            } catch (ex) {}
        }
    });
}

function fillSearchWidget(data) {
    $("#station").val("");
    $("#station").autocomplete({
        // Make the autocomplete fill with matches that "starts with" only
        source: function(request, response) {
            let matches = $.map(data, function(tag) {
                if (tag.label.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {
                    return {
                        label: tag.label,
                        value: tag.value
                    }
                }
            });
            response(matches);
        },
        select: function(event, ui) {
            let selectedObj = ui.item;
            $("#station").val(selectedObj.label);
            // Save selected stations signature
            $("#station").data("sign", selectedObj.value);
            return false;
        },
        focus: function(event, ui) {
            let selectedObj = ui.item;
            // Show station name in search field
            $("#station").val(selectedObj.label);
            return false;
        }
    });
}

function Search() {
    let sign = $("#station").data("sign");
    // Clear html table
    $('#timeTableDeparture tr:not(:first)').remove();

    // Request to load announcements for a station by its signature
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost/trafikverket/departures/" + sign,
        success: function(response) {
            if (response == null) return;
            if (response.RESPONSE.RESULT[0].TrainAnnouncement == null)
                jQuery("#timeTableDeparture tr:last").
            after('<tr class="list"><td colspan="4">Inga avgångar hittades</td></tr>');
            try {
                renderTrainAnnouncement(response.RESPONSE.RESULT[0].TrainAnnouncement);
            } catch (ex) {}
        }
    });
}

function renderTrainAnnouncement(announcement) {
    $(announcement).each(function(iterator, item) {
        let advertisedTime = new Date(item.AdvertisedTimeAtLocation);
        let hours = advertisedTime.getHours()
        let minutes = advertisedTime.getMinutes()
        if (minutes < 10) minutes = "0" + minutes
        if (hours < 10) hours = "0" + hours
        // Departure time
        let depTime = (hours + ":" + minutes)
        let toList = [];
        $(item.ToLocation).each(function(iterator, toItem) {
            toList.push(Stations[toItem]);
        });
        let owner = "";
        if (item.InformationOwner != null) owner = item.InformationOwner;
        jQuery("#timeTableDeparture tr:last").
        after(`<tr>
                <td>${hours}:${minutes}</td>
                <td>${toList.join(', ')}</td>
                <td>${owner}</td>
                <td style='text-align: center'>${item.TrackAtLocation}</td>
                <td>${item.AdvertisedTrainIdent}</td>
                <td><button class="selectTrain" data-owner=${owner} data-dep-time=${depTime} data-train-id=${item.AdvertisedTrainIdent} type='button' onclick='selectTrain(${item.AdvertisedTrainIdent})'>Välj resa</button></td>
            </tr>"
        `);
    });
}

function selectTrain(trainIdent) {
    // Hämta tåget
    let train = document.querySelector(`[data-train-id="${trainIdent}"]`);
    // Hämta annan data
    let owner = train.dataset["owner"];
    let depTime = train.dataset["depTime"];
    // Rensa?
    clearBox("main_content");
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
    let timeDiff = (hours < 9 ? "0" : "") + hours + ":" + (minutes < 9 ? "0" : "") + minutes;
    console.log(timeDiff)

}
//document.getElementById("timeDiffrence").value = calcTimeDiffrence(departureTime, arrivalTime);

/* HTML
<input id="departureTime" value="11:00">
<input id="arrivalTime" value="22:40">
<input id="timeDiffrence">
*/