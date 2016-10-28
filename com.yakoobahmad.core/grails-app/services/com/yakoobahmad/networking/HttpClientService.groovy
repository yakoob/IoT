package com.yakoobahmad.networking

import com.yakoobahmad.domain.visualization.HueEffect
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import groovyx.net.http.AsyncHTTPBuilder
import static grails.async.Promises.*

@Transactional
class HttpClientService {

    AsyncHTTPBuilder http = new AsyncHTTPBuilder(poolSize : 1)

    def get(String url) {

        try {
            def uri = "$url"
            http.uri = uri
            http.get(path:uri)
        } catch (e){
            e.printStackTrace()
        }

    }

    def put(String url, HueEffect hueEffect) {

        try {
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
        } catch (e) {
            e.printStackTrace()
        }

    }

    def execute(String url, String path) {
        def uri = "$url/${path.toString()}"
        http.uri = uri
        http.get(path:uri)
        // println uri
    }

}
