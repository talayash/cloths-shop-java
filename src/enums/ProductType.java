package enums;

import java.util.stream.Stream;

// Product Type enum
public enum ProductType {
	
	Soccer, 
	NBA, 
	PoloShirts, 
	SportShoes, 
	Kids;

	
	public static Stream<ProductType> stream(){
		return Stream.of(ProductType.values());
	}
}
