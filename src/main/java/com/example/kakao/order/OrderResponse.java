package com.example.kakao.order;

import java.util.List;
import java.util.stream.Collectors;

import com.example.kakao.cart.Cart;
import com.example.kakao.order.item.Item;
import com.example.kakao.product.Product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class OrderResponse {

    // (기능4) 주문상품 정보조회 (유저별)
    @ToString
    @Getter
    @Setter
    public static class FindAllByUserDTO {
        private Integer totalPrice;
        private List<CartDTO> carts;

        public FindAllByUserDTO(List<Cart> cartLists) {
            this.totalPrice = cartLists.stream()
                                            .mapToInt(cart -> cart.getPrice()).sum();
            this.carts = cartLists.stream()
                                        .map(cart -> new CartDTO(cart))
                                        .collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class CartDTO{
            private Integer cartId;
            private String optionName;
            private Integer price;
            private Integer quantity;

            public CartDTO(Cart cart) {
                this.cartId = cart.getId();
                this.optionName = cart.getOption().getOptionName();
                this.price = cart.getPrice();
                this.quantity = cart.getQuantity();
            }           
        }
    }

    // (기능5) 주문결과 확인
    @ToString
    @Getter
    @Setter
    public static class FindByIdDTO {
        private Integer orderId;
        private List<ProductDTO> orderItems;
        private Integer totalPrice;

        public FindByIdDTO(Order order, List<Item> items) {
            this.orderId = order.getId();
            this.orderItems = items.stream()
                                        .map(item -> item.getOption().getProduct()).distinct()
                                        .map(product -> new ProductDTO(product, items))
                                        .collect(Collectors.toList());
            this.totalPrice = items.stream()
                                        .mapToInt(item -> item.getOption().getPrice() * item.getQuantity()).sum();
        }

        @Getter @Setter
        public class ProductDTO{
            private Integer productId;
            private String productName;
            private List<ItemDTO> options;

            public ProductDTO(Product product, List<Item> items) {
                this.productId = product.getId();
                this.productName = product.getProductName();
                this.options = items.stream()
                                        .filter(item -> item.getOption().getProduct().getId() == product.getId())
                                        .map(item -> new ItemDTO(item))
                                        .collect(Collectors.toList());
            }

            @Getter @Setter
            public class ItemDTO{
                private Integer optionId;
                private String optionName;
                private Integer quantity;
                private Integer price;
                public ItemDTO(Item item) {
                    this.optionId = item.getId();
                    this.optionName = item.getOption().getOptionName();
                    this.quantity = item.getQuantity();
                    this.price = item.getOption().getPrice() * item.getQuantity();
                }               
            }
        }
    }
}
