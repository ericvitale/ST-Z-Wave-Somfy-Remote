# ST-Z-Wave-Somfy-Remote

## Summary
This SmartThings device handler is meant to be used with the Somfy remote that is shipped with both the Bali Autoview and Garber Virtual Cord brands.

## Installation via GitHub Integration
1. Open SmartThings IDE in your web browser and log into your account.
2. Click on the "My Device Handlers" section in the navigation bar.
3. Click on "Settings".
4. Click "Add New Repository".
5. Enter "ericvitale" as the namespace.
6. Enter "ST-Z-Wave-Somfy-Remote" as the repository.
7. Hit "Save".
8. Select "Update from Repo" and select "ST-Z-Wave-Somfy-Remote".
9. Select "z-wave-somfy-remote.groovy".
10. Check "Publish" and hit "Execute".

## Manual Installation (if that is your thing)
1. Open SmartThings IDE in your web browser and log into your account.
2. Click on the "My Device Handlers" section in the navigation bar.
3. On your Device Handlers page, click on the "+ Create New Device Handler" button on the right.
4. On the "New Device Handler" page, Select the Tab "From Code" , Copy the "z-wave-somfy-remote.groovy" source code from GitHub and paste it into the IDE editor window.
5. Click the blue "Create" button at the bottom of the page. An IDE editor window containing device handler template should now open.
6. Click the blue "Save" button above the editor window.
7. Click the "Publish" button next to it and select "For Me". You have now self-published your Device Handler.

## Pairing the Device
This device is a bit interesting to get it to pair. I've seen a few scenarios happen. Here is what I generally do:
1. Run the device exclusion process in SmartThings, do this even if you have never paired this with SmartThings.
2. Press and hold the reset button on the back of the device with a pin until the light on the front of the device begins to flash. Wait for SmartThings to confirm the device in excluded.
3. Run the device discovery process within SmartThings
4. Press and hold the reset button on the back of the device with a pin until the light on the front of the device begins to flash. Wait for SmartThings to find the device.
5. Continue with the normal setup.
6. If the device does not pair and auto select this device handler, switch it to this device handler in the device settings.
