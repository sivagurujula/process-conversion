package org.camunda.bpm.example.process;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;

public class CreateProcess2 {

	public static void main(String[] args) {
		BpmnModelInstance modelInstance = null;

		AbstractFlowNodeBuilder nodeBuilder = Bpmn.createExecutableProcess("org.camunda.bpm.ExclusiveGateway")
				.name("ExclusiveGateway").startEvent().name("Start");

		nodeBuilder.serviceTask().name("Save Employee Details")
				.camundaClass("org.camunda.bpm.example.service.SaveEmployeeService");
		nodeBuilder.endEvent().name("End");
		modelInstance = nodeBuilder.done();
		Bpmn.writeModelToStream(System.out, modelInstance);
	}

}
