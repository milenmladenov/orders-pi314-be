package com.pi314.orders.service.impl;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import com.pi314.orders.repository.*;
import com.pi314.orders.service.*;
import lombok.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;
    private final ModelMapperService modelMapperService;
    @Override
    public Type save(GroupDTO groupDTO) {
         return typeRepository.save(modelMapperService.map(groupDTO.getDetailType(),Type.class));
    }
}
