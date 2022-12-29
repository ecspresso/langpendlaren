
async function getAllStopsByTrainId(trainIdent){
    const data = await fetch(`http://localhost/trafikverket/trains/stops/${trainIdent}`, {method: "GET"}).then((res) => res.json()).catch((e) => console.assert(e));
    const trainAnnouncements = data.RESPONSE.RESULT[0].TrainAnnouncement;
    return trainAnnouncements;
}


export {getAllStopsByTrainId};






