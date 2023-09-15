package com.miko.bookapp.repo;

import com.miko.bookapp.model.Order;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepo extends RepoTemplate<Order>{
    long getNextGeneratedId();
}
