//Funktion som hämtar genrer från spotify
async function getAvailableGenre(accessToken){
    const data = await fetch(
        `http://localhost/spotify/genre/seeds/${accessToken}`,
        { method: "GET" }
      )
        .then((res) => res.json())
        .catch((e) => console.assert(e));
      return data;
}

async function getUserProfile(accessToken){
    const data = await fetch(
        `http://localhost/spotify/user/profile/${accessToken}`,
        { method: "GET" }
      )
        .then((res) => res.json())
        .catch((e) => console.assert(e));
        console.log(data)
      return data;

}

async function getTokens(code){
    const data = await fetch(
        `http://localhost/spotify/authenticated?code=${code}`,
        { method: "GET" }
      )
        .then((res) => res.json())
        .catch((e) => console.assert(e));
      return data;
}

export { getAvailableGenre, getUserProfile, getTokens };