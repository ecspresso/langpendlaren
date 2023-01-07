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

async function getUserProfile(){
    const data = await fetch(
        `http://localhost/spotify/user/profile`,
        { method: "GET" }
      )
        .then((res) => res.json())
        .catch((e) => console.assert(e));
        console.log("userId: ", data);
      return data;
}

async function getTokens(code){
    const data = await fetch(
        `http://localhost/spotify/authenticated?code=${code}`,
        { method: "GET" }
      )
        .then((res) => res.json())
        .catch((e) => console.assert(e));
        console.log(data);
      return data;
}

export { getAvailableGenre, getUserProfile, getTokens };