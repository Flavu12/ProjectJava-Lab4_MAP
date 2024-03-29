package presentation;

import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import exceptions.RepositoryExceptions;
import service.FriendshipService;
import service.UserService;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private UserService userService;
    private FriendshipService friendshipService;
    public ConsoleUI(UserService userService, FriendshipService friendshipService){
        this.userService = userService;
        this.friendshipService = friendshipService;
        scanner = new Scanner(System.in);
    }
    private static void printMenu(){
        System.out.println("1.Adauga un utilizator");
        System.out.println("2.Sterge un utilizator");
        System.out.println("3.Afiseaza toti utilizatorii");
        System.out.println("4.Adauga o prietenie");
        System.out.println("5.Sterge o prietenie");
        System.out.println("6.Afiseaza toti prietenii unui utilizator");
        System.out.println("7.Afiseaza toate prieteniile");
        System.out.println("8.Numarul de comunitati");
        System.out.println("9.Cea mai sociabila comunitate");
        System.out.println("10.Iesire");
    }

    private void addFriend() throws RepositoryExceptions {
        System.out.print("User_1 ID: ");
        Long user1ID = scanner.nextLong();
        System.out.print("User_2 ID: ");
        Long user2ID = scanner.nextLong();
        Utilizator user1 = userService.getEntity(user1ID);
        Utilizator user2 = userService.getEntity(user2ID);
        try {
            if (friendshipService.addEntity(user1, user2)) {
                System.out.println("Prietenia a fost formata cu succes.");
                userService.addFriend(user1ID, user2ID);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void removeFriend(){
        System.out.print("ID-ul utilizatorului pentru care doriti sa stergeti un prieten: ");
        Long user1ID = scanner.nextLong();
        System.out.print("ID-ul prietenului de sters: ");
        Long user2ID = scanner.nextLong();
        Tuple<Long, Long> prietenieID = new Tuple<>(user1ID, user2ID);
        try{
            friendshipService.deleteEntity(prietenieID);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void showAllFriends() throws RepositoryExceptions{
        System.out.print("Introduceti ID-ul utilizatorului pentru care doriti sa aflati prietenii: ");
        Long userID = scanner.nextLong();
        Utilizator user = userService.getEntity(userID);
        if (user == null){
            System.out.println("Utilizatorul nu exista");
            return;
        }
        Iterable<Utilizator> prieteni = user.getFriends();
        for (Utilizator p : prieteni) {
            System.out.println(p);
        }
    }

    private void showAllFriendships(){
        Iterable<Prietenie> friendships = friendshipService.getAll();
        boolean found = false;
        for (Prietenie p: friendships){
            System.out.println(p);
            found = true;
        }
        if (!found){
            System.out.println("Nu exista nicio prietenie.");
        }
    }

    private void addUser(){
        System.out.print("Prenume: ");
        String firstName = scanner.next();
        System.out.print("Nume: ");
        String lastName = scanner.next();
        try {
            userService.addEntity(firstName, lastName);
            System.out.println("Utilizatorul a fost adaugat cu succes.");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser(){
        System.out.print("ID-ul utilizatorului: ");
        Long userID = scanner.nextLong();
        try{
            userService.deleteEntity(userID);
            System.out.println("Utilizatorul a fost sters cu succes.");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void showAllUsers(){
        Iterable<Utilizator> users = userService.getAll();
        boolean found = false;
        for (Utilizator u: users){
            System.out.print(u.getId() + " - ");
            System.out.println(u);
            found = true;
        }
        if (!found){
            System.out.println("Nu exista niciun utilizator.");
        }
    }

    private void noOfCommunities(){
        System.out.println("Aceasta retea este formata din " + userService.noOfCommunities() + " comunitati");
    }

    private void mostSociableCommunity(){
        List<Iterable<Utilizator>> communities = userService.mostSociableCommunity();
        System.out.println("Cea mai sociabila comunitate este formata din: ");
        int cnt = 1;
        for (Iterable<Utilizator> community: communities) {
            System.out.println("Comunitatea " + cnt);
            for (Utilizator u : community) {
                System.out.println(u);
            }
            cnt++;
        }
    }

    public void startConsole() throws RepositoryExceptions{
        boolean run = true;
        ConsoleUI.printMenu();
        while (run){

            int command = 0;
            while(true) {
                try {
                    System.out.print("Introduceti comanda: ");
                    command = scanner.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("Nu ati introdus o optiune valida.");
                }
            }
            switch (command){
                case 10:
                    System.out.println("La revedere!");
                    run = false;
                    break;
                case 0:
                    ConsoleUI.printMenu();
                    break;
                case 1:
                    addUser();
                    break;
                case 2:
                    deleteUser();
                    break;
                case 3:
                    showAllUsers();
                    break;
                case 4:
                    addFriend();
                    break;
                case 5:
                    removeFriend();
                    break;
                case 6:
                    showAllFriends();
                    break;
                case 7:
                    showAllFriendships();
                    break;
                case 8:
                    noOfCommunities();
                    break;
                case 9:
                    mostSociableCommunity();
                    break;
                default:
                    System.out.println("Nu ati introdus o optiune valida");
                    break;
            }
        }
    }
}
