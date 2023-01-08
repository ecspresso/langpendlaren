import { getPlayListByGenre } from "./spotify_events.js";
import { displayPlayListSuggestion } from "./spotify_templates.js";


function savePlayList(){

}

function newPlaylist() {
}

 // När användaren har klickat på knappen hämta från API:et låtar som är lika långa som resan från den valda genren
 async function displayPlayListByGenre(genre) {
    console.log("Get playlist by: ",genre)
    await getPlayListByGenre(genre).then(plist => {
        displayPlayListSuggestion(plist)
    })
    getRandomPlayList(genre)
};



function getRandomPlayList(listOfGenre){
    const max = listOfGenre.length;
    const randomNumber = Math.floor(Math.random() * max);
    console.log(randomNumber, listOfGenre[randomNumber]);
}

export { savePlayList, newPlaylist, displayPlayListByGenre, getRandomPlayList }