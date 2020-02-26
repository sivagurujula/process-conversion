package org.camunda.bpm.example.service;

import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class SaveEmployeeService implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("SaveEmployeeService");
		Random rnd = new Random();
		execution.setVariable("approved", rnd.nextBoolean());
	}

}
