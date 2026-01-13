package com.oexil.univote.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "positions")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_common")
    private boolean isCommon; // True for common posts, False for faculty specific

    @Column(name = "order_priority")
    private Integer orderPriority;

    private String description;
}