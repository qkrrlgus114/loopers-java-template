%% + : public
%% - : private
%% # : protected

```mermaid
classDiagram

    %% 회원
    class Member {
        -memberId      : Long
        -pointBalance  : Long
        -roles         : Set
        +getMemberId() Long
    }
    
    %% 권한
    class Role {
			  -roleId       : Long
			  +memberId     : Long
			  +roleName     : String
			  +getRoleId    : Long
		}
    
    %% 포인트
    class Point {
		    -pointId       : Long
		    +memberId      : Long
		    -amount        : Long
		    +getPointId() Long
		    +getAmount() Long
		}
		      
		    
    %% 브랜드
    class Brand {
        -brandId  : Long
        +getBrandId() Long
    }

    %% 상품
    class Product {
        -productId : Long
        +memberId  : Long
        +brandId   : Long
        +getProductId() Long
    }

    %% 상품 이미지
    class ProductImage {
        -productImageId  : Long
        -productImageUrl : String
        +productId       : Long
        +getProductImageId() Long
    }

    %% 상품 좋아요
    class ProductLike {
        -productLikeId : Long
        +productId     : Long
        +memberId      : Long
        +getProductLikeId() Long
    }

    %% 주문
    class Order {
        -orderId     : Long
        +memberId    : Long
        -totalAmount : Long
        -status      : OrderStatus
        +getOrderId() Long
    }

    %% 주문 항목
    class OrderItem {
        -orderItemId : Long
        +orderId     : Long
        +productId   : Long
        -quantity    : Int
        -price       : Long
    }

    %% 재고
    class Inventory {
        -inventoryId : Long
        +productId   : Long
        -stock       : Long
    }

    %% 포인트 트랜잭션
    class PointTransaction {
        -pointTransactionId : Long
        +memberId           : Long
        +orderId            : Long
        -amount             : Long
        -type               : TransactionType
    }

    class OrderStatus {
        <<enumeration>>
        PENDING - 결제중
        PAYED - 결제완료
        CANCELLED - 취소
    }

    class TransactionType {
        <<enumeration>>
        CHARGE - 충전
        PAY - 지불
    }

    Member  "1" --> "0..*" Product        : 소유
    Member  "1" --> "1..*" Role           : 소유
    Member  "1" --> "1*"   Point          : 소유
    Brand   "1" --> "0..*" Product        : 소유
    Product "1" --> "1..*" ProductImage   : 소유
    Product "1" --> "0..*" ProductLike    : 소유
    Member  "1" --> "0..*" ProductLike    : 소유
    Member  "1" --> "0..*" Order          : 소유
    Order   "1" --> "1..*" OrderItem      : 소유
    Product "1" --> "0..*" OrderItem      : 소유
    Product "1" --> "1"    Inventory      : 소유
    Member  "1" --> "0..*" PointTransaction : 소유