package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.model.OrderItem;
import com.miko.bookapp.repo.OrderItemRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepoSQL extends OrderItemRepo, JpaRepository<OrderItem, Long> {
    @Override
    @Query(nativeQuery = true, value = "select if(max(id) is null, 0, max(id)+1) from order_item")
    long getNextGeneratedId();

}
