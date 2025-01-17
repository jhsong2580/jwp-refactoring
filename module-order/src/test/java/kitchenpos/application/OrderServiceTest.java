package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.OrderException;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;


    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private OrderRepository orderRepository;

    private Order order;

    private OrderLineItemDTO chickenOrder;
    private OrderLineItemDTO hamOrder;

    @BeforeEach
    public void init() {
        setOrderLineItem();

        orderService = new OrderService(orderRepository, eventPublisher);

        order = new Order();

        OrderLineItem hamOrderLineItem = new OrderLineItem(null, hamOrder.getMenuId(),
            1L);
        OrderLineItem chickenOrderLineItem = new OrderLineItem(null,
            chickenOrder.getMenuId(),
            1L);

        order.mapOrderLineItem(hamOrderLineItem);
        order.mapOrderLineItem(chickenOrderLineItem);

        order.startCooking();
    }


    private void setOrderLineItem() {
        chickenOrder = new OrderLineItemDTO();
        chickenOrder.setMenuId(1L);
        chickenOrder.setQuantity(1L);

        hamOrder = new OrderLineItemDTO();
        hamOrder.setMenuId(2L);
        hamOrder.setQuantity(2L);
    }

    @Test
    @DisplayName("주문 생성 정상로직")
    void createOrderHappyCase() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));

        orderRequest.setOrderTableId(1L);

        doNothing().when(eventPublisher)
            .publishEvent(any());
        when(orderRepository.save(any())).thenReturn(order);

        //when
        OrderResponse orderResponse = orderService.create(orderRequest);

        //then
        assertAll(
            () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(orderResponse.getOrderLineItems()).hasSize(2)
        );
    }

    @Test
    @DisplayName("주문 상품 없이 주문을 하면 에러 발생")
    void createWithoutItemsThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(
            OrderException.class);
    }

    @Test
    @DisplayName("주문 상품이 존재하지 않는것이 있다면 에러 발생")
    void createWithoutOrderThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));

        doThrow(IllegalArgumentException.class).when(eventPublisher)
            .publishEvent(any());

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문할 테이블이 존재하지 않다면 에러 발생")
    void createWithNotSavedTableThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));

        doThrow(IllegalArgumentException.class).when(eventPublisher)
            .publishEvent(any());

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문할 테이블이 비어있다면 에러 발생")
    void createWithEmptyTableThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));
        orderRequest.setOrderTableId(1L);

        doThrow(OrderException.class).when(eventPublisher)
            .publishEvent(any());

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(OrderException.class);
    }

    @Test
    @DisplayName("오더 상태 수정 정상로직")
    void changeOrderStatusHappyCase() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.COOKING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        //when
        OrderResponse orderResponse = orderService.changeOrderStatus(1L, orderRequest);

        assertThat(orderResponse.getOrderStatus()).isEqualTo("COOKING");
    }

    @Test
    @DisplayName("이미 완료된 오더 수정시 에러 발생")
    void changeOrderStatusAlreadyCompleteThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.COMPLETION);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderRequest))
            .isInstanceOf(OrderException.class);
    }
}