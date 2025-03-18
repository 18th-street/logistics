package auth;

import lombok.Getter;

@Getter
public enum Role {
	MASTER("마스터 관리자"),
	HUB("허브 관리자"),
	DELIVERY("배송 관리자"),
	COMPANY("업체 관리자");

	private final String description;

	Role(String description) {
		this.description = description;
	}
}


