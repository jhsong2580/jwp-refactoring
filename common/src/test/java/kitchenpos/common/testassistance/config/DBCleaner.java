package kitchenpos.common.testassistance.config;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class DBCleaner implements InitializingBean {
    private final JdbcTemplate jdbcTemplate;

    public DBCleaner(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void DbDataInitialize() {
        jdbcTemplate.execute("DELETE FROM order_line_item");
        jdbcTemplate.execute("DELETE FROM orders");
        jdbcTemplate.execute("DELETE FROM order_table");
        jdbcTemplate.execute("DELETE FROM table_group");
        jdbcTemplate.execute("DELETE FROM menu_product");
        jdbcTemplate.execute("DELETE FROM menu");
        jdbcTemplate.execute("DELETE FROM product");
        jdbcTemplate.execute("DELETE FROM menu_group");

        jdbcTemplate.execute("INSERT INTO menu_group (id, name) VALUES (1, '두마리메뉴')");
        jdbcTemplate.execute("INSERT INTO menu_group (id, name) VALUES (2, '한마리메뉴')");
        jdbcTemplate.execute("INSERT INTO menu_group (id, name) VALUES (3, '순살파닭두마리메뉴')");
        jdbcTemplate.execute("INSERT INTO menu_group (id, name) VALUES (4, '신메뉴')");
        jdbcTemplate.execute("INSERT INTO product (id, name, price) VALUES (1, '후라이드', 16000)");
        jdbcTemplate.execute("INSERT INTO product (id, name, price) VALUES (2, '양념치킨', 16000)");
        jdbcTemplate.execute("INSERT INTO product (id, name, price) VALUES (3, '반반치킨', 16000)");
        jdbcTemplate.execute("INSERT INTO product (id, name, price) VALUES (4, '통구이', 16000)");
        jdbcTemplate.execute("INSERT INTO product (id, name, price) VALUES (5, '간장치킨', 17000)");
        jdbcTemplate.execute("INSERT INTO product (id, name, price) VALUES (6, '순살치킨', 17000)");
        jdbcTemplate.execute("INSERT INTO menu (id, name, price, menu_group_id) VALUES (1, '후라이드치킨', 16000, 2)");
        jdbcTemplate.execute("INSERT INTO menu (id, name, price, menu_group_id) VALUES (2, '양념치킨', 16000, 2)");
        jdbcTemplate.execute("INSERT INTO menu (id, name, price, menu_group_id) VALUES (3, '반반치킨', 16000, 2)");
        jdbcTemplate.execute("INSERT INTO menu (id, name, price, menu_group_id) VALUES (4, '통구이', 16000, 2)");
        jdbcTemplate.execute("INSERT INTO menu (id, name, price, menu_group_id) VALUES (5, '간장치킨', 17000, 2)");
        jdbcTemplate.execute("INSERT INTO menu (id, name, price, menu_group_id) VALUES (6, '순살치킨', 17000, 2)");
        jdbcTemplate.execute("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (1, 1, 1)");
        jdbcTemplate.execute("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (2, 2, 1)");
        jdbcTemplate.execute("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (3, 3, 1)");
        jdbcTemplate.execute("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (4, 4, 1)");
        jdbcTemplate.execute("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (5, 5, 1)");
        jdbcTemplate.execute("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (6, 6, 1)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (1, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (2, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (3, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (4, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (5, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (6, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (7, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (8, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (9, 10, false)");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
