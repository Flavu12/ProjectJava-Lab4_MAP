package repository;

import domain.Entity;
import exceptions.RepositoryExceptions;
import validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepo<ID, E extends Entity<ID>> implements Repository<ID,E> {
    private Validator<E> validator;
    //implementeaza interfata 'Repository'
    //pastreaza entitatile intr-un hashmap
    Map<ID,E> entities;

    public InMemoryRepo(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id) throws RepositoryExceptions{
        if (id==null)
            throw new RepositoryExceptions("id must be not null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null!");
        validator.validate(entity);

        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }



    @Override
    public Optional<E> delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("ID must be not null");

        return Optional.ofNullable(entities.remove(id));
    }



    @Override
    public Optional<E> update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null!");
        validator.validate(entity);

        return Optional.ofNullable(entities.put(entity.getId(), entity));
    }


}