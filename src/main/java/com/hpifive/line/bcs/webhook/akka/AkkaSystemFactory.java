package com.hpifive.line.bcs.webhook.akka;

import java.util.List;

import com.hpifive.line.bcs.webhook.akka.supervision.SupervisionStrategyFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;


public class AkkaSystemFactory<T>{

	public AkkaSystemFactory(List<ActorSystem> systems, List<ActorRef> masters, Class<T> targetClass, String systemName, String masterName){
		Integer size = 5;

		this.createListSystem(size, systems, masters, targetClass, systemName, masterName);
	}
	
	public AkkaSystemFactory(int size, List<ActorSystem> systems, List<ActorRef> masters, Class<T> targetClass, String systemName, String masterName){
		this.createListSystem(size, systems, masters, targetClass, systemName, masterName);
	}
	
	private void createListSystem(int size, List<ActorSystem> systems, List<ActorRef> masters, Class<T> targetClass, String systemName, String masterName){
		ActorSystem system = ActorSystem.create(systemName);
		systems.add(system);
		for(int i = 0; i < size; i++){
			masters.add(system.actorOf(
					new RoundRobinPool(size)
					.withSupervisorStrategy(SupervisionStrategyFactory.createApplicationActorStrategy())
					.props(Props.create(targetClass)), masterName+i));
		}
	}
}
