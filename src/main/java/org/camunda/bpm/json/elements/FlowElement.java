package org.camunda.bpm.json.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowElement {
	private String name;
	private String declaredType;
	private String id;
	@JsonProperty("flowElement")
	private List<String> outgoing;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeclaredType() {
		return declaredType;
	}
	public void setDeclaredType(String declaredType) {
		this.declaredType = declaredType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "FlowElement [name=" + name + ", declaredType=" + declaredType + ", id=" + id + ", outgoing=" + outgoing
				+ "]";
	}
	
	
}
