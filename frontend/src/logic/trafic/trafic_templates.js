/**
 * Return a pre defined stops template
 * @returns {HTMLElement}
 */
//Skriver ut HTML för vilka stationer ett tåg stannar vid
function getStopsTemplate() {
  return `
    <main>            
                <h2 id="station_header">Välj station från listan</h4>
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

export { getStopsTemplate };
