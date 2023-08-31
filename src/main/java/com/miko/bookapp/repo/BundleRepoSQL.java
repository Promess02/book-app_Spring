package com.miko.bookapp.repo;

import com.miko.bookapp.model.BookBundle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BundleRepoSQL extends BundleRepo, JpaRepository<BookBundle, Long> {
}
