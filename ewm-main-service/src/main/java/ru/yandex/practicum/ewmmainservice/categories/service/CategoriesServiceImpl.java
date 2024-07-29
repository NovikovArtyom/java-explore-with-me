package ru.yandex.practicum.ewmmainservice.categories.service;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.categories.model.CategoriesEntity;
import ru.yandex.practicum.ewmmainservice.categories.repository.CategoriesRepository;
import ru.yandex.practicum.ewmmainservice.exception.CategoriesNotFoundException;
import ru.yandex.practicum.ewmmainservice.exception.DataIntegrityViolationException;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CategoriesEntity addCategories(CategoriesEntity categoriesEntity) {
        try {
            return categoriesRepository.save(categoriesEntity);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }


    //TODO: Обратите внимание: с категорией не должно быть связано ни одного события.
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteCategories(Long catId) {
        if (categoriesRepository.existsById(catId)) {
            categoriesRepository.deleteById(catId);
        } else {
            throw new CategoriesNotFoundException(catId);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CategoriesEntity patchCategories(CategoriesEntity categoriesEntity, Long catId) {
        if (categoriesRepository.existsById(catId)) {
            try {
                return categoriesRepository.save(categoriesEntity);
            } catch (DataAccessException e) {
                throw new DataIntegrityViolationException(e.getMessage());
            }
        } else {
            throw new CategoriesNotFoundException(catId);
        }
    }

    //TODO: Запрос составлен некорректно - обработка исключения
    @Override
    @Transactional(readOnly = true)
    public Page<CategoriesEntity> findAllCategories(Integer from, Integer size) {
        return categoriesRepository.findAll(PageRequest.of(from, size));
    }

    //TODO: Запрос составлен некорректно - обработка исключения
    @Override
    @Transactional(readOnly = true)
    public CategoriesEntity findCategoriesById(Long catId) {
        return categoriesRepository.findById(catId).orElseThrow(() -> new CategoriesNotFoundException(catId));
    }
}
