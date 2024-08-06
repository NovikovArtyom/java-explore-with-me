package ru.yandex.practicum.ewmmainservice.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainservice.categories.model.CategoriesEntity;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {
    Boolean existsByName(String name);
}
