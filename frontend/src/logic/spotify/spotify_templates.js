function getGenreTemplate(travelTime){
    //Vi vill att när man skickar formuläret så ska skapa spellista endpointen köras med access token, name och description
    return `
        <div class='center_me'>
            <h2 class>Generera spellista</h2>
            <p>Din reselängd är </p><h4>${travelTime}H</h4>

            <div id="playListCreatorWraper">
                <label for="name">Name:</label><br>
                <input type="text" id="pname" name="name"><br>
                <label for="description">Description:</label><br>
                <textarea id="pdescription" name="description"></textarea><br><br>
                <button id="createPlayList" class="basic_button">Create play list</button>
            </div>
            
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
    const p_suggestion = document.getElementById("p_suggestion");
    for(let i = 0; i < playlist.length; ++i){
        const template = `
            <div style="display:flex; width="300px; height:200px;">
                <div>
                    <img style="width: 100%;" src="${playlist[i].images[0].url}">
                </div>
                <div>
                    <h4>${playlist[i].name}</h4>
                </div>                
            </div>
        `
        p_suggestion.appendChild(template);
    }
    console.log(p_suggestion);
}

    export {getGenreTemplate, displayAllGenre, displayPlayListSuggestion};
    