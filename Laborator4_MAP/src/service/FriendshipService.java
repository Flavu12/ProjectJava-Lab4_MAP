package service;

import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import exceptions.RepositoryExceptions;
import exceptions.ServiceExceptions;
import repository.Repository;

import java.util.Optional;

public class FriendshipService implements Service<Tuple<Long,Long>, Prietenie>{
    private Repository<Tuple<Long, Long>, Prietenie> friendshipRepo;
    public FriendshipService(Repository<Tuple<Long,Long>, Prietenie> friendshipRepo){
        this.friendshipRepo = friendshipRepo;
    }


    /**
     * adds the friendship between the two given users if it's valid, and it isn't already saved
     * creates the id based on the users' id's
     * @param user1
     *         the first user of the friendship
     * @param user2
     *          the second user of the friendship
     * @return true - if the entity is saved
     *         otherwise returns false(id already exists)
     * @throws ServiceExceptions
     *            if the friendship is not valid
     *
     */

    public boolean addEntity(Utilizator user1, Utilizator user2) throws ServiceExceptions, RepositoryExceptions {
        Prietenie entity = new Prietenie(user1, user2);
        Tuple<Long, Long> prietenieID = new Tuple<>(entity.getUser1().getId(), entity.getUser2().getId());
        entity.setId(prietenieID);
        Long id1 = entity.getId().getLeft();
        Long id2 = entity.getId().getRight();
        Tuple<Long, Long> newID = new Tuple<>(id2, id1);

        Optional<Prietenie> friendship1 = friendshipRepo.findOne(entity.getId());
        Optional<Prietenie> friendship2 = friendshipRepo.findOne(newID);

        if (friendship1.isPresent() || friendship2.isPresent()) {
            throw new ServiceExceptions("Prietenia exista deja");
        }

        Optional<Prietenie> savedFriendship = friendshipRepo.save(entity);
        return savedFriendship.isPresent();
    }

    /**
     * Removes the friendship between the two given users if it exists.
     * It tries to delete the friendship in both possible directions (user1 with user2 and user2 with user1)
     * since the order of users in a friendship might differ.
     *
     * @param id
     *         a tuple containing the IDs of the two users whose friendship is to be deleted.
     *
     * @return the removed friendship if it exists.
     *
     * @throws ServiceExceptions
     *         if the friendship doesn't exist.
     * @throws RepositoryExceptions
     *         if there are any issues during the deletion process from the repository.
     *
     */
    @Override
    public Prietenie deleteEntity(Tuple<Long, Long> id) throws ServiceExceptions, RepositoryExceptions {
        Optional<Prietenie> friendship1Optional = friendshipRepo.delete(id);
        Long firstID = id.getLeft();
        Long secondID = id.getRight();
        Tuple<Long, Long> newID = new Tuple<>(secondID, firstID);
        Optional<Prietenie> friendship2Optional = friendshipRepo.delete(newID);

        if (friendship1Optional.isPresent()) {
            Prietenie friendship1 = friendship1Optional.get();
            friendship1.getUser1().removeFriend(friendship1.getUser2().getId());
            friendship1.getUser2().removeFriend(friendship1.getUser1().getId());
            return friendship1;
        } else if (friendship2Optional.isPresent()) {
            Prietenie friendship2 = friendship2Optional.get();
            friendship2.getUser1().removeFriend(friendship2.getUser2().getId());
            friendship2.getUser2().removeFriend(friendship2.getUser1().getId());
            return friendship2;
        } else {

            throw new ServiceExceptions("Nu exista aceasta prietenie!");
        }
    }


    @Override
    public Iterable<Prietenie> getAll() {
        return friendshipRepo.findAll();
    }
}