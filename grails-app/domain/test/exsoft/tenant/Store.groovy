package test.exsoft.tenant

import grails.gorm.MultiTenant
import groovy.transform.ToString
import org.bson.types.ObjectId

@ToString
class Store implements MultiTenant<Store> {
    ObjectId id
    String tenantId

    String name
    String address

    static constraints = {
    }

    static mapping = {
        name index:true
    }
}
