$u = (Invoke-RestMethod http://localhost/spotify/login).spotify_auth_uri
$u | clip
$u2 = (Invoke-RestMethod $u)
$u2

$at = Get-Clipboard
$at | clip
$r  = Get-Clipboard

(Invoke-RestMethod http://localhost/spotify/user/profile/$at) # user profile
(Invoke-RestMethod http://localhost/spotify/genre/seeds) | convertto-json # seeds

(Invoke-RestMethod http://localhost/spotify/authenticated) # skaffa access token kräver ?code=

$a = (Invoke-RestMethod http://localhost/spotify/login/refresh?refresh_token=$r) # förnya access token kräver ?refresh_token=
$at = $a.access_token.value
$a | ConvertTo-Json -Depth 100


$body = @{name="emile";description="kaffe"} |ConvertTo-Json
$body = @{name="emile"} |ConvertTo-Json
$body = @{description="kaffe"} |ConvertTo-Json
Remove-Variable body
(Invoke-RestMethod http://localhost/spotify/playlist/create?access_token=$at -Method POST -body $body)

(Invoke-RestMethod http://localhost/spotify/search/playlist/pop?access_token=$at)
$pl = (Invoke-RestMethod http://localhost/spotify/search/playlist/pop?access_token=$at)
$pl | select next, previous

$plof = (Invoke-RestMethod "http://localhost/spotify/search/playlist/pop?offset=10&access_token=$at")
$plof | select next, previous


$playlists = "7FTgAwVwH6gTuEWPVwxnEx","6MuZEElHAAwMQEKn0AUV3y","6EHQm1IIdDi9lJ5mFObBsg","0HjeOtM5RaMaMIGZJ1TMzK","2bO3c30nB4obd7b20a20hJ","77PPFLByKqhK1s6V7iDsob","3FMe1rK4iNLKax4EAoRhwN","3FMe1rK4iNLKax4EAoRhwN","4jQI3Xo4EmGoQ1561AfRHA","2fU25sAYX1KdmhUXWsapg2","6NXwvo3bJr8p7dI4ntLFaf","5QFWnJsKTodSqJX6BCXPGq","2SokukFYxLzFb8vrexBNJv","6jzsqIT7QrlFAzrvjyrAhE","6C55uA5rltMxsALpuQs8WW","2X2XB177C75v49QPYYeLGo"
foreach($playlist in $playlists) {
    (Invoke-RestMethod "http://localhost/spotify/playlist/delete/$playlist`?access_token=$at" -Method Delete)
}



$d = (Invoke-RestMethod "http://localhost/spotify/playlist/delete/0vMa3bNF3eUUM2ob3dZtvB?access_token=$at" -Method Delete)
$d

Invoke-RestMethod "http://localhost/spotify/user/profile?access_token=$at"


$track = "6p8eFfPw3nQkf37aT3AkmK"
$plst = "3Q0TPrX8wilm5zKKOPhYIo"

$rm = (Invoke-RestMethod -Method PUT -Uri "http://localhost/spotify/playlist/add/$plst/$track`?access_token=$at"); $rm