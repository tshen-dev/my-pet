package service;

import java.util.List;

public interface IEntityService<T, I> {

  T findById(I id);
  List<T> findAll();
}
