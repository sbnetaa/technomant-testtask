package ru.terentyev.technomant_testtasak.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.util.UUID;

@MappedSuperclass
@Data
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    public AbstractEntity(){}
}
