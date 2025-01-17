package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", foreignKey = @ForeignKey(name = "fk_OrderLineItem_Order"))
    private Order order;
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Long menuId, long quantity) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderLineItem)) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq)
            && Objects.equals(order, that.order) && Objects.equals(menuId,
            that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, menuId, quantity);
    }
}
