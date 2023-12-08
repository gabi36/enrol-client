package com.example.clientreputationapi.controller;

import com.example.clientreputationapi.service.ClientReputationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client-reputation")
public class ClientReputationController {

    private final ClientReputationService clientReputationService;

    public ClientReputationController(ClientReputationService clientReputationService) {
        this.clientReputationService = clientReputationService;
    }

    /**
     * GET /{cnp} : Call for getting the client reputation
     *
     * @param cnp cnp of the user
     * @return client reputation(status code 200)
     * or Internal server error; Failed to get the client reputation (status code 500)
     */
    @Operation(
            summary = "Call for getting the client reputation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "get the client reputation", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = double.class)))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error; Failed to get the client reputation", content = {
                            @Content(mediaType = "application/json")
                    })
            }
    )
    @GetMapping("/{cnp}")
    public double clientReputation(@PathVariable long cnp){
        return clientReputationService.clientReputationFormula(cnp);
    }
}
