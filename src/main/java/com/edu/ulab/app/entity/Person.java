package com.edu.ulab.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PERSON")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FULL_NAME", nullable = false, unique = true)
    private String fullName;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "AGE", nullable = false)
    private int age;
}
