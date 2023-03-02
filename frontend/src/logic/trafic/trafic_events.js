//Hämtar vilka stationer ett tåg stannar på
async function getAllStopsByTrainId(trainIdent) {
  const data = await fetch(
    `http://localhost:8080/trafikverket/trains/stops/${trainIdent}`,
    { method: "GET" }
  )
    .then((res) => res.json())
    .catch((e) => console.assert(e));
  return data.stationStops;
}

// Emile exprement with localStorage
// let trainId = window.localStorage.getItem("trainId");
// let stops = await getAllStopsByTrainId(trainId);
// console.log(stops);

export { getAllStopsByTrainId };
