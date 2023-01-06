//Funktion som hämtar genrer från spotify
async function getAvailableGenre(){
    const data = await fetch(
        `http://localhost/spotify/genre/seeds`,
        { method: "GET" }
      )
        .then((res) => res.json())
        .catch((e) => console.assert(e));
        console.log(data);
      return data;
}

export { getAvailableGenre };