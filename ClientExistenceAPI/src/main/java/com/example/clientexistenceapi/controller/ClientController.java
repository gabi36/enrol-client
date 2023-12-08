package com.example.clientexistenceapi.controller;

import com.example.clientexistenceapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/check-client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * GET /{cnp} : Call for checking if the client already exists
     *
     * @param cnp cnp of the user
     * @return boolean value if the client already exists or not (status code 200)
     * or Internal server error; Failed to check if the client already exists (status code 500)
     */
    @Operation(
            summary = "Call for checking if the client already exists",
            responses = {
                    @ApiResponse(responseCode = "200", description = "check if the client already exists", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Boolean.class)))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error; Failed to check client", content = {
                            @Content(mediaType = "application/json")
                    })
            }
    )
    @GetMapping("/{cnp}")
    public Boolean checkClient(@PathVariable long cnp){
        return clientService.clientExistsByCNP(cnp);
    }
}
