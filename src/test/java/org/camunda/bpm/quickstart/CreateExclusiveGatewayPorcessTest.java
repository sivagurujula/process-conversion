package org.camunda.bpm.quickstart;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.example.invoice.service.ArchiveInvoiceService;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.GatewayDirection;
import org.junit.Rule;
import org.junit.Test;

public class CreateExclusiveGatewayPorcessTest {
	@Rule
	  public ProcessEngineRule processEngine = new ProcessEngineRule();

	  @Test
	  public void testCreateExclusiveGatewayProcess() throws Exception {
	    BpmnModelInstance modelInstance = Bpmn.createExecutableProcess("org.camunda.bpm.ExclusiveGateway")
	      .name("ExclusiveGateway")
	      .startEvent()
	        .name("Start")
	      .serviceTask()
	        .name("Save Employee Details")
	        .camundaClass("org.camunda.bpm.example.service.SaveEmployeeService")
	      .exclusiveGateway()
	        .name("Exlusive Gateway Diverge")
	        .id("Diverge")
	        .gatewayDirection(GatewayDirection.Diverging)
	      .condition("yes", "${approved}")
	      .userTask()
	        .name("Submit Leave Manually")
	        .camundaFormKey("embedded:app:forms/prepare-bank-transfer.html")
	        .camundaCandidateGroups("accounting")
	      .inclusiveGateway()
	      	.name("Inclusive Gateway Join")
	      	.id("Join")
	      	.gatewayDirection(GatewayDirection.Converging)
	      .endEvent()
	        .name("End")
	      .moveToNode("Diverge")
	      .condition("no", "${!approved}")
	      .serviceTask()
	        .name("Submit to Leave System")
	        .camundaClass("org.camunda.bpm.example.service.SubmitLeaveToSystemService")
	      .connectTo("Join")
	      .done();

	      // deploy process model
	      processEngine.getRepositoryService().createDeployment().addModelInstance("ExclusiveGateway.bpmn", modelInstance).deploy();
	      
	      /*
	      // start process model
	      processEngine.getRuntimeService().startProcessInstanceByKey("ExclusiveGateway");

	      TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
	      // check and complete task "Assign Approver"
	      org.junit.Assert.assertEquals(1, taskQuery.count());
	      processEngine.getTaskService().complete(taskQuery.singleResult().getId());

	      // check and complete task "Approve Invoice"
	      Map<String, Object> variables = new HashMap<String, Object>();
	      variables.put("approved", true);

	      org.junit.Assert.assertEquals(1, taskQuery.count());
	      processEngine.getTaskService().complete(taskQuery.singleResult().getId(), variables);

	      // check and complete task "Prepare Bank Transfer"
	      org.junit.Assert.assertEquals(1, taskQuery.count());
	      processEngine.getTaskService().complete(taskQuery.singleResult().getId());

	      // check if Delegate was executed
	      org.junit.Assert.assertEquals(true, ArchiveInvoiceService.wasExecuted);

	      // check if process instance is ended
	      org.junit.Assert.assertEquals(0, processEngine.getRuntimeService().createProcessInstanceQuery().count());

			*/
	      /**
	       * to see the BPMN 2.0 process model XML on the console log
	       * copy the following code line at the end of the test case
	       *
	       * Bpmn.writeModelToStream(System.out, modelInstance);
	       */
	      Bpmn.writeModelToStream(System.out, modelInstance);
	  }
}
