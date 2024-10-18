package kr.gyk.voyageventures.beautyq.lite.web.service.repository;

import kr.gyk.voyageventures.beautyq.lite.web.service.entity.CosmeticIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<CosmeticIngredient, Long> {
}
