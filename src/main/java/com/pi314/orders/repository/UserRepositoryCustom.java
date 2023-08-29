package com.pi314.orders.repository;

import com.pi314.orders.model.entity.*;

import java.util.*;

public interface UserRepositoryCustom {
    List<User> filterUsers(String searchParam);
}
