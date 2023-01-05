/**
 * Return a pre defined stops template
 * @returns {HTMLElement}
 */
function getStopsTemplate() {
  return `
    <main>
            <div id="searchbar">
                <h3 class="pagetitle">Sök slutstation</h3>
                <div id="search_station">
                    <input id="station" type="text" placeholder="Ange station..." onkeyup="search()"/>
                </div>
            
                <h4>Välj station från listan</h4>
                <div id="result" class="limit">
                    <table id="timeTableDeparture">
                        <tr>
                            <th scope="col" style="width:200px;">Ankomsttid</th>
                            <th scope="col"  style="width:200px;">Välj slutstation</th>
                        </tr>
                    </table>
                </div>
            </div> 
        </main>
    `;
}




/**
 *
 * @param {string} elementId
 */
function removeContent(elementId) {
  document.getElementById(elementId).innerHTML = "";
}

export { getStopsTemplate, removeContent };
