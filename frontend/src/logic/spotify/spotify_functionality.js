import { getTracksByGenre, addToPlayList, getPlayListTracksByPlayListId } from "./spotify_events.js";


function savePlayList(){

}

function newPlaylist() {
}

 // När användaren har klickat på knappen hämta från API:et låtar som är lika långa som resan från den valda genren
 function displayTracks(genre, travelTime, accessToken, pId) {
    // find tracks.
    const tarckIds = [];
    getTracksByGenre(genre, accessToken).then(album => {
        let timeCounter = 0;
        for(let i = 0; i < album.length; ++i){
            if(timeCounter < travelTime){
                tarckIds.push(album.id);
            }
            timeCounter = timeCounter + album[i].duration;
        }
    });
    
    // Add tracks to playList
    //addToPlayList(accessToken, tracks, pId);

    // Pull out all tracks in playlist and show on the screen to play.
    // getPlayListTracksByPlayListId(accessToken, pId).then(res => {
    //     const album = res.album;
    //     const image = "url....";
    //     const duration = 12;
    //     const name = "";
    //     for(let i = 0; i < album.length; ++i){
    //         jQuery("#tracksTable tr:last").after(`<tr>
    //                 <td>${i+1}</td>
    //                 <td><img src="${image}"></td>
    //                 <td>${name}</td>
    //                 <td>${duration}</td>
    //                 <td><button 
    //                 class="basic_button"
    //                 type='button'
    //                 onclick="playMusic()">Välj resa</button></td>
    //             </tr>"
    //         `);
    //     }
    // });
    
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