function getGenreTemplate(travelTime){
    return `<div class='center_me'>
    <h2 class>Generera spellista</h2>
    <h4>Din reselängd - ${travelTime}H</h2>
        <label for="genre">Välj genre:</label>
        <select name="genres" id="genres">
        <option value="pop">Pop</option>
        <option value="rock">Rock</option>
        <option value="julmusik">Julmusik</option>
        <option value="hiphop">Hip hop</option>
        <option value="jazz">Jazz</option>
        </select>

        <input type="submit" class="basic_button" id="send_genre_button" value="Skicka"/>
        </div>
        
        <h2>Förslag på spellista - visas upp efter användaren skickat</h2>
        
        <div id="song_suggestions">
        <table id="timeTableDeparture">
        <tr>
        <th>Låt</th>
        <th>Artist</th>
        </tr>
        
        <tr>
        <td>Sommar och sol</td>
        <td>Markoolio</td>
        </tr>
        
        <tr>
        <td>Sommar och sol</td>
        <td>Markoolio</td>
        </tr>
        
        <tr>
        <td>Sommar och sol</td>
        <td>Markoolio</td>
        </tr>
        </table>
        </div>
        
        <div id="flex_buttons">
        <button type="button" id="green">Spara</button>
        <button type="button" id="red">Generera ny lista</button>
        </div>
        `;
    }


    export {getGenreTemplate};
    