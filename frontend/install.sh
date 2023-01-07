electron-packager . frontend --overwrite --asar --platform=linux --arch=x64  --prune=true --out=release-builds
electron-packager . --overwrite --platform=darwin --arch=x64 --prune=true --out=release-builds
electron-packager . frontend --overwrite --asar --platform=win32 --arch=x64 --prune=true --out=release-builds --version-string.CompanyName=CE --version-string.FileDescription=CE --version-string.ProductName="Frontend"
