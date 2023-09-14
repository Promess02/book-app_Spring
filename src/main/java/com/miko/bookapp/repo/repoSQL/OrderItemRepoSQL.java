package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.model.OrderItem;
import com.miko.bookapp.repo.OrderItemRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepoSQL extends OrderItemRepo, JpaRepository<OrderItem, Long> {
}
