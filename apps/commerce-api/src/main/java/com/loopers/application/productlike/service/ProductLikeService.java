package com.loopers.application.productlike.service;

import com.loopers.application.productlike.query.ProductLikeGroup;
import com.loopers.domain.member.Member;
import com.loopers.domain.product.Product;
import com.loopers.domain.productlike.ProductLike;
import com.loopers.domain.productlike.ProductLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;

    public Optional<ProductLike> findProductLikeByMemberAndProduct(Product product, Member member) {
        return productLikeRepository.getProductLike(product.getId(), member.getId());
    }

    public void registerProductLike(ProductLike productLike) {
        productLikeRepository.register(productLike);
    }

    public void cancelProductLike(ProductLike productLike) {
        productLikeRepository.delete(productLike);
    }

    public boolean isLikedByMember(Long productId, Long memberId) {
        return productLikeRepository.existsByProductIdAndMemberId(productId, memberId);
    }
    
    public List<ProductLikeGroup> countGroupByProductId() {
        return productLikeRepository.countGroupByProductId();
    }
}
