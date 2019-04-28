package data;

import java.util.List;

public interface Repository {
    <T> void add(T model);
    <T> void update(T model);
    <T> T get(long id, Class<T> modelClass);
    <T> List<T> getAll(Class<T> modelClass);
    <T> void delete(T model);
}
