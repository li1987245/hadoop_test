package spring;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by d on 2016/8/14.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }
}
