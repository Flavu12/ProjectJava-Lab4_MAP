package service;

import domain.Entity;
import exceptions.RepositoryExceptions;
import exceptions.ServiceExceptions;

/**
 * Interfata pentru service
 */

public interface Service<ID, E extends Entity<ID>> {

    /**
     * adauga enitatea daca e valida si nu e deja salvata
     * @param entity
     *         enitatea trebuie sa nu fie null
     * @return true - daca enitatea este salvata
     *         altfel returns false(id-ul exista deja)
     * @throws ServiceExceptions
     *            daca enitatea nu e valid
     *
     */

    //boolean addEntity(E entity) throws RepositoryExceptions, ServiceExceptions;

    /**
     *  remove enitate cu id dat
     * @param id
     *      id-ul trebuie sa nu fie null
     * @return enitatea removed sau null daca nu a fost o enitate cu id-ul dat
     * @throws ServiceExceptions
     *                   daca enitatea nu exista
     */

    E deleteEntity(ID id) throws RepositoryExceptions, ServiceExceptions;

    /**
     * return toate enitatile din service
     *
     * @return Iterable
     *
     */
    Iterable<E> getAll();
}