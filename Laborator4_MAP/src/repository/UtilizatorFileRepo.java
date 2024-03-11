package repository;

import domain.Utilizator;
import exceptions.RepositoryExceptions;
import validators.Validator;

import java.util.List;

public class UtilizatorFileRepo extends AbstractFileRepo<Long, Utilizator> {

    public UtilizatorFileRepo(String fileName, Validator<Utilizator> validator) throws RepositoryExceptions {
        super(fileName, validator);
    }

    @Override
    public Utilizator extractEntity(List<String> attributes) {
        //TODO: implement method
        Utilizator user = new Utilizator(attributes.get(1),attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    protected String createEntityAsString(Utilizator entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName();
    }
}