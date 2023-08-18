package com.pi314.orders.service;

import com.pi314.orders.model.entity.*;

import java.util.*;

public interface HandleService {
    Handle findByName(String name);

    Handle findById(Long handleId);

    List<Handle> findAll();
}
