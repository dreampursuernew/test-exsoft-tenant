package test.exsoft.tenant

import com.mongodb.BasicDBObject
import com.mongodb.client.model.Filters
import grails.converters.JSON
import grails.gorm.multitenancy.Tenants
import org.bson.Document
import org.bson.types.ObjectId

class StoreController {

    def index() {
//        def currentTenantId = Tenants.currentId()
//        println "currentTenantId:${currentTenantId}"
//        def data = Store.list()
//        println "list:${data}"
//
//        def data = Store.findAllByName("麦德龙")
//        println "findAllByName:${data}"
//
        // 这个id:661f4273767c7442bca7b085 属于tenantId为5678的租户，在发送postman的请求中tenantId设置为123456，
        // 我们期望是应该读取不到数据的，但是如果我们不做任何处理，mongodb plugin会返回这个id对应的数据的，这就跟我们期望的不符。
        // 为了能够解决这个问题

        def data = Store.read(new ObjectId("661f4273767c7442bca7b085"))
        println "read:${data}"

//        def queryObj = new BasicDBObject()
//        queryObj.put("name", "麦德龙")
//        def data = Store.find(queryObj)
//        println "find:${data.toList()}"
        println "done!"
        render "ok"
    }

    def save(){
        def store = new Store(name:"老凤祥银楼",address:"南京东路1288号")
        store.save()
        render "ok"
    }
}
