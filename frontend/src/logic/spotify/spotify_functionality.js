import { getPlayListByGenre } from "./spotify_events.js";
import { displayPlayListSuggestion } from "./spotify_templates.js";


function savePlayList(){

}

function newPlaylist() {
}

 // När användaren har klickat på knappen hämta från API:et låtar som är lika långa som resan från den valda genren
 async function displayPlayListByGenre(genre, accessToken) {
    await getPlayListByGenre(genre, accessToken).then(plist => {
        displayPlayListSuggestion(plist);
    });
}


/**
 * Create play list by random tracks of a genre.
 * @param {String[]} listOfGenre 
 */
function getRandomPlayList(listOfGenre){
    const max = listOfGenre.length;
    const randomNumber = Math.floor(Math.random() * max);
    console.log(randomNumber, listOfGenre[randomNumber]);
}

export { savePlayList, newPlaylist, displayPlayListByGenre, getRandomPlayList }