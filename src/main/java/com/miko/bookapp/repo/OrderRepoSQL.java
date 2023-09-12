package com.miko.bookapp.repo;

import com.miko.bookapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepoSQL extends JpaRepository<Order, Long>, OrderRepo {
}
