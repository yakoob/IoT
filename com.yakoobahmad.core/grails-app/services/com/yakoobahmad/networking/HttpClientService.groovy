package com.yakoobahmad.networking

import com.yakoobahmad.domain.visualization.HueEffect
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import groovyx.net.http.AsyncHTTPBuilder
import static grails.async.Promises.*

@Transactional
class HttpClientService {

    AsyncHTTPBuilder http = new AsyncHTTPBuilder(poolSize : 1)

    def execute(String url, String path) {


        def uri = "$url/${path.toString()}"
        http.uri = uri

        http.get(path:uri)

        // println uri

    }

    def put(String url, HueEffect hueEffect) {

        RestBuilder rest = new RestBuilder()
        task {
            def resp = rest.put(url){
                contentType "application/json"
                json {
                    on = hueEffect.on
                    bri = hueEffect.bri
                    sat = hueEffect.sat
                    hue = hueEffect.hue
                    effect = "hueEffect"
                }
            }
        }
    }

}
