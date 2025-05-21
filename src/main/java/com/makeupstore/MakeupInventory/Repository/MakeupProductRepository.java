package com.makeupstore.MakeupInventory.Repository;

import com.makeupstore.MakeupInventory.Entities.MakeupProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MakeupProductRepository extends JpaRepository<MakeupProductEntity, UUID> {
    Page<MakeupProductEntity> findAllByProductNameContaining(String productName, Pageable pageable);

    @Override
    Page<MakeupProductEntity> findAll(Pageable pageable);
}
