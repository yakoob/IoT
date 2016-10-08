package com.yakoobahmad.actor

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Address
import akka.actor.Props
import akka.cluster.Cluster
import akka.cluster.ClusterEvent

import com.typesafe.config.ConfigFactory
import com.yakoobahmad.actor.networking.ClusterListener
import com.yakoobahmad.device.Smoke
import grails.gsp.PageRenderer
import grails.transaction.Transactional
import groovy.text.Template
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PreDestroy

@Log
@Transactional
class AkkaService {

    private static ActorSystem system
    private static final ActorRef ACTOR_NO_SENDER = ActorRef.noSender()

    ActorRef clusterListener
    ActorRef halloweenManager
    ActorRef christmasManger
    ActorRef soundDetection

    PageRenderer groovyPageRenderer

    def serverService

    void init() {
        // create actor system
        system = ActorSystem.create("Halloween")
        log.info "Initialized Akka ActorSystem $system"
        actorSetup()
        def cluster = Cluster.get(system)
        ConfigFactory.parseString("akka.remote.netty.tcp.hostname=${serverService.thisServer.ipAddress}").withFallback(ConfigFactory.load())
        // create list of akka cluster seed nodes to join to
        List seeds = new LinkedList<Address>()
        // leader always goes first in list
        seeds.add(new Address("akka.tcp", system.name().toString(), serverService.thisServer.ipAddress, 2552))
        cluster.joinSeedNodes(akka.japi.Util.immutableSeq(seeds))
        cluster.subscribe(clusterListener, ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class, ClusterEvent.LeaderChanged.class)

    }

    ActorSystem getSystem() {
        return system
    }

    void actorSetup() {
        clusterListener = actorOf(ClusterListener, "ClusterListener")
        halloweenManager = actorOf(com.yakoobahmad.actor.halloween.Manager, "HalloweenManager")
        christmasManger = actorOf(com.yakoobahmad.actor.christmas.Manager, "ChristmasManger")
        soundDetection = actorOf(com.yakoobahmad.actor.device.SoundDetection, "SoundDetection")
    }

    ActorRef actorNoSender() {
        return ACTOR_NO_SENDER
    }

    Props props(Class clazz) {
        assert clazz != null
        Props props = Props.create(clazz)
        return props
    }

    ActorRef actorOf(Props props) {
        assert props != null
        assert system != null

        ActorRef actor = system.actorOf(props)
        return actor
    }

    ActorRef actorOf(Props props, String name) {
        assert props != null
        assert name != null

        assert system != null

        ActorRef actor = system.actorOf(props, name)
        return actor
    }

    ActorRef actorOf(Class clazz) {
        assert clazz != null

        Props props = props(clazz)
        assert props != null
        assert system != null

        ActorRef actor = system.actorOf(props)
        return actor
    }

    ActorRef actorOf(Class clazz, String name) {
        assert clazz != null
        assert name != null

        Props props = props(clazz)
        assert props != null
        assert system != null

        ActorRef actor = system.actorOf(props, name)
        return actor
    }

    @PreDestroy
    void destroy() {
        system?.shutdown()
        system = null
        log.error("destroying Akka ActorSystem: done.")
    }

}
