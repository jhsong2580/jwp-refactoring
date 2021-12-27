package kitchenpos.common.domain;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price {
    @Column
    private BigDecimal price;

    protected Price() {

    }

    public Price(int amount) {
        this(new BigDecimal(amount));
    }

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private void validate(BigDecimal price) {
        if (price == null) {
            throw new InputDataException(InputDataErrorCode.THE_PRICE_MUST_INPUT);
        }

        if (price.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new InputDataException(InputDataErrorCode.THE_PRICE_CAN_NOT_INPUT_LESS_THAN_ZERO);
        }
    }
}
