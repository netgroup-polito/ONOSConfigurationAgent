# ONOS CONFIGURATION AGENT

This module is a Web Service that is in charge of mediate between one or more service layers and the (one or more) Java/ONOS applications that will be orchestrated.

## Installing and Running

1. If you want to use the DoubleDecker service, power up a DoubleDecker broker.
2. Deploy the web service.
3. Power on the ONOS Application.

You are now able to reach the web service through the APIs, and orchestrate each Application is attached to it.

APIs are:
- GET /{AppId}/{varId}
- POST /{AppId}/{varId}
- DELETE /{AppId}/{varId}

where the {varId} is the path from the root of the DataModel to the variable you want to indicate (e.g. /rootVar/containerVar/listVar[index]/leafVar)
