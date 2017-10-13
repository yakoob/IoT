package com.yakoobahmad.utils

import com.yakoobahmad.domain.utils.DomainSerializable
import grails.transaction.Transactional
import grails.util.Holders
import groovy.text.Template

@Transactional
class JsonService {

    def jsonViewTemplateEngine = Holders.applicationContext.getBean("jsonViewTemplateEngine")

    def toJsonFromDomainTemplate(DomainSerializable domain) {
        Template t = jsonViewTemplateEngine.resolveTemplate(domain.jsonTemplatePath)
        def writable = t.make(obj: domain)
        def sw = new StringWriter()
        writable.writeTo( sw )

        println "json " + sw.toString()

        return sw.toString()
    }
}
