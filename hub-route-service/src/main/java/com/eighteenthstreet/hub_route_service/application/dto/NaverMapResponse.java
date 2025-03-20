package com.eighteenthstreet.hub_route_service.application.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverMapResponse {

	@JsonProperty("route")
	private Route route;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Route {

		@JsonProperty("traoptimal")
		private List<Traoptimal> traoptimal;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Traoptimal {
		@JsonProperty("summary")
		private Summary summary;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Summary {
		@JsonProperty("distance")
		private Double distance;

		@JsonProperty("duration")
		private Double duration;
	}

	// 거리와 시간 데이터를 반환하는 헬퍼 메서드 추가
	public Double getDistance() {
		return (route != null && route.traoptimal != null && !route.traoptimal.isEmpty())
			? route.traoptimal.get(0).summary.distance
			: null;
	}

	public Double getDuration() {
		return (route != null && route.traoptimal != null && !route.traoptimal.isEmpty())
			? route.traoptimal.get(0).summary.duration
			: null;
	}
}
