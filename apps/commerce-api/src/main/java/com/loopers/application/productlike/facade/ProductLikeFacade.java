package com.loopers.application.productlike.facade;

import com.loopers.application.member.service.MemberService;
import com.loopers.application.product.service.ProductService;
import com.loopers.application.productlike.command.ProductLikeCommand;
import com.loopers.application.productlike.result.ProductLikeResult;
import com.loopers.application.productlike.service.ProductLikeService;
import com.loopers.domain.member.Member;
import com.loopers.domain.product.Product;
import com.loopers.domain.productlike.ProductLike;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductLikeFacade {

    private final ProductLikeService productLikeService;
    private final ProductService productService;
    private final MemberService memberService;

    /*
     * 상품 좋아요 처리
     *
     * 1. 상품 확인
     * 2. 회원 확인
     * 3. 상품 좋아요 확인
     * */
    @Transactional
    public ProductLikeResult toggleProductLike(ProductLikeCommand command) {
        Product product = productService.findProductById(command.getProductId());

        Member member = memberService.findMemberById(command.getMemberId());

        Optional<ProductLike> optionalProductLike = productLikeService.findProductLikeByMemberAndProduct(product, member);

        if (optionalProductLike.isEmpty()) {
            ProductLike productLikeModel = ProductLike.create(product.getId(), member.getId());
            productLikeService.registerProductLike(productLikeModel);

            product.increaseLikeCount();

            return ProductLikeResult.of(product.getId(), true, product.getLikeCount(), true);
        } else {
            return ProductLikeResult.of(product.getId(), true, product.getLikeCount(), false);
        }
    }

    /*
     * 상품 좋아요 취소 처리
     * */
    @Transactional
    public ProductLikeResult cancelProductLike(ProductLikeCommand command) {
        Product product = productService.findProductById(command.getProductId());

        Member member = memberService.findMemberById(command.getMemberId());

        Optional<ProductLike> optionalProductLike = productLikeService.findProductLikeByMemberAndProduct(product, member);

        if (optionalProductLike.isPresent()) {
            ProductLike productLike = optionalProductLike.get();
            productLikeService.cancelProductLike(productLike);
            product.decreaseLikeCount();

            return ProductLikeResult.of(product.getId(), false, product.getLikeCount(), true);
        } else {
            return ProductLikeResult.of(product.getId(), false, product.getLikeCount(), false);
        }
    }
}
