package com.yakoobahmad.domain.utils

trait DomainSerializable implements Serializable {

    private static final long serialVersionUID = 10102020303040405050L

    abstract public String getJsonTemplatePath()

}