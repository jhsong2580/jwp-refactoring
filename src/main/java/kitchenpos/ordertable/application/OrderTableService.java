package kitchenpos.ordertable.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderTableService {
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderTableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest request) {
		return OrderTableResponse.of(orderTableRepository.save(request.toEntity()));
	}

	public List<OrderTableResponse> listTables() {
		return OrderTableResponse.of(orderTableRepository.findAll());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(() -> new IllegalArgumentException());

		if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException("계산완료된 테이블인 경우에만 상태를 변경할 수 있습니다.");
		}
		orderTable.changeEmpty(request.isEmpty());
		return OrderTableResponse.of(orderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(() -> new IllegalArgumentException());
		orderTable.changeNumberOfGuests(request.getNumberOfGuests());
		return OrderTableResponse.of(orderTable);
	}
}