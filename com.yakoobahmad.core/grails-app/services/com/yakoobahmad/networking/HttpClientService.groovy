package com.yakoobahmad.networking

import grails.transaction.Transactional
import groovyx.net.http.AsyncHTTPBuilder

@Transactional
class HttpClientService {

    def http = new AsyncHTTPBuilder(poolSize : 1)


    def execute(String url, String path) {


        def uri = "$url/${path.toString()}"
        http.uri = uri

        http.get(path:uri)

        // println uri

    }

}
