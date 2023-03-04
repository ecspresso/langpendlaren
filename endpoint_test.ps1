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

# OK Spotify - authenticate [GET/spotify/login/refresh?refresh_token={refresh token}]
$a = (Invoke-RestMethod http://localhost:8080/spotify/login/refresh?refresh_token=$r); $at = $a.access_token.value # förnya access token kräver ?refresh_token=
$a | ConvertTo-Json -Depth 100

# OK Spotify - hämta användares profil [GET/spotify/user/profile?access_token={access token}]
(Invoke-RestMethod http://localhost:8080/spotify/user/profile?access_token=$at) | convertto-json -Depth 100 # user profile

# OK Spotify - hämta genrer[GET/spotify/genre/seeds?access_token={access token}]
(Invoke-RestMethod http://localhost:8080/spotify/genre/seeds?access_token=$at) | convertto-json # seeds

# OK Spotify - hämta spellista [GET/spotify/playlist/{pId}?access_token={access token}]
$playlistId = "1wwa8ZWAYg7FohkFUqxcnb"
(Invoke-RestMethod "http://localhost:8080/spotify/playlist/$($playlistId)?access_token=$($at)}" -Method GET) | ConvertTo-Json

# OK Spotify - skapa spellista [POST/spotify/playlist?access_token={access token}]
$body = @{name="emile";description="kaffe"} |ConvertTo-Json
$body = @{name="emile"} |ConvertTo-Json
$body = @{description="kaffe"} |ConvertTo-Json
Remove-Variable body
(Invoke-RestMethod "http://localhost:8080/spotify/playlist?access_token=$($at)" -Method POST -body $body) | ConvertTo-Json

# OK Spotify - radera spellista [DELETE/spotify/playlist/{id}?access_token={access token}]
$playlistId = "1wwa8ZWAYg7FohkFUqxcnb"
(Invoke-RestMethod "http://localhost:8080/spotify/playlist/$($playlistId)?access_token=$($at)}" -Method DELETE) | ConvertTo-Json

# OK Spotify - lägg till låt [PUT/spotify/playlist/{pid}?access_token={access token}]
$playlistId = "1wwa8ZWAYg7FohkFUqxcnb"
$trackId = "4ZbGXds397GVZFRdv9nDxf"
$body = @{trackId = $trackId} | ConvertTo-Json
(Invoke-RestMethod "http://localhost:8080/spotify/playlist/$($playlistId)?access_token=$($at)" -Method PUT -body $body) | ConvertTo-Json

# OK Spotify - ta bort spellista [DELETE/spotify/playlist/{pid}?access_token={access token}]
$playlistId = "1wwa8ZWAYg7FohkFUqxcnb"
(Invoke-RestMethod "http://localhost:8080/spotify/playlist/$($playlistId)?access_token=$($at)" -Method DELETE)


# IOK Spotify - ta bort låtar från spellista [DELETE/spotify/track/{pid}/{tid}?access_token={access token}]
$playlistId = "1wwa8ZWAYg7FohkFUqxcnb"
$trackId = "4ZbGXds397GVZFRdv9nDxf"
(Invoke-RestMethod "http://localhost:8080/spotify/playlist/track/$($playlistId)/$($trackId)?access_token=$($at)" -Method DELETE)

# Spotify - /spotify/search/track/{type} OK MEN UPPDATERA DOC
$type = "swedish"
(Invoke-RestMethod "http://localhost:8080/spotify/search/track/$($type)?access_token=$($at)}" -Method GET)
$tracks = (Invoke-RestMethod "http://localhost:8080/spotify/search/track/$($type)?access_token=$($at)}" -Method GET)
$tracks.tracks.items.id