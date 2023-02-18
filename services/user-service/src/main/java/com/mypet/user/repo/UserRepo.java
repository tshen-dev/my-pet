package com.mypet.user.repo;

import com.mypet.user.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

  void deleteByUserName(String userName);
  List<User> findAllByUserNameOrEmail(String userName, String email);
}
