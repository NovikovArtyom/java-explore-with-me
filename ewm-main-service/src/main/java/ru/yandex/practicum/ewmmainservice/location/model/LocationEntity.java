package ru.yandex.practicum.ewmmainservice.location.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {
    @Id
    private Long id;
    private Double lat;
    private Double lon;
}
