package com.example.enrolclientapi.controller;

import com.example.enrolclientapi.dto.ClientDTO;
import com.example.enrolclientapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * POST: Call for generating enrolment or denial document for a new or existing client
     *
     * @param clientDTO information of the client
     * @return pdf document (status code 200)
     * or Internal server error; Failed to generate the document (status code 500)
     */
    @Operation(
            summary = "Call for generating enrolment or denial document for a client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "generate the document", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = double.class)))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error; Failed to generate document for the client", content = {
                            @Content(mediaType = "application/json")
                    })
            }
    )
    @PostMapping
    public ResponseEntity<InputStreamResource> enrolClient(@RequestBody ClientDTO clientDTO) {
        InputStreamResource inputStreamResource = clientService.enrolClient(clientDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "enrolment_document.pdf");

        return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
    }
}
