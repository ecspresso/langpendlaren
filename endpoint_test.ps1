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
$at = $a.accessToken.value
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

