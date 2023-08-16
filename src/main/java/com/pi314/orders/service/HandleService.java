package com.pi314.orders.service;

import com.pi314.orders.model.entity.*;

public interface HandleService {
    Handle findByName(String name);

    Handle findById(Long handleId);
}
