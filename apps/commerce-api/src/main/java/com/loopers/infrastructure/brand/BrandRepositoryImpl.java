package com.loopers.infrastructure.brand;

import com.loopers.domain.brand.BrandModel;
import com.loopers.domain.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BrandRepositoryImpl implements BrandRepository {
    private final BrandJpaRepository brandJpaRepository;

    @Override
    public Optional<BrandModel> findById(Long id) {
        return brandJpaRepository.findById(id);
    }

    @Override
    public Optional<BrandModel> register(BrandModel brandModel) {
        try {
            BrandModel saved = brandJpaRepository.save(brandModel);
            return Optional.of(saved);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<BrandModel> findByName(String name) {
        try {
            return brandJpaRepository.findByName(name);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
