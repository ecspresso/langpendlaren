
async function getAllStopsByTrainId(trainIdent){
    const data = await fetch(`http://localhost/trafikverket/trains/stops/${trainIdent}`, {method: "GET"}).then((res) => res.json()).catch((e) => console.assert(e));
    const trainAnnouncements = data.RESPONSE.RESULT[0].TrainAnnouncement;
    return trainAnnouncements;
}


//export {getAllStopsByTrainId};


let trainId = window.localStorage.getItem("trainId");
let stops = await getAllStopsByTrainId(trainId);
console.log(stops);


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
                    onclick="">${stationName}</button></td>
                </tr>"
            `);
      });
    })
    .catch((e) => console.assert(e));