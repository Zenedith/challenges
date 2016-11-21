# GoEuro coding challenges

My solution for coding challenge described as follows.

## Problem

We are adding a new bus provider to our system. In order to implement a very
specific requirement of this bus provider our system needs to be able to filter
direct connections. We have access to a weekly updated list of bus routes
in form of a **bus route data file**. As this provider has a lot of long bus
routes, we need to come up with a proper service to quickly answer if two given
stations are connected by a bus route.


## Some of my assumptions:
- routes can't have a loop (starting bus stop can't be the last one)
- service is prepared for provide new storage and import components - is easy to change memory storage to eg. elastic search or changing plain text file importer to eg. json/xml

### Features:
- java 8 + gradle + spring boot
- spock unit tests and integration tests
- used findbugs and sonarqube plugin
- easy configuration based on application.yaml file
- additional REST API endpoints: `/api/imports` (information about imports and changes) and `/api/routes` (my routes API design)
- swagger support: `/swagger-ui.html` (UI)
- monitoring `/metrics` (default instance metrics + custom ones, mainly for importing process)

### Improvements:
- memory & performance should be improved
- add another storage type like elastic search
