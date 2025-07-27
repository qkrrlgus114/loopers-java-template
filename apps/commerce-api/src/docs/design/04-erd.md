```mermaid

erDiagram
%% 등록시간, 수정시간 생략

    MEMBER {
        LONG    member_id      PK
        STRING  login_id
        STRING  password
        STRING  email
	      STRING  name
    }
    
    POINT {
        LONG    point_id      PK
        LONG    member_id     FK
        LONG    amount
    }
    
    POINT_HISTORY {
        LONG    point_history_id      PK
        LONG    member_id     FK
        LONG    amount
    }
    
    ROLE {
		    LONG    role_id        PK
		    LONG    member_id      FK
		    STRING  role_name      
    }

    BRAND {
        LONG    brand_id       PK
        STRING name
        STRING decription
    }

    PRODUCT {
        LONG    product_id     PK
        LONG    member_id      FK
        LONG    brand_id       FK
        STRING  product_name
    }

    PRODUCT_IMAGE {
        LONG    product_image_id PK
        LONG    product_id       FK
        STRING image_url
    }

    PRODUCT_LIKE {
        LONG   product_like_id  PK
        LONG   product_id       FK
        LONG   member_id        FK
    }

    ORDER {       
        LONG    order_id     PK
        LONG    member_id    FK
        LONG    total_amount
        STRING  status
    }

    ORDER_ITEM {
        LONG    order_item_id PK
        LONG    order_id      FK
        LONG    product_id    FK
        INT     quantity
        LONG    price
        STRING  status 
    }

    INVENTORY {
        LONG    inventory_id PK
        LONG    product_id   FK
        INT     stock
    }

    POINT_TRANSACTION {
        LONG    point_transaction_id PK
        LONG    member_id            FK
        LONG    order_id             FK
        INT     amount
        STRING  type
    }

    MEMBER        ||--o{ PRODUCT            : has
    MEMBER        ||--o{ ROLE               : has
    BRAND         ||--o{ PRODUCT            : has
    PRODUCT       ||--o{ PRODUCT_IMAGE      : has
    PRODUCT       ||--o{ PRODUCT_LIKE       : has
    MEMBER        ||--o{ PRODUCT_LIKE       : has
    MEMBER        ||--o{ ORDER              : has
		ORDER         ||--o{ ORDER_ITEM         : has
    PRODUCT       ||--o{ ORDER_ITEM         : has
    PRODUCT       ||--|| INVENTORY          : has
    MEMBER        ||--o{ POINT_TRANSACTION  : has
    ORDER         ||--o{ POINT_TRANSACTION  : has
