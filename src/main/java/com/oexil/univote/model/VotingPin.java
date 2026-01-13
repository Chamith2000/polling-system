package com.oexil.univote.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "voting_pin")
public class VotingPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pin_value", nullable = false)
    private String pinValue;

    @Column(name = "active_from")
    private LocalDateTime activeFrom;

    @Column(name = "active_until")
    private LocalDateTime activeUntil;

    @Column(name = "is_active")
    private boolean isActive;
}