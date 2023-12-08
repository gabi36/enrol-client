package com.example.enrolclientapi.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "client")
@Data
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "series", length = 255)
    private String series;

    @Column(name = "number")
    private Integer number;

    @Column(name = "cnp")
    private Long cnp;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "nationality", length = 255)
    private String nationality;

    @Column(name = "birth_place", length = 255)
    private String birthPlace;

    @Column(name = "home", length = 255)
    private String home;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
}
