async function getAvailableGener(){
    const data = await fetch(
        `http://localhost/spotify/genre/seeds`,
        { method: "GET" }
      )
        .then((res) => res.json())
        .catch((e) => console.assert(e));
        console.log(data);
      return data;
}



window.getAvailableGener = getAvailableGener;
export {getAvailableGener};