import { getTracksByGenre } from "./spotify_events.js";
import { displayPlayListSuggestion } from "./spotify_templates.js";


function savePlayList(){

}

function newPlaylist() {
}

 // När användaren har klickat på knappen hämta från API:et låtar som är lika långa som resan från den valda genren
 function displayTracks(genre, travelTime, accessToken) {
    // find tracks.
    console.log("control 1: ", accessToken);
    getTracksByGenre(genre, accessToken).then(res => {
        console.log(res);
    });
    // filter out 
    
    
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

export { savePlayList, newPlaylist, displayTracks, getRandomPlayList }