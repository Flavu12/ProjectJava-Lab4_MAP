package validators;

import domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        validateFirstName(entity.getFirstName());
        validateLastName(entity.getLastName());
    }

    private static void validateFirstName(String firstName) throws ValidationException{
        String errors = "";
        if (firstName.isEmpty()){
            errors += "Prenumele trebuie sa contina cel putin un caracter!\n";
        }
        if (firstName.length() > 30){
            errors += "Lungimea prenumelui nu poate sa depaseasca 30 de caractere.\n";
        }
        if (!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }

    private static void validateLastName(String lastName) throws ValidationException{
        String errors = "";
        if (lastName.isEmpty()){
            errors += "Numele trebuie sa contina cel putin un caracter.\n";
        }
        if (lastName.length() > 30){
            errors += "Lungimea numelui nu poate sa depaseasca 30 de caractere.\n";
        }
        if (!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}