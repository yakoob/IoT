package com.yakoobahmad.utils

import com.yakoobahmad.domain.DomainSerializer
import grails.transaction.Transactional
import grails.util.Holders
import groovy.text.Template

@Transactional
class JsonService {

    def jsonViewTemplateEngine = Holders.applicationContext.getBean("jsonViewTemplateEngine")

    def toJsonFromDomainTemplate(DomainSerializer domain) {
        Template t = jsonViewTemplateEngine.resolveTemplate(domain.jsonTemplatePath)
        def writable = t.make(obj: domain)
        def sw = new StringWriter()
        writable.writeTo( sw )
        return sw.toString()
    }
}