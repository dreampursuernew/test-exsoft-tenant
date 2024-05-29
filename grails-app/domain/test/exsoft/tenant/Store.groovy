package test.exsoft.tenant

import grails.gorm.MultiTenant
import groovy.transform.ToString
import org.bson.types.ObjectId

@ToString
class Store{
    ObjectId id

    String name
    String address

    static constraints = {
    }

    static mapping = {
        name index:true
    }
}
