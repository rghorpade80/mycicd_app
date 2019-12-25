package com.pvmsys.brix.graph.websession.impl;

import org.apache.tinkerpop.gremlin.structure.Graph;

import com.pvmsys.brix.graph.websession.WebAppSessionContext;

public interface GraphSessionContext extends WebAppSessionContext 
{

	Graph getGraph();
}
