package com.tshen.pet.user.repo;

import com.tshen.pet.user.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

  void deleteByUserName(String userName);
  List<User> findAllByUserNameOrEmail(String userName, String email);
  Optional<User> findByKeycloakId(String keycloakId);
}
