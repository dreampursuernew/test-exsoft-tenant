package test.exsoft.tenant

import grails.gorm.multitenancy.Tenants
import org.grails.datastore.mapping.engine.event.PostLoadEvent
import org.grails.datastore.mapping.model.PersistentEntity
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class PostLoadEventListenerService {

    @EventListener
    void onPostLoad(PostLoadEvent event) {
        def entity = event.entity
        if (!entity.isMultiTenant()){
            return
        }

        def tenantIdValue = event.entityAccess.getProperty(entity.tenantId.name)
        def currentId = Tenants.currentId()
        if (!tenantIdValue.toString().equals(currentId.toString())){
            throw new Exception("Error on PostLoadEvent to read/get " + entity.getName() + " with tentantId " + tenantIdValue + " is not equals current tenantId " + currentId + "!");
        }
    }
}
