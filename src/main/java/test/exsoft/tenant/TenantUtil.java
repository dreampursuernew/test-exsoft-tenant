package test.exsoft.tenant;

import grails.config.Config;
import grails.gorm.multitenancy.Tenants;
import grails.util.Holders;
import org.grails.datastore.mapping.core.Datastore;
import org.grails.datastore.mapping.core.connections.ConnectionSource;
import org.grails.datastore.mapping.model.PersistentEntity;
import org.grails.datastore.mapping.multitenancy.MultiTenantCapableDatastore;
import org.grails.datastore.mapping.reflect.ClassUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TenantUtil {

    private static Boolean g_isEnabledMutiTantMode = null;

    /**
     * 是否启用了多租户模式
     * @return
     */
    public static boolean isEnabledMutiTantMode(){
        if (g_isEnabledMutiTantMode == null){
            Config config = Holders.getConfig();
            Object tenancyMode = config.get("grails.gorm.multiTenancy.mode");
            g_isEnabledMutiTantMode = false;
            if (tenancyMode != null && tenancyMode instanceof String && "DISCRIMINATOR".equalsIgnoreCase((String) tenancyMode)){
                g_isEnabledMutiTantMode = true;
            }
        }
        return g_isEnabledMutiTantMode;
    }

    //租户模式下的排它domain类列表
    private static Set<String> g_excludeDomainClassList = null;
    /**
     * 获得不使用租户模式的domain类列表
     * @return
     */
    private static Set<String> getExcludeDomainList(){
        if (g_excludeDomainClassList != null){
            return g_excludeDomainClassList;
        }
        Config config = Holders.getConfig();
        List<String> data = (List<String>) config.get("grails.gorm.multiTenancy.exclude");
        if (data == null){
            g_excludeDomainClassList = new HashSet<>();
        }
        else{
            g_excludeDomainClassList = new HashSet<>(data);
        }
        addDefaultExcludeDomains(g_excludeDomainClassList);
        return g_excludeDomainClassList;
    }

    /**
     * 增加默认的非租户模式的Domain类
     * @param data
     */
    private static void addDefaultExcludeDomains(Set<String> data){
//        data.add("exsoft.SysUser");
    }

    /**
     * 是否是不设置多租户模式的domain实体类
     * @param className 全类名，例如：exsoft.SysUser
     * @return
     */
    public static boolean isExcludeTenantDomain(String className){
        Set<String> excludeDomainList = getExcludeDomainList();
        if (excludeDomainList.contains(className)){
            return true;
        }
        return false;
    }


    /**
     * 判断某domain class是否是Tentant类
     * @param clazz
     * @return
     */
    public static boolean isMutiTenantClass(Class clazz){
        if (ClassUtils.isMultiTenant(clazz) && !isExcludeTenantDomain(clazz.getName())){
            return true;
        }
        return false;
    }

    /**
     * 获得当前数据库中某实体的租户ID
     * @param datastore
     * @param entity
     * @return
     */
    public static Serializable getCurrentId(Datastore datastore, PersistentEntity entity){
        if (!isEnabledMutiTantMode()){
            //如果没有启用多租户模式则认为租户ID为null
            return null;
        }
        if (!entity.isMultiTenant()){
            //如果当前实体类中没有标注为多租户模式，则也认为是非多租户类
            return null;
        }
        if (isExcludeTenantDomain(entity.getName())){
            //当前实体类定义成了非多租户模式
            return null;
        }

        Serializable currentId = null;

        if(datastore instanceof MultiTenantCapableDatastore) {
            try {
                currentId = Tenants.currentId((MultiTenantCapableDatastore) datastore);
            }
            catch (Exception e){
                System.out.println("Error on Tenants.currentId on " + entity.getName() + ":" + e.toString());
                throw e;
            }
        }
        else {
            currentId = Tenants.currentId(datastore.getClass());
        }

        if (currentId == null || ConnectionSource.DEFAULT.equals(currentId)){
            return null;
        }
        return currentId;
    }
}
