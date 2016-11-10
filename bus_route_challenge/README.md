# Bus Route Challenge

### Problem

We are adding a new bus provider to our system. In order to implement a very
specific requirement of this bus provider our system needs to be able to filter
direct connections. We have access to a weekly updated list of bus routes
in form of a **bus route data file**. As this provider has a lot of long bus
routes, we need to come up with a proper service to quickly answer if two given
stations are connected by a bus route.


### Task

The bus route data file provided by the bus provider contains a list of bus
routes. These routes consist of an unique identifier and a list of stations
(also just unique identifiers). A bus route **connects** its list of stations.

> unikalnosci nazw linii

Your task is to implement a micro service which is able to answer whether there
is a bus route providing a direct connection between two given stations. *Note:
The station identifiers given in a query may not be part of any bus route!*

> id stacji moze nie wystepowac w zadnej linii!

### Bus Route Data

The first line of the data gives you the number **N** of bus routes, followed by
**N** bus routes. For each bus route there will be **one** line containing a
space separated list of integers. This list contains at least three integers. The
**first** integer represents the bus **route id**. The bus route id is unique
among all other bus route ids in the input. The remaining integers in the list
represent a list of **station ids**.
A station id may occur in multiple bus
routes, but can never occur twice within the same bus route.

> VALIDATORY:
- schema
- unikalnosc stacji w linii


> LIMITY

You may assume 100,000 as upper limit for the number of bus routes, 1,000,000 as
upper limit for the number of stations, and 1,000 as upper limit for the number
of station of one bus route. Therefore, your internal data structure should
still fit into memory on a suitable machine.

> dane w pamieci powinny sie zmiescic

*Note: The bus route data file will be a local file and your service will get
the path to file as the first command line argument. Your service will get
restarted if the file or its location changes.*

> plik ladowany przy starcie uslugi, jesli sie zmieni, powinna sie zrestartowac (watch?)
> obsluga sytuacji, gdy sciezka jest niepoprawna

### REST API

Your micro service has to implement a REST-API supporting a single URL and only
GET requests. It has to serve
`http://localhost:8088/api/direct?dep_sid={}&arr_sid={}`. The parameters
`dep_sid` (departure) and `arr_sid` (arrival) are two station ids (sid)
represented by 32 bit integers.

> poprawic api > api/routes?departureStation.id={}&arrivalStation.id
> parametry to Stringi z konwersja to Integera (unsigned?)

The response has to be a single JSON Object:

```
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "properties": {
    "dep_sid": {
      "type": "integer"
    },
    "arr_sid": {
      "type": "integer"
    },
    "direct_bus_route": {
      "type": "boolean"
    }
  },
  "required": [
    "dep_sid",
    "arr_sid",
    "direct_bus_route"
  ]
}
```

> poprawic API:
- integery na stringi
- tablica routes
- filtr na routes.type=DIRECT

{
    "departureStation": {
      "id": ""
    },
    "arrivalStation": {
      "id": ""
    },
    "routes":[
        {
          "route":  {
            "id": "",
            "type": "DIRECT"
          }
        }
    ]
}

The `direct_bus_route` field has to be set to `true` if there exists a bus route
in the input data that connects the stations represented by `dep_sid` and
`arr_sid`. Otherwise `direct_bus_route` must be set to `false`.




### Example Data

Bus Routes Data File:
```
3
0 0 1 2 3 4
1 3 1 6 5
2 0 6 4
```

Query:
````
http://localhost:8088/api/direct?dep_sid=3&arr_sid=6
```

Response:
```
{
    "dep_sid": 3,
    "arr_sid": 6,
    "direct_bus_route": true
}
```

> jak powinno zadzialac 1 3 >
to zalezy, bo jesli jest cykl to nie ale jest nie jest to tak

### Implementation

Please implement your solution in Java, preferably Java 8. We expect you to
demonstrate best practices for general software development. Feel free to use
helpful open source libraries if applicable. We will evaluate your source code
as well as the functionality and compliance of the application.

> java 8, gradle

### Packaging

Done with the fun part, the implementation? We have a few more requirements
for you that might sound boring but help us a lot in testing your code. Before you
send us your solution, package it.

Your micro service must contain two simple bash scripts in the top level
repository directory.

- `build.sh`: builds your project. This could be as simple as:
  ```
  #!/bin/bash
  mvn clean package
  ```

  We will execute this on an **Ubuntu 16.04** docker container with installed:

  ```
  openjdk-8-jdk
  maven
  gradle
  ```

>  build.sh > ./gradlew build

- `service.sh`: starts / stops your micro service. Accepts `start|stop|block`
  and the path to a **bus routes data file** as arguments (`bash service.sh
  start FILE`). After your micro service got started it shall answer queries
  until it is terminated. Please use the template provided in the `template`
  sub-directory. Feel encouraged to improve the script. The least you have to do
  is to specify `RUN` and `NAME`. Something like this:

  ```
  …

  RUN="java -jar my-fancy-fat-jar.jar"
  NAME=my-awesome-bus-route-service
  
  …
  ```
  
  *Note: as stated above `service.sh` must be located in the top level directory
  of your repository.

> service.sh > bash service.sh start FILE
> root directory
> remove template

### Shipping

The preferred option is a link to a **git** repository. GitHub, GitLab,
Bitbucket or your self hosted git. As long as `git clone LINK` works we're
happy. If you are not able to share a git repository, e.g. for privacy reasons,
please share a download link for a **zip** file. This zip file should contain a
single folder that contains your project. The content of the zip file should
look like this:
```
project_folder
├── src
├── build.gradle/pom.xml
├── build.sh
└── service.sh
```


### Quick Smoke Test

*Note: This smoke test only checks for compliance, not for correctness!*

We will run some tests on your implementation, and because we are a friendly
bunch of developers, we share a (simplified) version of what we run. There 
are some bash scripts located in the `tests/` directory:
```
build_docker_image.sh
run_test_docker.sh
run_test_local.sh
simple_test.sh
```

Assuming a `bash` environment, you can do a quick local test:
```
bash build.sh
cd tests/
bash run_test_local.sh ../service.sh
```
This should output:
```
TEST PASSED!
```

Given a running `docker` installation and a UNIX-like environment you can run:
```
cd tests/
bash build_docker_image.sh YOUR_GIT_REPO_URL|ZIP_FILE
bash run_test_docker.sh
```
This should output:
```
TEST PASSED!
```



*Note: The docker based test assumes your running native docker. If not (e.g.
your on OSX) please adopt the `run_test_docker.sh` file and replace `localhost`
with the IP of your docker VM*

> moznaby przekazywac IP dockera do skryptu