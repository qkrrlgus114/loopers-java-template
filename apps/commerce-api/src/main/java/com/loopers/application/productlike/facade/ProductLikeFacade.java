package com.loopers.application.productlike.facade;

import com.loopers.application.member.service.MemberService;
import com.loopers.application.product.service.ProductService;
import com.loopers.application.productlike.command.ProductLikeCommand;
import com.loopers.application.productlike.query.ProductLikeGroup;
import com.loopers.application.productlike.result.ProductLikeResult;
import com.loopers.application.productlike.result.ProductLikeView;
import com.loopers.application.productlike.service.ProductLikeService;
import com.loopers.domain.member.Member;
import com.loopers.domain.product.Product;
import com.loopers.domain.productlike.ProductLike;
import com.loopers.domain.productlike.event.ProductLikedEvent;
import com.loopers.domain.productlike.event.ProductUnlikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductLikeFacade {

    private final ProductLikeService productLikeService;
    private final ProductService productService;
    private final MemberService memberService;
    private final ApplicationEventPublisher eventPublisher;

    /*
     * 상품 좋아요 처리
     *
     * 1. 상품 확인
     * 2. 회원 확인
     * 3. 상품 좋아요 확인
     * */
    @Transactional
    public ProductLikeResult registerProductLike(ProductLikeCommand command) {
        Product product = productService.findProductById(command.getProductId());
        Member member = memberService.findMemberById(command.getMemberId());

        Optional<ProductLike> optionalProductLike = productLikeService.findProductLikeByMemberAndProduct(product, member);

        if (optionalProductLike.isEmpty()) {
            productLikeService.registerProductLike(ProductLike.create(product.getId(), member.getId()));
            eventPublisher.publishEvent(ProductLikedEvent.of(command.getProductId(), command.getMemberId()));
            return ProductLikeResult.of(product.getId(), true, product.getLikeCount() + 1, true);
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
            productLikeService.cancelProductLike(optionalProductLike.get());
            eventPublisher.publishEvent(ProductUnlikedEvent.of(command.getProductId(), command.getMemberId()));
            return ProductLikeResult.of(product.getId(), false, product.getLikeCount() - 1, true);
        } else {
            return ProductLikeResult.of(product.getId(), false, product.getLikeCount(), false);
        }
    }

    /*
     * 1. ProductLike 테이블에서 모든 상품의 좋아요를 카운팅한다.
     * 2. Product 테이블에서 해당 상품을 조회한다.
     * 3. Product 도메인에서 좋아요 수를 업데이트 한다.
     * */
    @Transactional
    public void updateAllProductLikeCount() {
        Map<Long, Integer> likeCountMap = productLikeService.countGroupByProductId().stream()
                .collect(Collectors.toMap(
                        ProductLikeGroup::getProductId,
                        ProductLikeGroup::getLikeCount));

        List<Product> products = productService.findProductListByProductId(
                new ArrayList<>(likeCountMap.keySet()));

        products.forEach(p ->
                p.updateLikeCount(likeCountMap.getOrDefault(p.getId(), 0)));
    }

    /*
     * 현재 사용자가 좋아요한 상품 목록 가져오기
     * */
    @Transactional(readOnly = true)
    public List<ProductLikeView> getProductLikeList(Long memberId) {
        Member member = memberService.findMemberById(memberId);

        List<Long> productIds = productLikeService.findProductLikeIdsByMemberId(member.getId());

        List<Product> productList = productService.findProductListByProductId(productIds);

        List<ProductLikeView> productLikeViews = productList.stream()
                .map(product -> {
                    return ProductLikeView.of(
                            product.getId(),
                            product.getName(),
                            product.getLikeCount()
                    );
                })
                .collect(Collectors.toList());

        return productLikeViews;
    }
}
