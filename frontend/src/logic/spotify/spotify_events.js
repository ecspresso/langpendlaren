//Funktion som hämtar genrer från spotify
async function getAvailableGenre(accessToken){
    const data = await fetch(
        `http://localhost/spotify/genre/seeds?access_token=${accessToken}`,
        { method: "GET" }
      )
        .then((res) => res.json())
        .catch((e) => console.assert(e));
      return data;
}

async function getUserProfile(accessToken){
    const data = await fetch(
        `http://localhost/spotify/user/profile?access_token=${accessToken}`,
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

async function getPlayListByGenre(genre, accessToken){
  const data = await fetch(
    `http://localhost/spotify/search/playlist/${genre}?access_token=${accessToken}`,
    { method: "GET" }
  )
    .then((res) => res.json())
    .catch((e) => console.assert(e));
  return data;
}

async function createPlayListByName(accessToken, name, description){
  const response = await fetch(
    `http://localhost/spotify/playlist/create/?access_token=${accessToken}`
    ,
    { method: "POST",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ 
        "name": name,
        "description": description,
      })},
  )
  
  return response.json();
}

export { getAvailableGenre, getUserProfile, getTokens, getPlayListByGenre, createPlayListByName };