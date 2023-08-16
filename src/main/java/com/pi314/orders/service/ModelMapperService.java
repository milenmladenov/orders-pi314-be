package com.pi314.orders.service;

import java.lang.reflect.*;
import java.util.*;

public interface ModelMapperService {
    <D> D map(Object source, Type destinationType);

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass);
}
