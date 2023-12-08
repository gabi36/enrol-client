package com.example.enrolclientapi.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(force = true)
public class ClientDTO {

    private Long id;
    @NotNull
    private String series;
    @NotNull
    private Integer number;
    @NotNull
    private Long cnp;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String nationality;
    @NotNull
    private String birthPlace;
    @NotNull
    private String home;
    @NotNull
    private LocalDateTime expirationDate;
}
