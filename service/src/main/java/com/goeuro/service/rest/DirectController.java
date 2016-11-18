package com.goeuro.service.rest;

import com.codahale.metrics.annotation.Timed;
import com.goeuro.api.model.DirectRouteInformationResponse;
import com.goeuro.router.Router;
import com.goeuro.service.exception.InvalidStationIdException;
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

@RestController
public class DirectController {
    private static final Logger logger = LoggerFactory.getLogger(DirectController.class);

    private final Router router;

    @Autowired
    public DirectController(Router router) {
        this.router = router;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/direct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Timed
    @ApiOperation(value = "getDirectRouteInformation", nickname = "getDirectRouteInformation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = DirectRouteInformationResponse.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public DirectRouteInformationResponse getDirectRouteInformation(
            @RequestParam("dep_sid") String depSid,
            @RequestParam("arr_sid") String arrSid
    ) throws InvalidStationIdException {
        logger.info("Get direct route information from dep_sid: \"{}\" to arr_sid: \"{}\"", depSid, arrSid);

        try {
            int departureStationId = Integer.parseInt(depSid);
            int arrivalStationId = Integer.parseInt(arrSid);

            boolean direct = router.isDirectRoute(departureStationId, arrivalStationId);

            return DirectRouteInformationResponse.DirectRouteInformationResponseBuilder.aDirectRouteInformation()
                    .withDepartureStationId(departureStationId)
                    .withArrivalStationId(arrivalStationId)
                    .withDirectBusRoute(direct)
                    .build();
        } catch (NumberFormatException e) {
            logger.info("Unable to parse station id. {}", e);
            throw new InvalidStationIdException("Unable to parse station id");
        }
    }
}
