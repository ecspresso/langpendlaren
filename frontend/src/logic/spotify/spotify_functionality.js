import { getTracksByGenre, addToPlayList, getPlayListTracksByPlayListId } from "./spotify_events.js";


function savePlayList(){
    let songsJSON = localStorage.getItem("songs");
    let allTracks = JSON.parse(atob(songsJSON));
    let pId = localStorage.getItem("p_id");
    let accessToken = localStorage.getItem("access_token");

    for(const track of allTracks) {
        console.log("adding...")
        addToPlayList(accessToken, track.id, pId).then(res => {
            console.log(res);
        });
    }
}

function newPlaylist() {
}



// När användaren har klickat på knappen hämta från API:et låtar som är lika långa som resan från den valda genren
async function displayTracks(genre, travelTime, accessToken) {
    let table = document.getElementById("tracksTable");
    while(table.rows.length > 1) {
        table.deleteRow(1);
    }

    // find tracks.
    // const tracks = [];
    let timeCounter = 0;
    let offset = localStorage.getItem("offset");
    if(offset == null) {
        offset = "0";
    }
    let counter = 1;
    let allTracks = [];

    // Add tracks to playList
    while(timeCounter < travelTime) {
        let tracks = await getTracksByGenre(genre, accessToken, offset);

        for(const track of tracks.items){
            allTracks.push(track);

            let minutes = Math.floor(track.durationMs / 60000);
            let seconds = ((track.durationMs % 60000) / 1000).toFixed(0);
            let dur = minutes + ":" + (seconds < 10 ? '0' : '') + seconds;

            jQuery("#tracksTable tr:last").after(`<tr>
                     <td>${counter++}</td>
                     <td><img src="${track.imgSrc}"></td>
                     <td>${track.name}</td>
                     <td>${dur}</td>
                 </tr>`
            );

            timeCounter = timeCounter + track.durationMs;
            offset = Number(offset) + 10;
            if(Number(offset) >= 1000) {
                offset = "0";
            }
            if(timeCounter >= travelTime) {
                break;
            }
        }
    }

    let base64 = btoa(JSON.stringify(allTracks))
    localStorage.setItem("songs", base64);
    localStorage.setItem("offset", offset);
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