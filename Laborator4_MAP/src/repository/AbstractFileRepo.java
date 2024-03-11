package repository;

import domain.Entity;
import exceptions.RepositoryExceptions;
import validators.Validator;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepo<ID, E extends Entity<ID>> extends InMemoryRepo<ID,E> {
    String fileName;
    //este o extensie a InMemoryRepo , adaugand capacitatea de a stoca
    //entitati intr-un fisier

    public AbstractFileRepo(String fileName, Validator<E> validator) throws RepositoryExceptions {
        super(validator);
        this.fileName = fileName;
        loadData();

    }
    //Decorator Pattern
    //Aceasta e o tehnică de design ce permite adăugarea de comportament suplimentar unui
    // obiect fără a modifica structura acestuia.
    private void loadData() throws RepositoryExceptions { //decorator pattern
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String newLine;
            while ((newLine = reader.readLine()) != null) {
                System.out.println(newLine);
                List<String> data = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(data);
                super.save(entity);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Path path= Paths.get(fileName);
//        try{
//            List<String> lines= Files.readAllLines(path);
//            lines.forEach(line -> {
//                E entity=extractEntity(Arrays.asList(line.split(";")));
//                super.save(entity);
//            });
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }

    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);  //Template Method


    protected abstract String createEntityAsString(E entity); //Template Method

    @Override
    public Optional<E> save(E entity) {
        Optional<E> resultOptional = super.save(entity);
        if (!resultOptional.isPresent()) {
            writeToFile(entity);
        }
        return resultOptional;
    }



    protected void writeToFile(E entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

            writer.write(createEntityAsString(entity));
            writer.newLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}