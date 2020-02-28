package org.camunda.bpm.json.elements;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtensionElements {
	private List<String> activityType;

	public List<String> getActivityType() {
		return activityType;
	}

	public void setActivityType(List<String> activityType) {
		this.activityType = activityType;
	}

	@Override
	public String toString() {
		return "ExtensionElements [activityType=" + activityType + "]";
	}

	
	
}
