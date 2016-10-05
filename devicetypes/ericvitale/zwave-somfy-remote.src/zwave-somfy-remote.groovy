/**
 *  Z-Wave Somfy Remote
 *
 *  Version 1.0.0 - Initial Release (10/5/2016)
 *
 *  Copyright 2016 ericvitale@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * You can find this device handler @ https://github.com/ericvitale/ST-Z-Wave-Somfy-Remote/
 * You can find my other device handlers & SmartApps @ https://github.com/ericvitale
 *
 */
metadata {
	definition (name: "Z-Wave Somfy Remote", namespace: "ericvitale", author: "ericvitale@gmail.com") {
        capability "Actuator"
		capability "Button"
		capability "Configuration"
		capability "Sensor"
        
		attribute "numButtons", "STRING"
        attribute "buttonOneLastActivity", "string"
        attribute "buttonTwoLastActivity", "string"
        
        fingerprint mfr: "026E", prod: "5643", model: "5A31"
        fingerprint deviceId: "0x0101", inClusters: "0x5E,0x80,0x72,0x59,0x85,0x5A,0x86,0x84", outClusters: "0x20,0x82,0x5B,0x26", deviceJoinName: "Z-Wave Somfy Remote"
	}

    preferences {
        input "logging", "enum", title: "Log Level", required: false, defaultValue: "INFO", options: ["TRACE", "DEBUG", "INFO", "WARN", "ERROR"]
    }

    tiles(scale: 2) {
        valueTile("ButtonOne", "device.button1", width: 6, height: 2) {
        	state "default", label: 'Button 1 was ${currentValue}.'
        }
        
        valueTile("ButtonOneActivity", "device.buttonOneLastActivity", width: 6, height: 2) {
        	state "default", label: '${currentValue}'
        }
        
        valueTile("ButtonTwo", "device.button2", width: 6, height: 2) {
        	state "default", label: 'Button 2 was ${currentValue}.'
        }
        
        valueTile("ButtonTwoActivity", "device.buttonTwoLastActivity", width: 6, height: 2) {
        	state "default", label: '${currentValue}'
        }
        
        main "ButtonOne", "ButtonOneActivity", "ButtonTwo", "ButtonTwoActivity"
        details "ButtonOne", "ButtonOneActivity", "ButtonTwo", "ButtonTwoActivity"
	}
}

def installed() {
	log("Device Installed.", "INFO")
    initialization()
}

def updated() {
	log("Device Updated.", "INFO")
    initialization()
}

def initialization() {
	log("Log level selected = ${logging}.", "INFO")
    createEvent(name: "numButtons", value: "2", source: "DEVICE") 
}

def buttonEvent(buttonNumber, buttonAction) {
	def button = buttonNumber as Integer
    
    switch(button) {
    	case 1:
        	updateButtonOneLastActivity(new Date())
            break
        case 2:
        	updateButtonTwoLastActivity(new Date())
            break
        default:
        	break
    }
    
	switch(buttonAction) {
    	case "pushed":
        	log("Button #${button} was pushed.", "INFO")
            createEvent(name: "button", value: "pushed", data: [buttonNumber: button], source: "DEVICE", descriptionText: "$device.displayName button $button was pushed", isStateChange: true)
            break
    	case "held":
        	log("Button #${button} was held.", "INFO")
            createEvent(name: "button", value: "held", data: [buttonNumber: button], source: "DEVICE", descriptionText: "$device.displayName button $button was held", isStateChange: true)
            break
        case "released":
        	log("Button #${button} was released.", "INFO")
            createEvent(name: "button", value: "released", data: [buttonNumber: button], source: "DEVICE", descriptionText: "$device.displayName button $button was released", isStateChange: true)
            break
        default:
        	log("Unknown Event on Button #${button}.", "WARN")
    }
}

def determinePress(button, action) {
    switch(action) {
    	case "0":
        	log("Button ${button} Pressed", "DEBUG")
            buttonEvent(button, "pushed")
            break
        case "1":
        	log("Button ${button} Held and Released", "DEBUG")
            buttonEvent(button, "released")
            break
        case "2":
        	log("Button ${button} Held", "TRACE")
            buttonEvent(button, "held")
            break
        default:
        	log("Unknown Press on Button ${button}", "WARN")
    }  
}

def updateButtonOneLastActivity(lastActivity) {
	def finalString = lastActivity?.format('MM/d/yyyy hh:mm a',location.timeZone)    
	sendEvent(name: "buttonOneLastActivity", value: finalString, source: "DEVICE")
}

def updateButtonTwoLastActivity(lastActivity) {
	def finalString = lastActivity?.format('MM/d/yyyy hh:mm a',location.timeZone)    
	sendEvent(name: "buttonTwoLastActivity", value: finalString, source: "DEVICE")
}

def parse(String description) {
	log("Parse <><> ${description}", "DEBUG")
	def result = null
	def cmd = zwave.parse(description)
	if (cmd) {
		result = createEvent(zwaveEvent(cmd))
	}
	return result
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
    log("Unhandled Z-Wave Command: ${cmd}", "WARN")
	[:]
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
	log("Scene Notification: ${cmd}", "DEBUG")
    log("keyAttributes = ${cmd.keyAttributes}", "DEBUG")
    log("sceneNumber = ${cmd.sceneNumber}", "DEBUG")
    determinePress(cmd.sceneNumber, cmd.keyAttributes)
}

/************ Begin Logging Methods *******************************************************/

def determineLogLevel(data) {
    switch (data?.toUpperCase()) {
        case "TRACE":
            return 0
            break
        case "DEBUG":
            return 1
            break
        case "INFO":
            return 2
            break
        case "WARN":
            return 3
            break
        case "ERROR":
        	return 4
            break
        default:
            return 1
    }
}

def log(data, type) {
    data = "Somfy-Remote -- v${dhVersion()} --  ${data ?: ''}"
        
    if (determineLogLevel(type) >= determineLogLevel(settings?.logging ?: "INFO")) {
        switch (type?.toUpperCase()) {
            case "TRACE":
                log.trace "${data}"
                break
            case "DEBUG":
                log.debug "${data}"
                break
            case "INFO":
                log.info "${data}"
                break
            case "WARN":
                log.warn "${data}"
                break
            case "ERROR":
                log.error "${data}"
                break
            default:
                log.error "Somfy-Remote -- Invalid Log Setting"
        }
    }
}

def dhVersion() { return "1.0.0" }

/************ End Logging Methods *********************************************************/