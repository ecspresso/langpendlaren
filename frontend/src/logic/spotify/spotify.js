//Kommer ej användas? Ta bort?
function getGener(){
    var month = [["1", "January", ""], [["3", "Mars", ""], ["4", "April", ""], ["5", "May", ""], ["2", "February", "1"]];
    const selectGener = document.getElementById("select_gener");
    for (var i=0; i < month.length;++i){
        var option = document.createElement("OPTION"),
            txt = document.createTextNode(month[i][1]);
        option.appendChild(txt);
        option.setAttribute("value",month[i][0]);
        if(month[i][2]!=''){
            // February need to be selected
            select.insertBefore(option,select.lastchild);
        } else {
            // others not
            select.insertBefore(option,select.lastchild);
        }
    }
}