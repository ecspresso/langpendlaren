$u = (Invoke-RestMethod http://localhost/spotify/login).spotify_auth_uri
$u | clip

$at = Get-Clipboard
$r  = Get-Clipboard

(Invoke-RestMethod http://localhost/spotify/user/profile/$at) # user profile
(Invoke-RestMethod http://localhost/spotify/genre/seeds) | convertto-json # seeds

(Invoke-RestMethod http://localhost/spotify/authenticated) # skaffa access token kräver ?code=

$a = (Invoke-RestMethod http://localhost/spotify/login/refresh?refresh_token=$r) # förnya access token kräver ?refresh_token=
$at = $a.accessToken.value
$a | ConvertTo-Json -Depth 100


$body = @{name="emile";description="kaffe"} |ConvertTo-Json
$body = @{name="emile"} |ConvertTo-Json
Remove-Variable body
(Invoke-RestMethod http://localhost/spotify/playlist/create?access_token=$at -Method POST -body $body)