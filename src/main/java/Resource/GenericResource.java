package Resource;

import Utils.ResourceDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class  GenericResource<T> {
    private static final Logger logger = LoggerFactory.getLogger(GenericResource.class);
    //protected List<GenericResource<T>> resourceListenerList;
    private String type;
    private String id;
    protected List<ResourceDataListener<T>> resourceListenerList;

    public GenericResource() {
        this.resourceListenerList=new ArrayList<>();
    }

    public GenericResource(String type,String id) {
        this.type=type;
        this.id=id;
        this.resourceListenerList=new ArrayList<>();
    }


    public abstract T loadUpdatedValue();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addDataListener(ResourceDataListener<T> resourceDataListener){
        if(this.resourceListenerList != null)
            this.resourceListenerList.add(resourceDataListener);
    }

    public void removeDataListener(ResourceDataListener<T> resourceDataListener){
        if(this.resourceListenerList != null && this.resourceListenerList.contains(resourceDataListener))
            this.resourceListenerList.remove(resourceDataListener);
    }

    protected void notifyUpdate(T updatedValue){
        if(this.resourceListenerList != null && this.resourceListenerList.size() > 0)
            this.resourceListenerList.forEach(resourceDataListener -> {
                if(resourceDataListener != null)
                    resourceDataListener.onDataChanged(this, updatedValue);
            });
        else
            logger.error("Empty or Null Resource Data Listener ! Nothing to notify ...");
    }

    @Override
    public String toString() {
        return "GenericResource{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", resourceListenerList=" + resourceListenerList +
                '}';
    }
}
