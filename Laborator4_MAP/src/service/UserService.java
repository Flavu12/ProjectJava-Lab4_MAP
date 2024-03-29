package service;

import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import exceptions.RepositoryExceptions;
import exceptions.ServiceExceptions;
import repository.Repository;

import java.util.*;

public class UserService implements Service<Long, Utilizator>{
    private Repository <Long, Utilizator> userRepo;
    private Repository<Tuple<Long,Long>, Prietenie> prietenieRepo;

    private static Long usersID;
    public UserService(Repository<Long, Utilizator> userRepo, Repository<Tuple<Long, Long>, Prietenie> prietenieRepo){
        usersID = 0L;
        this.userRepo = userRepo;
        this.prietenieRepo = prietenieRepo;
    }

    /**
     * adds a friend to a certain user(it also adds it to the other user)
     * @param userID: the first user in the friendship
     * @param friendID: the second user in the friendship
     * @return true: if the two users are friends
     *         otherwise it returns false
     * @throws RepositoryExceptions
     *          if either one of the users doesn't exist
     */

    public boolean addFriend(Long userID, Long friendID) throws RepositoryExceptions {
        Optional<Utilizator> userOptional = userRepo.findOne(userID);
        Optional<Utilizator> friendOptional = userRepo.findOne(friendID);

        if (!userOptional.isPresent() || !friendOptional.isPresent()) {
            return false;
        }

        Utilizator user = userOptional.get();
        Utilizator friend = friendOptional.get();

        user.addFriend(friend);
        friend.addFriend(user);

        List<Utilizator> friendList = user.getFriends();
        return friendList.contains(friend);
    }


    /**
     * generates an unique ID using an incrementing static variable
     * @return an ID(Long)
     */
    private Long generateID(){
        usersID += 1;
        return usersID;
    }

    /**
     * creates an entity with firstName and lastName
     * adds the entity if it's valid, and it isn't already saved
     * @param firstName
     *         the first name of the entity to be created and saved
     * @param lastName
     *          the last name of the entity to be saved
     * @return true - if the entity is saved
     *         otherwise returns false(id already exists)
     * @throws RepositoryExceptions from Repository
     *            if the entity is not valid
     *
     */

    public boolean addEntity(String firstName, String lastName) throws RepositoryExceptions {
        Utilizator entity = new Utilizator(firstName, lastName);
        entity.setId(generateID());
        return userRepo.save(entity) == null;
    }

    @Override
    public Utilizator deleteEntity(Long id) throws ServiceExceptions, RepositoryExceptions {
        Optional<Utilizator> userToDeleteOptional = userRepo.delete(id);

        if (userToDeleteOptional.isPresent()) {
            Utilizator userToDelete = userToDeleteOptional.get();

            userToDelete.getFriends().forEach(f -> {
                try {
                    Tuple<Long, Long> newID = new Tuple<>(f.getId(), id);
                    Tuple<Long, Long> newID2 = new Tuple<>(id, f.getId());
                    prietenieRepo.delete(newID);
                    prietenieRepo.delete(newID2);
                    f.removeFriend(userToDelete.getId());
                } catch (RepositoryExceptions e) {
                    System.out.println("Entitate invalida!");
                }
            });
            return userToDelete;
        } else {
            throw new ServiceExceptions("Utilizatorul pe care doriti sa-l stergeti nu exista.");
        }
    }


    /**
     * Depth First Search to find the users of a community
     * @param user: the user we reached with searching
     * @param set: the set of users so we won't visit them twice
     * @return the users who form a community starting from 'user'
     */
    public List<Utilizator> DFS(Utilizator user, Set<Utilizator> set){
        List<Utilizator> users = new ArrayList<>();
        users.add(user);
        set.add(user);
        user.getFriends().forEach(u -> {
            if (!set.contains(u)) {
                List<Utilizator> list = DFS(u, set);
                users.addAll(list);
            }
        });
        return users;
    }

    /**
     * function which calculates the total number of communities in our social network(using DFS for finding communities)
     * @return the number of distinct communities in the network
     */
    public int noOfCommunities(){
        Iterable<Utilizator> users = userRepo.findAll();
        Set<Utilizator> set = new HashSet<>();
        int count = 0;
        for (Utilizator user: users){
            if (!set.contains(user)){
                count++;
                DFS(user, set);
            }
        }
        return count;
    }

    /**
     * function which finds the most sociable community/communities in our network
     * @return a list of iterable's with the most sociable communities
     */
    public List<Iterable<Utilizator>> mostSociableCommunity(){
        List<Iterable<Utilizator>> result = new ArrayList<>();
        Iterable<Utilizator> users = userRepo.findAll();
        Set<Utilizator> set = new HashSet<>();

        int max = -1;
        int friendsCounter;
        for (Utilizator u: users){
            friendsCounter = 0;
            if (!set.contains(u)){
                List<Utilizator> aux = DFS(u, set);
                for (Utilizator a: aux){
                    friendsCounter += a.getFriends().size();
                }
                if (friendsCounter > max){
                    max = friendsCounter;
                    result = new ArrayList<>();
                    result.add(aux);
                }
                else if (friendsCounter == max){
                    result.add(aux);
                }
            }
        }
        return result;
    }

    @Override
    public Iterable<Utilizator> getAll() {
        return userRepo.findAll();
    }

    /**
     * function which finds an entity by its id
     * @return the entity if there is an entity with this id
     *          otherwise it returns null
     */
    public Utilizator getEntity(Long id) throws RepositoryExceptions{
        Optional<Utilizator> userOptional = userRepo.findOne(id);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            // Handle the case where the user is not found
            throw new RepositoryExceptions("User not found with ID: " + id);
        }
    }

}