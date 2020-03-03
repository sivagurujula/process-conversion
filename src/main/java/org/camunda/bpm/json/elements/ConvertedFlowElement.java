package org.camunda.bpm.json.elements;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConvertedFlowElement {
	private String name;
	private String declaredType;
	private String id;
	private Integer seq;
	private List<Integer> toNodes;
	private List<String> outgoing;
	private List<String> incoming;
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
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public List<Integer> getToNodes() {
		return toNodes;
	}
	public void setToNodes(List<Integer> toNodes) {
		this.toNodes = toNodes;
	}
	@Override
	public String toString() {
		return "ConvertedFlowElement [name=" + name + ", declaredType=" + declaredType + ", id=" + id + ", seq=" + seq
				+ ", toNodes=" + toNodes + ", outgoing=" + outgoing + ", incoming=" + incoming + "]";
	}
	public List<String> getOutgoing() {
		return outgoing;
	}
	public void setOutgoing(List<String> outgoing) {
		this.outgoing = outgoing;
	}
	public List<String> getIncoming() {
		return incoming;
	}
	public void setIncoming(List<String> incoming) {
		this.incoming = incoming;
	}
	
	
}
