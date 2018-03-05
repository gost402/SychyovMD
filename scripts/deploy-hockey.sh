#!/bin/sh

# upload apk to hockey app
curl \
-F "status=2" \
-F "notify=0" \
-F "ipa=@app/build/outputs/apk/release/app-release.apk" \
-H "X-HockeyAppToken: f82e4ebfbd354bcf8797fa018cc4144e" \
https://rink.hockeyapp.net/api/2/apps/1a44ecbec3324f39b2e6b2dd14047154/app_versions/upload