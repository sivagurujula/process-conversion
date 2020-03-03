package org.camunda.bpm.example.process;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import org.camunda.bpm.json.elements.ConvertedFlowElement;
import org.camunda.bpm.json.elements.FlowElement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

public class CreateProcessFromXMLsJSON {
	
	private static int seqFlow = 1;
	public static void main(String[] args) throws IOException, ParseException {
		String path = "C:\\Siva\\Java\\IBPM\\FromXML";
		FileReader reader = new FileReader(path + "\\ExclusiveGateway 2.json");
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
		JSONArray rootElement = (JSONArray) jsonObject.get("rootElement");
		JSONArray flowElementArray = (JSONArray) ((JSONObject) rootElement.get(0)).get("flowElement");
		JSONObject flowElementObj = (JSONObject) flowElementArray.get(0);
		// System.out.println(flowElementObj);

		ObjectMapper objectMapper = new ObjectMapper();
		// objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
		// false);
		// objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		// objectMapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		String json = flowElementObj.toJSONString();
		FlowElement startFlowElement = objectMapper.readValue(json, FlowElement.class);
		// System.out.println(startFlowElement);

		List<FlowElement> flowList = objectMapper.readValue(flowElementArray.toJSONString(),
				new TypeReference<List<FlowElement>>() {
				});
		// System.out.println(flowList);
		/*
		 * flowList.stream().filter(flow -> ((flow.getOutgoing() != null &&
		 * flow.getOutgoing().size() > 0) || (flow.getIncoming() != null &&
		 * flow.getIncoming().size() > 0))).forEach(System.out::println);
		 */
		Map<String, String> sequenceFlowMap = flowList.stream()
				.filter(flow -> flow.getDeclaredType().equals("sequenceFlow"))
				.collect(Collectors.toMap(FlowElement::getId, FlowElement::getName));
		System.out.println("basic");
		
		
		List<ConvertedFlowElement> convertedList = new ArrayList<ConvertedFlowElement>();
		convertedList.add(convert(flowList.get(0)));
	
		String outgoingNode = convertedList.get(0).getOutgoing().get(0);
		Queue<String> splitPointsQueue = new LinkedList<>();
		splitPointsQueue.add("Start");
		Map<String, String> nodeSequenceFlowIdsMap = new HashMap<String, String>();
		while( !splitPointsQueue.isEmpty()) {
			for(FlowElement innerFlow: flowList) {
				if (innerFlow.getIncoming() != null && outgoingNode.equals(innerFlow.getIncoming().get(0)) && !nodeSequenceFlowIdsMap.containsKey(outgoingNode) ) {
					//System.out.println(innerFlow);
					ConvertedFlowElement convertedEle = null;
					if(innerFlow.getOutgoing() != null) {
						nodeSequenceFlowIdsMap.put(outgoingNode, innerFlow.getId());
						outgoingNode = innerFlow.getOutgoing().get(0);
						if(innerFlow.getOutgoing().size()>1 && !nodeSequenceFlowIdsMap.containsKey(outgoingNode)) {
							//System.out.println("addall");
							if(splitPointsQueue.peek().equals("Start")) {
								splitPointsQueue.poll();
							}
							splitPointsQueue.addAll(innerFlow.getOutgoing().subList(1,innerFlow.getOutgoing().size()));
							
							//System.out.println("+++++++++++");
							//System.out.println(splitPointsQueue);
							//System.out.println("+++++++++++");
							convertedEle= convert(innerFlow);
							List<Integer> nodes = new ArrayList<>();
							nodes.add(convertedEle.getSeq()+1);
							convertedEle.setToNodes(nodes);
							//System.out.println(convertedEle);
						}
						else if(!nodeSequenceFlowIdsMap.containsKey(outgoingNode)) {
							//System.out.println("new node");
							convertedEle= convert(innerFlow);
						}
						else {
							if(outgoingNode.equals(splitPointsQueue.peek()) ) {
								splitPointsQueue.poll();
								//System.out.println("Already added");
								break;
							}
							outgoingNode = splitPointsQueue.poll();
							//System.out.println("poll 1 ");
							
							
						}
						convertedList.add(convertedEle);
						
					}
					else {
						//System.out.println("poll 2: "+splitPointsQueue.peek());
						convertedList.add(convert(innerFlow));
						if(splitPointsQueue.peek().equals("Start") || nodeSequenceFlowIdsMap.containsKey(splitPointsQueue.peek())) {
							if(nodeSequenceFlowIdsMap.containsKey(splitPointsQueue.peek())) {
								String id = nodeSequenceFlowIdsMap.get(splitPointsQueue.peek());
								System.out.println(id);
								Integer seqId =convertedList.stream()
													.filter(flow -> flow.getId().equals(id))
													//.forEach(System.out::println);
													.mapToInt(ConvertedFlowElement::getSeq).findFirst().getAsInt();
								System.out.println(seqId);
								convertedList.stream()
												.filter(flow -> flow.getToNodes() != null)
												.filter(flow -> flow.getOutgoing().contains(splitPointsQueue.peek()))
												.forEach(flow -> {List<Integer> nodes = flow.getToNodes();
													nodes.add(seqId);
													flow.setToNodes(nodes);});
												
							}
							outgoingNode = splitPointsQueue.poll();
							//System.out.println("start or other node");
							
						}
						else {
							outgoingNode = splitPointsQueue.peek();
							//System.out.println("not start or other node");
						}
						
					}
					break;
				}				
			}
		}
		convertedList.stream().forEach(System.out::println);

	}
	private static ConvertedFlowElement convert(FlowElement flowEle) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		objectMapper.writeValue(writer, flowEle);
		ConvertedFlowElement obj1 =  objectMapper.readValue(writer.toString(), ConvertedFlowElement.class);
		obj1.setSeq(seqFlow++);
		//System.out.println(obj1);
		return obj1;
	}
}
