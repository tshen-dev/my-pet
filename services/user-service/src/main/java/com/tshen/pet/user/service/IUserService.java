package com.tshen.pet.user.service;

import com.tshen.pet.user.model.User;
import java.util.Optional;

public interface IUserService {

  Optional<User> findById(Integer id);
}
