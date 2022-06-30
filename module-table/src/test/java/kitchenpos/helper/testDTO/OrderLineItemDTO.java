package kitchenpos.helper.testDTO;

public class OrderLineItemDTO {

    private Long menuId;
    private Long quantity;

    public OrderLineItemDTO() {
    }

    protected OrderLineItemDTO(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }


    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}