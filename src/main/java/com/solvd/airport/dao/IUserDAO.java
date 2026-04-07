package com.solvd.airport.dao;

import com.solvd.airport.models.User;

public interface IUserDAO extends IBaseDAO<User> {
    User getByEmail(String email);
}
