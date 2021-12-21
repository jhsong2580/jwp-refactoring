package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.exception.table.HasOtherTableGroupException;
import kitchenpos.exception.table.NotEmptyOrderTableException;
import kitchenpos.exception.table.NotGroupingOrderTableCountException;
import kitchenpos.vo.TableGroupId;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(Long id) {
        this.id = id;
    }

    public static TableGroup of(Long id, OrderTables orderTables) {
        checkOrderTableSize(orderTables);

        for (int index = 0; index < orderTables.size(); index++) {
            checkHasTableGroup(orderTables.get(index));
            checkNotEmptyTable(orderTables.get(index));
        }

        TableGroup tableGroup = new TableGroup(id);

        for (int index = 0; index < orderTables.size(); index++) {
            orderTables.get(index).groupingTable(TableGroupId.of(tableGroup));
        }

        return tableGroup;
    }

    public static TableGroup of(OrderTables orderTables) {
        return TableGroup.of(null, orderTables);
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    private static void checkHasTableGroup(final OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new HasOtherTableGroupException();
        }
    }

    private static void checkNotEmptyTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new NotEmptyOrderTableException();
        }
    }
    
    private static void checkOrderTableSize(final OrderTables orderTables) {
        if (orderTables.size() < 2) {
            throw new NotGroupingOrderTableCountException();
        }
    }
}
