package com.pi314.orders.repository.impl;

import com.pi314.orders.model.entity.*;
import com.pi314.orders.repository.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.*;
import lombok.*;
import org.springframework.stereotype.*;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
  private final EntityManager entityManager;

  @Override
  public List<User> filterUsers(String searchParam) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> cq = cb.createQuery(User.class);

    Root<User> user = cq.from(User.class);
    Predicate companyNamePredicate =
        cb.like(cb.upper(user.get("companyName")), "%" + searchParam + "%");
    cq.where(companyNamePredicate);

    TypedQuery<User> query = entityManager.createQuery(cq);
    return query.getResultList();
  }
}
