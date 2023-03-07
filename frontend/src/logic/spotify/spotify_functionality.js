import { getTracksByGenre, addToPlayList, getPlayListTracksByPlayListId } from "./spotify_events.js";


function savePlayList(){

}

function newPlaylist() {
}



// När användaren har klickat på knappen hämta från API:et låtar som är lika långa som resan från den valda genren
async function displayTracks(genre, travelTime, accessToken, pId) {
    // find tracks.
    // const tracks = [];
    let timeCounter = 0;
    let offset = "0";
    let counter = 1;

    // Add tracks to playList
    while(timeCounter < travelTime) {
        var tracks = await getTracksByGenre(genre, accessToken, offset);
        var allTracks = [];

        for(const track of tracks.items){
            // console.log("adding...")
            // addToPlayList(accessToken, track.id, pId).then(res => {
            //     console.log(res);
            // });

            allTracks.push(track);

            var minutes = Math.floor(track.durationMs / 60000);
            var seconds = ((track.durationMs % 60000) / 1000).toFixed(0);
            var dur = minutes + ":" + (seconds < 10 ? '0' : '') + seconds;

            jQuery("#tracksTable tr:last").after(`<tr>
                     <td>${counter++}</td>
                     <td><img src="${track.imgSrc}"></td>
                     <td>${track.name}</td>
                     <td>${dur}</td>
                         <td><button
                         class="basic_button"
                         type='button'
                         onclick="playMusic()">KNAPP?</button></td>
                 </tr>"
         `);

            timeCounter = timeCounter + track.durationMs;
            offset = offset + 10;
            if(timeCounter >= travelTime) {
                break;
            }
        }
    }

    for(const track of allTracks) {
        console.log("adding...")
        addToPlayList(accessToken, track.id, pId).then(res => {
            console.log(res);
        });
    }

    // Pull out all tracks from playlist and show on the screen to play.
    //     getPlayListTracksByPlayListId(accessToken, pId).then(res => {

        // for(let i = 0; i < tracks.length; ++i){
        //     let thisTrack = tracks[i];
    //     }

    //
    //
    // getTracksByGenre(genre, accessToken, offset).then(data => {
    //     for(let i = 0; i < data.albums.length; i++) {
    //         let thisAlbum = data.albums[i];
    //         for(let j = 0; j < thisAlbum.tracks.length; j++) {
    //             tracks.push(thisAlbum.tracks[j]);
    //         }
    //     }
    //
    //     offset = data.offset;
    // }).then(() => {
    //     // Add tracks to playList
    //     if(tracks.length > 0){
    //         for(const track of tracks){
    //             console.log("adding...")
    //             addToPlayList(accessToken, track.id, pId).then(res => {
    //                 console.log(res);
    //             });
    //         }
    //     }
    //     // Pull out all tracks from playlist and show on the screen to play.
    //     // getPlayListTracksByPlayListId(accessToken, pId).then(res => {
    //
    //     for(let i = 0; i < tracks.length; ++i){
    //         let thisTrack = tracks[i];
    //         jQuery("#tracksTable tr:last").after(`<tr>
    //                 <td>${i+1}</td>
    //                 <td><img src="${thisTrack.imageUri}"></td>
    //                 <td>${thisTrack.name}</td>
    //                 <td>${thisTrack.durationMs}ms</td>
    //                 <td><button
    //                 class="basic_button"
    //                 type='button'
    //                 onclick="playMusic()">Välj resa</button></td>
    //             </tr>"
    //         `);
    //     }
        // });
    // })
    // .then(() => {
    //     localStorage.setItem("timeCounter", timeCounter);
    //     localStorage.setItem("offset", offset);
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