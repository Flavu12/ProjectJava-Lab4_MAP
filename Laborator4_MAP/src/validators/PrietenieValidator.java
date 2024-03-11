package validators;

import domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie>{
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        String errors = "";
        if (entity.getUser1() == null || entity.getUser2()==null){
            errors += "Utilizator inexistent!\n";
        }
        if (entity.getUser1() == entity.getUser2()){
            errors += "Utilizator invalid!\n";
        }
        if (!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}
