package org.camunda.bpm.example.process;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.camunda.bpm.json.elements.FlowElement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

public class CreateProcessFromXMLsJSON {

	public static void main(String[] args) throws IOException, ParseException {
		String path = "C:\\Siva\\Java\\IBPM\\FromXML";
		FileReader reader = new FileReader(path+"\\ExclusiveGateway 2.json");
	    JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
		JSONArray rootElement = (JSONArray)jsonObject.get("rootElement");
		JSONArray flowElementArray =(JSONArray) ((JSONObject)rootElement.get(0)).get("flowElement");
		JSONObject flowElementObj = (JSONObject) flowElementArray.get(0);
		//System.out.println(flowElementObj);
		
		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		objectMapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		String json = flowElementObj.toJSONString();
		FlowElement startFlowElement = objectMapper.readValue(json, FlowElement.class);
		//System.out.println(startFlowElement);
		
		List<FlowElement> flowList = objectMapper.readValue(flowElementArray.toJSONString(),new TypeReference<List<FlowElement>>(){});
		//System.out.println(flowList);
		flowList.stream()
				.filter(flow -> ((flow.getOutgoing() != null && flow.getOutgoing().size()>0) || (flow.getIncoming() != null  && flow.getIncoming().size()>0)))
				.forEach(System.out::println);
	}

}
