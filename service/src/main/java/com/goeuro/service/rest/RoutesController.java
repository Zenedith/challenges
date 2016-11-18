package com.goeuro.service.rest;

import com.codahale.metrics.annotation.Timed;
import com.goeuro.api.model.RouteResponse;
import com.goeuro.api.model.RouteType;
import com.goeuro.api.model.RoutesCollection;
import com.goeuro.api.model.StationRef;
import com.goeuro.importer.core.Importer;
import com.goeuro.router.Router;
import com.goeuro.service.exception.InvalidStationIdException;
import com.goeuro.service.exception.RoutesUnavailableException;
import com.goeuro.service.rest.validator.StationIdValidator;
import com.goeuro.storage.core.model.ConnectionType;
import com.google.common.base.Enums;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class RoutesController {
    private static final Logger logger = LoggerFactory.getLogger(RoutesController.class);

    private final Router router;
    private final Importer importer;
    private final StationIdValidator stationIdValidator;

    @Autowired
    public RoutesController(Router router, Importer importer, StationIdValidator stationIdValidator) {
        this.router = router;
        this.importer = importer;
        this.stationIdValidator = stationIdValidator;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/routes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Timed
    @ApiOperation(value = "getRoutes", nickname = "getRoutes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RoutesCollection.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public RoutesCollection getRoutes(
            @RequestParam("departureStation.id") String depSid,
            @RequestParam("arrivalStation.id") String arrSid,
            @RequestParam(value = "route.type", required = false) String filterByType
    ) throws InvalidStationIdException {
        logger.info("Get routes from dep_sid: \"{}\" to arr_sid: \"{}\"", depSid, arrSid);

        if (importer.getImports().isEmpty()) {
            throw new RoutesUnavailableException();
        }

        if (!stationIdValidator.validate(depSid)) {
            throw new InvalidStationIdException("Invalid station id: " + depSid);
        }
        if (!stationIdValidator.validate(arrSid)) {
            throw new InvalidStationIdException("Invalid station id: " + arrSid);
        }

        int departureStationId = Integer.parseInt(depSid);
        int arrivalStationId = Integer.parseInt(arrSid);

        ConnectionType filterRouteType = Optional.ofNullable(filterByType)
                .map(val -> Enums.getIfPresent(ConnectionType.class, val).orNull())
                .orElse(null);

        List<RouteResponse> routes = Optional.of(router)
                .map(router -> router.isConnection(departureStationId, arrivalStationId))
                .filter(connectionType -> filterRouteType(connectionType, filterRouteType))
                .map(this::connectionTypeToRouteType)
                .map(this::buildRoute)
                .orElse(Collections.emptyList());

        return RoutesCollection.RoutesCollectionBuilder.aRoutesCollection()
                .withDepartureStation(StationRef.StationRefBuilder.aStationRef().withId(depSid).build())
                .withArrivalStation(StationRef.StationRefBuilder.aStationRef().withId(arrSid).build())
                .withRoutes(routes)
                .build();
    }

    private RouteType connectionTypeToRouteType(ConnectionType connectionType) {
        return Enums.getIfPresent(RouteType.class, connectionType.name()).orNull();
    }

    private boolean filterRouteType(ConnectionType connectionType, ConnectionType filterByConnectionType) {
        return filterByConnectionType == null || connectionType.equals(filterByConnectionType);
    }

    private List<RouteResponse> buildRoute(RouteType routeType) {
        return ImmutableList.of(
                RouteResponse.RouteResponseBuilder.aRouteResponse()
                        .withId("should be route id")
                        .withType(routeType)
                        .build()
        );
    }
}
