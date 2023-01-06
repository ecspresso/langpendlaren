import { getAvailableGenre } from "../spotify/spotify_events";

/**
 * 
 */
function displayAllGenre(){
    const selectGener = document.getElementById("send_genre_button");
    for (var i=0; i < genre.length;++i){
        var option = document.createElement("OPTION"),
            txt = document.createTextNode(genre[i][1]);
        option.appendChild(txt);
        option.setAttribute("value",genre[i][0]);
        if(genre[i][2]!=''){
            // February need to be selected
            select.insertBefore(option,select.lastchild);
        } else {
            // others not
            select.insertBefore(option,select.lastchild);
        }
    }
}


 // När användaren har klickat på knappen hämta från API:et låtar som är lika långa som resan från den valda genren
 function getPlaylistFromGenre() {
    getAvailableGenre().then(result => {
        console.log(result);
    }).catch(e => console.log(e));
  };

  document.getElementById("send_genre_button").addEventListener("click", getPlaylistFromGenre)
