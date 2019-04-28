package data;

public interface Repository {
    <T> void add(T model);
    <T> void update(T model);
    <T> T get(long id, Class<T> modelClass);
    <T> void delete(T model);
}
