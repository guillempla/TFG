package com.example.workflow;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.io.File;
import java.util.*;

public class ModelReader {
    String path;
    File file;
    BpmnModelInstance modelInstance;
    Collection<ModelElementInstance> read_elements;
    Map<String, int[]> pools;

    public ModelReader(String path) {
        this.path = path;
        this.file = new File(path);
        this.modelInstance = Bpmn.readModelFromFile(file);
        this.pools = new HashMap<>();
        this.getLanesFromModel();
    }

    public void getLanesFromModel() {
        Collection<ModelElementInstance> lanesSets = getElementsOfType(LaneSet.class);
        for (ModelElementInstance laneSetInstance : lanesSets) {
            LaneSet laneSet = (LaneSet) laneSetInstance;
            System.out.println(laneSet.getName());
        }
    }

    public void getElementsFromModel() {
        // Find all elements of the type Task
        Collection<ModelElementInstance> taskInstances = getElementsOfType(Task.class);
        for (ModelElementInstance taskInstance : taskInstances) {
            System.out.println(taskInstance.getAttributeValue("name"));
        }
        System.out.println();

        // Find all elements of type Start Event
        Collection<ModelElementInstance> startInstances = getElementsOfType(StartEvent.class);
        for (ModelElementInstance startInstance : startInstances) {
            StartEvent start = (StartEvent) startInstance;
            System.out.println(start.getName());
            Collection<SequenceFlow> sequenceFlow = start.getOutgoing();
            for (SequenceFlow flow : sequenceFlow) {
                FlowNode source = flow.getTarget();
                System.out.println(source.getName());
            }
        }
        System.out.println();

        ModelElementType flowNode = modelInstance.getModel().getType(FlowNode.class);
        System.out.println(flowNode);
    }

    public Collection<ModelElementInstance> getElementsOfType(Class c) {
        ModelElementType taskType = modelInstance.getModel().getType(c);
        return modelInstance.getModelElementsByType(taskType);
    }

    // Find the following flow nodes of a task or a gateway
    public static Collection<FlowNode> getFlowingFlowNodes(FlowNode node) {
        Collection<FlowNode> followingFlowNodes = new ArrayList<>();
        for (SequenceFlow sequenceFlow : node.getOutgoing()) {
            followingFlowNodes.add(sequenceFlow.getTarget());
        }
        return followingFlowNodes;
    }

    public static Collection<FlowNode> getAllFlowNodes(FlowNode source) {
        Collection<FlowNode> allFlowNodes = new ArrayList<>();
        Collection<FlowNode> flowNodes = getFlowingFlowNodes(source);
        for (FlowNode flowNode : flowNodes) {
            System.out.println(flowNode.getName());
        }
        return allFlowNodes;
    }
}
