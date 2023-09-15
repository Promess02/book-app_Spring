package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.model.Order;
import com.miko.bookapp.repo.OrderRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepoSQL extends JpaRepository<Order, Long>, OrderRepo {
    @Override
    @Query(nativeQuery = true, value = "select if(max(id) is null, 0, max(id)+1) from custom_order")
    long getNextGeneratedId();
}
