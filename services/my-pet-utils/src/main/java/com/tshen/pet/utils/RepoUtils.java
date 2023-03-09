package com.tshen.pet.utils;

import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepoUtils {

  public static <T, D> Page<D> findAll(JpaRepository<T, ?> repo, Function<T, D> mapper, Pageable pageable) {
    var entityPage = repo.findAll(pageable);
    var dto = entityPage.stream().map(mapper).toList();
    return new PageImpl<>(dto, pageable, entityPage.getTotalElements());
  }
}
