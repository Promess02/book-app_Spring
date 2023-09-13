package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.repo.BundleRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BundleRepoSQL extends BundleRepo, JpaRepository<BookBundle, Long> {
}
