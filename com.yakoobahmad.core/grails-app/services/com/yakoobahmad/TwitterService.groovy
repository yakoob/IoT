package com.yakoobahmad

import com.yakoobahmad.actor.social.Tweet
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.social.twitter.api.impl.TwitterTemplate

@Slf4j
class TwitterService {

    static transactional = false

    @Value('${twitter.consumerKey}')
    String consumerKey

    @Value('${twitter.consumerSecret}')
    String consumerSecret

    @Value('${twitter.accessToken}')
    String accessToken

    @Value('${twitter.accessTokenSecret}')
    String accessTokenSecret

    TwitterTemplate getTwitterApi() {
        TwitterTemplate twitterOperations = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret)
        return twitterOperations
    }



    def mentions(lastId=null){

        def twts

        if (lastId)
            twts = twitterApi.timelineOperations().getMentions(200, lastId, lastId*100)
        else
            twts = twitterApi.timelineOperations().getMentions(200)

        log.debug "fetching tweets: ${twts?.toListString()}"

        LinkedList<Tweet> tweets = new LinkedList<Tweet>()

        twts.sort{it.id}.each {
            tweets.addLast(new Tweet(id:it.id, text: it.text))
        }

        log.debug "bindable tweets: " + tweets?.toListString()

        return tweets
    }
}
