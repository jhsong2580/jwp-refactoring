package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@Service
@Transactional
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public ProductResponse create(final ProductRequest.Create request) {
		Product product = request.toEntity();
		product = productRepository.save(product);
		return new ProductResponse(product);
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> getList() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(ProductResponse::new).collect(Collectors.toList());
	}

}
