package org.befikreyatra.controller;

import org.befikreyatra.dto.ResponseStructure;
import org.befikreyatra.dto.TripRequest;
import org.befikreyatra.dto.TripResponse;
import org.befikreyatra.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/trip")
public class TripController {

    @Autowired
    private TripService tripService;


    @PostMapping("/{busId}")
    public ResponseEntity<ResponseStructure<TripResponse>> createTrip(@PathVariable int busId , @RequestBody TripRequest tripRequest){
        return tripService.craeteTrip(busId,tripRequest);
    }
    @GetMapping("/bus/{busId}")
    public ResponseEntity<ResponseStructure<List<TripResponse>>> getTripsByBus(@PathVariable int busId) {
        return tripService.getTripByBus(busId);
    }
}
