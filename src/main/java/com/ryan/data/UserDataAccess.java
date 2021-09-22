package com.ryan.data;

import com.ryan.models.User;

import java.util.List;

public interface UserDataAccess extends DataAccess<User, Integer> {

   User getUserByUserName(String name);

}
