function getGenreTemplate(travelTime){
    return `
        <div class='center_me'>
            <h2 class>Generera spellista</h2>
            <p>Din reselängd är </p><h4>${travelTime}H</h4>

            <p>Find play list by genre.</p>
            <label for="genre">Välj genre:</label>
            
            <select name="genres" id="genres">
            <option>Ingen val</option>
            </select>
            <button type="submit" class="basic_button" id="send_genre_button" value="Skicka">Find</button>
        </div>

        <div>
            <div>
            <div id="playlist_suggestions">
                <h3>Play list suggestions</h3>
                <div id="p_suggestion"></div>
            </div>
            </div>
            <div id="song_suggestions">
                <h3>Song list suggestions</h3>
                <table id="timeTableDeparture">
                    <tr>
                        <th>Name</th>
                        <th>Antal låter</th>
                        <th>En låt</th>
                    </tr>
                </table>
            </div>
        </div>
        
        <div id="flex_buttons">
            <button class="basic_button" type="button" id="savePlayList">Save playlist</button>
            <button class="basic_button" type="button" id="generatePlaylist">Generate playlist</button>
        </div>
    `;
}

function displayAllGenre(genre){
    console.log("displayGenre", genre);
    var select = document.getElementById("genres");
    for (let i = 0; i < genre.length; ++i){
        var option = document.createElement("option");
        option.textContent = genre[i];
        option.value = genre[i];
        select.appendChild(option);
    }
}


function displayPlayListSuggestion(playlist){
    for(let i = 0; i < playlist.length; ++i){
        const n = playlist[i].name;
        const name = document.createElement("p");
        name.innerHTML = n;
        //const img = document.createElement("img").src = playlist[i].images[0].url;
        document.getElementById("p_suggestion").appendChild(n);
    }
}

    export {getGenreTemplate, displayAllGenre, displayPlayListSuggestion};
    