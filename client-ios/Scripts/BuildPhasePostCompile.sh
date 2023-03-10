#!/bin/bash

### Move Moko KSwift generated files into a static location.
echo "🚚 Moving Moko KSwift files..."

# The location of the static file.
moko_kswift_shared_file_location=$SRCROOT/Provbit/MokoKSwift/Shared.swift

# Find the most recently generated swift file in the build folder.
moko_kswift_export=`find $SRCROOT/../shared/build/xcode-frameworks -type f \( -name "*.swift" \) -print0 \
    | xargs -0 stat -f "%m %N" \
    | sort -rn | head -1 | cut -f2- -d" "`

echo "Copying file $moko_kswift_export to $moko_kswift_shared_file_location"

# Copy the file into the new location. We want to copy, instead of move, so that the Moko KSwift
# generated file remains for future executions of this script.
cp $moko_kswift_export $moko_kswift_shared_file_location

echo "✅ Successfully moved the Moko KSwift files"
