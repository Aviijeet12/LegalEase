# Start all emulators
firebase emulators:start --import=./emulator-data --export-on-exit

# Initialize test data (optional)
# firebase emulators:exec './scripts/init-test-data.js'