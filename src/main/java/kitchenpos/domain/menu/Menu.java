package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.exception.CreateMenuException;
import kitchenpos.exception.MenuPriceException;

@Entity
@Table(name = "menu")
public class Menu {

    private static final String MENU_GROUP_IS_NOT_NULL = "메뉴생성 시 메뉴그룹이 필수입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = MenuProducts.createEmpty();

    protected Menu() {}

    private Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
        validateCreateMenu(menuGroup);
        return new Menu(name, price, menuGroup);
    }

    public Long getId() {
        return id;
    }

    public String findName() {
        return name.getValue();
    }

    public BigDecimal findPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return this.menuGroup.getId();
    }

    public List<MenuProduct> findMenuProducts() {
        return menuProducts.getReadOnlyValues();
    }

    public void appendMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.mappedByMenu(this);
    }

    public void appendAllMenuProducts(List<MenuProduct> menuProducts) {
        validateMenuPrice(menuProducts);
        this.menuProducts.addAll(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.mappedByMenu(this));
    }

    public MenuGroup getMenuGroup() {
        return this.menuGroup;
    }

    private static void validateCreateMenu(MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new CreateMenuException(MENU_GROUP_IS_NOT_NULL);
        }
    }

    private void validateMenuPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sum) > 0) {
            throw new MenuPriceException(price, sum);
        }
    }
}