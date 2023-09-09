package Utils;

import Resource.GenericResource;
public interface ResourceDataListener<T> {

    public void onDataChanged(GenericResource<T> resource, T updatedValue);

}
