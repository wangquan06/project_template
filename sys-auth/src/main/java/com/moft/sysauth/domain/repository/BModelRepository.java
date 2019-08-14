package com.moft.sysauth.domain.repository;

import com.moft.sysauth.domain.entity.BModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BModelRepository<T extends BModel> extends JpaRepository<T, Integer>, JpaSpecificationExecutor<T> {
    public T findByUuid(String uuid);
    public Long deleteByUuid(String uuid);
}
