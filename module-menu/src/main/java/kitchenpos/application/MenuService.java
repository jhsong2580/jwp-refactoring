package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.event.MenuCreatedEvent;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.event.MenuCreateEvent;
import kitchenpos.exception.MenuException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
        ApplicationEventPublisher eventPublisher) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final List<MenuProductDTO> menuProducts = menuRequest.getMenuProducts();

        menuCreateEventPublish(menuRequest);

        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(() -> new MenuException("메뉴 그룹이 저장되어있어야 합니다"));
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(),
            menuGroup);

        setMenuProducts(menuProducts, menu);

        return MenuResponse.of(menuRepository.save(menu));
    }

    private void setMenuProducts(List<MenuProductDTO> menuProducts, Menu menu) {
        for (MenuProductDTO menuProductDTO : menuProducts) {
            menu.addMenuProduct(
                new MenuProduct(menu, menuProductDTO.getProductId(), menuProductDTO.getQuantity()));
        }
    }

    private void menuCreateEventPublish(MenuRequest menuRequest) {
        Map<Long, Long> quantityPerProduct = menuRequest.getQuantityPerProduct();
        eventPublisher.publishEvent(new MenuCreateEvent(
            new MenuCreatedEvent(quantityPerProduct, menuRequest.getPrice())));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }
}
