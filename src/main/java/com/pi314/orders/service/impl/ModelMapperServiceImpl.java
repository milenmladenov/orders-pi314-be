package com.pi314.orders.service.impl;

import com.pi314.orders.service.*;
import java.lang.reflect.*;
import java.util.*;
import org.modelmapper.*;
import org.springframework.stereotype.*;

@Service
public class ModelMapperServiceImpl implements ModelMapperService {

  private final ModelMapper modelMapper;

  public ModelMapperServiceImpl() {
    modelMapper = new ModelMapper();
  }

  @Override
  public <D> D map(Object source, Type destinationType) {
    if (source != null) {
      return modelMapper.map(source, destinationType);
    }
    return null;
  }

  @Override
  public <S, T> List<T> mapList(final List<S> source, Class<T> targetClass) {
    return source.stream().map(element -> modelMapper.map(element, targetClass)).toList();
  }
}
