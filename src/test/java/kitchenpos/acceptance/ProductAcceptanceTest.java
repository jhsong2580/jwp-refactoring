package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("제품(상품) 인수테스트 기능")
public class ProductAcceptanceTest extends AcceptanceTest {
    private static final String PRODUCT_URI = "/api/products";

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    /**
     *  When 제품(상품)을 만들면
     *  Then 제품(상품) 조회할 수 있다
     */
    @Test
    @DisplayName("제품(상품)을 만들면 조회 할 수 있다.")
    void createProduct() {
        // when
        final ExtractableResponse<Response> 제품_생성_요청_결과 = 제품_생성_요청("강정치킨", 17_000);
        제품_생성_요청_확인(제품_생성_요청_결과);

        // then
        final ExtractableResponse<Response> 제품_조회_결과 = 제품_조회();
        제품_조회_확인(제품_조회_결과, Arrays.asList(new Product("강정치킨", BigDecimal.valueOf(17_000.0))));
    }

    public static ExtractableResponse<Response> 제품_생성_요청(String 제품명, Integer 금액) {
        final Product 생성할_제품 = new Product(제품명, BigDecimal.valueOf(금액));

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(생성할_제품)
                .when().post(PRODUCT_URI)
                .then().log().all()
                .extract();
    }

    public static void 제품_생성_요청_확인(ExtractableResponse<Response> 제품_생성_요청_결과) {
        assertThat(제품_생성_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 제품_조회() {
        return RestAssured.given().log().all()
                .when().get(PRODUCT_URI)
                .then().log().all()
                .extract();
    }

    public static void 제품_조회_확인(ExtractableResponse<Response> 제품_조회_결과, List<Product> 예상된_제품_조회_결과) {
        final List<Product> actual = 제품_조회_결과.body().jsonPath().getList(".", Product.class);

        assertAll(
                () -> assertThat(제품_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual).hasSize(예상된_제품_조회_결과.size()),
                () -> 제품_내용_확인(actual, 예상된_제품_조회_결과)
        );
    }

    public static void 제품_내용_확인(List<Product> 제품_리스트1, List<Product> 제품_리스트2) {
        for (int idx = 0; idx < 제품_리스트1.size(); idx++) {
            int innerIdx = idx;
            assertAll(
                    () -> assertThat(제품_리스트1.get(innerIdx).getName()).isEqualTo(제품_리스트2.get(innerIdx).getName()),
                    () -> assertThat(제품_리스트1.get(innerIdx).getPrice()).isEqualTo(제품_리스트2.get(innerIdx).getPrice())
            );
        }
    }
}