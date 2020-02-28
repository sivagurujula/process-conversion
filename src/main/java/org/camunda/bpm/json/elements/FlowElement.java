package org.camunda.bpm.json.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowElement {
	public List<String> getOutgoing() {
		return outgoing;
	}
	public void setOutgoing(List<String> outgoing) {
		this.outgoing = outgoing;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	private String name;
	private String declaredType;
	private String id;
	private List<String> outgoing;
	private List<String> incoming;
	private ExtensionElements extensionElements;
	private String gatewayDirection;
	
	public String getGatewayDirection() {
		return gatewayDirection;
	}
	public void setGatewayDirection(String gatewayDirection) {
		this.gatewayDirection = gatewayDirection;
	}
	public ExtensionElements getExtensionElements() {
		return extensionElements;
	}
	public void setExtensionElements(ExtensionElements extensionElements) {
		this.extensionElements = extensionElements;
	}
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
	public List<String> getIncoming() {
		return incoming;
	}
	public void setIncoming(List<String> incoming) {
		this.incoming = incoming;
	}
	@Override
	public String toString() {
		return "FlowElement [name=" + name + ", declaredType=" + declaredType + ", id=" + id + ", outgoing=" + outgoing
				+ ", incoming=" + incoming + ", extensionElements=" + extensionElements + ", gatewayDirection="
				+ gatewayDirection + ", additionalProperties=" + additionalProperties + "]";
	}
	
}
