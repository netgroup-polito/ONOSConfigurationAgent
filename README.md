# ONOS CONFIGURATION AGENT

This module is a Web Service that is in charge of mediate between one or more service layers and the (one or more) Java/ONOS applications that will be orchestrated.

## Getting Started

### Prerequisites

No dependency should be installed.

####Installing

If you want, you can use the DoubleDecker service, in this case you need to have a DoubleDecker broker, and put the tenant keys in the keys.json file (that you can find in the package resources/files). You should also change eventually the address where to find the ddbroker (default: localhost:5555) in the file ConnectionModule.java (line 59).

#### RUN

You just have to deploy the web service.

Only after that you have to run the SDN Application with the StateListener.

You are now able to reach the web service through the APIs, and orchestrate each Application is attached to it.

APIs are:
- GET /{AppId}/DM
- GET /{AppId}/{varId}
- POST /{AppId}/{varId}
- DELETE /{AppId}/{varId}

where DM is the Data Model, and the {varId} is the path from the root of the DataModel to the variable you want to indicate (e.g. /rootVar/containerVar/listVar[index]/leafVar)
