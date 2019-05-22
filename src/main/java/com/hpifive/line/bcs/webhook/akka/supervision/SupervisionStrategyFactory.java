package com.hpifive.line.bcs.webhook.akka.supervision;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.japi.Function;

import scala.concurrent.duration.Duration;

public final class SupervisionStrategyFactory {

	private static final Logger logger = LoggerFactory.getLogger(SupervisionStrategyFactory.class);
	
	  private SupervisionStrategyFactory() {
	  }

	  public static SupervisorStrategy createIoRouterStrategy() {
	    return buildResumeOrEscalateStrategy();
	  }

	  public static SupervisorStrategy createOpsActorStrategy() {
	    return buildResumeOnRuntimeErrorStrategy();
	  }

	  public static SupervisorStrategy createTenantActorStrategy() {
	    return buildResumeOnRuntimeErrorStrategy();
	  }

	  public static SupervisorStrategy createApplicationActorStrategy() {
	    return buildRestartOrEscalateStrategy();
	  }

	  private static SupervisorStrategy buildResumeOrEscalateStrategy() {
	    return new OneForOneStrategy(-1, Duration.Inf(),
	            new Function<Throwable, SupervisorStrategy.Directive>() {
	        @Override
	        public Directive apply(Throwable throwable) throws Exception {
	          logException(throwable);
	          if (throwable instanceof Error) {
	            return OneForOneStrategy.escalate();
	          } else {
	            return OneForOneStrategy.resume();
	          }
	        }
	      });
	  }

	  private static SupervisorStrategy buildRestartOrEscalateStrategy() {
	    return new OneForOneStrategy(-1, Duration.Inf(),
	            new Function<Throwable, SupervisorStrategy.Directive>() {
	        @Override
	        public Directive apply(Throwable throwable) throws Exception {
	          logException(throwable);
	          if (throwable instanceof Error) {
	            return OneForOneStrategy.escalate();
	          } else {
	            return OneForOneStrategy.restart();
	          }
	        }
	      });
	  }

	  private static SupervisorStrategy buildResumeOnRuntimeErrorStrategy() {
	    return new OneForOneStrategy(-1, Duration.Inf(),
	            new Function<Throwable, SupervisorStrategy.Directive>() {
	        @Override
	        public Directive apply(Throwable throwable) throws Exception {
	          logException(throwable);
	          if (throwable instanceof Error) {
	            return OneForOneStrategy.escalate();
	          } else if (throwable instanceof RuntimeException) {
	            return OneForOneStrategy.resume();
	          } else {
	            return OneForOneStrategy.restart();
	          }
	        }
	      });
	  }

	  private static void logException(Throwable throwable) {
		  String str = String.format("Supervisor strategy got exception: %s",  throwable.getMessage());
		  logger.error(str, throwable);
	  }

	}
