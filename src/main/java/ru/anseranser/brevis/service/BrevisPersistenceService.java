package ru.anseranser.brevis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.anseranser.brevis.model.Brevis;
import ru.anseranser.brevis.repository.BrevisRepository;

/*
Сервис создан для выделения сохранения сущности в БД в отдельную независимую транзакцию
Иначе в методе @Transactional BrevisService.create цикл, в случае коллизии shortUrl,
не отрабатывал положенное количество раз, а падал в UnexpectedRollbackException
* */
@Service
public class BrevisPersistenceService {

    private final BrevisRepository brevisRepository;

    public BrevisPersistenceService(BrevisRepository brevisRepository) {
        this.brevisRepository = brevisRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Brevis save(Brevis brevis) {
        return brevisRepository.save(brevis);
    }
}
