function getGenreTemplate(travelTime){
    return `
        <div class='center_me'>
            <h2 class>Generera spellista</h2>
            <p>Din reselängd är <h4> ${travelTime}H</h4></p>
        </div>
        <div>
            <p>Find play list by genre.</p>
            <label for="genre">Välj genre:</label>
            
            <select name="genres" id="genres">
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
            <button type="button" id="savePlayList">Save playlist</button>
            <button type="button" id="generatePlaylist">Generate new play list</button>
        </div>
        `;
    }


    export {getGenreTemplate};
    