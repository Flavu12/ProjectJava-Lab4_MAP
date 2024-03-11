import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import exceptions.RepositoryExceptions;
import exceptions.ServiceExceptions;
import presentation.ConsoleUI;
import repository.InMemoryRepo;
import service.FriendshipService;
import service.UserService;
import validators.PrietenieValidator;
import validators.UtilizatorValidator;
import validators.Validator;

public class Main {
    public static void main(String[] args) throws RepositoryExceptions, ServiceExceptions {
        Validator<Utilizator> userValidator = new UtilizatorValidator();
        Validator<Prietenie> friendshipValidator = new PrietenieValidator();

        InMemoryRepo<Long, Utilizator> repoUser =  new InMemoryRepo<>(userValidator);
        InMemoryRepo<Tuple<Long,Long>, Prietenie> repoFriendship= new InMemoryRepo<>(friendshipValidator);

        UserService userService = new UserService(repoUser, repoFriendship);
        FriendshipService friendshipService = new FriendshipService(repoFriendship);

        ConsoleUI console = new ConsoleUI(userService, friendshipService);
        console.startConsole();
    }
}