function auth() {
    Import-Module C:\Users\emile\OneDrive\Documents\PowerShell\Modules\Selenium
    if($password -eq $null) {$script:password = Read-Host "password"}

    $driver = Start-SeFirefox -StartURL (Invoke-RestMethod http://localhost:8080/spotify/login).spotify_auth_uri
    Send-SeClick -Element (Get-SeElement -By CssSelector -Selection '[data-testid="facebook-login"]' -Target $driver)
    Send-SeClick -Element (Get-SeElement -By CssSelector -Selection '[title="Only allow essential cookies"]' -Target $driver)
    Send-SeKeys -Keys "abc@ecspresso.com"  -Element (Get-SeElement -By CssSelector -Selection "#email" -Target $driver)
    Send-SeKeys -Keys $script:password -Element (Get-SeElement -By CssSelector -Selection "#pass" -Target $driver)
    Send-SeClick -Element (Get-SeElement -By CssSelector -Selection "#loginbutton" -Target $driver)
    
    $script:r = (Get-SeElement -By CssSelector -Selection "#\/refresh_token\/value td:nth-child(2) span span" -Target $driver).Text -replace '"',''
    $script:at = (Get-SeElement -By CssSelector -Selection "#\/access_token\/value td:nth-child(2) span span" -Target $driver).Text -replace '"',''
    Stop-SeDriver -Target $driver
}

auth

# Spotify - authenticate [GET/spotify/login/refresh?refresh_token={refresh token}] OK
$a = (Invoke-RestMethod http://localhost:8080/spotify/login/refresh?refresh_token=$r); $at = $a.access_token.value # förnya access token kräver ?refresh_token=
$a | ConvertTo-Json -Depth 100

# Spotify - hämta användares profil [GET/spotify/user/profile?access_token={access token}] OK
(Invoke-RestMethod http://localhost:8080/spotify/user/profile?access_token=$at) | convertto-json -Depth 100 # user profile

# Spotify - hämta genrer[GET/spotify/genre/seeds?access_token={access token}] OK
(Invoke-RestMethod http://localhost:8080/spotify/genre/seeds?access_token=$at) | convertto-json # seeds

# Spotify - hämta spellista [GET/spotify/playlist/{pId}?access_token={access token}] OK
$playlistId = "2dz2WV3hixHDmIKnMXLs1K"
(Invoke-RestMethod "http://localhost:8080/spotify/playlist/$($playlistId)?access_token=$($at)}" -Method GET) | ConvertTo-Json

# Spotify - skapa spellista [POST/spotify/playlist?access_token={access token}]
$body = @{name="emile";description="kaffe"} |ConvertTo-Json
$body = @{name="emile"} |ConvertTo-Json
$body = @{description="kaffe"} |ConvertTo-Json
Remove-Variable body
(Invoke-RestMethod "http://localhost:8080/spotify/playlist?access_token=$($at)" -Method POST -body $body) | ConvertTo-Json

# Spotify - radera spellista [DELETE/spotify/playlist/{id}?access_token={access token}]
$playlistId = "2dz2WV3hixHDmIKnMXLs1K"
(Invoke-RestMethod "http://localhost:8080/spotify/playlist/$($playlistId)?access_token=$($at)}" -Method DELETE) | ConvertTo-Json

# Spotify - lägg till låt [PUT/spotify/playlist/{pid}?access_token={access token}]
(Invoke-RestMethod "http://localhost:8080/spotify/playlist/?access_token=$($at)}" -Method POST -body $body) | ConvertTo-Json

# Spotify - ta bort spellista [DELETE/spotify/playlist/{pid}?access_token={access token}]


# Spotify - ta bort låtar från spellista [DELETE/spotify/track/{pid}/{tid}?access_token={access token}]




# Skapa spellista


# Hämta? Ska inte finnas verkar det som
(Invoke-RestMethod http://localhost:8080/spotify/search/playlist/pop?access_token=$at)
$pl = (Invoke-RestMethod http://localhost:8080/spotify/search/playlist/pop?access_token=$at)
$pl | select next, previous





















# Ska inte heller finnas?
$plof = (Invoke-RestMethod "http://localhost:8080/spotify/search/playlist/pop?offset=10&access_token=$at")
$plof | select next, previous


# Rensa något?
$playlists = "7FTgAwVwH6gTuEWPVwxnEx","6MuZEElHAAwMQEKn0AUV3y","6EHQm1IIdDi9lJ5mFObBsg","0HjeOtM5RaMaMIGZJ1TMzK","2bO3c30nB4obd7b20a20hJ","77PPFLByKqhK1s6V7iDsob","3FMe1rK4iNLKax4EAoRhwN","3FMe1rK4iNLKax4EAoRhwN","4jQI3Xo4EmGoQ1561AfRHA","2fU25sAYX1KdmhUXWsapg2","6NXwvo3bJr8p7dI4ntLFaf","5QFWnJsKTodSqJX6BCXPGq","2SokukFYxLzFb8vrexBNJv","6jzsqIT7QrlFAzrvjyrAhE","6C55uA5rltMxsALpuQs8WW","2X2XB177C75v49QPYYeLGo"
foreach($playlist in $playlists) {
    (Invoke-RestMethod "http://localhost:8080/spotify/playlist/delete/$playlist`?access_token=$at" -Method Delete)
}


# Ta bort spellista?
$d = (Invoke-RestMethod "http://localhost:8080/spotify/playlist/delete/0vMa3bNF3eUUM2ob3dZtvB?access_token=$at" -Method Delete)
$d

# ???
Invoke-RestMethod "http://localhost:8080/spotify/user/profile?access_token=$at"

# ???
$track = "7AUVdpcqbxLSCOQqKHjPx7"
$plst = "0WdNC7mrRWmyUpXpbEbFjk"
$body = @{trackId="$track"} | ConvertTo-Json

# ???
(Invoke-RestMethod -Method DELETE -Uri "http://localhost:8080/spotify/playlist/track/$plst/$track/?access_token=$at") | convertto-json

$t = (Invoke-RestMethod "http://localhost:8080/spotify/search/track/pop?access_token=$at")
$t | ConvertTo-Json -Depth 100

(Invoke-RestMethod "http://localhost:8080/spotify/user/profile?access_toke=$at")




(Invoke-RestMethod "http://localhost:8080/spotify/playlist/7vCaVSzfVoKxl1FbRupxux?access_token=$at" -Method Delete) | ConvertTo-Json