/**
 * Return a pre defined stops template
 * @returns {HTMLElement}
 */
function getStopsTemplate() {
  return `
    <main>
            <div id="searchbar">
                <h3 class="pagetitle">Sök slut station</h3>
                <div id="search_station">
                    <input id="station" type="text" placeholder="Ange station..." onkeyup="search()"/>
                </div>
            
                <h4>Eller välj en station från listan under</h4>
                <div id="result" class="limit">
                    <table id="timeTableDeparture">
                        <tr>
                            <th scope="col" style="width:200px;">Tid</th>
                            <th scope="col"  style="width:200px;">Välj slut station</th>
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
