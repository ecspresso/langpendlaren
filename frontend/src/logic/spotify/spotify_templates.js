function getGenreTemplate(travelTime, options){
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

        
        <div id="song_suggestions">
            <h3>Suggestions</h3>
            <table id="timeTableDeparture">
                <tr>
                    <th>Låt</th>
                    <th>Artist</th>
                </tr>
            </table>
        </div>
        
        <div id="flex_buttons">
            <button class="basic_button" type="button" id="savePlayList">Save playlist</button>
            <button class="basic_button" type="button" id="generatePlaylist">Generate new play list</button>
        </div>
        `;
    }

    function displayAllGenre(genre){
        var select = document.getElementById("genres");
        for (let i = 0; i < genre.length; ++i){
            var option = document.createElement("option");
            option.textContent = genre[i];
            option.value = genre[i];
            console.log(option, select);
            select.appendChild(option);
    }
}


    export {getGenreTemplate, displayAllGenre};
    