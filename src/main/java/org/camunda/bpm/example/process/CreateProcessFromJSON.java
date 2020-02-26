package org.camunda.bpm.example.process;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.GatewayDirection;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.EndEventBuilder;
import org.camunda.bpm.model.bpmn.builder.ExclusiveGatewayBuilder;
import org.camunda.bpm.model.bpmn.builder.StartEventBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CreateProcessFromJSON {
	private static StartEventBuilder startEventBuilder= null;
	private static AbstractFlowNodeBuilder nodeBuilder = null;
	public static void main(String[] args) throws IOException, ParseException {
		String path = "C:\\Siva\\Java\\IBPM\\REST Responses";
		FileReader reader = new FileReader(path+"\\SimpleSystemTaskProcess.json");
	    JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
		JSONObject dataObject =(JSONObject) jsonObject.get("data"); 
		JSONArray daiagramSteps = (JSONArray) ((JSONObject)dataObject.get("Diagram")).get("step");
	   // System.out.println(daiagramSteps);
	    //daiagramSteps.forEach(System.out::println);
		Map<String, JSONObject> stepsMap = new HashMap<>();
		Map<String, List<String>> stepsConnectionMap = new HashMap<>();
		List<String> startIdList = new ArrayList<>();
		List<String> endIdList = new ArrayList<>();
		for(int i=0;i< daiagramSteps.size();i++) {
			JSONObject obj = (JSONObject) daiagramSteps.get(i);
			stepsMap.put(obj.get("ID").toString(),obj);
			JSONArray lines = (JSONArray) obj.get("lines");
			//System.out.println(lines);
			List<String> linesList = new ArrayList<>();
			if(lines != null) {
				for(int j=0;j<lines.size();j++) {
					linesList.add(((JSONObject) lines.get(j)).get("to").toString());
				}
			}
			stepsConnectionMap.put(obj.get("ID").toString(), linesList);
			if(obj.get("type").toString().equalsIgnoreCase("event")) {
				if(lines == null) {
					endIdList.add(obj.get("ID").toString());
				}
				else {
					startIdList.add(obj.get("ID").toString());
				}
			}
		}
		//System.out.println(stepsMap.get(endIdList.get(0)));
		//System.out.println(stepsMap.get(startIdList.get(0)));
		List<JSONObject> links = new ArrayList<>();
		String startNodeId = startIdList.get(0);
		JSONObject startNode = stepsMap.get(startNodeId);
		links.add(stepsMap.get(startIdList.get(0)));
		boolean foundLink = ((JSONArray)startNode.get("lines"))==null?false:true;
		List<String> visitedNodesList = new ArrayList<>();
		visitedNodesList.add(startNodeId);
		List<String> moveToNodeList =  new ArrayList<>();
		BpmnModelInstance modelInstance = null;
		String processName = ((JSONObject)dataObject.get("Header")).get("name").toString();
		nodeBuilder=Bpmn.createExecutableProcess("org.camunda.bpm."+processName)
			      .name(processName)
			      .startEvent().name(startNode.get("name").toString());
		while(foundLink) {
			JSONArray lines = (JSONArray)startNode.get("lines");
			System.out.println(startNode.get("name").toString()+" :: "+startNode);
			if(lines != null) {
				String nextNodeId = ((JSONObject)lines.get(0)).get("to").toString();
				if(visitedNodesList.contains(nextNodeId)) {
					//use moveTo API call
					moveToNodeList.remove(0);
					System.out.println("Move TO ");
					if(moveToNodeList.size() == 0) {
						foundLink = false;
					}
					else {
						startNode = stepsMap.get(moveToNodeList.get(0));
						//startEventBuilder= createNode(startEventBuilder,startNode);
					}
				}
				else {
					if(lines.size()>1) {
						moveToNodeList.add(startNode.get("ID").toString());
					}
					startNode = stepsMap.get(nextNodeId);
					links.add(startNode);
					visitedNodesList.add(nextNodeId);
					createNode(startNode);
				}
			}
			else {
				if(moveToNodeList.size() == 0) {
					foundLink = false;
				}
				else {
					startNode = stepsMap.get(moveToNodeList.get(0));
				}
			}
		}
		modelInstance = nodeBuilder.done();
		System.out.println(links.size());
		Bpmn.writeModelToStream(System.out, modelInstance);
		
	}
	
	private static AbstractFlowNodeBuilder createNode(JSONObject node ) {
		String name = node.get("name").toString();
		String ID = node.get("ID").toString();
		String type = node.get("type").toString();
		AbstractFlowNodeBuilder obj = null;
		if(type.equals("event")) {
			 nodeBuilder.endEvent()
				.name(name);
				//.id(ID);
		}
		else if(type.equals("gateway")) {
			if(((JSONArray)node.get("lines")).size() >1){
				nodeBuilder.exclusiveGateway()
					.name(name)
					//.id(ID)
					.gatewayDirection(GatewayDirection.Diverging)
					.condition("yes", "${approved}");
			}
			else {
				 nodeBuilder.inclusiveGateway()
				.name(name)
				//.id(ID)
				.gatewayDirection(GatewayDirection.Converging);
			}
		}
		else if(type.equals("activity")) {
			 nodeBuilder.serviceTask()
				.name(name)
				//.id(ID)
				.camundaClass("org.camunda.bpm.example.service.SaveEmployeeService");
		}
		return obj;
	}

}
