package com.hpifive.line.bcs.webhook.akka;

import java.util.ArrayList;
import java.util.List;

import com.hpifive.line.bcs.webhook.akka.supervision.SupervisionStrategyFactory;

import akka.actor.AbstractActor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.DefaultResizer;
import akka.routing.RoundRobinPool;
import akka.routing.Routee;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;

public class AkkaRouterFactory<T>{

    private Router router;
    
    private ActorRef routerActor;
    
    private int lowerBound = 5;

	public AkkaRouterFactory(ActorContext context, Class<T> targetClass){
		int size = 10;
		this.createRouter(context, targetClass, size, false);
	}
	
	public AkkaRouterFactory(ActorContext context, Class<T> targetClass, int size){
		this.createRouter(context, targetClass, size, false);
	}
	
	public AkkaRouterFactory(ActorContext context, Class<T> targetClass, boolean isRrouterActor){
		int size = 10;
		this.createRouter(context, targetClass, size, isRrouterActor);
	}
	
	public AkkaRouterFactory(ActorContext context, Class<T> targetClass, int size, boolean isRrouterActor){
		this.createRouter(context, targetClass, size, isRrouterActor);
	}
	
	private void createRouter(ActorContext context, Class<T> targetClass, int size, boolean isRrouterActor){

		if(isRrouterActor){
			DefaultResizer resizer = new DefaultResizer(lowerBound, size);
			
			routerActor = context.actorOf(
					new RoundRobinPool(size)
					.withSupervisorStrategy(SupervisionStrategyFactory.createApplicationActorStrategy())
					.withResizer(resizer).props( Props.create(targetClass)), "routerActor" + this.toString());
		}
		else{
			
			List<Routee> routees = new ArrayList<>();
		    for (int i = 0; i < 20; i++) {
		      ActorRef r = context.actorOf(Props.create(targetClass));
		      context.watch(r);
		      routees.add(new ActorRefRoutee(r));
		    }
		    router =  new Router(new SmallestMailboxRoutingLogic(), routees);
		}
	}

	public ActorRef getRouterActor() {
		return routerActor;
	}

	public void setRouterActor(ActorRef routerActor) {
		this.routerActor = routerActor;
	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}
	
}
