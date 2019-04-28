package data;

public interface Repository {
    <T> void add(T model);
    <T> void update(T model);
    Object get(long id, Class modelClass);
    <T> void delete(T model);
}
