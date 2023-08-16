package com.pi314.orders.service.impl;

import com.pi314.orders.model.entity.*;
import com.pi314.orders.repository.*;
import com.pi314.orders.service.*;
import lombok.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class HandleServiceImpl implements HandleService {
    private final HandleRepository handleRepository;
    @Override
    public Handle findByName(String name) {
        return handleRepository.findByName(name);
    }

    @Override
    public Handle findById(Long handleId) {
        return handleRepository.findById(handleId).orElseThrow();
    }
}
