package ru.yandex.practicum.ewmmainservice.categories.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.categories.dto.CategoriesRequestDto;
import ru.yandex.practicum.ewmmainservice.categories.model.CategoriesEntity;
import ru.yandex.practicum.ewmmainservice.categories.repository.CategoriesRepository;
import ru.yandex.practicum.ewmmainservice.events.repository.EventsRepository;
import ru.yandex.practicum.ewmmainservice.exception.CategoriesNotFoundException;
import ru.yandex.practicum.ewmmainservice.exception.DataIntegrityViolationException;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final EventsRepository eventsRepository;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository, EventsRepository eventsRepository) {
        this.categoriesRepository = categoriesRepository;
        this.eventsRepository = eventsRepository;
    }

    @Override
    @Transactional
    public CategoriesEntity addCategories(CategoriesEntity categoriesEntity) {
        if (!categoriesRepository.existsByName(categoriesEntity.getName())) {
            return categoriesRepository.save(categoriesEntity);
        } else {
            throw new DataIntegrityViolationException("Одинаковые поля!");
        }
    }


    @Override
    @Transactional
    public void deleteCategories(Long catId) {
        CategoriesEntity categories = categoriesRepository.findById(catId).orElseThrow(() ->
                new CategoriesNotFoundException(catId));
        if (!eventsRepository.existsByCategories_Id(catId)) {
            categoriesRepository.deleteById(catId);
        } else {
            throw new DataIntegrityViolationException("С данной категорией есть связанные события!");
        }
    }

    @Override
    @Transactional
    public CategoriesEntity patchCategories(CategoriesRequestDto categoriesRequestDto, Long catId) {
        CategoriesEntity categories = categoriesRepository.findById(catId).orElseThrow(() ->
                new CategoriesNotFoundException(catId));
        if (!categoriesRepository.existsByName(categoriesRequestDto.getName()) ||
                categories.getName().equals(categoriesRequestDto.getName())) {
            categories.setName(categoriesRequestDto.getName());
            return categoriesRepository.save(categories);
        } else {
            throw new DataIntegrityViolationException("Одинаковые поля!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriesEntity> findAllCategories(Integer from, Integer size) {
        return categoriesRepository.findAll(PageRequest.of(from, size));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriesEntity findCategoriesById(Long catId) {
        return categoriesRepository.findById(catId).orElseThrow(() -> new CategoriesNotFoundException(catId));
    }
}
