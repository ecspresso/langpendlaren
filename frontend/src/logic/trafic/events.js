
async function getAllStopsByTrainId(trainIdent){
    const data = await fetch(`http://localhost/trafikverket/trains/stops`, {method: "GET"}).then((res) => res.json()).catch((e) => console.assert(e));
    const trainAnnouncements = data.RESPONSE.RESULT[0].TrainAnnouncement;
    console.log(trainAnnouncements);
    return trainAnnouncements;
}



export {getAllStopsByTrainId};






