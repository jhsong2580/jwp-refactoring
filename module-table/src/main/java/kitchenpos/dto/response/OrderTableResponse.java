package kitchenpos.dto.response;

import kitchenpos.domain.OrderTable;
import org.springframework.util.ObjectUtils;

public class OrderTableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    protected OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        Long orderTableGroupId;
        if (ObjectUtils.isEmpty(orderTable.getTableGroup())) {
            orderTableGroupId = null;
        } else {
            orderTableGroupId = orderTable.getTableGroup().getId();
        }
        return new OrderTableResponse(orderTable.getId(), orderTableGroupId,
            orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
