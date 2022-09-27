package org.devgateway.toolkit.persistence.dto;

public class ServiceMeasure extends ServiceCategory {

    private String filter;

    private String expression;

    private String delegate;

    public ServiceMeasure() {
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(final String expression) {
        this.expression = expression;
    }

    public String getDelegate() {
        return delegate;
    }

    public void setDelegate(final String delegate) {
        this.delegate = delegate;
    }

}
