#!/bin/bash

# exit if the current directory isn't client-ios
if [ ! "${PWD##*/}" == "client-ios" ]; then
  if [ -d "client-ios" ]; then
    cd client-ios
  else
    echo "❌ ERROR: This script must be run from the client-ios directory."
    exit
  fi
fi

echo "🏁 Running client-ios project bootstrap..."
echo ""

# Touch Shared.swift so it can be added to the project by xcodegen later. This fixes
# an issue where MokoKSwift files are generated in different locations for different
# versions of Xcode (as well as building for a physical device).
if [ ! -d "Provbit/MokoKSwift" ]; then
  mkdir -p "Provbit/MokoKSwift"
  touch "Provbit/MokoKSwift/Shared.swift"
fi

# run mint's bootstrap
mint bootstrap

# run chimney
mint run chimney chimney setup
mint run chimney generate -o "Provbit/Application/Support/ProvbitKeys.swift"

# run xcodegen
mint run xcodegen

echo ""
echo "🎉 Bootstrap success."
