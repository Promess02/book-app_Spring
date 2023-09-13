package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.model.Order;
import com.miko.bookapp.repo.OrderRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepoSQL extends JpaRepository<Order, Long>, OrderRepo {
}
