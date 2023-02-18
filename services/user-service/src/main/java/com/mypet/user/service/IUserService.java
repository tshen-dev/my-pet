package com.mypet.user.service;

import com.mypet.user.model.User;
import java.util.Optional;

public interface IUserService {

  Optional<User> findById(Integer id);
}
